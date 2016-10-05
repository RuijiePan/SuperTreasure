package com.supertreasure.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.google.gson.Gson;
import com.supertreasure.R;
import com.supertreasure.base.BaseFragment;
import com.supertreasure.bean.GoodBean;
import com.supertreasure.bean.NowTime;
import com.supertreasure.eventbus.RefleshOtherProFragment;
import com.supertreasure.greenDaoUtils.GreenUtils;
import com.supertreasure.main.TheApp;
import com.supertreasure.service.HttpResultUtil;
import com.supertreasure.trade.ActivityGoodDetail;
import com.supertreasure.trade.ActivityMarketInPut;
import com.supertreasure.trade.ActivityMyselfGoodDetail;
import com.supertreasure.trade.DecorateAdapter;
import com.supertreasure.util.Bimp;
import com.supertreasure.util.Config;
import com.supertreasure.util.DividerStaggerItemDecoration;
import com.supertreasure.util.LoadMoreRecyclerView;
import com.supertreasure.util.NetUtils;
import com.supertreasure.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import me.itangqi.greendao.Goods;

/**
 * Created by Administrator on 2016/1/27.
 */
public class OtherFragment extends BaseFragment{
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    //private DBTool tool;
    private GreenUtils greenUtils;
    private FloatingActionButton fab;
    private int[] lastPositions ;
    private int lastVisibleItemPosition;
    private List<Goods> otherList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreRecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private DecorateAdapter adapter;
    //private boolean isLoadingMore = false;
    private boolean isPullToRefresh = false;
    /*private View mFootView;
    private boolean isShowFooter = false;*/
    public static final int REQUEST_CODE = 10086;
    private String userName;
    private String token;

    private boolean ifHadPullToRefreshedSucess = false;
    private long referenceOtherProductId ;
    int currentreferencePage = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==resultCode){
            int position = data.getIntExtra(Config.Position,0);
            boolean isRemove = data.getBooleanExtra("remove",false);
            if(isRemove){
                //Log.w("hahaid",adapter.getList().get(position).getSellID()+"");
                //tool.deleteGoodTable(adapter.getList().get(position).getSellID());
                greenUtils.deleteGoodTable(adapter.getList().get(position).getSellID());
                adapter.removeItemView(position);
                recyclerView.getAdapter().notifyItemRemoved(position);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Bimp.drr.clear();
        Bimp.bmp.clear();
        Bimp.max = 0;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.layout_trade_decorate,null);
        //AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.root));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        adapter = new DecorateAdapter(TheApp.instance, otherList);
        return view;
    }

    @Override
    public void initData() {
        //tool = DBTool.getInstance(getTheApp.instance());
        greenUtils = GreenUtils.getInstance();
        //otherList = DBTool.getInstance(TheApp.instance).queryGoodList(TheApp.instance,Config.GoodType_Other);
        //----------------------------------------------------------------------


        userName = AbSharedUtil.getString(TheApp.instance, Config.UserName);
        token = AbSharedUtil.getString(TheApp.instance,Config.Token);
        AbSharedUtil.putInt(TheApp.instance, Config.CouponPage, 1);
        swipeRefreshLayout.setColorSchemeColors(R.color.text_color_title);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
       // recyclerView.addItemDecoration(new DividerStaggerItemDecoration(5));
        recyclerView.setAutoLoadMoreEnable(true);

        //referenceOtherProductId = tool.queryGoodCurrentNewestId(TheApp.instance,Config.GoodType_Other);
        referenceOtherProductId = greenUtils.queryGoodCurrentNewestId(TheApp.instance, Config.GoodType_Other);
        //otherList = tool.queryGoodList(TheApp.instance,Config.GoodType_Other,currentreferencePage,referenceOtherProductId);
        isPrepared = true;
        lazyLoad();

    }

    private void PullToRefresh() {
        if (!NetUtils.isConnected(TheApp.instance)) {
            ToastUtil.showToast(TheApp.instance, "请先打开网络");
            return;
        }
        isPullToRefresh = true;
        swipeRefreshLayout.setRefreshing(true);
        String url = Config.getAPI(Config.NowTime);
        TheApp.mAbHttpUtil.post(url, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //Logger.json("response content", content);

                Gson gson = new Gson();
                NowTime nowTime = gson.fromJson(content,NowTime.class);
                if(nowTime.getStatus().equals(Config.SUCCESS)){
                    AbSharedUtil.putInt(TheApp.instance,Config.OtherPage,1);
                    AbSharedUtil.putString(TheApp.instance,Config.OtherTime,nowTime.getNowtime());
                    getGoodList();
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

    private void getGoodList() {

        String url = Config.getAPI(Config.APIAllGood);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName,AbSharedUtil.getString(TheApp.instance,Config.UserName));
        params.put(Config.Token,AbSharedUtil.getString(TheApp.instance,Config.Token));
        params.put(Config.Page, AbSharedUtil.getInt(TheApp.instance, Config.OtherPage));
        params.put(Config.Rows,Config.RowNumber);
        params.put(Config.Type,Config.Other);
        params.put(Config.BeginTime, AbSharedUtil.getString(TheApp.instance, Config.OtherTime));

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
               // Logger.json("response content",content);
                if (HttpResultUtil.handleUnloginErrorStatus(content, RefleshOtherProFragment.getInstance(), OtherFragment.this.getActivity())) {
                    return;
                }
                Gson gson = new Gson();
                GoodBean goodBean = gson.fromJson(content,GoodBean.class);
                //Log.w("haha22222",content);
                if(goodBean.getStatus().equals(Config.SUCCESS)){
                    if(isPullToRefresh){

                        if (goodBean.getList()!=null && goodBean.getList().size()>0)
                            greenUtils.deleteAllGoodsByType(Config.GoodType_Other);

                        adapter.setList(goodBean.getList());
                        //isShowFooter = true;
                        ifHadPullToRefreshedSucess = true;
                        recyclerView.notifyMoreFinish(true,1);
                        if(goodBean.getList().size()<Config.RowNumber)
                            recyclerView.setAutoLoadMoreEnable(false);
                        else
                            recyclerView.setAutoLoadMoreEnable(true);
                    }else {
                        adapter.addList(goodBean.getList());
                        recyclerView.notifyMoreFinish(true,0);
                    }
                    for (Goods goods : goodBean.getList()) {
                        goods.setGoodType(Config.GoodType_Other);
                        //greenUtils.getGoodsDao().insertOrReplace(goods);
                        greenUtils.insertOrReplaceGoods(goods);

                    }
                    //tool.inserTGoodTable(TheApp.instance,goodBean.getList(),Config.GoodType_Other);

                    if (goodBean.getList().size()<Config.RowNumber){
                        //isShowFooter = false;
                        //adapter.setFootViewVisible(false);
                        recyclerView.setAutoLoadMoreEnable(false);
                    }

                    AbSharedUtil.putInt(TheApp.instance, Config.OtherPage, AbSharedUtil.getInt(TheApp.instance, Config.OtherPage) + 1);
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
                recyclerView.setAutoLoadMoreEnable(true);
            }
        });
    }
    private void LoadingDataFromDatabase() {
        /*new Thread(new Runnable() {
            @Override
            public void run() {


            }
        }).start();*/
        List<Goods> clist = greenUtils.queryGoodList(TheApp.instance,Config.GoodType_Other,currentreferencePage++,referenceOtherProductId);
        adapter.addList(clist);
        adapter.addList(clist);
        if (currentreferencePage==1)
            recyclerView.notifyMoreFinish(true,1);
        else
            recyclerView.notifyMoreFinish(true,0);

        if (clist.size()<Config.RowNumber){
            recyclerView.setAutoLoadMoreEnable(false);
        }

        isPullToRefresh = false;
        swipeRefreshLayout.setRefreshing(false);
        //List<GoodBean.Good> clist = tool.queryGoodList(TheApp.instance,Config.GoodType_Other,currentreferencePage,referenceOtherProductId);
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

                if (lastVisibleItemPosition >= totalItemCount - Config.AutoLoadingNum && dy > 0 && !isLoadingMore&!isPullToRefresh&isShowFooter) {
                    if (ifHadPullToRefreshedSucess)//如果已经 进行了刷新操作，那么就从网络获取数据
                        getGoodList();
                    else{
                        LoadingDataFromDatabase();//如果没有 进行了刷新操作，那么就从数据库获取数据
                    }
                    mFootView.setVisibility(View.VISIBLE);
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
                if (ifHadPullToRefreshedSucess)//如果已经 进行了刷新操作，那么就从网络获取数据
                    getGoodList();
                else{
                    LoadingDataFromDatabase();//如果没有 进行了刷新操作，那么就从数据库获取数据
                }
            }
        });

        adapter.setOnClickListener(new DecorateAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View view, int sellID, int position,String UserName) {

                Intent intent;
                if(userName.equals(UserName)){
                    intent = new Intent(TheApp.instance,ActivityMyselfGoodDetail.class);
                    intent.putExtra(Config.GoodType,Config.GoodType_Other);

                }else {
                    intent = new Intent(TheApp.instance, ActivityGoodDetail.class);
                }
                intent.putExtra(Config.Position,position);
                intent.putExtra(Config.SellID,sellID);
                intent.putExtra(Config.UserName,userName);
                intent.putExtra(Config.Token,token);
                startActivityForResult(intent,REQUEST_CODE);
            }

            @Override
            public void ItemLongClickListener(View view, int position) {

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TheApp.instance, ActivityMarketInPut.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    protected void lazyLoad() {//已经初始化完成，并且可见，并且第一次加载才  运行加载.
        if (!isPrepared || !isVisible || mHasLoadedOnce)
            return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                otherList = greenUtils.queryGoodList(TheApp.instance,Config.GoodType_Other,currentreferencePage++,referenceOtherProductId);
                adapter.setList(otherList);
                mHasLoadedOnce = true;
                //LoadingDataFromDatabase();
        /*mFootView = View.inflate(TheApp.instance,R.layout.recyclerview_footer,null);
        adapter.setmFootView(mFootView);
        mFootView.setVisibility(View.GONE);*/

                AbSharedUtil.putInt(TheApp.instance,Config.OtherPage,1);
                AbSharedUtil.putString(TheApp.instance,Config.OtherTime,Config.PullToRefreshTime);
                //swipeRefreshLayout.setRefreshing(true);
                //PullToRefresh();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                        if (NetUtils.isConnected(TheApp.instance))
                            PullToRefresh();
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
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }
    @Subscribe
    public void onEventMainThread(RefleshOtherProFragment event) {
        String msg = "RefleshOtherProFragment";
        Log.d("eventbus", msg);
        if (NetUtils.isConnected(TheApp.instance)) {
            if (!isPrepared||!mHasLoadedOnce)
                return;
            //已经准备好，而且已经加载过 数据
            PullToRefresh();
        }
    }
}
