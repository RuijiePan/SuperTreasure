package com.supertreasure.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
import com.supertreasure.bean.MoodBean;
import com.supertreasure.bean.NowTime;
import com.supertreasure.bean.Praise;
import com.supertreasure.database.DBTool;
import com.supertreasure.eventbus.RefleshSchoolOutMoodFragment;
import com.supertreasure.greenDaoUtils.GreenUtils;
import com.supertreasure.main.TheApp;
import com.supertreasure.service.HttpResultUtil;
import com.supertreasure.util.Bimp;
import com.supertreasure.util.Config;
import com.supertreasure.util.DateUtils;
import com.supertreasure.util.LoadMoreRecyclerView;
import com.supertreasure.util.NetUtils;
import com.supertreasure.util.PrjBase64;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.itangqi.greendao.Mood;

public class  OutSchoolFriend extends AbActivity implements View.OnClickListener{
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    private GreenUtils greenUtils;
    private DBTool tool;
    public static final int RequestCode = 10086;
    private FloatingActionButton fab;
    private List<Mood> moodList;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreRecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SchoolAdapter adapter;
    //private boolean isLoadingMore = false;
    private boolean isPullToRefresh = false;
    private long referenceMoodId;
    private int page = 1;
    private boolean ifHadPullToRefreshedSucess = false;

    private TextView tv_title;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private Button bt_title_left;
    private Button bt_title_right;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==resultCode){
            boolean isPraised = data.getBooleanExtra("isPraised",false);
            int position = data.getIntExtra("position",0);
            String praiseTimes = data.getStringExtra("praiseTimes");
            String commentCount = data.getStringExtra("commentCount");
            adapter.updateItem(position,isPraised,praiseTimes,commentCount);
            recyclerView.notifyMoreFinish(true,1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bimp.drr.clear();
        Bimp.bmp.clear();
        Bimp.max = 0;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outschool_friend_mian);
        SwipeBackHelper.onCreate(this);
        EventBus.getDefault().register(this);

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

    private void initView() {
        //AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));
        context = this;
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("外校说说");
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        ll_title_right.setVisibility(View.INVISIBLE);
    }

    private void initData() {
        greenUtils = GreenUtils.getInstance();
        AbSharedUtil.putInt(context, Config.SchoolOutPage, 1);
        swipeRefreshLayout.setColorSchemeColors(R.color.text_color_title);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAutoLoadMoreEnable(true);

        moodList = new ArrayList<>();
        adapter = new SchoolAdapter(context,moodList);
        recyclerView.setAdapter(adapter);

        referenceMoodId =  greenUtils.queryMoodCurrentNewestId(context,Config.DATABASE_MOOD_TYPE_out);

        isPrepared = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                moodList = greenUtils.queryMoodList(context,page, referenceMoodId,Config.DATABASE_MOOD_TYPE_out);
                mHasLoadedOnce = true;

                //if (moodList == null) moodList = new ArrayList<>();
                adapter.setMoodList(moodList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);

                        if (NetUtils.isConnected(TheApp.instance))
                            PullToRefresh();//更新完时间会加载新数据
                    }
                });

            }
        }).start();

    }

    private void PullToRefresh() {

        isPullToRefresh = true;
        String url = Config.getAPI(Config.NowTime);
        TheApp.mAbHttpUtil.post(url, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //Logger.json("response content", content);

                Gson gson = new Gson();
                NowTime nowTime = gson.fromJson(content, NowTime.class);
                if (nowTime.getStatus().equals(Config.SUCCESS)) {
                    AbSharedUtil.putString(context, Config.SchoolOutTime, nowTime.getNowtime());
                    AbSharedUtil.putInt(context, Config.SchoolOutPage, 1);
                    getMoodList();
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

    private void setListener() {
        bt_title_left.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);

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
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = adapter.getItemCount();

                if (lastVisibleItem >= totalItemCount - Config.AutoLoadingNum && dy > 0 && !isLoadingMore&&!isPullToRefresh) {
                    getMoodList();
                }
            }
        });*/
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (ifHadPullToRefreshedSucess) {//如果已经 进行了刷新操作，那么就从网络获取数据
                    getMoodList();
                } else {
                    LoadingDataFromDatabase();//如果没有 进行了刷新操作，那么就从数据库获取数据
                }
            }
        });

        adapter.setOnClickListener(new SchoolAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(final View ll_like, final View iv_like, final TextView tv_like, int moodID, final int position) {
                String url = Config.getAPI(Config.APIMoodPraise);
                AbRequestParams params = new AbRequestParams();
                params.put(Config.UserName, AbSharedUtil.getString(context, Config.UserName));
                params.put(Config.Token, AbSharedUtil.getString(context, Config.Token));
                params.put(Config.MoodID, moodID);
                TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        //Logger.json("response content", content);
                        if (HttpResultUtil.handleUnloginErrorStatus(content, RefleshSchoolOutMoodFragment.getInstance(), OutSchoolFriend.this)) {
                            return;
                        }
                        Gson gson = new Gson();
                        Praise praise = gson.fromJson(content, Praise.class);
                        if (praise.getStatus().equals(Config.SUCCESS)) {
                            if (Integer.parseInt(praise.getPraiseTimes()) - Integer.parseInt(tv_like.getText().toString()) < 0) {
                                iv_like.setBackgroundResource(R.drawable.like_normal);
                                adapter.updateIsPraised(position, false, praise.getPraiseTimes());
                            } else {
                                iv_like.setBackgroundResource(R.drawable.like_press);
                                adapter.updateIsPraised(position, true, praise.getPraiseTimes());
                            }
                            tv_like.setText(praise.getPraiseTimes());
                        }
                        //  Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {
                        // Toast.makeText(context,"点赞",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        Toast.makeText(TheApp.instance, "点赞失败，与服务器链接错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void ItemClickListener(View ll_message, Mood mood, int position) {
                Intent intent = new Intent(TheApp.instance, ActivityMoodInDeatil.class);
                //传递详情说说说需要的参数
                Bundle bundleMood = new Bundle();
                bundleMood.putInt(Config.MoodID, mood.getMoodId());
                bundleMood.putString("content", PrjBase64.decode(mood.getContent()));
                bundleMood.putString("paths", mood.getPaths());
                bundleMood.putString("publishtime", DateUtils.fromToday(mood.getPublishtime()));
                bundleMood.putString("praiseTimes", mood.getPraiseTimes());
                bundleMood.putString("commentCount", mood.getCommentCount());
                bundleMood.putBoolean("isPraised", mood.getIsPraised());
                Bundle bundlePeople = new Bundle();
                bundlePeople.putString("belongschool", mood.getMe().getBelongschool());
                bundlePeople.putString("sex", mood.getSex());
                bundlePeople.putString("userPic", mood.getMe().getUserPic());
                bundlePeople.putString("nickName", mood.getMe().getNickName());
                bundlePeople.putString("userName", mood.getUserName());
                bundleMood.putBundle("people", bundlePeople);
                intent.putExtra("list", bundleMood);
                intent.putExtra("position", position);
                startActivityForResult(intent, RequestCode);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityMoodInPut.class);
                intent.putExtra(Config.IsInMoodInput, false);
                startActivity(intent);
            }
        });
    }

    private void getMoodList() {

        //isLoadingMore = true;
        String url = Config.getAPI(Config.APIMoodOut);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(context, Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(context, Config.Token));
        params.put(Config.Page, AbSharedUtil.getInt(context, Config.SchoolOutPage));
        params.put(Config.Rows, Config.RowNumber);
        params.put(Config.BeginTime, AbSharedUtil.getString(context, Config.SchoolOutTime));

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //Logger.json("response content", content);
                if (HttpResultUtil.handleUnloginErrorStatus(content, RefleshSchoolOutMoodFragment.getInstance(), OutSchoolFriend.this)) {
                    return;
                }
                Gson gson = new Gson();
                MoodBean moodBean = gson.fromJson(content, MoodBean.class);

                if (moodBean.getStatus().equals(Config.SUCCESS)) {
                    if (isPullToRefresh) {
                        if (moodBean.getList() != null && moodBean.getList().size() > 0)
                            greenUtils.deleteAllMoodByType(Config.DATABASE_MOOD_TYPE_out);

                        ifHadPullToRefreshedSucess = true;
                        adapter.setMoodList(moodBean.getList());
                        recyclerView.notifyMoreFinish(true, 1);
                        if (moodBean.getList().size() < Config.RowNumber)
                            recyclerView.setAutoLoadMoreEnable(false);
                    } else {
                        adapter.addMood(moodBean.getList());
                        recyclerView.notifyMoreFinish(true, 0);
                    }

                    if (moodBean.getList().size() == 0) {
                        recyclerView.setAutoLoadMoreEnable(false);
                    }

                    for (Mood mood : moodBean.getList()) {
                        greenUtils.insertOrReplaceMood(mood, Config.DATABASE_MOOD_TYPE_out);
                    }
                    //Toast.makeText(context,AbSharedUtil.getString(context,),Toast.LENGTH_SHORT).show();
                    AbSharedUtil.putInt(TheApp.instance, Config.SchoolOutPage, AbSharedUtil.getInt(context, Config.SchoolOutPage) + 1);
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


    private void LoadingDataFromDatabase() {
        List<Mood> mList = greenUtils.queryMoodList(context,++page, referenceMoodId,Config.DATABASE_MOOD_TYPE_out);
        adapter.addMood(mList);
        recyclerView.notifyMoreFinish(true,0);

        if (mList.size()<Config.RowNumber){
            recyclerView.setAutoLoadMoreEnable(false);
        }

        isPullToRefresh = false;
        swipeRefreshLayout.setRefreshing(false);
        /*new Thread(new Runnable() {
            @Override
            public void run() {


            }
        }).start();*/
    }
    @Subscribe
    public void onEventMainThread(RefleshSchoolOutMoodFragment event) {
        String msg = "RefleshSchoolOutMoodFragment";
        Log.d("eventbus", msg);
        if (NetUtils.isConnected(TheApp.instance)) {
            if (!isPrepared)
                return;
            PullToRefresh();
        }
    }
}
