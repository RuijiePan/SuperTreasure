package com.supertreasure.home;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbDialogFragment;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.image.AbImageLoader;
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
import com.jude.swipbackhelper.SwipeListener;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.supertreasure.R;
import com.supertreasure.bean.CommentBean;
import com.supertreasure.bean.NowTime;
import com.supertreasure.bean.Praise;
import com.supertreasure.bean.MoodCommentBean;
import com.supertreasure.fragment.SchoolFragment;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.util.LoadMoreRecyclerView;
import com.supertreasure.util.NoScrollGridView;
import com.supertreasure.dialog.mSweetAlertDialog;
import com.supertreasure.util.PrjBase64;
import com.supertreasure.util.ToastUtil;
import com.supertreasure.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by prj on 2016/1/29.
 */
public class ActivityMoodInDeatil extends AbActivity implements View.OnClickListener,View.OnLayoutChangeListener,EmojiconsFragment.OnEmojiconBackspaceClickedListener,EmojiconGridFragment.OnEmojiconClickedListener{

    private int softKeyboardHeight = 0;
    private boolean isCommitCommenting;
    private int position;
    private ImageView iv_like;
    private Button bt_title_left;
    private Button bt_title_right;
    private Button bt_commit;
    private TextView tv_title;
    private TextView tv_comment;
    private TextView tv_like;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AbDialogFragment mdialogFragment = null;
    private mSweetAlertDialog sweetAlertDialog;
    private LoadMoreRecyclerView recyclerView;
    private LinearLayout ll_comment;
    private LinearLayout ll_like;
    private LinearLayout ll_bottom;
    private LinearLayout ll_emoji_bar;
    private LayoutInflater inflater;
    private MoodAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<CommentBean.Comment> commentList;
    private String praiseTimes;
    private String commentCount;
    private boolean isPullToRefresh = false;
    //private boolean isLoadingMore = false;
    private boolean isUpdateTime = false;
    private boolean isPraised = false;
    //moji视图是否显示
    private boolean emojiContainerShown = false;
    /* 软键盘关闭时是否显示emoji视图 */
    private boolean showEmojiView = false;
    private int moodID;
    private int toCommentID;
    private View moodView;
    private Dialog dialog;
    private FrameLayout ll_root;
    private EmojiconEditText et_content;
    private View emojiView;
    private ImageView iv_emojicon_switch;
    private Intent intent;
    private View ll_title_left;



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.school_say);

        SwipeBackHelper.onCreate(this);
        /*SwipeBackHelper.getCurrentPage(this)//获取当前页面
                .setSwipeBackEnable(true)//设置是否可滑动
                //.setSwipeEdge(10)//可滑动的范围。px。200表示为左边200px的屏幕
                .setSwipeEdgePercent(0.01f)//可滑动的范围。百分比。0.2表示为左边20%的屏幕
                .setSwipeSensitivity(1)//对横向滑动手势的敏感程度。0为迟钝 1为敏感
                //.setScrimColor(Color.WHITE)底层阴影颜色
                .setClosePercent(0.2f)//触发关闭Activity百分比
                .setSwipeRelateEnable(false)//是否与下一级activity联动(微信效果)。默认关
               // .setSwipeRelateOffset(200)//activity联动时的偏移量。默认500px。
                .addListener(new SwipeListener() {//滑动监听

                    @Override
                    public void onScroll(float percent, int px) {//滑动的百分比与距离
                    }

                    @Override
                    public void onEdgeTouch() {//当开始滑动
                    }

                    @Override
                    public void onScrollToClose() {//当滑动关闭
                    }
                });*/
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onCreate(this);
    }

    private void initView() {
        AbViewUtil.scaleContentView((FrameLayout)findViewById(R.id.root));
        ll_title_left = findViewById(R.id.ll_title_left);
        context = ActivityMoodInDeatil.this;
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("说说");
        bt_commit = (Button) findViewById(R.id.bt_commit);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        bt_title_right.setVisibility(View.INVISIBLE);
        ll_emoji_bar = (LinearLayout) findViewById(R.id.ll_emoji_bar);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
        ll_like = (LinearLayout) findViewById(R.id.ll_like);
        ll_root = (FrameLayout) findViewById(R.id.root);
        iv_like = (ImageView) findViewById(R.id.iv_like);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerView);
        emojiView =  findViewById(R.id.emoji_view);
        iv_emojicon_switch = (ImageView) findViewById(R.id.iv_emojicon);
        et_content = (EmojiconEditText) findViewById(R.id.et_content);

        intent = new Intent();
        initMoodView();
    }

    private void initMoodView() {

        //添加Emoji控件
        getSupportFragmentManager().beginTransaction().replace(R.id.emoji_view, new EmojiconsFragment()).commitAllowingStateLoss();

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        moodView = inflater.inflate(R.layout.school_say_common,null);
        AbViewUtil.scaleContentView((LinearLayout)moodView.findViewById(R.id.root));

        SimpleDraweeView draweeView = (SimpleDraweeView) moodView.findViewById(R.id.civ_photo);
        TextView tv_username = (TextView) moodView.findViewById(R.id.tv_username);
        TextView tv_time = (TextView) moodView.findViewById(R.id.tv_time);
        TextView tv_school = (TextView) moodView.findViewById(R.id.tv_school);
        TextView tv_content = (TextView) moodView.findViewById(R.id.tv_content);
        NoScrollGridView gridView = (NoScrollGridView) moodView.findViewById(R.id.gridView);
        tv_comment = (TextView) moodView.findViewById(R.id.tv_comment);
        tv_like = (TextView) moodView.findViewById(R.id.tv_like);

        Bundle bundle = getIntent().getBundleExtra("list");
        Bundle bundlePeople = bundle.getBundle("people");

        assert bundlePeople != null;
        int width = 80, height = 80;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(
                Uri.parse(bundlePeople.getString("userPic")))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.getController())
                .setImageRequest(request)
                .build();
        draweeView.setController(controller);
        //draweeView.setImageURI(Uri.parse(bundlePeople.getString("userPic")));
        tv_username.setText(bundlePeople.getString("nickName"));
        tv_time.setText(bundle.getString("publishtime"));
        tv_school.setText(bundlePeople.getString("belongschool"));
        tv_content.setText(bundle.getString("content"));
        commentCount= bundle.getString("commentCount");
        tv_comment.setText(commentCount);
        praiseTimes = bundle.getString("praiseTimes");
        tv_like.setText(praiseTimes);
        isPraised = bundle.getBoolean("isPraised");
        moodID = bundle.getInt("moodID");
        position = getIntent().getIntExtra("position",0);

        if(!isPraised){
            iv_like.setBackgroundResource(R.drawable.like_normal);
        }else {
            iv_like.setBackgroundResource(R.drawable.like_press);
        }

        intent.putExtra("isPraised",isPraised);
        intent.putExtra("position",position);
        intent.putExtra("praiseTimes",praiseTimes);
        intent.putExtra("commentCount",commentCount);

        if(bundle.getString("paths")!=null){
            final String[] pics = bundle.getString("paths").split(",");
            if(pics.length>0){
                gridView.setVisibility(View.VISIBLE);
                if(pics.length==1)
                    gridView.setNumColumns(1);
                else{
                    gridView.setNumColumns(3);
                }
                gridView.setAdapter(new MyGridAdapter(pics, context));

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        imageBrower(position,pics);
                    }
                });
            }else {
                gridView.setVisibility(View.GONE);
            }
        }
    }

    private void initData() {
        AbSharedUtil.putInt(context, Config.MoodInDeatailPage, 1);

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

        commentList = new ArrayList<>();
        adapter = new MoodAdapter(context,commentList);
        recyclerView.setAdapter(adapter);
        recyclerView.addHeaderView(moodView);
        //adapter.setHeaderView(moodView);

        toCommentID = 0;
        PullToRefresh();
    }

    private void PullToRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        isPullToRefresh = true;
        AbSharedUtil.putInt(context, Config.MoodInDeatailPage, 1);
        updateBegainTime();
    }

    private void updateBegainTime() {

        isUpdateTime = true;
        String url = Config.getAPI(Config.NowTime);
        TheApp.mAbHttpUtil.post(url, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                NowTime nowTime = gson.fromJson(content, NowTime.class);
                if (nowTime.getStatus().equals(Config.SUCCESS)) {
                    AbSharedUtil.putString(context, Config.MoodInDeatailTime, nowTime.getNowtime());
                    getCommentList();
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                isUpdateTime = false;
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                isUpdateTime = false;
                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(context, "获取服务器当前时间失败，数据更新失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCommentList() {

        String url = Config.getAPI(Config.APIMoodInComments);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(context, Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(context, Config.Token));
        params.put(Config.Rows, Config.RowNumber);
        params.put(Config.MoodID, moodID);
        params.put(Config.Page,AbSharedUtil.getInt(context,Config.MoodInDeatailPage)+"");
        params.put(Config.BeginTime, AbSharedUtil.getString(context, Config.MoodInDeatailTime));

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                CommentBean commentBean = gson.fromJson(content, CommentBean.class);
                if (commentBean.getStatus().equals(Config.SUCCESS)) {
                    if (isPullToRefresh) {
                        adapter.setCommentList(commentBean.getList());
                        recyclerView.notifyMoreFinish(true,1);
                        if(commentBean.getList().size()<Config.RowNumber)
                            recyclerView.setAutoLoadMoreEnable(false);
                        else
                            recyclerView.setAutoLoadMoreEnable(true);
                        //AbSharedUtil.putInt(context, Config.MoodInDeatailPage, 1);
                        //Log.w("haha",content);
                        /*Toast.makeText(context, "刷新成功", Toast.LENGTH_SHORT).show();*/
                    } else {
                        //  Toast.makeText(context, "加载更多", Toast.LENGTH_LONG).show();
                        adapter.addCommentList(commentBean.getList());
                        recyclerView.notifyMoreFinish(true,0);
                    }

                    if (commentBean.getList().size()<Config.RowNumber){
                        recyclerView.setAutoLoadMoreEnable(false);
                    }
                    //  Toast.makeText(context, content, Toast.LENGTH_LONG).show();
                    AbSharedUtil.putInt(context, Config.MoodInDeatailPage, AbSharedUtil.getInt(context, Config.MoodInDeatailPage) + 1);
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                if (mdialogFragment != null)
                    mdialogFragment.loadFinish();
                //isLoadingMore = false;
                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Toast.makeText(context, content, Toast.LENGTH_LONG).show();
                if (mdialogFragment != null)
                    mdialogFragment.loadFinish();
                //isLoadingMore = false;
                isPullToRefresh = false;
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setListener(){
        bt_title_left.setOnClickListener(this);
        /*emojiView.setEventListener(this);*/
        ll_title_left.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isCommitCommenting) {
                    PullToRefresh();
                }

            }
        });

        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.w("haha11",et_content.getText().toString());
                hideKeyBoard();
                commitComment();
            }
        });

        //自动加载更多
        /*recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = adapter.getItemCount();

                if (lastVisibleItem >= totalItemCount - Config.AutoLoadingNum && dy > 0 && !isLoadingMore&&!isPullToRefresh&!isUpdateTime) {
                    getCommentList();
                }
            }
        });*/
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                getCommentList();
            }
        });

        ll_like.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Config.getAPI(Config.APIMoodPraise);
                final AbRequestParams params = new AbRequestParams();
                params.put(Config.UserName, AbSharedUtil.getString(context, Config.UserName));
                params.put(Config.Token, AbSharedUtil.getString(context, Config.Token));
                params.put(Config.MoodID, moodID);
                TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        Gson gson = new Gson();
                        Praise praise = gson.fromJson(content, Praise.class);
                        if (praise.getStatus().equals(Config.SUCCESS)) {
                            if (Integer.parseInt(praise.getPraiseTimes()) - Integer.parseInt(tv_like.getText().toString()) < 0) {
                                iv_like.setBackgroundResource(R.drawable.like_normal);
                                isPraised = false;
                            } else {
                                iv_like.setBackgroundResource(R.drawable.like_press);
                                isPraised = true;
                            }
                            tv_like.setText(praise.getPraiseTimes());
                            intent.putExtra("isPraised",isPraised);
                            intent.putExtra("praiseTimes",praise.getPraiseTimes());
                            ActivityMoodInDeatil.this.setResult(SchoolFragment.RequestCode,intent);
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
                        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        ll_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_emoji_bar.setVisibility(View.VISIBLE);
                showKeyBoard();
            }
        });

        ll_root.addOnLayoutChangeListener(this);
        /*ll_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                ll_root.getWindowVisibleDisplayFrame(r);

                ScreenHeight = ll_root.getRootView().getHeight();
                if (ScreenHeight - (r.bottom - r.top) > ScreenHeight / 3) {
                    KeyBoardHeight = KeyBoardHeight>ScreenHeight - (r.bottom - r.top)?KeyBoardHeight:ScreenHeight - (r.bottom - r.top);
                   // Toast.makeText(context,KeyBoardHeight+"",Toast.LENGTH_SHORT).show();
                }

                KeyBoardVisible = (ScreenHeight - (r.bottom - r.top)) > ScreenHeight / 3;
                if (!KeyBoardVisible) {
                    *//*if (dialog != null) {
                        dialog.cancel();
                    }*//*
                    *//*if(emojiView.getVisibility()!=View.VISIBLE)
                    ll_bottom.setVisibility(View.VISIBLE);*//*
                    if(showEmojiView){
                        //显示Emoji视图
                        showEmojiView = false;
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ll_emoji_bar.getLayoutParams();
                        lp.bottomMargin = KeyBoardHeight;
                        ll_emoji_bar.setLayoutParams(lp);

                        lp = (FrameLayout.LayoutParams) emojiView.getLayoutParams();
                        lp.height = KeyBoardHeight;
                        emojiView.setVisibility(View.VISIBLE);

                        emojiContainerShown = true;
                    }else {
                        iv_emojicon_switch.setImageResource(R.drawable.ic_emoji);
                        ll_emoji_bar.setVisibility(View.VISIBLE);
                    }
                }

                if (KeyBoardVisible) {
                    //showDialog();
                    hideEmojiLayout(false);
                    iv_emojicon_switch.setImageResource(R.drawable.ic_emoji);
                    ll_emoji_bar.setVisibility(View.VISIBLE);
                    ll_bottom.setVisibility(View.GONE);
                }
                //    Toast.makeText(context, ScreenHeight + "!!!" + KeyBoardHeight, Toast.LENGTH_SHORT).show();
            }
        });*/

        adapter.setOnClickListener(new MoodAdapter.OnItemClickListener() {
            @Override
            public void ItemClickListener(View ll_comment, String toCommentId) {
                toCommentID = Integer.parseInt(toCommentId);
                showKeyBoard();
            }
        });

        iv_emojicon_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emojiContainerShown){
                    iv_emojicon_switch.setImageResource(R.drawable.ic_emoji);
                    hideEmojiLayout(false);
                    showKeyBoard();
                }else {
                    iv_emojicon_switch.setImageResource(R.drawable.ic_keyboard);
                    showEmojiView = true;
                    hideKeyBoard();
                }
            }
        });

    }

    /**
     * 隐藏Emoji视图
     *
     * @param hideEmojiBar 是否隐藏Emoji按钮
     */
    private void hideEmojiLayout(boolean hideEmojiBar) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ll_emoji_bar.getLayoutParams();
        lp.bottomMargin = 0;
        ll_emoji_bar.setLayoutParams(lp);
        emojiView.setVisibility(View.GONE);

        if(hideEmojiBar){
            iv_emojicon_switch.setImageResource(R.drawable.ic_emoji);
            ll_emoji_bar.setVisibility(View.GONE);
        }

        emojiContainerShown = false;
    }

    private void showKeyBoard() {
        InputMethodManager imm=(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(et_content, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        et_content.requestFocus();
        SwipeBackHelper.getCurrentPage(this)//获取当前页面
                .setSwipeBackEnable(false);
    }

    private void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /*private void showDialog() {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentview = inflater.inflate(R.layout.popwindow_comment,null);
        AbViewUtil.scaleContentView((LinearLayout) contentview.findViewById(R.id.root));
        et_content = (EditText) contentview.findViewById(R.id.et_comment);
        et_content.setFocusable(true);
        bt_commit = (Button) contentview.findViewById(R.id.bt_commit);
        bt_commit.setOnClickListener(this);
        dialog = new Dialog(context,R.style.transparentFrameWindowStyle);
        dialog.setContentView(contentview, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.mood_comment_dialog_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        //90是底部评论跟喜欢布局的高度，但是dialog底部仍有空白。大致用0.082*KeyBoardHeight代替
        //dialog对话是从屏幕中间弹出的，所以初始高度是ScreenHeight/2
        wl.y = ScreenHeight/2-KeyBoardHeight+90+(int)(0.082*KeyBoardHeight);
       // Toast.makeText(context,KeyBoardHeight+"",Toast.LENGTH_SHORT).show();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                hideKeyBoard();
                toCommentID = 0;
            }
        });
        dialog.show();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.bt_title_left:
                finish();
                break;
            case R.id.bt_commit:
                commitComment();
                break;
        }
    }

    private void commitComment() {
        isCommitCommenting = true;
        String comment = et_content.getText().toString();
        String url = Config.getAPI(Config.APIMoodToComment);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName,AbSharedUtil.getString(context,Config.UserName));
        params.put(Config.Token,AbSharedUtil.getString(context, Config.Token));
        params.put(Config.CommentText, PrjBase64.encode(comment));
        params.put(Config.ToCommentID,toCommentID);
        params.put(Config.MoodID, moodID);

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //Log.w("hahac",content);
                Gson gson = new Gson();
                MoodCommentBean moodCommentBean = gson.fromJson(content, MoodCommentBean.class);
                if (moodCommentBean.getStatus().equals(Config.SUCCESS)) {
                    et_content.setText("");
                    tv_comment.setText(moodCommentBean.getCommentCount());
                    intent.putExtra("commentCount",moodCommentBean.getCommentCount());
                    ActivityMoodInDeatil.this.setResult(SchoolFragment.RequestCode,intent);
                    sweetAlertDialog = new mSweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setTitleText("提交成功");
                    sweetAlertDialog.setCanceledOnTouchOutside(true);
                    sweetAlertDialog.show();
                    new CountDownTimer(1000,500){
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            PullToRefresh();
                            sweetAlertDialog.cancel();
                            isCommitCommenting = false;
                        }
                    }.start() ;
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
                Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        int keyHeight = getResources().getDisplayMetrics().widthPixels / 3;
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
            //监听到软键盘弹起
            if (softKeyboardHeight == 0) {
                softKeyboardHeight = oldBottom - bottom;
            }
            hideEmojiLayout(false);
            iv_emojicon_switch.setImageResource(R.drawable.ic_emoji);
            ll_emoji_bar.setVisibility(View.VISIBLE);

        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
            //监听到软件盘关闭
            if (showEmojiView) {
                //显示Emoji视图
                showEmojiView = false;

                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ll_emoji_bar.getLayoutParams();
                lp.bottomMargin = softKeyboardHeight;
                ll_emoji_bar.setLayoutParams(lp);

                lp = (FrameLayout.LayoutParams) emojiView.getLayoutParams();
                lp.height = softKeyboardHeight;
                emojiView.setVisibility(View.VISIBLE);

                emojiContainerShown = true;

                SwipeBackHelper.getCurrentPage(this)//获取当前页面
                        .setSwipeBackEnable(false);
            } else {
                iv_emojicon_switch.setImageResource(R.drawable.ic_emoji);
                ll_emoji_bar.setVisibility(View.GONE);

                SwipeBackHelper.getCurrentPage(this)//获取当前页面
                        .setSwipeBackEnable(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (emojiContainerShown) {
            hideEmojiLayout(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(et_content);
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(et_content, emojicon);
    }

    /*@Override
    public void onBackspace() {
        et_content.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }

    @Override
    public void onEmojiSelected(String res) {
        *//*end = et_content.getText().length();
        content = content+et_content.getText().toString().substring(start,end)+"["+res+"]";
*//*
        String result = String.valueOf(Character.toChars(Integer.decode("0x" + res)));
        et_content.setText(et_content.getText().append(result).toString());
        et_content.setSelection(et_content.getText().length());

        *//*ToastUtil.showToast(context,et_content.getText().toString());
        start = et_content.getText().length();
        Log.w("hahac",content);*//*
    }*/
}
