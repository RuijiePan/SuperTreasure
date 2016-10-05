package com.supertreasure.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ab.activity.AbActivity;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.supertreasure.R;
import com.supertreasure.constant.Constant;
import com.supertreasure.home.ImagePagerActivity;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.util.GetHeightResUrl;
import com.supertreasure.util.MyUtils;

public class ActivityMyCouponDetial extends AbActivity implements View.OnClickListener{
    private SimpleDraweeView draweeView;
    private View btn_return;
    private View ll_title_left;
    private int width;
    private int height;
    private String couponPics;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_detail_mine);
        SwipeBackHelper.onCreate(this);
        initView();
        initData();
        initListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    private void initListener() {
        btn_return.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);
    }

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("coupon");
        width = bundle.getInt("width");
        height = bundle.getInt("height");
        couponPics = bundle.getString("couponPics");

        draweeView.setLayoutParams(new LinearLayout.LayoutParams(
                (LinearLayout.LayoutParams.MATCH_PARENT)
                ,TheApp.screenWidth*height/width));
        draweeView.setImageURI(Uri.parse(GetHeightResUrl.getHeightUrl(couponPics)));
        draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TheApp.instance, ImagePagerActivity.class);
                String[] pic = new String[1];
                pic[0] = couponPics;
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, pic);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));
        draweeView          = (SimpleDraweeView) findViewById(R.id.draweeView);
        btn_return            = findViewById(R.id.bt_title_left);
        ll_title_left         = findViewById(R.id.ll_title_left);
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
}
