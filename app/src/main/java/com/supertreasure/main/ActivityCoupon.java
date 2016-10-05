package com.supertreasure.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.supertreasure.R;
import com.supertreasure.bean.CouponBean;
import com.supertreasure.bean.NowTime;
import com.supertreasure.coupon.ActivityCouponDetail;
import com.supertreasure.coupon.CouponAdapter;
import com.supertreasure.eventbus.RefleshCuponFragment;
import com.supertreasure.eventbus.UpdateActivityCoupon;
import com.supertreasure.greenDaoUtils.GreenUtils;
import com.supertreasure.service.HttpResultUtil;
import com.supertreasure.util.Config;
import com.supertreasure.util.DividerStaggerItemDecoration;
import com.supertreasure.util.LoadMoreRecyclerView;
import com.supertreasure.util.NetUtils;
import com.supertreasure.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import me.itangqi.greendao.Coupon;

/**
 * Created by Administrator on 2016/3/12.
 */
public class ActivityCoupon extends AbActivity {
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    private GreenUtils greenUtils;
    private List<Coupon> clist;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private TextView tv_title;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreRecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private CouponAdapter adapter;
    //private boolean isLoadingMore = false;
    private boolean isPullToRefresh = false;
    /*private View mFootView;
    private boolean isShowFooter = false;*/
    public static final int REQUEST_CODE = 10086;

    private boolean ifHadPullToRefreshedSucess = false;
    private long referenceCouponId ;
    int currentreferencePage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trade_coupon);
        EventBus.getDefault().register(this);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        context = this;
        AbViewUtil.scaleContentView((LinearLayout)findViewById(R.id.root));
        //fab = (FloatingActionButton) findViewById(R.id.fab);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        recyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerView);
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        ll_title_left.setVisibility(View.INVISIBLE);
        ll_title_right.setVisibility(View.INVISIBLE);
        tv_title.setText("校外逛逛");
    }

    public void initData() {
        //tool = DBTool.getInstance(context);
        greenUtils = GreenUtils.getInstance();
        AbSharedUtil.putInt(context, Config.CouponPage, 1);
        swipeRefreshLayout.setColorSchemeColors(R.color.text_color_title);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerStaggerItemDecoration(5));
        recyclerView.setAutoLoadMoreEnable(true);
        adapter = new CouponAdapter(context,clist);

        //referenceCouponId = tool.queryCouponCurrentNewestId(context);
        referenceCouponId = greenUtils.queryCouponCurrentNewestId(context);
        isPrepared = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                clist = greenUtils.queryCouponList(context,currentreferencePage, (int) referenceCouponId);
                //couponList = tool.queryCouponList(context,currentreferencePage,referenceCouponId);
                adapter.setCoupon(clist);
                mHasLoadedOnce = true;
                //LoadingDataFromDatabase();
                AbSharedUtil.putInt(context,Config.CouponPage,1);
                AbSharedUtil.putString(context,Config.CouponTime,Config.PullToRefreshTime);
                //swipeRefreshLayout.setRefreshing(true);
                //PullToRefresh();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);

                        if (NetUtils.isConnected(context)) {
                            PullToRefresh();
                        }
                    }
                });
            }
        }).start();

    }

    private void PullToRefresh() {
   if (!NetUtils.isConnected(context)) {
            ToastUtil.showToast(context, "请先打开网络");
            return;
        }        isPullToRefresh = true;
        swipeRefreshLayout.setRefreshing(true);
        recyclerView.setAutoLoadMoreEnable(false);
        String url = Config.getAPI(Config.NowTime);
        TheApp.mAbHttpUtil.post(url, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //Logger.json("response content", content);

                Gson gson = new Gson();
                NowTime nowTime = gson.fromJson(content,NowTime.class);
                if(nowTime.getStatus().equals(Config.SUCCESS)){
                    AbSharedUtil.putInt(context,Config.CouponPage,1);
                    AbSharedUtil.putString(context,Config.CouponTime,nowTime.getNowtime());
                    getCouponList();
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
                Toast.makeText(context, "获取服务器当前时间失败，数据更新失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCouponList() {

        String url = Config.getAPI(Config.APIShowCoupon);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName,AbSharedUtil.getString(context,Config.UserName));
        params.put(Config.Token,AbSharedUtil.getString(context,Config.Token));
        params.put(Config.Page, AbSharedUtil.getInt(context, Config.CouponPage));
        params.put(Config.Rows, Config.RowNumber);

        params.put(Config.FlushTime, AbSharedUtil.getString(context, Config.CouponTime));
        //Log.w("haha222223", AbSharedUtil.getString(context, Config.CouponTime));
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
//                Logger.json("onSuccess content", content);
                if (HttpResultUtil.handleUnloginErrorStatus(content, RefleshCuponFragment.getInstance(), ActivityCoupon.this)) {
                    return;
                }
                CouponBean couponBean = new Gson().fromJson(content, CouponBean.class);
                if(couponBean.getStatus().equals(Config.SUCCESS)){
                    if(isPullToRefresh){
                        if (couponBean.getList()!=null && couponBean.getList().size()>0)
                            greenUtils.getCouponDao().deleteAll();

                        ifHadPullToRefreshedSucess = true;
                        adapter.setCoupon(couponBean.getList());
                        recyclerView.notifyMoreFinish(true,1);
                        if(couponBean.getList().size()<Config.RowNumber)
                            recyclerView.setAutoLoadMoreEnable(false);
                    }else {
                        adapter.addCoupon(couponBean.getList());
                        recyclerView.notifyMoreFinish(true,0);
                    }
                    for (Coupon coupon : couponBean.getList()) {
                        greenUtils.insertOrReplaceCoupon(coupon);
                    }
                    //tool.insertCouponTable(context,couponBean.getList());

                    if (couponBean.getList().size()==0){
                        /*isShowFooter = false;
                        adapter.setFootViewVisible(false);*/
                        recyclerView.setAutoLoadMoreEnable(false);
                    }
                    AbSharedUtil.putInt(TheApp.instance, Config.CouponPage, AbSharedUtil.getInt(context, Config.CouponPage) + 1);
                }
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
                recyclerView.setAutoLoadMoreEnable(true);
            }
        });
    }

    public void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PullToRefresh();
            }
        });

        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (ifHadPullToRefreshedSucess)//如果已经 进行了刷新操作，那么就从网络获取数据
                    getCouponList();
                else {
                    LoadingDataFromDatabase();//如果没有 进行了刷新操作，那么就从数据库获取数据
                }
            }
        });

        adapter.setOnClickListener(new CouponAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, Coupon coupon, int position) {
                Intent intent = new Intent(TheApp.instance, ActivityCouponDetail.class);
                Bundle bundle = new Bundle();
                bundle.putInt("activityType", 1);
                bundle.putInt("ActivityCouponCouponID", coupon.getCouponID());
                bundle.putInt("width", coupon.getWidth());
                bundle.putInt("height", coupon.getHeight());
                bundle.putInt("couponPraise", coupon.getCouponPraise());
                bundle.putInt("browserNum", coupon.getBrowserNum());
                bundle.putString("couponPics", coupon.getCouponPics());
                bundle.putBoolean("isPraise", coupon.getIsPraise());
                bundle.putBoolean("isCollect", coupon.getIsCollect());
                bundle.putInt("shopID", coupon.getShopID());
                intent.putExtra("coupon", bundle);
                intent.putExtra("ActivityCouponCouponPosition", position);
                intent.putExtra("shopName", coupon.getShopName());
                //Log.i("ActivityNewsHistory", "ShopID:" + coupon.getShopID());

                startActivity(intent);
            }

            @Override
            public void ItemLongClickListener(View view, int position) {

            }
        });

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }

    private void LoadingDataFromDatabase() {
        /*new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();*/
        List<Coupon> clist = getCouponListFromLocalNative();
        adapter.addCoupon(clist);
        recyclerView.notifyMoreFinish(true, 0);

        if (clist.size()<Config.RowNumber){
            recyclerView.setAutoLoadMoreEnable(false);
        }

        isPullToRefresh = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    private List<Coupon> getCouponListFromLocalNative() {
        //List<CouponBean.Coupon> clist= tool.queryCouponList(context,currentreferencePage++,referenceCouponId);
        List<Coupon> clist = greenUtils.queryCouponList(context,++currentreferencePage, (int) referenceCouponId);
        //Log.w("hahasi",clist.toString());
        return clist;
    }

    public static int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value: lastPositions){
            max = value>max?value:max;
        }
        return max;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEventMainThread(RefleshCuponFragment event) {
        String msg = "RefleshCuponFragment";
        //Log.d("eventbus", msg);
        if (NetUtils.isConnected(TheApp.instance)) {
            if (!isPrepared)
                return;
            //已经准备好，而且已经加载过 数据
            PullToRefresh();
        }

    }
    @Subscribe
    public void onEventMainThread(UpdateActivityCoupon event) {
        String msg = "UpdateActivityCoupon";
        Log.d("eventbus", msg);
        boolean isPraise = event.isPraise();
        boolean isCollect = event.isCollect();
        int praiseNum = event.getPraiseNum();
        int browseNum = event.getBrowseNum();
        int position = event.getPosition();
        adapter.updataView(position, isPraise, isCollect, praiseNum, browseNum);
    }
}
