package com.supertreasure.coupon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
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
import com.supertreasure.R;
import com.supertreasure.bean.CouponBean;
import com.supertreasure.eventbus.UpdateActivityCouponHistory;
import com.supertreasure.main.TheApp;
import com.supertreasure.mine.DividerItemDecoration;
import com.supertreasure.util.Config;
import com.supertreasure.util.LoggerUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.itangqi.greendao.Coupon;

/**
 * Created by prj on 2016/3/25.
 */
public class ActivityCouponHistory extends AbActivity implements View.OnClickListener{
    public static final int REQUEST_CODE = 10086;
    private Context context;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private Button bt_title_left;
    private Button bt_title_right;
    private TextView tv_title;
    private TextView nullContent;
    private CouponHistoryAdapter adapter;
    private boolean isPullToRefresh;
    private boolean isLoadingMore;
    private View mFootView;
    private List<Coupon> list;
    private int ActivityCouponCouponID;
    private int ActivityCouponCouponPosition;
    private String shopName;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_news_history);
        SwipeBackHelper.onCreate(this);

        initView();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);

        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    private void initView() {
        context = this;
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        nullContent = (TextView) findViewById(R.id.nullcontent);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //bt_title_right.setBackgroundResource(R.drawable.ic_history);
        tv_title.setText("");

        ll_title_right.setVisibility(View.INVISIBLE);
    }

    private void initData() {
        list = new ArrayList<>();
        ActivityCouponCouponID = getIntent().getIntExtra("ActivityCouponCouponID",-1);
        shopName = getIntent().getStringExtra("shopName");
        tv_title.setText(shopName);
        ActivityCouponCouponPosition = getIntent().getIntExtra("ActivityCouponCouponPosition",-1);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeColors(R.color.colorwhite, R.color.colorgray, R.color.grey_backgroud, R.color.colorwhite);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(TheApp.instance, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);

        mFootView = View.inflate(context,R.layout.recyclerview_footer,null);
        mFootView.setVisibility(View.GONE);

        adapter = new CouponHistoryAdapter(TheApp.instance);
        adapter.setFooterView(mFootView);
        recyclerView.setAdapter(adapter);

        PullToRefresh();
    }

    private void setListener() {

        bt_title_left.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);
/*        bt_title_right.setOnClickListener(this);
        ll_title_right.setOnClickListener(this);*/

        adapter.setOnClickListener(new CouponHistoryAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, int position) {
                Intent intent = new Intent(TheApp.instance, ActivityCouponDetail.class);
                Bundle bundle = new Bundle();
                bundle.putInt("activityType", 2);
                bundle.putInt("couponID", adapter.getCouponList().get(position).getCouponID());
                bundle.putInt("width", adapter.getCouponList().get(position).getWidth());
                bundle.putInt("height", adapter.getCouponList().get(position).getHeight());
                bundle.putInt("couponPraise", adapter.getCouponList().get(position).getCouponPraise());
                bundle.putInt("browserNum", adapter.getCouponList().get(position).getBrowserNum());
                bundle.putString("couponPics", adapter.getCouponList().get(position).getCouponPics());
                bundle.putBoolean("isPraise", adapter.getCouponList().get(position).getIsPraise());
                bundle.putBoolean("isCollect", adapter.getCouponList().get(position).getIsCollect());

                bundle.putInt("ActivityCouponCouponID", ActivityCouponCouponID);
                intent.putExtra("coupon", bundle);
                intent.putExtra("position", position);
                intent.putExtra("ActivityCouponCouponPosition", ActivityCouponCouponPosition);
                startActivity(intent);
            }

            @Override
            public void ItemLongClickListener(View view, int position) {

            }

        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PullToRefresh();
            }
        });

        //自动加载更多
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = adapter.getItemCount();

                if (lastVisibleItem >= totalItemCount - Config.AutoLoadingNum && dy > 0 && !isLoadingMore && !isPullToRefresh) {
                    LodingMore();
                }
            }
        });
    }

    private void PullToRefresh() {
        AbSharedUtil.putInt(TheApp.instance, Config.CouponHistoryPage, 1);
        isPullToRefresh = true;
        swipeRefreshLayout.setRefreshing(true);
        getCouponList();
    }

    private void LodingMore() {
        isLoadingMore = true;
        mFootView.setVisibility(View.VISIBLE);
        //adapter.setFootViewVisible(true);
        adapter.setFooterView(mFootView);

        getCouponList();
    }

    private void getCouponList() {

        String url = Config.getAPI(Config.APICouponHistory);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance, Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance, Config.Token));
        params.put(Config.Page, AbSharedUtil.getInt(TheApp.instance, Config.CouponHistoryPage));
        params.put(Config.ShopID,getIntent().getIntExtra("shopID", -1));
        params.put(Config.Rows, Config.RowNumber);

        //Log.i("ActivityNewsHistory", "UserName:" + AbSharedUtil.getString(TheApp.instance, Config.UserName));
        //Log.i("ActivityNewsHistory", "ShopID:" + getIntent().getIntExtra("shopID", -1));
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {

                //-------------------------------------------------
                //Log.i("ActivityNewsHistory", content);
                Gson gson = new Gson();
                CouponBean couponBean = gson.fromJson(content, CouponBean.class);
                LoggerUtil.json("couponHistory onSuccess content", content);
                //LoggerUtil.d("newsHistory onSuccess", "adminID:" + adminID);
                //LoggerUtil.d("newsHistory onSuccess", "Topnews"+news.toString());
                if (couponBean.getStatus().equals(Config.SUCCESS)) {
                    if (isPullToRefresh) {
                        list = couponBean.getList();
                        adapter.setCouponList(list);
                        //isShowFooter = true;
                    } else if (isLoadingMore) {
                        //list_goods.addAll(goodsBeen.getList());
                        adapter.addCouponList(couponBean.getList());
                    }

                    if (couponBean.getList().size() == 0) {
                        adapter.setFooterView(null);
                    }
                    AbSharedUtil.putInt(TheApp.instance, Config.CouponHistoryPage, AbSharedUtil.getInt(TheApp.instance, Config.CouponHistoryPage) + 1);
                }
                //handler.sendEmptyMessage(1);

            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                isLoadingMore = false;
                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);
                adapter.setFooterView(null);

                showView(list.size());
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Toast.makeText(TheApp.instance, content, Toast.LENGTH_LONG).show();
                isLoadingMore = false;
                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);
                adapter.setFooterView(null);
                showView(list.size());

            }
        });
    }

    public void showView(int number){
        if (number == 0)
            showNullContent();
        else
            showRecyclerView();
    }
    public void showNullContent(){
        swipeRefreshLayout.setVisibility(View.GONE);
        nullContent.setVisibility(View.VISIBLE);
    }
    public void showRecyclerView(){
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        nullContent.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.bt_title_left:
                finish();
                break;
            case R.id.ll_title_left:
                finish();
                break;

        }
    }

    @Subscribe
    public void onEventMainThread(UpdateActivityCouponHistory event) {
        String msg = "UpdateActivityCouponHistory";
        Log.d("eventbus", msg);

        boolean isPraise = event.isPraise();
        boolean isCollect = event.isCollect();
        int praiseNum = event.getPraiseNum();
        int browseNum = event.getBrowseNum();
        int position = event.getPosition();
        //int couponID = event.getCouponID();
        adapter.updataView(position, isPraise, isCollect, praiseNum, browseNum);

    }

}
