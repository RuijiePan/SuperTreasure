package com.supertreasure.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.orhanobut.logger.Logger;
import com.supertreasure.R;
import com.supertreasure.bean.MoodBean;
import com.supertreasure.bean.Praise;
import com.supertreasure.eventbus.RefleshMyMoodFragment;
import com.supertreasure.greenDaoUtils.GreenUtils;
import com.supertreasure.home.ActivityMoodInDeatil;
import com.supertreasure.main.TheApp;
import com.supertreasure.service.HttpResultUtil;
import com.supertreasure.util.BlurUtil;
import com.supertreasure.util.Config;
import com.supertreasure.util.DateUtils;
import com.supertreasure.util.LoadMoreRecyclerView;
import com.supertreasure.util.NetUtils;
import com.supertreasure.util.PrjBase64;
import com.supertreasure.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import me.itangqi.greendao.Mood;

/**
 * Created by prj on 2016/3/29.
 */
public class ActivityMyMood extends AppCompatActivity implements View.OnClickListener,AppBarLayout.OnOffsetChangedListener{
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    private GreenUtils greenUtils;
    public static final int RequestCode = 10086;
    private Context context;
    private TextView tv_title;
    private TextView nullContent;
    private String nickName;
    private String userPic;
    private Button bt_title_left;
    private Button bt_title_right;
    private LinearLayout root;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private SimpleDraweeView draweeView;
    private TextView tv_nickName;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private LoadMoreRecyclerView recyclerView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout app_bar_layout;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private LinearLayout head_layout;
    //private List<MyMoodBean.Mood> moodList;
    private List<Mood> moodList;
    private MyMoodAdapter adapter;
    private boolean isPullToRefresh = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==resultCode){
            boolean isPraised = data.getBooleanExtra("isPraised",false);
            int position = data.getIntExtra("position",0);
            Log.w("hahaposition",position+"");
            String praiseTimes = data.getStringExtra("praiseTimes");
            String commentCount = data.getStringExtra("commentCount");
            adapter.updateItem(position,isPraised,praiseTimes,commentCount);
            recyclerView.notifyMoreFinish(true,1);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mood);
        SwipeBackHelper.onCreate(this);
        EventBus.getDefault().register(this);
        initView();
        initData();
        initToolbar();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        SwipeBackHelper.onDestroy(this);
    }

    private void initView() {
        context = this;
        /*tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("我的说说");
        nullContent = (TextView) findViewById(R.id.nullcontent);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        root = (LinearLayout) findViewById(R.id.root);
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        ll_title_right.setVisibility(View.INVISIBLE);*/
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerView);
        draweeView = (SimpleDraweeView) findViewById(R.id.civ_photo);
        tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        //AbViewUtil.scaleContentView(root);
    }

    private void initData() {
  /*      greenUtils = GreenUtils.getInstance();
        referenceId = greenUtils.queryMyOwnMoodCurrentNewestId(context);
        moodList = greenUtils.queryMyOwnMoodList(context,page,referenceId);*/

        //moodList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeColors(R.color.text_color_title);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAutoLoadMoreEnable(true);

        //adapter = new MyMoodAdapter(context,moodList);
        adapter = new MyMoodAdapter(context);
        recyclerView.setAdapter(adapter);

        /*View viewHead = View.inflate(this,R.layout.activity_my_mood_headview,null);
        LinearLayout viewRoot = (LinearLayout) viewHead.findViewById(R.id.root);
        viewRoot.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,360));
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHead.findViewById(R.id.civ_photo);
        TextView tv_nickName = (TextView) viewHead.findViewById(R.id.tv_nickName);
        AbViewUtil.scaleContentView(viewRoot);*/
        userPic = AbSharedUtil.getString(TheApp.instance,Config.Content_AboutMe_HeadImg);
        nickName = AbSharedUtil.getString(TheApp.instance,Config.Content_AboutMe_Nickname);
        int width = 200, height = 200;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(userPic))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.getController())
                .setImageRequest(request)
                .build();
        draweeView.setController(controller);
        //TheApp.mAbImageLoader.display(civ_photo,userPic);
        tv_nickName.setText(nickName);

        //recyclerView.addHeaderView(viewHead);
        AbSharedUtil.putInt(TheApp.instance, Config.MyMoodPage, 1);
        isPrepared = true;
        mHasLoadedOnce = true;
        PullToRefresh();
        //swipeRefreshLayout.setEnabled(false);
    }

    private void initToolbar() {
        app_bar_layout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        head_layout = (LinearLayout) findViewById(R.id.head_layout);
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        //head_layout.setBackgroundDrawable(new BitmapDrawable(BlurUtil.fastblur(this, bitmap, 180)));
        app_bar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -head_layout.getHeight() / 2) {
                    mCollapsingToolbarLayout.setTitle("我的说说");
                    tv_nickName.setVisibility(View.GONE);
                } else {
                    mCollapsingToolbarLayout.setTitle(" ");
                    tv_nickName.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void PullToRefresh() {
        isPullToRefresh = true;
        recyclerView.setLoadingMore(false);
        //recyclerView.addFooterView(R.layout.list_foot_loading);
        AbSharedUtil.putInt(TheApp.instance, Config.MyMoodPage, 1);
        getMyMoodList();
    }

    private void setListener(){

        /*bt_title_left.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);*/

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
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
                    getMyMoodList();
                }
            }
        });*/
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                getMyMoodList();
            }
        });

        adapter.setOnClickListener(new MyMoodAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(final View ll_like, final View iv_like, final TextView tv_like, int moodID, final int position) {
                String url = Config.getAPI(Config.APIMoodPraise);
                AbRequestParams params = new AbRequestParams();
                params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance, Config.UserName));
                params.put(Config.Token, AbSharedUtil.getString(TheApp.instance, Config.Token));
                params.put(Config.MoodID, moodID);
                TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
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
                        Toast.makeText(context, "点赞失败，与服务器链接错误", Toast.LENGTH_SHORT).show();
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
                bundlePeople.putString("belongschool", AbSharedUtil.getString(context, Config.Content_AboutMe_Belongschool));
                //bundlePeople.putString("sex", mood.getPeople().getSex());
                bundlePeople.putString("userPic", mood.getMe().getUserPic());
                bundlePeople.putString("nickName", mood.getMe().getNickName());
                //bundlePeople.putString("userName", mood.getPeople().getUserName());
                bundleMood.putBundle("people", bundlePeople);
                intent.putExtra("list", bundleMood);
                intent.putExtra("position", position);
                startActivityForResult(intent, RequestCode);
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

    public void getMyMoodList() {

        String url = Config.getAPI(Config.APIMyMood);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName,AbSharedUtil.getString(context,Config.UserName));
        params.put(Config.Token,AbSharedUtil.getString(context,Config.Token));
        params.put(Config.Page, AbSharedUtil.getInt(context, Config.MyMoodPage));
        params.put(Config.Rows,Config.RowNumber);

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
//                Logger.i("onSuccess content",content);
                if (HttpResultUtil.handleUnloginErrorStatus(content, RefleshMyMoodFragment.getInstance(), ActivityMyMood.this)) {
                    return;
                }
                Gson gson = new Gson();

                //MyMoodBean moodList = gson.fromJson(content,MyMoodBean.class);
                MoodBean moodList = gson.fromJson(content,MoodBean.class);
                Log.w("haha",moodList.toString());
                if(moodList.getStatus().equals(Config.SUCCESS)){
                    if(isPullToRefresh)
                    {
                        adapter.setMoodList(moodList.getList());
                        recyclerView.notifyMoreFinish(true,1);
                        if(moodList.getList().size()<Config.RowNumber)
                            recyclerView.setAutoLoadMoreEnable(false);
                    }
                    else
                    {
                        adapter.addMood(moodList.getList());
                        recyclerView.notifyMoreFinish(true,0);
                    }

                    Log.w("haha",moodList.getList().toString());
                    //Log.w("hahasize",moodList.getList().size()+"");
                    if(moodList.getList().size()==0){
                        recyclerView.setAutoLoadMoreEnable(false);
                    }

                    AbSharedUtil.putInt(TheApp.instance, Config.MyMoodPage, AbSharedUtil.getInt(TheApp.instance, Config.MyMoodPage) + 1);
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
                //isLoadingMore = false;
                isPullToRefresh = false;
                recyclerView.notifyMoreFinish(true,1);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    @Subscribe
    public void onEventMainThread(RefleshMyMoodFragment event) {
        String msg = "RefleshMyMoodFragment";
        Log.d("eventbus", msg);
        if (NetUtils.isConnected(TheApp.instance)) {
            if (!isPrepared||!mHasLoadedOnce)
                return;
            //已经准备好，而且已经加载过 数据
            PullToRefresh();
        }

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            swipeRefreshLayout.setEnabled(true);
        } else {
            swipeRefreshLayout.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        app_bar_layout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        app_bar_layout.removeOnOffsetChangedListener(this);
    }
}
