package com.supertreasure.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.orhanobut.logger.Logger;
import com.supertreasure.R;
import com.supertreasure.bean.Banner;
import com.supertreasure.eventbus.RefleshOutschool;
import com.supertreasure.eventbus.initdotEvent;
import com.supertreasure.greenDaoUtils.GreenUtils;
import com.supertreasure.main.TheApp;
import com.supertreasure.service.HttpResultUtil;
import com.supertreasure.util.BannerAdapter;
import com.supertreasure.util.ChildViewPager;
import com.supertreasure.util.Config;
import com.supertreasure.util.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


public class ActivityOutSchool extends AbActivity implements View.OnClickListener{
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;

    private GreenUtils greenUtils;
    private TextView tv_title_name;

    private int autoChangeTime= 5000;
    private Runnable runnable;
    private Context context;
    private ChildViewPager viewPager;
    private TopnewBannerAdapter bannerAdapter;
    private CircleIndicator indicator;
    private FrameLayout fl_banner;
    private LinearLayoutManager linearLayoutManager;
    private List<me.itangqi.greendao.Banner> listBanner = new ArrayList<>();
    private LinearLayout head;
    private LinearLayout friend;
    private View ll_title_left;
    private Button bt_title_left;
    private Button bt_title_right;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outside_school);
        SwipeBackHelper.onCreate(this);
        EventBus.getDefault().register(this);

        initView();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
        EventBus.getDefault().unregister(this);
    }

    private void setListener() {
        head.setOnClickListener(this);
        friend.setOnClickListener(this);
        bt_title_left.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);

    /*    bannerAdapter.setListener(new BannerAdapter.OnItemClickListener() {

            @Override
            public void onItemShortClick(View v, int position) {
                Intent intent = new Intent(TheApp.instance, ActivityAdvertisementDetail.class);
                //intent.putExtra(Config.URL, bannerAdapter.getList().get(position).getUrl());
                //intent.putExtra(Config.URL, adapter.getCouponList().get(0).getUrl());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });*/
    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        tv_title_name= (TextView) findViewById(R.id.tv_title_name);
        bt_title_left= (Button) findViewById(R.id.bt_title_left);
        ll_title_left = findViewById(R.id.ll_title_left);
        bt_title_right= (Button) findViewById(R.id.bt_title_right);
        context = this;
        fl_banner = (FrameLayout) findViewById(R.id.fl_banner);
        viewPager = (ChildViewPager) findViewById(R.id.bannerViewPager);
        head= (LinearLayout) findViewById(R.id.head);
        friend= (LinearLayout) findViewById(R.id.friend);
    }

    private void initData() {
        int page = 1;
        greenUtils = GreenUtils.getInstance();
        long newestBannerId = greenUtils.queryBannerCurrentNewestId(Config.DATABASE_BANNER_TYPE_MIDDLE);
        bannerAdapter = new TopnewBannerAdapter(context,listBanner);
        isPrepared = true;

        //listBanner = greenUtils.queryBannerList(page,newestBannerId,Config.DATABASE_BANNER_TYPE_MIDDLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                listBanner = greenUtils.queryBannerList(Config.DATABASE_BANNER_TYPE_MIDDLE);
                mHasLoadedOnce = true;

                tv_title_name.setText("外校");
                bt_title_right.setVisibility(View.INVISIBLE);
                initViewPager();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        getBanner();
                    }
                });
            }
        }).start();
    }

    private void getBanner(){

        String url = Config.getAPI(Config.Banner);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance, Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance,Config.Token));
        params.put(Config.Type,Config.AdSecond);

        TheApp.mAbHttpUtil.post(url, params ,new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //Logger.json("response content", content);
                if (HttpResultUtil.handleUnloginErrorStatus(content, RefleshOutschool.getInstance(), ActivityOutSchool.this)) {
                    return;
                }
                Gson gson = new Gson();
                Banner banner = gson.fromJson(content,Banner.class);
                //Toast.makeText(context,banner.toString(),Toast.LENGTH_LONG).show();
                String status = banner.getStatus();
                if (status.equals(Config.SUCCESS)){
                    listBanner = banner.getList();
                    bannerAdapter.updateView(listBanner);
                    if (listBanner!=null && listBanner.size()>0)
                        greenUtils.deleteAllBannerByType(Config.DATABASE_BANNER_TYPE_MIDDLE);
                    //greenUtils.getBannerDao().deleteAll();
                    for (me.itangqi.greendao.Banner banner1 : listBanner) {
                        //greenUtils.insertOrReplaceBanner(banner1,Config.DATABASE_BANNER_TYPE_MIDDLE);
                        banner1.setType(Config.DATABASE_BANNER_TYPE_MIDDLE);
                        Logger.i("insert", banner1.toString());

                        greenUtils.getBannerDao().insertOrReplace(banner1);
                        //greenUtils.insertOrReplaceBanner(banner1,Config.DATABASE_BANNER_TYPE_MIDDLE);
                    }
                    //initDot();
                    initViewPager();
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
                Toast.makeText(TheApp.instance,content,Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initViewPager() {
        viewPager.setVisibility(View.VISIBLE);
        //bannerAdapter.updateView(listBanner);
        viewPager.setAdapter(bannerAdapter);
        viewPager.setOnPageChangeListener(myOnPageChangeListener);
       // 点击事件
        if(!Config.CloseADOnclick)
        viewPager.setOnSingleTouchListener(new ChildViewPager.OnSingleTouchListener() {
            @Override
            public void onSingleTouch() {
                Logger.i("BannerAdapter", "onSingleTouch");
                int position = viewPager.getCurrentItem();

                Intent intent = new Intent(TheApp.instance, ActivityAdvertisementDetail.class);
                intent.putExtra(Config.URL, bannerAdapter.getList().get(position).getAlink());

                //intent.putExtra(Config.URL, bannerAdapter.getList().get(position).getUrl());
                //intent.putExtra(Config.URL, adapter.getCouponList().get(0).getUrl());
                startActivity(intent);
            }
        });

        if(listBanner.size()!=0){
            if(listBanner.size()!=1){
                //initDot();
                EventBus.getDefault().post(new initdotEvent(Config.OutSchool));
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        int nextPager = viewPager.getCurrentItem()+1;
                        if(nextPager>=bannerAdapter.getCount()){
                            nextPager = 0;
                        }
                        viewHandler.sendEmptyMessage(nextPager);
                    }
                };
                viewHandler.postDelayed(runnable,autoChangeTime);
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

    private final Handler viewHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;

            viewPager.setCurrentItem(what);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head:
                Intent intent=new Intent(TheApp.instance,OutSchoolHead.class);
                startActivity(intent);
                break;
            case R.id.friend:
                Intent intent1=new Intent(TheApp.instance,OutSchoolFriend.class);
                startActivity(intent1);
                break;
            case R.id.ll_title_left:
                finish();
                break;
        }
    }
    @Subscribe
    public void onEventMainThread(RefleshOutschool event) {
        String msg = "RefleshOutschool";
        //Log.d("eventbus", msg);
        if (NetUtils.isConnected(TheApp.instance)) {
            if (!isPrepared)
                return;
            getBanner();
        }
    }

    @Subscribe
    public void onEventMainThread(initdotEvent event) {
        if(event.getIndex()==Config.OutSchool)
        indicator.setViewPager(viewPager);
    }
}
