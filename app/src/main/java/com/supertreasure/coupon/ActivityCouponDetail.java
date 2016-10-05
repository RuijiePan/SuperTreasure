package com.supertreasure.coupon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.orhanobut.logger.Logger;
import com.supertreasure.R;
import com.supertreasure.bean.MarketBrowser;
import com.supertreasure.bean.MarketCollect;
import com.supertreasure.bean.MarketPraise;
import com.supertreasure.eventbus.RefleshMineActivity;
import com.supertreasure.eventbus.UpdateActivityCoupon;
import com.supertreasure.eventbus.UpdateActivityCouponDetail;
import com.supertreasure.eventbus.UpdateActivityCouponHistory;
import com.supertreasure.home.ImagePagerActivity;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.util.GetHeightResUrl;
import com.supertreasure.util.LoggerUtil;
import com.supertreasure.util.ReboundScrollView;
import com.supertreasure.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2016/1/29.
 */
public class ActivityCouponDetail extends AbActivity implements View.OnClickListener{
    public int activityType;
    public static final int COLLECT_SUCCESS = 1;
    public static final int COLLECT_FAILURE = 2;
    public static final int RESULT_CODE = 10086;
    private ReboundScrollView reboundScrollView;
    private SimpleDraweeView draweeView;
    private Context context;
    private LinearLayout ll_browse;
    private LinearLayout ll_like;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private LinearLayout root;
    private LinearLayout ll_collect;
    private TextView tv_browse;
    private TextView tv_like;
    private TextView tv_title;
    private TextView tv_hadcollect;
    private ImageView iv_like;
    private ImageView iv_collect;
    private boolean isCollect;
    private boolean isPraise;
    private String couponPics;
    private int height;
    private int width;
    //private int couponID;
    private Button bt_title_right;
    private Button bt_title_right_history;
    private Button bt_title_left;
    private Intent intent;
    private int position ;
    private String shopName ;
    private int ActivityCouponCouponID ;
    private int ActivityCouponCouponPosition ;
    private int CouponID ;
    private  Bundle bundle ;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.coupon_detail);
        SwipeBackHelper.onCreate(this);

        initView();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout)findViewById(R.id.root));
        context = this;
        draweeView = (SimpleDraweeView) findViewById(R.id.draweeView);
        root = (LinearLayout) findViewById(R.id.root);
        ll_collect = (LinearLayout) findViewById(R.id.ll_collect);
        ll_browse = (LinearLayout) findViewById(R.id.ll_browse);
        ll_like = (LinearLayout) findViewById(R.id.ll_like);
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("优惠券详情");
        tv_browse = (TextView) findViewById(R.id.tv_browse);
        tv_like = (TextView) findViewById(R.id.tv_like);
        tv_hadcollect = (TextView) findViewById(R.id.tv_hadcollect);
        iv_like = (ImageView) findViewById(R.id.iv_like);
        iv_collect = (ImageView) findViewById(R.id.iv_collect);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        bt_title_right_history = (Button) findViewById(R.id.bt_title_right_history);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);

        reboundScrollView = (ReboundScrollView) findViewById(R.id.scrollView);
        intent = new Intent();
    }


    private void initData() {

        bundle = getIntent().getBundleExtra("coupon");

        activityType = bundle.getInt("activityType");
        position = getIntent().getIntExtra("position", 0);
        shopName = getIntent().getStringExtra("shopName");
        tv_like.setText(bundle.getInt("couponPraise")+"");
        tv_browse.setText(bundle.getInt("browserNum")+"");
        ActivityCouponCouponID = bundle.getInt("ActivityCouponCouponID");
        ActivityCouponCouponPosition = getIntent().getIntExtra("ActivityCouponCouponPosition", -1);
        CouponID = bundle.getInt("couponID");
        width = bundle.getInt("width");
        height = bundle.getInt("height");
        isPraise = bundle.getBoolean("isPraise");
        isCollect = bundle.getBoolean("isCollect");
        couponPics = bundle.getString("couponPics");
        intent.putExtra("position",position);
        intent.putExtra("isPraise",isPraise);
        intent.putExtra("isCollect",isCollect);
        intent.putExtra("praiseNum",bundle.getInt("couponPraise"));
        intent.putExtra("browseNum",bundle.getInt("browserNum"));


        if(isCollect)
        {
            iv_collect.setBackgroundResource(R.drawable.ic_collected);
            tv_hadcollect.setText("已收藏");
        }
        if(isPraise) {
            iv_like.setBackgroundResource(R.drawable.like_press);
        }else {
            iv_like.setBackgroundResource(R.drawable.like_normal);
        }

        couponBrowser();
        initCouponPic();
    }

    private void couponBrowser() {
        String url = Config.getAPI(Config.APIBrowserCoupon);
        final AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName,AbSharedUtil.getString(context,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(context, Config.Token));
        if (activityType == 1)
            params.put(Config.CouponID, ActivityCouponCouponID);
        else
            params.put(Config.CouponID, CouponID);

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                MarketBrowser browser = gson.fromJson(content, MarketBrowser.class);
                if (browser.getStatus().equals(Config.SUCCESS)) {
                    tv_browse.setText(browser.getBrowserNum() + "");
                    postBrowseEvent();
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
                Toast.makeText(context, "增加浏览次数失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initCouponPic() {
        draweeView.setLayoutParams(new LinearLayout.LayoutParams(
                (LinearLayout.LayoutParams.MATCH_PARENT)
                , TheApp.screenWidth * height / width));
        draweeView.setImageURI(Uri.parse(GetHeightResUrl.getHeightUrl(couponPics)));
    }

    private void setListener() {
        if(activityType == 2) {
            bt_title_right.setVisibility(View.INVISIBLE);
        }
        else if(activityType == 1){
            bt_title_right_history.setVisibility(View.VISIBLE);
            bt_title_right.setVisibility(View.GONE);
            /*bt_title_right.setBackgroundResource(R.drawable.ic_history);
            bt_title_right.setText("");
            bt_title_right.setOnClickListener(this);*/
            ll_title_right.setOnClickListener(this);
        }
        bt_title_left.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);
        bt_title_right_history.setOnClickListener(this);

        ll_like.setOnClickListener(this);
        ll_collect.setOnClickListener(this);
        iv_collect.setOnClickListener(this);
        draweeView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.bt_title_left:
                finish();
                break;
            case R.id.iv_collect:
                if(!isCollect)
                couponCollect();
                break;
            case R.id.ll_collect:
                if(!isCollect)
                couponCollect();
                break;
            case R.id.ll_like:
                if(!isCollect)
                couponPraise();
                break;
            case R.id.iv_like:
                if(!isCollect)
                couponPraise();
                break;
            case R.id.draweeView:
                initDetailView();
                break;
            case R.id.bt_title_right_history:
                Log.i("ActivityNewsHistory","ShopID:"+bundle.getInt("shopID"));

                intent = new Intent(TheApp.instance,ActivityCouponHistory.class);
                intent.putExtra("shopID",bundle.getInt("shopID"));
                intent.putExtra("ActivityCouponCouponID",ActivityCouponCouponID);
                intent.putExtra("ActivityCouponCouponPosition",ActivityCouponCouponPosition);
                intent.putExtra("shopName", shopName);

                startActivity(intent);
                //finish();
                break;
            case R.id.ll_title_right:
                Log.i("ActivityNewsHistory","ShopID:"+bundle.getInt("shopID"));

                intent = new Intent(TheApp.instance,ActivityCouponHistory.class);
                intent.putExtra("shopID", bundle.getInt("shopID"));
                intent.putExtra("ActivityCouponCouponID",ActivityCouponCouponID);
                intent.putExtra("ActivityCouponCouponPosition",ActivityCouponCouponPosition);
                intent.putExtra("shopName", shopName);
                startActivity(intent);
                //finish();
                break;
        }
    }

    /*Dialog dialog ;
    ViewPager mPager;*/
    private void initDetailView() {
        String[] pic = new String[1];
        pic[0] = couponPics;
        imageBrower(0, pic);
        /*View view = getLayoutInflater().inflate(R.layout.item_show_goodview,null);
        dialog = new Dialog(this,R.style.ChoiceGoodPicDialog);
        dialog.setContentView(view,new ViewGroup.LayoutParams(
                TheApp.screenWidth,(int)(0.6*TheApp.screenHeight)));
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView pohotoView = new PhotoView(ActivityCouponDetail.this);
                    pohotoView.setScaleType(ImageView.ScaleType.FIT_XY);
                pohotoView.isEnabled();
                TheApp.mAbImageLoader.display(pohotoView, GetHeightResUrl.getHeightUrl(couponPics));
                container.addView(pohotoView);
                return pohotoView;
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view==object;
            }
        });*/
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(TheApp.instance, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private synchronized void couponPraise() {
        iv_like.setEnabled(false);
        ll_like.setEnabled(false);
        String url = Config.getAPI(Config.APICouponPraise);
        final AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(context, Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(context, Config.Token));
        params.put(Config.CouponID, ActivityCouponCouponID);

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {

                Gson gson = new Gson();
                MarketPraise praise = gson.fromJson(content, MarketPraise.class);
                if (praise.getStatus().equals(Config.SUCCESS)) {
                    if (praise.getPraiseNum() <= Integer.parseInt(tv_like.getText().toString())) {
                        //取消点赞

                        EventBus.getDefault().post(RefleshMineActivity.getInstance());
                        ToastUtil.showToast(TheApp.instance,"元宝币-"+Config.prisecouponmoney);
                        iv_like.setBackgroundResource(R.drawable.like_normal);
                        isPraise = false;
                    } else {
                        //点赞

                        EventBus.getDefault().post(RefleshMineActivity.getInstance());
                        ToastUtil.showToast(TheApp.instance,"元宝币+"+Config.prisecouponmoney);
                        iv_like.setBackgroundResource(R.drawable.like_press);
                        isPraise = true;
                    }
                    if (activityType == 1) {
                        UpdateActivityCoupon update = new UpdateActivityCoupon();
                        tv_like.setText(praise.getPraiseNum() + "");
                        update.setIsCollect(isCollect);
                        update.setPraiseNum(praise.getPraiseNum());
                        update.setBrowseNum(Integer.parseInt(tv_browse.getText().toString()));
                        update.setIsPraise(isPraise);
                        update.setPosition(ActivityCouponCouponPosition);
                        EventBus.getDefault().post(update);
                    } else if (activityType == 2) {
                        tv_like.setText(praise.getPraiseNum() + "");
                        UpdateActivityCouponHistory updateHistory = new UpdateActivityCouponHistory();
                        updateHistory.setIsCollect(isCollect);
                        updateHistory.setPraiseNum(Integer.parseInt(tv_like.getText().toString()));
                        updateHistory.setBrowseNum(Integer.parseInt(tv_browse.getText().toString()));
                        updateHistory.setIsPraise(isPraise);
                        updateHistory.setPosition(position);
                        EventBus.getDefault().post(updateHistory);
                        LoggerUtil.d("ssssss","ActivityCouponCouponID:"+ ActivityCouponCouponID + "");
                        LoggerUtil.d("ssssss","CouponID:"+CouponID+"");
                        LoggerUtil.d("ssssss", "ActivityCouponCouponPosition:" + ActivityCouponCouponPosition+"");

                        if (ActivityCouponCouponID == CouponID) {
                            UpdateActivityCouponDetail updateDetail = new UpdateActivityCouponDetail();
                            updateDetail.setIsCollect(isCollect);
                            updateDetail.setPraiseNum(Integer.parseInt(tv_like.getText().toString()));
                            updateDetail.setBrowseNum(Integer.parseInt(tv_browse.getText().toString()));
                            updateDetail.setIsPraise(isPraise);
                            EventBus.getDefault().post(updateDetail);

                            UpdateActivityCoupon updateCoupon = new UpdateActivityCoupon();
                            updateCoupon.setIsCollect(isCollect);
                            updateCoupon.setPraiseNum(praise.getPraiseNum());
                            updateCoupon.setBrowseNum(Integer.parseInt(tv_browse.getText().toString()));
                            updateCoupon.setIsPraise(isPraise);
                            updateCoupon.setPosition(ActivityCouponCouponPosition);
                            EventBus.getDefault().post(updateCoupon);
                        }

                    }
                }



            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                iv_like.setEnabled(true);
                ll_like.setEnabled(true);
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Toast.makeText(context, "点赞失败", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private synchronized void couponCollect() {

        iv_collect.setEnabled(false);
        ll_collect.setEnabled(false);

        LoggerUtil.d("activityType","couponCollect");

        String url = Config.getAPI(Config.APICollectCoupon);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this, Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this, Config.Token));
        params.put(Config.CouponID,ActivityCouponCouponID);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                MarketCollect collect = gson.fromJson(content, MarketCollect.class);
                if(collect.getStatus().equals(Config.ERROR))
                    ToastUtil.showToast(TheApp.instance,collect.getMsg());
                LoggerUtil.d("activityType","content == "+content);
                if (collect.getStatus().equals(Config.SUCCESS)) {
                    iv_collect.setBackgroundResource(R.drawable.ic_collected);
                    tv_hadcollect.setText("已收藏");
                    //收藏成功,
                    EventBus.getDefault().post(RefleshMineActivity.getInstance());
                    if (isPraise)
                        ToastUtil.showToast(TheApp.instance, "元宝币-" + Config.collectcouponmoney_hadprised);
                    else
                        ToastUtil.showToast(TheApp.instance, "元宝币-"+Config.collectcouponmoney_unhadprised);
                    postCollectEvent();
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                iv_collect.setEnabled(true);
                ll_collect.setEnabled(true);

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {

            }
        });

    }


    @Subscribe
    public void onEventMainThread(UpdateActivityCouponDetail event) {
        String msg = "UpdateActivityCouponDetail";
        Log.d("eventbus", msg);
        if (activityType == 1) {
            boolean isPraise = event.isPraise();
            boolean isCollect = event.isCollect();
            int praiseNum = event.getPraiseNum();
            int browseNum = event.getBrowseNum();
            updataView(isPraise, isCollect, praiseNum, browseNum);
        }
    }

    private void updataView(boolean isPraise, boolean isCollect, int praiseNum, int browseNum) {
        this.isPraise = isPraise;
        this.isCollect = isCollect;
        if(isCollect)
        {
            tv_hadcollect.setText("已收藏");
            iv_collect.setBackgroundResource(R.drawable.ic_collected);
        }
        if(isPraise) {
            iv_like.setBackgroundResource(R.drawable.like_press);
        }else {
            iv_like.setBackgroundResource(R.drawable.like_normal);
        }

        tv_like.setText(praiseNum+"");
        tv_browse.setText(browseNum+"");

    }

    void postCollectEvent(){
        LoggerUtil.d("activityType","postCollectEvent");

        if (activityType == 1) {
            LoggerUtil.d("activityType","activityType == 1");
            UpdateActivityCoupon update = new UpdateActivityCoupon();
            update.setIsCollect(true);
            if (!isCollect) {
                //ToastUtil.showToast(TheApp.instance, "收藏成功");
                isCollect = true;
                if (!isPraise)
                    tv_like.setText(Integer.parseInt(tv_like.getText().toString()) + 1 + "");
                isPraise = true;
                //Log.w("haha",tv_like.getText().toString());

                update.setPraiseNum(Integer.parseInt(tv_like.getText().toString()));
                update.setBrowseNum(Integer.parseInt(tv_browse.getText().toString()));
                update.setIsPraise(true);
                update.setPosition(ActivityCouponCouponPosition);
                iv_like.setBackgroundResource(R.drawable.like_press);
            }
            EventBus.getDefault().post(update);
        } else if (activityType == 2) {
            LoggerUtil.d("","activityType == 2");
            if (!isCollect) {
                UpdateActivityCouponHistory updateHistory = new UpdateActivityCouponHistory();
                updateHistory.setIsCollect(true);
                ToastUtil.showToast(TheApp.instance, "收藏成功");
                isCollect = true;
                if (!isPraise)
                    tv_like.setText(Integer.parseInt(tv_like.getText().toString()) + 1 + "");
                isPraise = true;
                //Log.w("haha",tv_like.getText().toString());

                updateHistory.setPraiseNum(Integer.parseInt(tv_like.getText().toString()));
                updateHistory.setBrowseNum(Integer.parseInt(tv_browse.getText().toString()));
                updateHistory.setIsPraise(isPraise);
                updateHistory.setPosition(position);
                iv_like.setBackgroundResource(R.drawable.like_press);
                EventBus.getDefault().post(updateHistory);



                if (ActivityCouponCouponID == CouponID) {
                    UpdateActivityCouponDetail updateDetail = new UpdateActivityCouponDetail();
                    updateDetail.setIsCollect(isCollect);
                    //Log.w("haha",tv_like.getText().toString());
                    updateDetail.setPraiseNum(Integer.parseInt(tv_like.getText().toString()));
                    updateDetail.setBrowseNum(Integer.parseInt(tv_browse.getText().toString()));
                    updateDetail.setIsPraise(isPraise);
                    //updateDetail.setPosition(position);
                    EventBus.getDefault().post(updateDetail);

                    UpdateActivityCoupon updateCoupon = new UpdateActivityCoupon();
                    updateCoupon.setIsCollect(isCollect);
                    updateCoupon.setPraiseNum(Integer.parseInt(tv_like.getText().toString()));
                    updateCoupon.setBrowseNum(Integer.parseInt(tv_browse.getText().toString()));
                    updateCoupon.setIsPraise(isPraise);
                    updateCoupon.setPosition(ActivityCouponCouponPosition);
                    EventBus.getDefault().post(updateCoupon);
                }
            }


        }
    }
    void postBrowseEvent(){
        LoggerUtil.d("activityType","postBrowseEvent");

        if (activityType == 1) {
            LoggerUtil.d("activityType","activityType == 1");
            UpdateActivityCoupon update = new UpdateActivityCoupon();
            update.setIsCollect(isCollect);
            //ToastUtil.showToast(TheApp.instance, "收藏成功");
            //Log.w("haha",tv_like.getText().toString());
            update.setPraiseNum(Integer.parseInt(tv_like.getText().toString()));
            update.setBrowseNum(Integer.parseInt(tv_browse.getText().toString()));
            update.setIsPraise(isPraise);
            update.setPosition(ActivityCouponCouponPosition);
            EventBus.getDefault().post(update);
        } else if (activityType == 2) {
            LoggerUtil.d("", "activityType == 2");
            UpdateActivityCouponHistory updateHistory = new UpdateActivityCouponHistory();
            updateHistory.setIsCollect(isCollect);
            updateHistory.setPraiseNum(Integer.parseInt(tv_like.getText().toString()));
            updateHistory.setBrowseNum(Integer.parseInt(tv_browse.getText().toString()));
            updateHistory.setIsPraise(isPraise);
            updateHistory.setPosition(position);
            EventBus.getDefault().post(updateHistory);

                if (ActivityCouponCouponID == CouponID) {
                    UpdateActivityCouponDetail updateDetail = new UpdateActivityCouponDetail();
                    updateDetail.setIsCollect(isCollect);
                    updateDetail.setPraiseNum(Integer.parseInt(tv_like.getText().toString()));
                    updateDetail.setBrowseNum(Integer.parseInt(tv_browse.getText().toString()));
                    updateDetail.setIsPraise(isPraise);
                    EventBus.getDefault().post(updateDetail);

                    UpdateActivityCoupon updateCoupon = new UpdateActivityCoupon();
                    updateCoupon.setIsCollect(isCollect);
                    updateCoupon.setPraiseNum(Integer.parseInt(tv_like.getText().toString()));
                    updateCoupon.setBrowseNum(Integer.parseInt(tv_browse.getText().toString()));
                    updateCoupon.setIsPraise(isPraise);
                    updateCoupon.setPosition(ActivityCouponCouponPosition);
                    EventBus.getDefault().post(updateCoupon);
                }
        }
    }

}
