package com.supertreasure.trade;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.Browser;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.activity.BaseActivity;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
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
import com.supertreasure.R;
import com.supertreasure.bean.GoodBean;
import com.supertreasure.bean.GoodDetail;
import com.supertreasure.bean.MarketBrowser;
import com.supertreasure.bean.MarketCollect;
import com.supertreasure.bean.MarketPraise;
import com.supertreasure.dialog.RefreshDialog;
import com.supertreasure.dialog.mSweetAlertDialog;
import com.supertreasure.eventbus.RefleshMineActivity;
import com.supertreasure.eventbus.initdotEvent;
import com.supertreasure.home.ImagePagerActivity;
import com.supertreasure.main.TheApp;
import com.supertreasure.mine.ActivityMyShop;
import com.supertreasure.util.ChildViewPager;
import com.supertreasure.util.Config;
import com.supertreasure.trade.GoodPicAdapter;
import com.supertreasure.util.GetHeightResUrl;
import com.supertreasure.util.SlideBackActivity;
import com.supertreasure.util.ToastUtil;
import com.supertreasure.util.photoview.PhotoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.w3c.dom.Text;

import me.relex.circleindicator.CircleIndicator;


/**
 * Created by Administrator on 2016/2/20.
 */
public class ActivityGoodDetail extends AbActivity  {

    private RefreshDialog refreshDialog;
    private mSweetAlertDialog sweetAlertDialog;
    private final int NoChangeTime = 100000000;
    private int autoChangeTime= 5000;
    private Runnable runnable;
    private GoodPicAdapter adapter;
    private AbRequestParams params;
    private int sellID;
    private int userID;
    private String userName;
    private String token;
    private String pics;
    private String[] pic;
    private Context context;
    private ChildViewPager viewPager;
    private LinearLayout root;
    private LinearLayout ll_like;
    private FrameLayout fl_good;
    private CircleIndicator indicator;
    private View ll_title_left;
    private View bt_title_left;
    private LinearLayout ll_title_right;
    private SimpleDraweeView draweeView;
    private ImageView iv_like;
    private ImageView iv_sex;
    private Button bt_collect;
    private TextView tv_title;
    private TextView tv_price;
    private TextView tv_old_price;
    private TextView tv_nickName;
    private TextView tv_begin_time;
    private TextView tv_school;
    private TextView tv_introduction;
    private TextView tv_linker;
    private TextView tv_tele_phone;
    private TextView tv_type;
    private TextView tv_like;
    private Intent intent;
    private boolean isPraise;
    private boolean isCollect;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_details);
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

    private void initView() {
        context = this;
        root = (LinearLayout) findViewById(R.id.root);
        AbViewUtil.scaleContentView(root);
        viewPager = (ChildViewPager) findViewById(R.id.goodViewPager);
        ll_like = (LinearLayout) findViewById(R.id.ll_like);
        fl_good = (FrameLayout) findViewById(R.id.fl_good);
        ll_title_left =  findViewById(R.id.ll_title_left);
        bt_title_left =  findViewById(R.id.bt_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        ll_title_right.setVisibility(View.INVISIBLE);
        draweeView = (SimpleDraweeView) findViewById(R.id.draweeView);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        iv_like = (ImageView) findViewById(R.id.iv_like);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        bt_collect = (Button) findViewById(R.id.bt_collect);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("宝贝详情");
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_old_price = (TextView) findViewById(R.id.tv_old_price);
        tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        tv_begin_time = (TextView) findViewById(R.id.tv_begin_time);
        tv_school = (TextView) findViewById(R.id.tv_school);
        tv_introduction = (TextView) findViewById(R.id.tv_introduction);
        tv_linker = (TextView) findViewById(R.id.tv_linker);
        tv_tele_phone = (TextView) findViewById(R.id.tv_tele_phone);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_like = (TextView) findViewById(R.id.tv_like);
        intent = new Intent();
    }

    private void initData() {

        sellID = getIntent().getIntExtra(Config.SellID,0);
        userName = getIntent().getStringExtra(Config.UserName);
        token = getIntent().getStringExtra(Config.Token);
        refreshDialog = new RefreshDialog(this);
        refreshDialog.show();
        getData();
    }

    private final Handler viewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;

            viewPager.setCurrentItem(what);
        }
    };

    private void initViewPager(String pics) {

        viewPager.setVisibility(View.VISIBLE);
        pic = pics.split(",");
        adapter = new GoodPicAdapter(context,pic);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(myOnPageChangeListener);

        //判断如果只有一张图片,那就不显示圆点
        if(pic.length!=0){
            if(pic.length!=1){
                //开始
                EventBus.getDefault().post(new initdotEvent(Config.Good));
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        int next = viewPager.getCurrentItem() + 1;
                        if(next >= adapter.getCount()) {
                            next = 0;
                        }
                        viewHandler.sendEmptyMessage(next);
                    }
                };
                viewHandler.postDelayed(runnable, autoChangeTime);
                //结束
            }
        }
    }

    ViewPager.OnPageChangeListener myOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            viewHandler.removeCallbacks(runnable);
            viewHandler.postDelayed(runnable, autoChangeTime);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void getData() {

        String url = Config.getAPI(Config.APIGoodDetail);
        params = new AbRequestParams();
        params.put(Config.SellID,sellID);
        params.put(Config.UserName,userName);
        params.put(Config.Token,token);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //Log.w("haha222",content);
                refreshDialog.dismiss();
                Gson gson = new Gson();
                GoodDetail goodDetail = gson.fromJson(content,GoodDetail.class);
                if(goodDetail.getStatus().equals(Config.SUCCESS)){
                    GoodDetail.User user = goodDetail.getUser();
                    int width = 80, height = 80;
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(user.getUserPic()))
                            .setResizeOptions(new ResizeOptions(width, height))
                            .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setOldController(draweeView.getController())
                            .setImageRequest(request)
                            .build();
                    draweeView.setController(controller);
                    //draweeView.setImageURI(Uri.parse(user.getUserPic()));
                    tv_nickName.setText(user.getNickName());
                    tv_school.setText(user.getBelongschool());
                    userID = user.getUserId();
                    if(user.getSex().equals(Config.Boy)){
                        iv_sex.setBackgroundResource(R.drawable.sex_boy);
                    }else {
                        iv_sex.setBackgroundResource(R.drawable.sex_girl);
                    }
                    GoodDetail.Good good = goodDetail.getGood();
                    pics = good.getPics();
                    tv_introduction.setText(good.getIntroduction());
                    tv_price.setText(good.getPrice());
                    tv_old_price.setText(good.getCost());
                    tv_tele_phone.setText(good.getTelePhone());
                    tv_linker.setText(good.getLinker());
                    String beginTime[] = good.getBeginTime().split("T");
                    tv_begin_time.setText(beginTime[0]);
                    tv_type.setText(GetType(good.getType()));
                    tv_like.setText(good.getPraise()+"");
                    isPraise = good.isPraise();
                    isCollect = good.isCollect();
                    if(isCollect){
                        bt_collect.setText("已收藏");
                    }else {
                        bt_collect.setText("收藏");
                    }
                    if(isPraise){
                        iv_like.setBackgroundResource(R.drawable.like_press);
                    }else{
                        iv_like.setBackgroundResource(R.drawable.like_normal);
                    }

                    initViewPager(pics);
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
                refreshDialog.dismiss();
                Toast.makeText(context,"获取宝贝详情失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String GetType(String type) {
        if(type.equals(Config.Life)){
            type = "生活用品";
        }else  if(type.equals(Config.Elect)){
            type = "电子产品";
        }else {
            type = "其他";
        }
        return type;
    }

    private void setListener() {

        draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ActivityHisStore.class);
                intent.putExtra(Config.UserID,userID);
                startActivity(intent);
            }
        });

        ll_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodPriase();
            }
        });

        bt_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCollect)
                     GoodCollect();
                else {
                    showIsColletedDialog();
                }
            }
        });

        ll_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //viewPager会截获其他监听事件，滑动之类。所以直接Onclik是无效的
        viewPager.setOnSingleTouchListener(new ChildViewPager.OnSingleTouchListener() {
            @Override
            public void onSingleTouch() {
                /*int position = viewPager.getCurrentItem()+1;
                int size = pic.length;*/
                showGoodDetailView(viewPager.getCurrentItem());
                /*tv_num.setText(position+"/"+size);*/
            }
        });
    }


    private void imageBrower(int position, String[] urls) {

        Intent intent = new Intent(TheApp.instance, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void showGoodDetailView(int position) {
        if (pic != null)
            imageBrower(position, pic);
    }

    private void showIsColletedDialog() {
        sweetAlertDialog = new mSweetAlertDialog(context,mSweetAlertDialog.ERROR_TYPE);
        sweetAlertDialog.setTitleText("该商品已经收藏啦");
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.show();
        new CountDownTimer(1000,500){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                sweetAlertDialog.cancel();
            }
        }.start() ;
    }

    private void GoodCollect() {
        String url = Config.getAPI(Config.APIWantGood);
        params = new AbRequestParams();
        params.put(Config.SellID,sellID);
        params.put(Config.UserName,userName);
        params.put(Config.Token,token);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                MarketCollect collect = gson.fromJson(content,MarketCollect.class);
                if(collect.getStatus().equals(Config.SUCCESS)){
                    sweetAlertDialog = new mSweetAlertDialog(context,mSweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setTitleText("收藏成功");
                    //收藏成功,
                    EventBus.getDefault().post(RefleshMineActivity.getInstance());
                    //ToastUtil.showToast(TheApp.instance, "元宝币-2");
                    new CountDownTimer(1000,500){
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            sweetAlertDialog.cancel();
                        }
                    }.start() ;
                    sweetAlertDialog.show();
                    sweetAlertDialog.setCanceledOnTouchOutside(false);

                    bt_collect.setText("已收藏");
                    isCollect = true;
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
                Toast.makeText(context,"收藏失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GoodPriase() {

        String url = Config.getAPI(Config.APIPraiseGood);
        params = new AbRequestParams();
        params.put(Config.SellID,sellID);
        params.put(Config.UserName,userName);
        params.put(Config.Token,token);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                MarketPraise praise = gson.fromJson(content,MarketPraise.class);
                //Log.w("hahalike",content);
                if(praise.getStatus().equals(Config.SUCCESS)){
                    if(praise.getPraiseNum()- Integer.parseInt(tv_like.getText().toString())<0){
                       //取消点赞

                        EventBus.getDefault().post(RefleshMineActivity.getInstance());
                        //ToastUtil.showToast(TheApp.instance, "元宝币-1");
                        iv_like.setBackgroundResource(R.drawable.like_normal);
                    }else {
                        //点赞
                        EventBus.getDefault().post(RefleshMineActivity.getInstance());
                        //ToastUtil.showToast(TheApp.instance, "元宝币+1");
                        iv_like.setBackgroundResource(R.drawable.like_press);
                    }
                    tv_like.setText(praise.getPraiseNum()+"");
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
                Toast.makeText(context,"点赞失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Subscribe
    public void onEventMainThread(initdotEvent event) {
        if(event.getIndex()==Config.Good)
        indicator.setViewPager(viewPager);
    }
}
