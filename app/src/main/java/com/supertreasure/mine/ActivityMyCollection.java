package com.supertreasure.mine;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
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
import com.supertreasure.bean.CollectionBeen;
import com.supertreasure.eventbus.RefleshMyCollectionFragment;
import com.supertreasure.main.TheApp;
import com.supertreasure.service.HttpResultUtil;
import com.supertreasure.trade.ActivityGoodDetail;
import com.supertreasure.util.Config;
import com.supertreasure.util.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ActivityMyCollection extends AbActivity implements View.OnClickListener{
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private Button bt_title_left;
    private TextView tv_title;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView nullContent;
    private List<CollectionBeen.Collection> list_collections;
    private CollectionAdapter adapter;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        EventBus.getDefault().register(this);
        SwipeBackHelper.onCreate(this);

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

        adapter.setmOnItemClickListener(new CollectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(CollectionAdapter.ContentViewHolder holder, int position) {

                int sellID = adapter.getList_collection().get(position).getSellID();
                Intent intent = new Intent(TheApp.instance, ActivityGoodDetail.class);
                intent.putExtra(Config.SellID, sellID);
                intent.putExtra(Config.UserName, AbSharedUtil.getString(ActivityMyCollection.this, Config.UserName));
                intent.putExtra(Config.Token, AbSharedUtil.getString(ActivityMyCollection.this, Config.Token));
                startActivity(intent);
                //adapter.addData(position,new CollectionBeen("http://172.18.0.157:8080/AndroidWebData/image/picture1.jpg","电脑","8000"));
                Log.i("ActivityMyCollection", "onItemClick position=" + position);
                //MyUtils.show(ActivityMyCollection.this,"onItemClick position="+position+"    textView_content="+holder.textView_content.getText().toString());
            }

            @Override
            public void onItemLongClick(CollectionAdapter.ContentViewHolder holder, int position) {
                //adapter.removeData(position);
                Log.i("ActivityMyCollection", "onItemLongClick position=" + position);
                //MyUtils.show(ActivityMyCollection.this,"onItemLongClick position="+position+"    textView_content="+holder.textView_content.getText().toString());

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                PullToRefresh();
                Log.i("ActivityMyCollection", "swipeRefreshLayout onRefresh() ");
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
        tv_title.setText("我的收藏");
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        ll_title_right.setVisibility(View.INVISIBLE);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);

        //listView_coupons = (ListView) findViewById(R.id.listview_mycoupon);
    }
    private void initData() {
        //这里 应该是从网络中获取数据
        list_collections = new ArrayList<CollectionBeen.Collection>();

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeColors(R.color.colorwhite, R.color.colorgray, R.color.grey_backgroud, R.color.colorwhite);

        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);

        adapter = new CollectionAdapter(this);

        recyclerView.setAdapter(adapter);
        //这里 应该是从网络中获取数据
        isPrepared = true;
        PullToRefresh();

    } private void PullToRefresh() {
        //AbSharedUtil.putInt(context,Config.MyShopPublishPage,1);
        //isPullToRefresh = true;
        swipeRefreshLayout.setRefreshing(true);
        getCollectionList();
    }
    private void getCollectionList(){
        String url = Config.getAPI(Config.APIMyCollection);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this, Config.Token));

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Logger.i("response content", content);
                if (HttpResultUtil.handleUnloginErrorStatus(content, RefleshMyCollectionFragment.getInstance(), ActivityMyCollection.this)) {
                    return;
                }
                Gson gson = new Gson();
                CollectionBeen collectionBeen = gson.fromJson(content, CollectionBeen.class);

                if (collectionBeen.getStatus().equals(Config.SUCCESS)) {
                    list_collections.clear();


                    if (collectionBeen.getPublish() != null && collectionBeen.getPublish().size() != 0) {
                        Logger.i("response content", collectionBeen.getPublish().toString());
                        CollectionBeen.Collection collection_public = new CollectionBeen.Collection();
                        collection_public.setType(CouponAdapter.TYPE_GROUP);
                        collection_public.setIntroduction("发布中");
                        list_collections.add(collection_public);
                        list_collections.addAll(collectionBeen.getPublish());
                    }
                    if (collectionBeen.getSold() != null && collectionBeen.getSold().size() != 0) {
                        Logger.i("response content", collectionBeen.getSold().toString());
                        CollectionBeen.Collection collection_sold = new CollectionBeen.Collection();
                        collection_sold.setType(CouponAdapter.TYPE_GROUP);
                        collection_sold.setIntroduction("已出售");
                        list_collections.add(collection_sold);
                        list_collections.addAll(collectionBeen.getSold());
                    }
                    if (collectionBeen.getRemove() != null && collectionBeen.getRemove().size() != 0) {
                        Logger.i("response content", collectionBeen.getRemove().toString());
                        CollectionBeen.Collection collection_remove = new CollectionBeen.Collection();
                        collection_remove.setType(CouponAdapter.TYPE_GROUP);
                        collection_remove.setIntroduction("已下架");
                        list_collections.add(collection_remove);
                        list_collections.addAll(collectionBeen.getRemove());
                    }


                    adapter.setList_collection(list_collections);
                    //recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

                swipeRefreshLayout.setRefreshing(false);
                showView(list_collections.size());

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Toast.makeText(ActivityMyCollection.this, content, Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
                showView(list_collections.size());

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_title_left:
                //MyUtils.show(this,"返回按钮");
                finish();
                break;
            case R.id.ll_title_left:
                //MyUtils.show(this,"返回按钮");
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
    public void onEventMainThread(RefleshMyCollectionFragment event) {
        String msg = "RefleshMyCollectionFragment";
        Log.d("eventbus", msg);
        if (NetUtils.isConnected(TheApp.instance)) {
            if (!isPrepared)
                return;
            //已经准备好，而且已经加载过 数据
            PullToRefresh();
        }

    }
}
