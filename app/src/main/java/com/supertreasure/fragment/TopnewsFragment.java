package com.supertreasure.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.supertreasure.R;
import com.supertreasure.base.BaseFragment;
import com.supertreasure.bean.Banner;
import com.supertreasure.bean.NowTime;
import com.supertreasure.bean.Praise;
import com.supertreasure.bean.Topnews;
import com.supertreasure.eventbus.RefleshTopNewsFragment;
import com.supertreasure.eventbus.UpdateNewParams;
import com.supertreasure.eventbus.initdotEvent;
import com.supertreasure.greenDaoUtils.GreenUtils;
import com.supertreasure.home.ActivityAdvertisementDetail;
import com.supertreasure.home.ActivityNewsDetail;
import com.supertreasure.home.RefreshAdapter;
import com.supertreasure.home.TopnewBannerAdapter;
import com.supertreasure.main.TheApp;
import com.supertreasure.service.HttpResultUtil;
import com.supertreasure.util.BannerAdapter;
import com.supertreasure.util.ChildViewPager;
import com.supertreasure.util.Config;
import com.supertreasure.util.LoadMoreRecyclerView;
import com.supertreasure.util.NetUtils;
import com.supertreasure.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import me.itangqi.greendao.New;
import me.relex.circleindicator.CircleIndicator;


/**
 * Created by prj on 2016/1/22.
 */
public class TopnewsFragment extends BaseFragment{
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    private GreenUtils greenUtils = GreenUtils.getInstance();
    int user_id;
    private List<New> newsList;
    private int autoChangeTime= 5000;
    private Runnable runnable;
    private Context context;
    private int page = 1;
    private ChildViewPager viewPager;
    private TopnewBannerAdapter bannerAdapter;
    private FrameLayout ll_banner;
    private LinearLayout root;
    private LinearLayoutManager linearLayoutManager;
    private List<me.itangqi.greendao.Banner> bannerList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreRecyclerView recyclerView;
    private RefreshAdapter adapter;
    private boolean isPullToRefresh = false;
    private boolean ifHadPullToRefreshedSucess = false;
    private Long referenceNewsId;
    private CircleIndicator indicator;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.layout_topnews, null);
        root = (LinearLayout) view.findViewById(R.id.root);
        AbViewUtil.scaleContentView(root);
        context = getContext();

        View view_ad= View.inflate(TheApp.instance,R.layout.outschool_head_ad,null);
        ll_banner   = (FrameLayout) view_ad.findViewById(R.id.ll_banner);
        viewPager   = (ChildViewPager) view_ad.findViewById(R.id.bannerViewPager);
        indicator   = (CircleIndicator) view_ad.findViewById(R.id.indicator);
        AbViewUtil.scaleContentView(ll_banner);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        AbViewUtil.scaleContentView((SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout));
        recyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void initData() {

        AbSharedUtil.putInt(TheApp.instance, Config.TopNewsPage, 1);
        swipeRefreshLayout.setColorSchemeColors(R.color.text_color_title);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(TheApp.instance);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAutoLoadMoreEnable(true);

        user_id = AbSharedUtil.getInt(TheApp.instance, Config.Current_user_id);

		referenceNewsId = greenUtils.queryNewCurrentNewestId(TheApp.instance, Config.DATABASE_NEWS_TYPE_IN);

        //bannerList = greenUtils.queryBannerList(1,greenUtils.queryBannerCurrentNewestId(Config.DATABASE_BANNER_TYPE_IN),Config.DATABASE_BANNER_TYPE_IN);
        adapter = new RefreshAdapter(context, newsList);
        recyclerView.addHeaderView(ll_banner);

        bannerAdapter = new TopnewBannerAdapter(TheApp.instance, bannerList);
        isPrepared = true;
        lazyLoad();
    }

    private void getTopNewList() {

        String url = Config.getAPI(Config.TopNews);
        final AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName,AbSharedUtil.getString(TheApp.instance,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance,Config.Token));
        params.put(Config.Type,"IN");
        params.put(Config.Page, AbSharedUtil.getInt(TheApp.instance, Config.TopNewsPage));
        params.put(Config.Rows, Config.RowNumber);
        params.put(Config.BeginTime, AbSharedUtil.getString(TheApp.instance, Config.TopNewsTime));

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {

                if (HttpResultUtil.handleUnloginErrorStatus(content, RefleshTopNewsFragment.getInstance(), TopnewsFragment.this.getActivity())) {
                    return;
                }
                Gson gson = new Gson();
                final Topnews topnews = gson.fromJson(content, Topnews.class);
                if (topnews.getStatus().equals(Config.SUCCESS)) {

                            if(isPullToRefresh) {
                                if (topnews.getList()!=null && topnews.getList().size()>0)
                                    greenUtils.deleteAllNewsByType(Config.DATABASE_NEWS_TYPE_IN);

                                adapter.setTopnews(topnews.getList());
                                ifHadPullToRefreshedSucess = true;
                                recyclerView.notifyMoreFinish(true, 1);
                                if(topnews.getList().size()<Config.RowNumber)
                                    recyclerView.setAutoLoadMoreEnable(false);
                                else
                                    recyclerView.setAutoLoadMoreEnable(true);
                            }
                            else
                            {
                                adapter.addTopnews(topnews.getList());
                                recyclerView.notifyMoreFinish(true,0);
                                if(topnews.getList().size()<Config.RowNumber){
                                    recyclerView.setAutoLoadMoreEnable(false);
                                }
                            }

                            for (New newtemp:topnews.getList()) {
                                greenUtils.insertOrReplaceNew(newtemp,Config.DATABASE_NEWS_TYPE_IN);
                            }
                            AbSharedUtil.putInt(TheApp.instance, Config.TopNewsPage, AbSharedUtil.getInt(TheApp.instance, Config.TopNewsPage) + 1);

                    //如果是下拉刷新的话，清除以往信息，加载新数据
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Toast.makeText(TheApp.instance, content, Toast.LENGTH_LONG).show();

                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void getBanner() {
        String url = Config.getAPI(Config.Banner);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance, Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance, Config.Token));
        params.put(Config.Type, Config.AdFirst);

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Toast.makeText(TheApp.instance, content, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, String content) {

                Gson gson = new Gson();
                Banner banner = gson.fromJson(content, Banner.class);

                String status = banner.getStatus();
                if (status.equals(Config.SUCCESS)) {
                    bannerList = banner.getList();
                    bannerAdapter.updateView(bannerList);
                    if (bannerList!=null && bannerList.size()>0)
                        greenUtils.deleteAllBannerByType(Config.DATABASE_BANNER_TYPE_IN);

                    for (me.itangqi.greendao.Banner banner1 : bannerList) {
                        banner1.setType(Config.DATABASE_BANNER_TYPE_IN);
                        //Logger.i("insert", banner1.toString());
                        greenUtils.getBannerDao().insertOrReplace(banner1);
                        //greenUtils.insertOrReplaceBanner(banner1,Config.DATABASE_BANNER_TYPE_IN);
                    }
                    bannerAdapter.updateView(bannerList);
                    recyclerView.addHeaderView(ll_banner);
                    initViewPager();
                }
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

    private void initViewPager() {
        viewPager.setVisibility(View.VISIBLE);
        bannerAdapter.updateView(bannerList);
        viewPager.setAdapter(bannerAdapter);
        viewPager.setOnPageChangeListener(myOnPageChangeListener);
       // 点击事件

        if(!Config.CloseADOnclick)
        viewPager.setOnSingleTouchListener(new ChildViewPager.OnSingleTouchListener() {
            @Override
            public void onSingleTouch() {
                //Logger.i("BannerAdapter", "onSingleTouch");
                int position = viewPager.getCurrentItem();
                //Logger.i("","onItemShortClick position = "+position);
                Intent intent = new Intent(TheApp.instance, ActivityAdvertisementDetail.class);
                //intent.putExtra(Config.URL, bannerAdapter.getList().get(position).getUrl());
                intent.putExtra(Config.URL, bannerAdapter.getList().get(position).getAlink());
                startActivity(intent);
            }
        });

        if(bannerList.size()!=0){
            if(bannerList.size()!=1){
                EventBus.getDefault().post(new initdotEvent(Config.NewIn));
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

    private void LoadingDataFromDatabase() {
        //List<New> clist = tool.queryNewsList(context,currentreferencePage, referenceNewsId);
 		List<New> clist = greenUtils.queryNewsList(TheApp.instance,++page,referenceNewsId,Config.DATABASE_NEWS_TYPE_IN);
        adapter.addTopnews(clist);
        recyclerView.notifyMoreFinish(true, 0);

        if (clist.size()<Config.RowNumber){
            recyclerView.setAutoLoadMoreEnable(false);
        }
        isPullToRefresh = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setListener() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PullToRefrsh();
            }
        });

        //自动加载更多
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (ifHadPullToRefreshedSucess) {//如果已经 进行了刷新操作，那么就从网络获取数据
                    //Log.i("TopnewsFragment", "getTopNewList()");
                    getTopNewList();
                } else {
                    //Log.i("TopnewsFragment", "LoadingDataFromDatabase()");
                    LoadingDataFromDatabase();//如果没有 进行了刷新操作，那么就从数据库获取数据
                }
            }
        });

        adapter.setOnClickListener(new RefreshAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, String topId, String url, String adminID, int position) {
                Intent intent = new Intent(TheApp.instance, ActivityNewsDetail.class);
                intent.putExtra(Config.URL, url);
                intent.putExtra(Config.Type, Config.NewsIn);
                intent.putExtra(Config.TopID, adapter.getNewses().get(position).getTopId());
                intent.putExtra(Config.Position, position);
                intent.putExtra(Config.AdminID, adapter.getNewses().get(position).getAdminID());
                intent.putExtra(Config.TopNewsBrowse, adapter.getNewses().get(position).getBrowserNum());
                intent.putExtra(Config.TopNewsPrise, adapter.getNewses().get(position).getPraise());
                intent.putExtra(Config.TopNewsIsPrise, adapter.getNewses().get(position).getIsPraised());
                startActivityForResult(intent, Config.REQUEST_CODE);
            }

            @Override
            public void ItemClickListener(final View iv_like, final TextView tv_like, String topId, final int position) {
                //iv_like.setEnabled(false);
                iv_like.setEnabled(true);
                AbRequestParams params = new AbRequestParams();
                params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance, Config.UserName));
                params.put(Config.Token, AbSharedUtil.getString(TheApp.instance, Config.Token));
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
                        adapter.getNewses().get(position).setPraise(praise.getPraiseTimes());
                        //recyclerView.getAdapter().notifyItemChanged(position+1);
                        tv_like.setText(praise.getPraiseTimes());
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {
                        //iv_like.setEnabled(true);
                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        ToastUtil.showToast(TheApp.instance, statusCode);
                    }
                });
            }

            @Override
            public void ItemLongClickListener(View view, int position) {
                //adapter.removeTopnews(position);
            }
        });
    }

    private void PullToRefrsh() {

        getBanner();

        if (!NetUtils.isConnected(context)) {
            ToastUtil.showToast(context, "请先打开网络");
            return;
        }
        isPullToRefresh = true;
        recyclerView.setLoadingMore(false);

        String url = Config.getAPI(Config.NowTime);
        TheApp.mAbHttpUtil.post(url, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Logger.json("response content", content);

                Gson gson = new Gson();
                NowTime nowTime = gson.fromJson(content, NowTime.class);
                if (nowTime.getStatus().equals(Config.SUCCESS)) {
                    AbSharedUtil.putString(TheApp.instance, Config.TopNewsTime, nowTime.getNowtime());
                    AbSharedUtil.putInt(TheApp.instance, Config.TopNewsPage, 1);
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
                Toast.makeText(TheApp.instance, "获取服务器当前时间失败，数据更新失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);

        super.onDestroy();
    }

    @Override
    protected void lazyLoad() {//已经初始化完成，并且可见，并且第一次加载才  运行加载.
        if (!isPrepared || !isVisible || mHasLoadedOnce)
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                newsList = greenUtils.queryNewsList(TheApp.instance, page, referenceNewsId, Config.DATABASE_NEWS_TYPE_IN);        //if (newsList == null) newsList = new ArrayList<>();
                adapter.setTopnews(newsList);
                bannerList = greenUtils.queryBannerList(Config.DATABASE_BANNER_TYPE_IN);
                bannerAdapter.setList(bannerList);
                mHasLoadedOnce = true;
                initViewPager();
                /*if (NetUtils.isConnected(TheApp.instance)) {
                    Looper.prepare();
                    recyclerView.setAdapter(adapter);
                    PullToRefrsh();
                }else {
                    //ToastUtil.showToast(TheApp.instance,"没有网络");
                }*/
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                        if (NetUtils.isConnected(TheApp.instance)) {
                            PullToRefrsh();
                        }else {
                            ToastUtil.showToast(TheApp.instance,"没有网络");
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    @Subscribe
    public void onEventMainThread(RefleshTopNewsFragment event) {
        if (NetUtils.isConnected(TheApp.instance)) {
            if (!isPrepared)
                return;
            PullToRefrsh();
        }
    }

    @Subscribe
    public void onEventMainThread(UpdateNewParams event) {

        if(event.getType().equals(Config.NewsIn)) {
            adapter.updateItem(event.getBrowserNum()
                    , event.getPraiseTimes()
                    , event.isPraised()
                    , event.getPosition());
            recyclerView.getAdapter().notifyItemChanged(event.getPosition() + 1);
        }
    }

    @Subscribe
    public void onEventMainThread(initdotEvent event) {
        if(event.getIndex()==Config.NewIn)
        indicator.setViewPager(viewPager);
    }
}
