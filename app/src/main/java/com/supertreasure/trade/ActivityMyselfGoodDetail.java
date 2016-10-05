package com.supertreasure.trade;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
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
import com.supertreasure.R;
import com.supertreasure.bean.GoodDetail;
import com.supertreasure.bean.MarketPraise;
import com.supertreasure.bean.Status;
import com.supertreasure.dialog.RefreshDialog;
import com.supertreasure.eventbus.RefleshDecorFragment;
import com.supertreasure.eventbus.RefleshEleProFragment;
import com.supertreasure.eventbus.RefleshMyShopPublish;
import com.supertreasure.eventbus.RefleshMyShopRemove;
import com.supertreasure.eventbus.RefleshMyShopSold;
import com.supertreasure.eventbus.RefleshOtherProFragment;
import com.supertreasure.eventbus.initdotEvent;
import com.supertreasure.fragment.DecorateFragment;
import com.supertreasure.home.ImagePagerActivity;
import com.supertreasure.main.TheApp;
import com.supertreasure.mine.ActivityMyShop;
import com.supertreasure.util.ChildViewPager;
import com.supertreasure.util.Config;
import com.supertreasure.dialog.mSweetAlertDialog;
import com.supertreasure.util.GetHeightResUrl;
import com.supertreasure.util.photoview.PhotoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Administrator on 2016/2/20.
 */
public class ActivityMyselfGoodDetail extends AbActivity{
    private String goodType;
    private final int Remove = 1;
    private final int Selled = 2;
    private mSweetAlertDialog sweetDialog;
    private final int NoChangeTime = 100000000;
    private int autoChangeTime= 5000;
    private int sellID;
    private int position;
    private Runnable runnable;
    private GoodPicAdapter adapter;
    private AbRequestParams params;
    private String userName;
    private String token;
    private String pics;
    private String[] pic;
    private Context context;
    private ChildViewPager viewPager;
    private CircleIndicator indicator;
    private FrameLayout fl_good;
    private LinearLayout root;
    private LinearLayout ll_like;
    private LinearLayout ll_bottom;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private View bt_title_left;
    private RefreshDialog refreshDialog;
    private SimpleDraweeView draweeView;
    private ImageView iv_like;
    private ImageView iv_sex;
    private Button bt_remove;
    private Button bt_selled;
    private TextView tv_my_shop;
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
    private boolean isShowStore = true;
    private boolean isSoldOrRemove = false;
    private boolean isPraise;
    private boolean isRemove = false;
    private boolean isSelled = false;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myself_goods_details);
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
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager = (ChildViewPager) findViewById(R.id.goodViewPager);
        ll_like = (LinearLayout) findViewById(R.id.ll_like);
        fl_good = (FrameLayout) findViewById(R.id.fl_good);
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        bt_title_left =  findViewById(R.id.bt_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        ll_title_right.setVisibility(View.INVISIBLE);
        ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
        draweeView = (SimpleDraweeView) findViewById(R.id.draweeView);
        iv_like = (ImageView) findViewById(R.id.iv_like);
        iv_sex = (ImageView) findViewById(R.id.iv_sex);
        bt_remove = (Button) findViewById(R.id.bt_remove);
        bt_selled = (Button) findViewById(R.id.bt_selled);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("我的宝贝");
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
        goodType = getIntent().getStringExtra(Config.GoodType);
        if (goodType == null)
            goodType = Config.GoodType_Decorate;
        position = getIntent().getIntExtra(Config.Position,0);
        sellID = getIntent().getIntExtra(Config.SellID,0);
        userName = getIntent().getStringExtra(Config.UserName);
        token = getIntent().getStringExtra(Config.Token);
        isShowStore = getIntent().getBooleanExtra(Config.ShowStore,true);
        isSoldOrRemove = getIntent().getBooleanExtra(Config.SoldOrRemove,false);
        if(isSoldOrRemove){
            ll_bottom.setVisibility(View.GONE);
        }
        if(isShowStore){
            draweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TheApp.instance, ActivityMyShop.class);
                    startActivity(intent);
                }
            });
        }
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
                EventBus.getDefault().post(new initdotEvent(Config.GoodSelf));
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
                    tv_nickName.setText(user.getNickName());
                    tv_school.setText(user.getBelongschool());
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

        ll_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodPriase();
            }
        });

        bt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRemove)
                    showRemoveOrSellDialog(Remove);
                else if(isSelled)
                    showRemoveOrSellDialog(Selled);
                else
                    showRemoveDialog();
            }
        });

        bt_selled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRemove)
                    showRemoveOrSellDialog(Remove);
                else if(isSelled)
                    showRemoveOrSellDialog(Selled);
                else
                    showSelledDialog();
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
                showGoodDetailView(viewPager.getCurrentItem());
            }
        });

        indicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(indicator.getId());
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
        if(pic!=null)
        imageBrower(position,pic);
    }

    private void showRemoveOrSellDialog(int Type){
        sweetDialog = new mSweetAlertDialog(context,mSweetAlertDialog.ERROR_TYPE);
        if(Type==Remove)
            sweetDialog.setTitleText("该商品已经下架啦");
        else
            sweetDialog.setTitleText("该商品已经售出啦");
        sweetDialog.setCanceledOnTouchOutside(true);
        sweetDialog.show();
        new CountDownTimer(1000,10){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                sweetDialog.cancel();
            }
        }.start() ;
    }

    private void showSelledDialog(){
        sweetDialog = new mSweetAlertDialog(context, mSweetAlertDialog.WARNING_TYPE);
        sweetDialog.setCanceledOnTouchOutside(true);
        sweetDialog.setTitleText("确定已出售了吗？").setConfirmText("确定").setCancelText("取消")
                .setConfirmClickListener(new mSweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(mSweetAlertDialog sweetAlertDialog) {
                        SelledGood();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        sweetDialog.dismiss();
                    }
                });
        sweetDialog.show();
    }

    private void showRemoveDialog(){
        sweetDialog = new mSweetAlertDialog(context, mSweetAlertDialog.WARNING_TYPE);
        sweetDialog.setCanceledOnTouchOutside(true);
        sweetDialog.setTitleText("确定下架吗？").setConfirmText("确定").setCancelText("取消")
                .setConfirmClickListener(new mSweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(mSweetAlertDialog sweetAlertDialog) {
                        RemoveGood();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        sweetDialog.dismiss();
                    }
                });
        sweetDialog.show();
    }

    private void RemoveGood() {

        String url = Config.getAPI(Config.APIRemoveGood);
        params = new AbRequestParams();
        params.put(Config.SellID,sellID);
        params.put(Config.UserName,userName);
        params.put(Config.Token,token);

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                Status status = gson.fromJson(content,Status.class);
                if(status.getStatus().equals(Config.SUCCESS)){
                    sweetDialog.changeAlertType(mSweetAlertDialog.SUCCESS_TYPE);
                    sweetDialog.setTitleText("下架成功");
                    intent.putExtra("position", position);
                    intent.putExtra("remove",true);
                    ActivityMyselfGoodDetail.this.setResult(DecorateFragment.REQUEST_CODE, intent);
                    bt_remove.setText(status.getMsg());
                    isRemove = true;
                    new CountDownTimer(Config.DimissAnimTime,Config.DimissAnimTime){
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            sweetDialog.dismiss();
                            switch (goodType) {
                                case Config.GoodType_Decorate:
                                    EventBus.getDefault().post(new RefleshDecorFragment());
                                    break;
                                case Config.GoodType_Other:
                                    EventBus.getDefault().post(new RefleshOtherProFragment());
                                    break;
                                case Config.GoodType_Eleproduct:
                                    EventBus.getDefault().post(new RefleshEleProFragment());
                                    break;
                            }
                            EventBus.getDefault().post(new RefleshMyShopPublish());
                            EventBus.getDefault().post(new RefleshMyShopRemove());
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
                sweetDialog.cancel();
            }
        });
    }

    private void SelledGood() {

        String url = Config.getAPI(Config.APISoldOutGood);
        params = new AbRequestParams();
        params.put(Config.SellID,sellID);
        params.put(Config.UserName,userName);
        params.put(Config.Token,token);

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                Status status = gson.fromJson(content,Status.class);
                if(status.getStatus().equals(Config.SUCCESS)){
                    sweetDialog.changeAlertType(mSweetAlertDialog.SUCCESS_TYPE);
                    sweetDialog.setTitleText("商品已出售");
                    bt_selled.setText(status.getMsg());
                    isSelled = true;
                    intent.putExtra("remove",true);
                    intent.putExtra("position",position);
                    ActivityMyselfGoodDetail.this.setResult(DecorateFragment.REQUEST_CODE,intent);
                    new CountDownTimer(Config.DimissAnimTime,Config.DimissAnimTime){
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            sweetDialog.dismiss();
                            switch (goodType) {
                                case Config.GoodType_Decorate:
                                    EventBus.getDefault().post(new RefleshDecorFragment());
                                    break;
                                case Config.GoodType_Other:
                                    EventBus.getDefault().post(new RefleshOtherProFragment());
                                    break;
                                case Config.GoodType_Eleproduct:
                                    EventBus.getDefault().post(new RefleshEleProFragment());
                                    break;
                            }
                            EventBus.getDefault().post(new RefleshMyShopSold());
                            EventBus.getDefault().post(new RefleshMyShopPublish());
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
                sweetDialog.cancel();
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
                        iv_like.setBackgroundResource(R.drawable.like_normal);
                    }else {
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
        if(event.getIndex()==Config.GoodSelf)
        indicator.setViewPager(viewPager);
    }
}
