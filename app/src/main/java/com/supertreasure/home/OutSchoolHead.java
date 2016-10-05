package com.supertreasure.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.orhanobut.logger.Logger;
import com.supertreasure.R;
import com.supertreasure.bean.Banner;
import com.supertreasure.bean.NowTime;
import com.supertreasure.bean.Praise;
import com.supertreasure.bean.Topnews;
import com.supertreasure.eventbus.RefleshOutschoolHead;
import com.supertreasure.eventbus.UpdateNewParams;
import com.supertreasure.eventbus.initdotEvent;
import com.supertreasure.greenDaoUtils.GreenUtils;
import com.supertreasure.main.TheApp;
import com.supertreasure.service.HttpResultUtil;
import com.supertreasure.util.BannerAdapter;
import com.supertreasure.util.ChildViewPager;
import com.supertreasure.util.Config;
import com.supertreasure.util.LoadMoreRecyclerView;
import com.supertreasure.util.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.itangqi.greendao.New;
import me.relex.circleindicator.CircleIndicator;


public class OutSchoolHead extends AbActivity implements View.OnClickListener{
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    private GreenUtils greenUtils;
    private List<New> list;
    private int page;
    private Button bt_title_left;
    private Button bt_title_right;
    private TextView tv_title_name;
    private int autoChangeTime= 5000;
    private Runnable runnable;
    private Context context;
    private ChildViewPager viewPager;
    private TopnewBannerAdapter bannerAdapter;
    private CircleIndicator indicator;
    private FrameLayout fl_banner;
    private LinearLayout root;
    private List<me.itangqi.greendao.Banner> listBanner = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreRecyclerView recyclerView;
    private RefreshAdapter adapter;
    //private boolean isLoadingMore = false;
    private boolean isPullToRefresh = false;
    private boolean ifHadPullToRefreshedSucess = false;
    private long referenceNewsId;
    private int newsPage = 1;
    private View ll_title_left;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outschool_head_main);
        SwipeBackHelper.onCreate(this);
        EventBus.getDefault().register(this);

        initView();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        SwipeBackHelper.onDestroy(this);
    }

    private void setListener() {
        bt_title_left.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PullToRefrsh();
            }
        });

        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (ifHadPullToRefreshedSucess)//如果已经 进行了刷新操作，那么就从网络获取数据
                    getTopNewList();
                else {
                    //LoadingDataFromDatabase();//如果没有 进行了刷新操作，那么就从数据库获取数据
                }
            }
        });
        /*recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = adapter.getItemCount();

                // Toast.makeText(context,lastVisibleItem+"??"+totalItemCount,Toast.LENGTH_SHORT).show();
                if(lastVisibleItem>=totalItemCount-Config.AutoLoadingNum&&dy>0&&!isLoadingMore&&!isPullToRefresh){
                    getTopNewList();
                }
            }
        });*/
        adapter.setOnClickListener(new RefreshAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, String topId, String url, String adminID,int position) {
                Intent intent = new Intent(TheApp.instance, ActivityNewsDetail.class);
                intent.putExtra(Config.URL, url);
                intent.putExtra(Config.Type,Config.NewsOut);
                intent.putExtra(Config.TopID,adapter.getNewses().get(position).getTopId());
                intent.putExtra(Config.Position,position);
                intent.putExtra(Config.AdminID, adapter.getNewses().get(position).getAdminID());
                intent.putExtra(Config.TopNewsBrowse, adapter.getNewses().get(position).getBrowserNum());
                intent.putExtra(Config.TopNewsPrise, adapter.getNewses().get(position).getPraise());
                intent.putExtra(Config.TopNewsIsPrise, adapter.getNewses().get(position).getIsPraised());
                startActivityForResult(intent, Config.REQUEST_CODE);
            }

            @Override
            public void ItemClickListener(final View iv_like, final TextView tv_like, String topId, final int position) {

                AbRequestParams params = new AbRequestParams();
                params.put(Config.UserName, AbSharedUtil.getString(context, Config.UserName));
                params.put(Config.Token, AbSharedUtil.getString(context, Config.Token));
                params.put(Config.TopID, topId);
                String url = Config.getAPI(Config.Praise);
                TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        //Logger.json("response content", content);

                        Gson gson = new Gson();
                        Praise praise = gson.fromJson(content, Praise.class);
                        //Toast.makeText(TheApp.instance, praise.getPraiseTimes(), Toast.LENGTH_SHORT).show();
                        if (Integer.parseInt(praise.getPraiseTimes()) - Integer.parseInt(tv_like.getText().toString()) < 0) {
                            iv_like.setBackgroundResource(R.drawable.like_normal);
                            adapter.getNewses().get(position).setIsPraised(false);
                        } else {
                            iv_like.setBackgroundResource(R.drawable.like_press);
                            adapter.getNewses().get(position).setIsPraised(true);
                        }
                        tv_like.setText(praise.getPraiseTimes());
                        adapter.getNewses().get(position).setPraise(praise.getPraiseTimes());
                        //recyclerView.notifyInsertFinish(position,position);
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        Toast.makeText(TheApp.instance, statusCode, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void ItemLongClickListener(View view, int position) {
                adapter.removeTopnews(position);
            }
        });
       /* bannerAdapter.setListener(new BannerAdapter.OnItemClickListener() {

            @Override
            public void onItemShortClick(View v, int position) {
                Intent intent = new Intent(TheApp.instance, ActivityAdvertisementDetail.class);
                //intent.putExtra(Config.URL, bannerAdapter.getList().get(position).getUrl());
                intent.putExtra(Config.URL, adapter.getCouponList().get(0).getUrl());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });*/
    }

    private void PullToRefrsh() {
        getBanner();
        isPullToRefresh = true;
        recyclerView.setLoadingMore(false);
        recyclerView.addFooterView(R.layout.list_foot_loading);

        String url = Config.getAPI(Config.NowTime);
        TheApp.mAbHttpUtil.post(url, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //Logger.json("response content",content);

                Gson gson = new Gson();
                NowTime nowTime = gson.fromJson(content,NowTime.class);
                if(nowTime.getStatus().equals(Config.SUCCESS)){
                    AbSharedUtil.putString(TheApp.instance,Config.OutTopNewsTime,nowTime.getNowtime());
                    AbSharedUtil.putInt(TheApp.instance,Config.OutTopNewsPage,1);
                    getTopNewList();
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(TheApp.instance,"获取服务器当前时间失败，数据更新失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));
        tv_title_name= (TextView) findViewById(R.id.tv_title_name);
        bt_title_left= (Button) findViewById(R.id.bt_title_left);
        ll_title_left= findViewById(R.id.ll_title_left);

        bt_title_right= (Button) findViewById(R.id.bt_title_right);
        View view_ad= View.inflate(this,R.layout.outschool_head_ad,null);
        context=this;
        fl_banner = (FrameLayout) view_ad.findViewById(R.id.ll_banner);
        indicator = (CircleIndicator) view_ad.findViewById(R.id.indicator);
        AbViewUtil.scaleContentView(fl_banner);
        viewPager = (ChildViewPager) view_ad.findViewById(R.id.bannerViewPager);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerView);
    }

    private void initData() {
        greenUtils = GreenUtils.getInstance();
        int page = 1;
        greenUtils = GreenUtils.getInstance();
        long newestBannerId = greenUtils.queryBannerCurrentNewestId(Config.DATABASE_BANNER_TYPE_OUT);
        //listBanner = greenUtils.queryBannerList(page, newestBannerId, Config.DATABASE_BANNER_TYPE_OUT);


        tv_title_name.setText("外校头条");
        bt_title_right.setVisibility(View.INVISIBLE);
        AbSharedUtil.putInt(context, Config.OutTopNewsPage, 1);
        swipeRefreshLayout.setColorSchemeColors(R.color.text_color_title);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAutoLoadMoreEnable(true);
        adapter = new RefreshAdapter(context,list);
        recyclerView.setAdapter(adapter);
        bannerAdapter = new TopnewBannerAdapter(context,listBanner);
        isPrepared = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                referenceNewsId = greenUtils.queryNewCurrentNewestId(context,Config.DATABASE_NEWS_TYPE_OUT);

                mHasLoadedOnce = true;
                list = greenUtils.queryNewsList(context, newsPage, referenceNewsId, Config.DATABASE_NEWS_TYPE_OUT);
                adapter.setTopnews(list);
                initViewPager();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (NetUtils.isConnected(TheApp.instance))
                            PullToRefrsh();

                    }
                });

            }
        }).start();
    }

    private void getTopNewList() {

        String url = Config.getAPI(Config.TopNews);
        final AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName,AbSharedUtil.getString(context,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(context,Config.Token));
        params.put(Config.Type,"OUT");

        params.put(Config.Page, AbSharedUtil.getInt(context,Config.OutTopNewsPage));
        params.put(Config.Rows, Config.RowNumber);
        params.put(Config.BeginTime,AbSharedUtil.getString(context,Config.OutTopNewsTime));

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //Logger.json("response content",content);
                if (HttpResultUtil.handleUnloginErrorStatus(content, RefleshOutschoolHead.getInstance(), OutSchoolHead.this)) {
                    return;
                }

                Gson gson = new Gson();
                Topnews topnews = gson.fromJson(content, Topnews.class);

                if (topnews.getStatus().equals(Config.SUCCESS)) {
                    //如果是下拉刷新的话，清除以往信息，加载新数据
                    if(isPullToRefresh) {
                        if (topnews.getList()!=null && topnews.getList().size()>0)
                            greenUtils.deleteAllNewsByType(Config.DATABASE_NEWS_TYPE_OUT);
                        adapter.setTopnews(topnews.getList());
                        ifHadPullToRefreshedSucess = true;
                        recyclerView.notifyMoreFinish(true,1);
                        if(topnews.getList().size()<Config.RowNumber)
                            recyclerView.setAutoLoadMoreEnable(false);
                    }
                    else
                    {
                        adapter.addTopnews(topnews.getList());
                        recyclerView.notifyMoreFinish(true,0);
                    }
                    for (New newtemp:topnews.getList()) {

                        greenUtils.insertOrReplaceNew(newtemp,Config.DATABASE_NEWS_TYPE_OUT);
                    }
                    if(topnews.getList().size()<Config.RowNumber){
                        recyclerView.setAutoLoadMoreEnable(false);
                    }
                    //    Toast.makeText(context,topnews.getList().toString(),Toast.LENGTH_SHORT).show();
                    AbSharedUtil.putInt(TheApp.instance, Config.OutTopNewsPage, AbSharedUtil.getInt(TheApp.instance, Config.OutTopNewsPage) + 1);
                }
                //   Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                //isLoadingMore = false;
                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Toast.makeText(context, content, Toast.LENGTH_LONG).show();
                //isLoadingMore = false;
                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void initViewPager() {
        listBanner = greenUtils.queryBannerList(Config.DATABASE_BANNER_TYPE_OUT);
        viewPager.setVisibility(View.VISIBLE);
        bannerAdapter.updateView(listBanner);
        viewPager.setAdapter(bannerAdapter);
        viewPager.setOnPageChangeListener(myOnPageChangeListener);
        // 点击事件
        if(!Config.CloseADOnclick)
        viewPager.setOnSingleTouchListener(new ChildViewPager.OnSingleTouchListener() {
            @Override
            public void onSingleTouch() {
                //Logger.i("BannerAdapter", "onSingleTouch");
                int position = viewPager.getCurrentItem();
                Intent intent = new Intent(TheApp.instance, ActivityAdvertisementDetail.class);
                intent.putExtra(Config.URL, bannerAdapter.getList().get(position).getAlink());

                //intent.putExtra(Config.URL, bannerAdapter.getList().get(position).getUrl());
                //intent.putExtra(Config.URL, adapter.getCouponList().get(0).getUrl());
                startActivity(intent);
            }
        });
        if(listBanner.size()!=0){
            if(listBanner.size()!=1){
                EventBus.getDefault().post(new initdotEvent(Config.NewOut));
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        int nextPager = viewPager.getCurrentItem()+1;
                        if(nextPager>=bannerAdapter.getCount()){
                            nextPager = 0;
                        }
                        viewHandler.sendEmptyMessage(nextPager);
                    }
                };
                viewHandler.postDelayed(runnable,autoChangeTime);
            }
        }
    }

    private void getBanner(){

        String url = Config.getAPI(Config.Banner);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance, Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance, Config.Token));
        params.put(Config.Type,Config.AdThird);
        TheApp.mAbHttpUtil.post(url, params ,new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //.json("response content", content);

                Gson gson = new Gson();
                Banner banner = gson.fromJson(content,Banner.class);
                //Toast.makeText(context,banner.toString(),Toast.LENGTH_LONG).show();
                String status = banner.getStatus();
                if (status.equals(Config.SUCCESS)){
                    listBanner = banner.getList();
                    if (listBanner!=null && listBanner.size()>0)
                        greenUtils.deleteAllBannerByType(Config.DATABASE_BANNER_TYPE_OUT);
                    //  greenUtils.getBannerDao().deleteAll();
                    for (me.itangqi.greendao.Banner banner1 : listBanner) {
                        banner1.setType(Config.DATABASE_BANNER_TYPE_OUT);
                        Logger.i("insert", banner1.toString());
                        greenUtils.getBannerDao().insertOrReplace(banner1);
                        //greenUtils.insertOrReplaceBanner(banner1,Config.DATABASE_BANNER_TYPE_OUT);
                    }
                    bannerAdapter.updateView(listBanner);
                    //initDot();
                    /*if(adapter.getHeaderView()==null) {
                        adapter.setHeaderView(ll_banner);
                        initViewPager();
                    }*/
                    recyclerView.addHeaderView(fl_banner);
                    initViewPager();
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Toast.makeText(TheApp.instance,content,Toast.LENGTH_LONG).show();
            }
        });

    }

    private final Handler viewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;

            viewPager.setCurrentItem(what);
        }
    };

    ViewPager.OnPageChangeListener myOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            viewHandler.removeCallbacks(runnable);
            viewHandler.postDelayed(runnable, autoChangeTime);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_left:
                finish();
                break;
        }
    }
    @Subscribe
    public void onEventMainThread(RefleshOutschoolHead event) {
        //String msg = "RefleshOutschoolHead";
        //Log.d("eventbus", msg);
        if (NetUtils.isConnected(TheApp.instance)) {
            if (!isPrepared)
                return;
            PullToRefrsh();
        }
    }

    @Subscribe
    public void onEventMainThread(UpdateNewParams event) {

        if(event.getType().equals(Config.NewsOut)) {
            adapter.updateItem(event.getBrowserNum()
                    , event.getPraiseTimes()
                    , event.isPraised()
                    , event.getPosition());
            recyclerView.getAdapter().notifyItemChanged(event.getPosition() + 1);
        }
    }

    @Subscribe
    public void onEventMainThread(initdotEvent event) {
        if(event.getIndex()==Config.NewOut)
            indicator.setViewPager(viewPager);
    }
}
