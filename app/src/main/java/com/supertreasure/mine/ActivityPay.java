package com.supertreasure.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.orhanobut.logger.Logger;
import com.supertreasure.R;
import com.supertreasure.bean.CollectionBeen;
import com.supertreasure.eventbus.RefleshMyCollectionFragment;
import com.supertreasure.main.TheApp;
import com.supertreasure.service.HttpResultUtil;
import com.supertreasure.trade.ActivityGoodDetail;
import com.supertreasure.util.Config;
import com.supertreasure.util.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ActivityPay extends AppCompatActivity{

    private SimpleDraweeView draweeView_alipay;
    private SimpleDraweeView draweeView_wechatpay;
    private Toolbar toolbar;
    private LinearLayout ll_pay;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        SwipeBackHelper.onCreate(this);

        initView();
        initData();
        initToolbar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    private void initView() {
        ll_pay = (LinearLayout) findViewById(R.id.ll_pay);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        draweeView_alipay = (SimpleDraweeView) findViewById(R.id.draweeView_alipay);
        draweeView_wechatpay = (SimpleDraweeView) findViewById(R.id.draweeView_wechatpay);
        AbViewUtil.scaleContentView(ll_pay);
    }

    private void initData() {
        draweeView_alipay.setImageURI(Uri.parse("res://"+getPackageName()+"/"+R.drawable.alipay));
        draweeView_wechatpay.setImageURI(Uri.parse("res://"+getPackageName()+"/"+R.drawable.wechatpay));
    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("打赏我们");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
