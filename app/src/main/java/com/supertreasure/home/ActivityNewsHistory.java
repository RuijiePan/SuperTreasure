package com.supertreasure.home;

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
import com.supertreasure.bean.Praise;
import com.supertreasure.bean.Topnews;
import com.supertreasure.main.TheApp;
import com.supertreasure.mine.DividerItemDecoration;
import com.supertreasure.mine.GoodsAdapter;
import com.supertreasure.trade.ActivityMyselfGoodDetail;
import com.supertreasure.util.Config;
import com.supertreasure.util.LoggerUtil;

import java.util.List;

import me.itangqi.greendao.New;

/**
 * Created by prj on 2016/3/25.
 */
public class ActivityNewsHistory extends AbActivity implements View.OnClickListener{

    private String adminID;
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
    private NewsHistoryAdapter adapter;
    private boolean isPullToRefresh;
    private boolean isLoadingMore;
    private View mFootView;
    private List<New> list;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_history);
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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        tv_title.setText("历史头条");
        ll_title_right.setVisibility(View.INVISIBLE);
    }

    private void initData() {
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

        adapter = new NewsHistoryAdapter(this);
        adapter.setFooterView(mFootView);
        recyclerView.setAdapter(adapter);

        adminID = getIntent().getStringExtra(Config.AdminID);
        PullToRefresh();
    }

    private void setListener() {

        bt_title_left.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);

        adapter.setOnClickListener(new NewsHistoryAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, String topId,String url,boolean isShowHistory,int position) {


                Intent intent = new Intent(TheApp.instance, ActivityNewsDetail.class);
                intent.putExtra(Config.URL, url);
                intent.putExtra(Config.IsShowHistory,isShowHistory);
                intent.putExtra(Config.TopNewsBrowse, adapter.getNewses().get(position).getBrowserNum());
                intent.putExtra(Config.TopNewsPrise, adapter.getNewses().get(position).getPraise());
                intent.putExtra(Config.TopNewsIsPrise, adapter.getNewses().get(position).getIsPraised());

                intent.putExtra(Config.Type,Config.NewsIn);
                intent.putExtra(Config.TopID,adapter.getNewses().get(position).getTopId());
                intent.putExtra(Config.Position,position);
                intent.putExtra(Config.AdminID, adapter.getNewses().get(position).getAdminID());
                startActivityForResult(intent, Config.REQUEST_CODE);



            }

            @Override
            public void ItemClickListener(final View iv_like, final TextView tv_like, String topId,final int position ) {

               /* AbRequestParams params = new AbRequestParams();
                params.put(Config.UserName,AbSharedUtil.getString(TheApp.instance,Config.UserName));
                params.put(Config.Token,AbSharedUtil.getString(TheApp.instance,Config.Token));
                params.put(Config.TopID, topId);
                String url = Config.getAPI(Config.Praise);
                TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        Gson gson = new Gson();
                        Praise praise = gson.fromJson(content, Praise.class);
                        //Toast.makeText(TheApp.instance,praise.getPraiseTimes(),Toast.LENGTH_SHORT).show();
                        if (Integer.parseInt(praise.getPraiseTimes())-Integer.parseInt(tv_like.getText().toString())<0) {
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

                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        Toast.makeText(TheApp.instance,statusCode,Toast.LENGTH_SHORT).show();
                    }
                });*/
            }

            @Override
            public void ItemLongClickListener(View view, int position) {
                //adapter.removeTopnews(position);
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

                if (lastVisibleItem >= totalItemCount - Config.AutoLoadingNum && dy > 0 && !isLoadingMore&&!isPullToRefresh) {
                    LodingMore();
                }
            }
        });
    }

    private void PullToRefresh() {
        AbSharedUtil.putInt(TheApp.instance, Config.NewsHistoryPage,1);
        isPullToRefresh = true;
        swipeRefreshLayout.setRefreshing(true);
        getNewsList();
    }

    private void LodingMore() {
        isLoadingMore = true;
        mFootView.setVisibility(View.VISIBLE);
        //adapter.setFootViewVisible(true);
        adapter.setFooterView(mFootView);

        getNewsList();
    }

    private void getNewsList() {

        String url = Config.getAPI(Config.APINewsHistory);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance, Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance, Config.Token));
        params.put(Config.Page, AbSharedUtil.getInt(TheApp.instance, Config.NewsHistoryPage));
        params.put(Config.AdminID,adminID);
        params.put(Config.Rows, Config.RowNumber);
        /*Log.w("haha",url);
        Log.w("haha",AbSharedUtil.getString(TheApp.instance, Config.UserName));
        Log.w("haha",AbSharedUtil.getString(TheApp.instance, Config.Token));
        Log.w("haha",AbSharedUtil.getInt(TheApp.instance, Config.NewsHistoryPage)+"");
        Log.w("haha",adminID+"");*/
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {

                //-------------------------------------------------
                Log.i("ActivityNewsHistory", content);
                Gson gson = new Gson();
                Topnews news = gson.fromJson(content, Topnews.class);
                //LoggerUtil.json("newsHistory onSuccess content",content);
                //LoggerUtil.d("newsHistory onSuccess", "adminID:" + adminID);
                //LoggerUtil.d("newsHistory onSuccess", "Topnews"+news.toString());
                if (news.getStatus().equals(Config.SUCCESS)) {
                    if (isPullToRefresh) {
                        list = news.getList();
                        adapter.setTopnews(list);
                        //isShowFooter = true;
                    } else if(isLoadingMore){
                        //list_goods.addAll(goodsBeen.getList());
                        adapter.addTopnews(news.getList());
                    }

                    if (news.getList().size() == 0) {
                        adapter.setFooterView(null);
                    }
                    AbSharedUtil.putInt(TheApp.instance, Config.NewsHistoryPage, AbSharedUtil.getInt(TheApp.instance, Config.NewsHistoryPage) + 1);
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
