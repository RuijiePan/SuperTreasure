package com.supertreasure.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.supertreasure.R;
import com.supertreasure.base.BaseFragment;
import com.supertreasure.bean.CouponBean;
import com.supertreasure.bean.NowTime;
import com.supertreasure.database.DBTool;
import com.supertreasure.main.TheApp;
import com.supertreasure.coupon.ActivityCouponDetail;
import com.supertreasure.coupon.CouponAdapter;
import com.supertreasure.util.Config;
import com.supertreasure.util.LoadMoreRecyclerView;
import com.supertreasure.util.NetUtils;

import java.util.ArrayList;
import java.util.List;

import me.itangqi.greendao.Coupon;

/**
 * Created by Administrator on 2016/1/27.
 */
public class CouponFragment extends BaseFragment {

    private DBTool tool;
    private SimpleDraweeView draweeView;
    private int[] lastPositions ;
    private int lastVisibleItemPosition;
    private List<Coupon> couponList;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreRecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private CouponAdapter adapter;
    //private boolean isLoadingMore = false;
    private boolean isPullToRefresh = false;
    private View mFootView;
    //private boolean isContinueLoading = false;
    public static final int REQUEST_CODE = 10086;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == resultCode) {
            boolean isPraise = data.getBooleanExtra("isPraise", false);
            boolean isCollect = data.getBooleanExtra("isCollect", false);
            int praiseNum = data.getIntExtra("praiseNum", 0);
            int browseNum = data.getIntExtra("browseNum", 0);
            int position = data.getIntExtra("position", 0);
            adapter.updataView(position, isPraise, isCollect, praiseNum, browseNum);
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.layout_trade_coupon,null);
        AbViewUtil.scaleContentView((FrameLayout) view.findViewById(R.id.root));
        context = getContext();
        draweeView = (SimpleDraweeView) view.findViewById(R.id.draweeView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void initData() {
        //tool = DBTool.getInstance(context);
        //couponList = DBTool.getInstance(context).queryCouponList(context);
        //-------------------------------------------------------------------

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
        recyclerView.addItemDecoration(new SpaceItemDecoration(10));

        System.out.println("=======================");
        //int referenceCouponId = tool.queryCouponCurrentNewestId(context);
        int currentPage = 1;
       /* couponList = tool.queryCouponList(context,currentPage,referenceCouponId);
        if (couponList == null) couponList = new ArrayList<>();

        adapter = new CouponAdapter(context,couponList);*/
        recyclerView.setAdapter(adapter);

        mFootView = View.inflate(context,R.layout.recyclerview_footer,null);
        //SimpleDraweeView draweeView = (SimpleDraweeView) mFootView.findViewById(R.id.draweeView);
        /*Resources r = null;
        Uri uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.drawable.ic_loading) + "/"
                + r.getResourceTypeName(R.drawable.ic_loading) + "/"
                + r.getResourceEntryName(R.drawable.ic_loading));
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        draweeView.setController(controller);*/
        //adapter.setmFootView(mFootView);

        /*AbSharedUtil.putInt(context,Config.CouponPage,1);
        AbSharedUtil.putString(context,Config.CouponTime,Config.PullToRefreshTime);*/
        //swipeRefreshLayout.setRefreshing(true);
        if (NetUtils.isConnected(context))
            PullToRefresh();
       // PullToRefresh();
    }

    private void PullToRefresh() {

        isPullToRefresh = true;
        swipeRefreshLayout.setRefreshing(true);
        String url = Config.getAPI(Config.NowTime);
        TheApp.mAbHttpUtil.post(url, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                NowTime nowTime = gson.fromJson(content,NowTime.class);
                if(nowTime.getStatus().equals(Config.SUCCESS)){
                    AbSharedUtil.putString(TheApp.instance,Config.CouponTime,nowTime.getNowtime());
                    AbSharedUtil.putInt(TheApp.instance,Config.CouponPage,1);
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
        params.put(Config.Rows,Config.RowNumber);
        params.put(Config.FlushTime,AbSharedUtil.getString(context,Config.CouponTime));
        //Log.w("haha222223",AbSharedUtil.getString(context,Config.CouponTime));
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Log.w("haha222",content);
                CouponBean couponBean = new Gson().fromJson(content,CouponBean.class);
                if(couponBean.getStatus().equals(Config.SUCCESS)){
                    if(isPullToRefresh){
                        adapter.setCoupon(couponBean.getList());
                        recyclerView.notifyMoreFinish(true,1);
                        if(couponBean.getList().size()<Config.RowNumber)
                            recyclerView.setAutoLoadMoreEnable(false);
                    }else {
                        adapter.addCoupon(couponBean.getList());
                        recyclerView.notifyMoreFinish(true,0);
                    }

                    //tool.insertCouponTable(context,couponBean.getList());

                    if (couponBean.getList().size()==0){
                        //isContinueLoading = false;
                        //adapter.setFootViewVisible(true);
                        recyclerView.setAutoLoadMoreEnable(false);
                    }
                    //adapter.setFootViewVisible(true);
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
                Toast.makeText(TheApp.instance, content, Toast.LENGTH_LONG).show();
                //isLoadingMore = false;
                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void setListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PullToRefresh();
            }
        });

        //自动加载更多
        /*recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(lastPositions==null){
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
                int totalItemCount = adapter.getItemCount();

                if (lastVisibleItemPosition >= totalItemCount - Config.AutoLoadingNum && dy > 0 && !isLoadingMore&!isPullToRefresh&isContinueLoading) {
                    getCouponList();
                    adapter.setFootViewVisible(true);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                staggeredGridLayoutManager.invalidateSpanAssignments();
            }
        });*/
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                getCouponList();
            }
        });

        adapter.setOnClickListener(new CouponAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, Coupon coupon,int position) {
                Intent intent = new Intent(TheApp.instance, ActivityCouponDetail.class);
                Bundle bundle = new Bundle();
                bundle.putInt("couponID",coupon.getCouponID());
                bundle.putInt("width",coupon.getWidth());
                bundle.putInt("height",coupon.getHeight());
                bundle.putInt("couponPraise",coupon.getCouponPraise());
                bundle.putInt("browserNum",coupon.getBrowserNum());
                bundle.putString("couponPics",coupon.getCouponPics());
                bundle.putBoolean("isPraise",coupon.getIsPraise());
                bundle.putBoolean("isCollect",coupon.getIsCollect());
                intent.putExtra("coupon",bundle);
                intent.putExtra("position",position);
                startActivityForResult(intent,REQUEST_CODE);
            }

            @Override
            public void ItemLongClickListener(View view, int position) {

            }
        });

    }

    public static int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value: lastPositions){
            max = value>max?value:max;
        }
        return max;
    }

    @Override
    public void onClick(View v) {

    }

    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration{

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            if(parent.getChildPosition(view) != 0)
                outRect.top = space;
                outRect.right = space;
                outRect.left = space;
                outRect.bottom = space;
        }
    }
}
