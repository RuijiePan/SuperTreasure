package com.supertreasure.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
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
import com.supertreasure.R;
import com.supertreasure.bean.MeBeen;
import com.supertreasure.constant.Constant;
import com.supertreasure.eventbus.EditMoney;
import com.supertreasure.eventbus.EditNickName;
import com.supertreasure.eventbus.EditSign;
import com.supertreasure.eventbus.RefleshMineActivity;
import com.supertreasure.home.ImagePagerActivity;
import com.supertreasure.mine.ActivityMoneyDetail;
import com.supertreasure.mine.ActivityMyCollection;
import com.supertreasure.mine.ActivityMyCoupon;
import com.supertreasure.mine.ActivityMyMood;
import com.supertreasure.mine.ActivityMyShop;
import com.supertreasure.mine.ActivityPay;
import com.supertreasure.mine.ActivityPersonData;
import com.supertreasure.mine.ActivitySetting;
import com.supertreasure.service.HttpResultUtil;
import com.supertreasure.util.Config;
import com.supertreasure.util.CustomToolBar;
import com.supertreasure.util.MyUtils;
import com.supertreasure.util.NetUtils;
import com.supertreasure.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ActivityMine extends AbActivity implements View.OnClickListener {
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    /*private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;*/
    private CustomToolBar toolBar;
    private SimpleDraweeView draweeView;
    //private TextView tv_title;
    private TextView tv_nickName;
    private TextView tv_sign;
    private TextView tv_money;

    private View head_viewGroup;
    private View view_nickname;
    private View view_ID;
    private View button_pay;
    private View button_mycoupon;
    private View button_mySaySay;
    private View button_myCollection;
    private View button_myShop;
    private View button_mySetting;
    private View button_myTreasure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        EventBus.getDefault().register(this);
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));

        initView();
        initData();
        initToolbar();
        setListener();
    }

    private void initData() {
        //从本地获取
        String userPic = AbSharedUtil.getString(TheApp.instance, Config.Content_AboutMe_HeadImg);
        String nickName = AbSharedUtil.getString(TheApp.instance, Config.Content_AboutMe_Nickname);
        //String sign = AbSharedUtil.getString(TheApp.instance,Config.Content_Me_Sign);
        String money = AbSharedUtil.getString(TheApp.instance, Config.Content_AboutMe_Money);
        String sign = AbSharedUtil.getString(TheApp.instance, Config.Content_AboutMe_Signature);

        if (userPic != null) {
            int width = 120, height = 120;
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(userPic))
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.getController())
                    .setImageRequest(request)
                    .build();
            draweeView.setController(controller);
        }
        //draweeView.setImageURI(Uri.parse(userPic));
        //AbImageLoader.getInstance(TheApp.instance).display(imageview_mine, userPic);
        tv_money.setText(money);
        tv_sign.setText(sign);
        tv_nickName.setText(nickName);
        //tv_sign.setText(nickName);

        isPrepared = true;
        mHasLoadedOnce = true;
        PullToRefrsh();
    }

    private void initToolbar() {
        toolBar.setCenterText("我");
        //toolBar.setLeftIcon(R.drawable.ic_arrow_back_black_24dp);
    }

    public void PullToRefrsh(){
        //从网络获取
        String url = Config.getAPI(Config.APIClickMe);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this, Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this, Config.Token));
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //Logger.json("response content", content);
                if (HttpResultUtil.handleUnloginErrorStatus(content, RefleshMineActivity.getInstance(), ActivityMine.this)) {
                    return;
                }
                Gson gson = new Gson();
                MeBeen meBeen = gson.fromJson(content, MeBeen.class);
                if (meBeen.getStatus().equals(Config.SUCCESS)) {

                    int width = 200, height = 200;
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(meBeen.getMydata().getUserPic()))
                            .setResizeOptions(new ResizeOptions(width, height))
                            .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setOldController(draweeView.getController())
                            .setImageRequest(request)
                            .build();
                    draweeView.setController(controller);
                    //draweeView.setImageURI(Uri.parse(meBeen.getMydata().getUserPic()));
                    //AbImageLoader.getInstance(TheApp.instance).display(imageview_mine, meBeen.getMydata().getUserPic());
                    tv_money.setText(meBeen.getMydata().getMoney());
                    tv_sign.setText(meBeen.getMydata().getSign());
                    tv_nickName.setText(meBeen.getMydata().getNickName());

                    AbSharedUtil.putString(TheApp.instance, Config.Content_AboutMe_HeadImg, meBeen.getMydata().getUserPic());
                    AbSharedUtil.putString(TheApp.instance, Config.Content_AboutMe_Nickname, meBeen.getMydata().getNickName());
                    AbSharedUtil.putString(TheApp.instance, Config.Content_AboutMe_Signature, meBeen.getMydata().getSign());
                    AbSharedUtil.putString(TheApp.instance, Config.Content_AboutMe_Money, meBeen.getMydata().getMoney());

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
                MyUtils.show(TheApp.instance, "获取我的资料失败");
            }
        });
    }

    public void initView() {
        /*ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        ll_title_right.setVisibility(View.INVISIBLE);
        ll_title_left.setVisibility(View.INVISIBLE);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("我");*/

        toolBar = (CustomToolBar) findViewById(R.id.toolbar);
        head_viewGroup = findViewById(R.id.head_viewGroup);
        draweeView = (SimpleDraweeView) findViewById(R.id.tv_mine);
        view_nickname = findViewById(R.id.tv_mine_nickname);
        //view_ID =  findViewById(R.id.tv_mine_ID);
        button_pay = findViewById(R.id.btn_mine_pay);
        button_mycoupon = findViewById(R.id.btn_mine_myCoupon);
        button_mySaySay = findViewById(R.id.btn_mine_mySaySay);
        button_myCollection = findViewById(R.id.btn_mine_myCollection);
        button_myShop = findViewById(R.id.btn_mine_myShop);
        button_mySetting = findViewById(R.id.btn_mine_mySetting);
        tv_nickName = (TextView) findViewById(R.id.tv_mine_nickname);
        tv_sign = (TextView) findViewById(R.id.tv_mine_sign_content);
        tv_money = (TextView) findViewById(R.id.tv_mymoney);
        button_myTreasure = findViewById(R.id.btn_mine_myTreasure);
    }

    public void setListener() {
        head_viewGroup.setOnClickListener(this);
        draweeView.setOnClickListener(this);
        button_pay.setOnClickListener(this);
        button_mycoupon.setOnClickListener(this);
        button_mySaySay.setOnClickListener(this);
        button_myCollection.setOnClickListener(this);
        button_myShop.setOnClickListener(this);
        button_mySetting.setOnClickListener(this);
        button_myTreasure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_mine_myCoupon:
                //MyUtils.show(this, "我的优惠卷");
                startActivity(new Intent(TheApp.instance, ActivityMyCoupon.class));
                break;
            case R.id.head_viewGroup:
                //MyUtils.show(this, "我的头像 条");
                startActivityForResult(new Intent(TheApp.instance, ActivityPersonData.class),Constant.RequestCode_ActivityMine);
                break;
            case R.id.tv_mine:
                //MyUtils.show(this, "我的头像");
                String userPic = AbSharedUtil.getString(TheApp.instance, Config.Content_AboutMe_HeadImg);
                //Log.i("userPic",userPic);
                imageBrower(0,new String[]{userPic});
                //startActivity(new Intent(this, ActivityPersonData.class));
                break;
            case R.id.btn_mine_pay:
                startActivity(new Intent(TheApp.instance, ActivityPay.class));
                break;
            case R.id.btn_mine_mySaySay:
                //MyUtils.show(this, "我的说说");
                startActivity(new Intent(TheApp.instance, ActivityMyMood.class));
                break;
            case R.id.btn_mine_myCollection:
                //MyUtils.show(this, "我的收藏");
                startActivity(new Intent(TheApp.instance, ActivityMyCollection.class));
                break;
            case R.id.btn_mine_myShop:
                //MyUtils.show(this, "我的小店");
                startActivity(new Intent(TheApp.instance, ActivityMyShop.class));
                break;
            case R.id.btn_mine_mySetting:
                //MyUtils.show(this, "设置");
                startActivity(new Intent(TheApp.instance, ActivitySetting.class));
                break;
            case R.id.btn_mine_myTreasure:
                //MyUtils.show(this, "元宝");
                intent = new Intent(TheApp.instance, ActivityMoneyDetail.class);
                intent.putExtra(Config.Content_Mine_money,tv_money.getText().toString().trim());
                startActivity(intent);
                //this.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
                //this.overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
                break;
        }
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(TheApp.instance, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case Constant.RequestCode_ActivityPersonData:
                String userPic = AbSharedUtil.getString(TheApp.instance, Config.Content_AboutMe_HeadImg);
                String nickName = AbSharedUtil.getString(TheApp.instance, Config.Content_AboutMe_Nickname);
                String sign = AbSharedUtil.getString(TheApp.instance, Config.Content_AboutMe_Signature);
                String money = AbSharedUtil.getString(TheApp.instance, Config.Content_AboutMe_Money);

                int width = 120, height = 120;
                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(userPic))
                        .setResizeOptions(new ResizeOptions(width, height))
                        .build();
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setOldController(draweeView.getController())
                        .setImageRequest(request)
                        .build();
                draweeView.setController(controller);
                //draweeView.setImageURI(Uri.parse(userPic));
                //AbImageLoader.getInstance(TheApp.instance).display(imageview_mine, userPic);
                tv_money.setText(money);
                tv_sign.setText(sign);
                tv_nickName.setText(nickName);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

   /* @Subscribe
    public void onEventMainThread(RefleshMineActivity event) {

        String msg = "onEventMainThread收到了消息：" + event.getUserPic();
        Log.d("eventbus", msg);

        AbImageLoader.getInstance(TheApp.instance).display(imageview_mine, event.getUserPic());

        //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }*/

    @Subscribe
    public void onEventMainThread(RefleshMineActivity event) {
        String msg = "RefleshMineActivity";
        Log.d("eventbus", msg);
        if (NetUtils.isConnected(TheApp.instance)) {
            if (!isPrepared)
                return;
            PullToRefrsh();
        }
    }

    @Subscribe
    public void onEventMainThread(EditSign event) {
        tv_sign.setText(event.getSign());
    }

    @Subscribe
    public void onEventMainThread(EditNickName event) {
        tv_nickName.setText(event.getNickName());
    }

    @Subscribe
    public void onEventMainThread(EditMoney event) {
        tv_money.setText(event.getMoney());
    }
}
