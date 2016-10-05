package com.supertreasure.mine.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.supertreasure.bean.GoodsBeen;
import com.supertreasure.eventbus.RefleshMyShopPublish;
import com.supertreasure.main.TheApp;
import com.supertreasure.mine.DividerItemDecoration;
import com.supertreasure.mine.GoodsAdapter;
import com.supertreasure.service.HttpResultUtil;
import com.supertreasure.trade.ActivityMyselfGoodDetail;
import com.supertreasure.util.Config;
import com.supertreasure.util.LoadMoreRecyclerView;
import com.supertreasure.util.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yum on 2016/3/2.
 */
public class PublishFragment extends BaseFragment{
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    /*private int[] lastPositions ;
    private int lastVisibleItemPosition;*/

    private Context context;
    private LoadMoreRecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private GoodsAdapter adapter;

    Animation anim_rotate180;
    List<GoodsBeen.Goods> list_goods;

    private boolean isPullToRefresh;
    /*private boolean isLoadingMore;
    //private boolean isShowFooter;
    private View mFootView;*/
    private TextView nullContent;


    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.activity_myshopfragement,null);
        AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.root));
        context = getContext();
        nullContent = (TextView) view.findViewById(R.id.nullcontent);

        anim_rotate180 = AnimationUtils.loadAnimation(context,R.anim.rotate180);
        anim_rotate180.setFillAfter(true);//保留运动后的状态

        recyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recyclerView_goods);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        return view;
    }

    @Override
    public void initData() {
        list_goods = new ArrayList<GoodsBeen.Goods>();

        linearLayoutManager = new LinearLayoutManager(TheApp.instance);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeColors(R.color.colorwhite, R.color.colorgray, R.color.grey_backgroud, R.color.colorwhite);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(TheApp.instance, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAutoLoadMoreEnable(false);

        adapter = new GoodsAdapter(context);
        recyclerView.setAdapter(adapter);
        isPrepared = true;
        lazyLoad();

    }

    @Override
    public void setListener() {
        adapter.setmOnItemClickListener(new GoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GoodsAdapter.ViewHolder holder, int position ,String sellID) {
                Log.i("ActivityMyShop","onItemClick position="+position);
                Intent intent = new Intent(TheApp.instance, ActivityMyselfGoodDetail.class);

                intent.putExtra(Config.SellID,Integer.parseInt(sellID));
                intent.putExtra(Config.UserName,AbSharedUtil.getString(TheApp.instance,Config.UserName));
                intent.putExtra(Config.Token,AbSharedUtil.getString(TheApp.instance,Config.Token));
                intent.putExtra(Config.ShowStore,false);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(GoodsAdapter.ViewHolder holder, int position,String sellID) {
                Log.i("ActivityMyShop","onItemLongClick position="+position);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                PullToRefresh();
                Log.i("ActivityMyShop","swipeRefreshLayout onRefresh() ");
            }
        });
        //自动加载更多
       /* recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem = 0;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                *//*int totalItemCount = linearLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 4 && dy > 0) {
                    if(isLoadingMore){
                        //Log.d(TAG,"ignore manually update!");
                    } else{
                        LodingMore();;//这里多线程也要手动控制isLoadingMore
                        mFootView.setVisibility(View.VISIBLE);
                        isLoadingMore = false;
                    }
                }*//*

                //Log.i("ActivityMyShop","recyclerView onScrolled"+"  lastVisibleItem="+lastVisibleItem+"  dx="+dx+"  dy="+dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //Log.i("ActivityMyShop","recyclerView onScrollStateChanged"+"  newState="+newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()&& !isLoadingMore&!isPullToRefresh) {
                    LodingMore();
                }
            }
        });*/

        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                getGoodList();
            }
        });
    }

    @Override
    public void onClick(View v) {
    }

    private void PullToRefresh() {
        AbSharedUtil.putInt(TheApp.instance,Config.MyShopPublishPage,1);
        isPullToRefresh = true;
        swipeRefreshLayout.setRefreshing(true);
        getGoodList();
    }

    private void getGoodList() {

        String url = Config.getAPI(Config.APIMyOwnGoods);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance, Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance, Config.Token));
        params.put(Config.Page, AbSharedUtil.getInt(TheApp.instance, Config.MyShopPublishPage));
        params.put(Config.Status, Config.Content_Goods_Publish);
        params.put(Config.Rows, Config.MyShopRowNumber);

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //-------------------------------------------------
//                Logger.i("onSuccess content", content);
                if (HttpResultUtil.handleUnloginErrorStatus(content, RefleshMyShopPublish.getInstance(), PublishFragment.this.getActivity())) {
                    return;
                }
                Gson gson = new Gson();
                GoodsBeen goodsBeen = gson.fromJson(content, GoodsBeen.class);
                if (goodsBeen.getStatus().equals(Config.SUCCESS)) {
                    if (isPullToRefresh) {
                        list_goods = goodsBeen.getList();
                        adapter.setList_goods(list_goods);
                        recyclerView.notifyMoreFinish(true, 1);
                        if (goodsBeen.getList().size() < Config.RowNumber)
                            recyclerView.setAutoLoadMoreEnable(false);
                        else
                            recyclerView.setAutoLoadMoreEnable(true);
                    } else {
                        adapter.addList(goodsBeen.getList());
                        recyclerView.notifyMoreFinish(true, 0);
                    }

                    if (goodsBeen.getList().size() == 0) {
                        recyclerView.setAutoLoadMoreEnable(false);
                    }
                    AbSharedUtil.putInt(TheApp.instance, Config.MyShopPublishPage, AbSharedUtil.getInt(TheApp.instance, Config.MyShopPublishPage) + 1);
                }
                //handler.sendEmptyMessage(1);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                //isLoadingMore = false;
                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);

                showView(list_goods.size());
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Toast.makeText(TheApp.instance, content, Toast.LENGTH_LONG).show();
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
    protected void lazyLoad() {//已经初始化完成，并且可见，并且第一次加载才  运行加载.
        if (!isPrepared || !isVisible || mHasLoadedOnce)
            return;
        mHasLoadedOnce = true;

        PullToRefresh();

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
    @Subscribe
    public void onEventMainThread(RefleshMyShopPublish event) {
        String msg = "RefleshMyShopPublish";
        Log.d("eventbus", msg);
        if (NetUtils.isConnected(TheApp.instance)) {
            if (!isPrepared||!mHasLoadedOnce)
                return;
            //已经准备好，而且已经加载过 数据
            PullToRefresh();
        }
    }
}
