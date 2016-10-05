package com.supertreasure.mine;

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
import com.orhanobut.logger.Logger;
import com.supertreasure.R;
import com.supertreasure.bean.CouponBeen;
import com.supertreasure.eventbus.RefleshMyCuponFragment;
import com.supertreasure.main.TheApp;
import com.supertreasure.service.HttpResultUtil;
import com.supertreasure.util.Config;
import com.supertreasure.util.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ActivityMyCoupon extends AbActivity implements View.OnClickListener{
    private boolean isPrepared;

    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private Button bt_title_left;
    private TextView tv_title;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<CouponBeen.Coupon> list_coupons;
    private TextView nullContent;
    private CouponAdapter adapter;
    //private CouponAdapter adapter_coupon;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        EventBus.getDefault().register(this);
        SwipeBackHelper.onCreate(this);

        initView();
        initData();//不知为何post不能放到 子线程去执行，执行 就没反应
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

        adapter.setmOnItemClickListener(new CouponAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CouponAdapter.ContentViewHolder holder, int position) {
                //adapter.addData(position,new CouponBeen("new1.png","new1",new Date(System.currentTimeMillis()).toString(),new Date(System.currentTimeMillis()).toString()));
                String couponPic  = adapter.getList_coupons().get(position).getCouponPics();
                int couponWidth   = adapter.getList_coupons().get(position).getWidth();
                int couponHeight  = adapter.getList_coupons().get(position).getHeight();
                Intent intent = new Intent(TheApp.instance, ActivityMyCouponDetial.class);
                Bundle bundle = new Bundle();
                bundle.putInt("width",couponWidth);
                bundle.putInt("height", couponHeight);
                bundle.putString("couponPics", couponPic);
                intent.putExtra("coupon", bundle);
                startActivity(intent);

                Log.i("ActivityMyCoupon", "onItemClick position=" + position);
                //MyUtils.show(ActivityMyCoupon.this,"onItemClick position="+position+"    textView_content="+holder.textView_content.getText().toString());
            }

            @Override
            public void onItemLongClick(CouponAdapter.ContentViewHolder holder, int position) {
                //adapter.removeData(position);
                Log.i("ActivityMyCoupon", "onItemLongClick position=" + position);
                //MyUtils.show(ActivityMyCoupon.this,"onItemLongClick position="+position+"    textView_content="+holder.textView_content.getText().toString());

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                PullToRefresh();
                //Log.i("ActivityMyCoupon","swipeRefreshLayout onRefresh() ");
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem = 0;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //System.out.println("recyclerView onScrolled"+"  dx="+dx+"  dy="+dy);

                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                //System.out.println("recyclerView onScrolled"+"  lastVisibleItem="+lastVisibleItem);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
                System.out.println("recyclerView onScrollStateChanged newState=" + newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    //swipeRefreshLayout.setRefreshing(true);
                    // 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
                    //handler.sendEmptyMessageDelayed(0, 3000);
                }
            }
        });
    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));
        nullContent = (TextView) findViewById(R.id.nullcontent);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_coupon);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("我的优惠券");
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        ll_title_right.setVisibility(View.INVISIBLE);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
    }
    private void initData() {
        list_coupons = new ArrayList<CouponBeen.Coupon>();
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeColors(R.color.colorwhite, R.color.colorgray, R.color.grey_backgroud, R.color.colorwhite);
        //swipeRefreshLayout.setRefreshing(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);
        //listView_coupons = (ListView) findViewById(R.id.listview_mycoupon);

        adapter = new CouponAdapter(this);
        recyclerView.setAdapter(adapter);

        isPrepared = true;
        //这里 应该是从网络中获取数据
        PullToRefresh();
    }
    private void PullToRefresh() {
        //AbSharedUtil.putInt(context,Config.MyShopPublishPage,1);
        //isPullToRefresh = true;
        swipeRefreshLayout.setRefreshing(true);
        getCouponList();
    }
    private void getCouponList(){

        String url = Config.getAPI(Config.APIMycoupon);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this, Config.Token));


        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Logger.json("response content", content);
                if (HttpResultUtil.handleUnloginErrorStatus(content, RefleshMyCuponFragment.getInstance(), ActivityMyCoupon.this)) {
                    return;
                }
                Gson gson = new Gson();
                CouponBeen couponBeen = gson.fromJson(content, CouponBeen.class);
                Logger.i("coupon", couponBeen.toString());

                if (couponBeen.getStatus().equals(Config.SUCCESS)) {
                    list_coupons.clear();
                    Log.i("couponBeen getIn size", couponBeen.getIn().size() + "");
                    Log.i("couponBeen getOut size", couponBeen.getOut().size() + "");

                    if (couponBeen.getIn() != null && couponBeen.getIn().size() != 0) {
                        CouponBeen.Coupon coupon_in = new CouponBeen.Coupon();
                        coupon_in.setType(CouponAdapter.TYPE_GROUP);
                        coupon_in.setIntro("有效优惠卷（注:使用时间请看优惠卷详情）");
                        list_coupons.add(coupon_in);
                        list_coupons.addAll(couponBeen.getIn());
                    }
                    if (couponBeen.getOut() != null && couponBeen.getOut().size() != 0) {
                        CouponBeen.Coupon coupon_out = new CouponBeen.Coupon();
                        coupon_out.setType(CouponAdapter.TYPE_GROUP);
                        coupon_out.setIntro("已失效的优惠卷（注:优惠时间过了！)");
                        list_coupons.add(coupon_out);
                        list_coupons.addAll(couponBeen.getOut());
                    }

                    adapter.setList_coupons(list_coupons);
                    showView(list_coupons.size());
                    //recyclerView.setAdapter(adapter);
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
                Toast.makeText(ActivityMyCoupon.this, content, Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_title_left:
                //ToastUtil.showToast(this,"返回按钮");
                finish();
                break;
            case R.id.ll_title_left:
                //ToastUtil.showToast(this,"返回按钮");
                finish();
                break;
        }
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
    @Subscribe
    public void onEventMainThread(RefleshMyCuponFragment event) {
        String msg = "RefleshMyCuponFragment";
        Log.d("eventbus", msg);
        if (NetUtils.isConnected(TheApp.instance)) {
            if (!isPrepared)
                return;
            //已经准备好，而且已经加载过 数据
            PullToRefresh();
        }

    }
}
