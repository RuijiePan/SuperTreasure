package com.supertreasure.trade;

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
import com.supertreasure.bean.GoodsBeen;
import com.supertreasure.main.TheApp;
import com.supertreasure.mine.DividerItemDecoration;
import com.supertreasure.mine.GoodsAdapter;
import com.supertreasure.util.Config;
import com.supertreasure.util.LoadMoreRecyclerView;

import java.util.List;

/**
 * Created by prj on 2016/3/25.
 */
public class ActivityHisStore extends AbActivity implements View.OnClickListener{

    private int userID;
    private List<GoodsBeen.Goods> list_goods;
    private Context context;
    private LoadMoreRecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GoodsAdapter adapter;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private Button bt_title_left;
    private Button bt_title_right;
    private TextView tv_title;
    private TextView nullContent;
    private boolean isPullToRefresh;
    /*private boolean isLoadingMore;
    private View mFootView;*/

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_his_store);
        SwipeBackHelper.onCreate(this);

        initView();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    private void initView() {
        context = this;
        AbViewUtil.scaleContentView((LinearLayout)findViewById(R.id.root));
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        nullContent = (TextView) findViewById(R.id.nullcontent);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerView);

        tv_title.setText("他的小店");
        ll_title_right.setVisibility(View.INVISIBLE);
    }

    private void initData() {
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeColors(R.color.colorwhite, R.color.colorgray, R.color.grey_backgroud, R.color.colorwhite);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAutoLoadMoreEnable(false);

        adapter = new GoodsAdapter(context);
        recyclerView.setAdapter(adapter);

        userID = getIntent().getIntExtra(Config.UserID,0);
        PullToRefresh();
    }

    private void setListener() {

        bt_title_left.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);

        adapter.setmOnItemClickListener(new GoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GoodsAdapter.ViewHolder holder, int position, String sellID) {
                Intent intent = new Intent(context, ActivityMyselfGoodDetail.class);
                intent.putExtra(Config.SellID,Integer.parseInt(sellID));
                intent.putExtra(Config.UserName,AbSharedUtil.getString(context,Config.UserName));
                intent.putExtra(Config.Token,AbSharedUtil.getString(context,Config.Token));
                intent.putExtra(Config.ShowStore,false);
                intent.putExtra(Config.SoldOrRemove,true);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(GoodsAdapter.ViewHolder holder, int position, String sellID) {

            }
        });
    }

    private void PullToRefresh() {
        AbSharedUtil.putInt(context, Config.HisShopPublishPage,1);
        isPullToRefresh = true;
        swipeRefreshLayout.setRefreshing(true);
        getGoodList();
    }

    private void getGoodList() {

        String url = Config.getAPI(Config.APIMarketStore);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(context, Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(context, Config.Token));
        params.put(Config.Page, AbSharedUtil.getInt(context, Config.HisShopPublishPage));
        params.put(Config.UserID, userID);
        params.put(Config.Rows, Config.MyShopRowNumber);

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {

                //-------------------------------------------------
                Log.i("ActivityHisStore", content);
                Gson gson = new Gson();
                GoodsBeen goodsBeen = gson.fromJson(content, GoodsBeen.class);
                if (goodsBeen.getStatus().equals(Config.SUCCESS)) {
                    if (isPullToRefresh) {
                        list_goods = goodsBeen.getList();
                        adapter.setList_goods(list_goods);
                        recyclerView.notifyMoreFinish(true,1);
                        if(goodsBeen.getList().size()<Config.RowNumber)
                            recyclerView.setAutoLoadMoreEnable(false);
                        else
                            recyclerView.setAutoLoadMoreEnable(true);
                    } else{
                        adapter.addList(goodsBeen.getList());
                        recyclerView.notifyMoreFinish(true,0);
                    }

                    if (goodsBeen.getList().size() == 0) {
                        recyclerView.setAutoLoadMoreEnable(false);
                    }
                    AbSharedUtil.putInt(context, Config.HisShopPublishPage, AbSharedUtil.getInt(context, Config.HisShopPublishPage) + 1);
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);

                showView(list_goods.size());
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Toast.makeText(context, content, Toast.LENGTH_LONG).show();

                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);

                showView(list_goods.size());
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
        switch (v.getId()){
            case R.id.bt_title_left:
                finish();
                break;
            case R.id.ll_title_left:
                finish();
                break;
        }
    }
}
