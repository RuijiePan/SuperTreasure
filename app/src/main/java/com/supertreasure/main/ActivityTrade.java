package com.supertreasure.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.util.AbViewUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.supertreasure.R;
import com.supertreasure.base.mFragmentPagerAdapter;
import com.supertreasure.bean.Topnews;
import com.supertreasure.fragment.CouponFragment;
import com.supertreasure.fragment.DecorateFragment;
import com.supertreasure.fragment.EleProductFragment;
import com.supertreasure.fragment.OtherFragment;
import com.supertreasure.mine.ActivityMyShop;
import com.supertreasure.util.CustomToolBar;

import java.util.ArrayList;
import java.util.List;

public class ActivityTrade extends AbActivity implements View.OnClickListener{

    private FloatingActionButton fab;
    private String[] title;
    /*private Button bt_title_left;
    private Button bt_title_right;
    private TextView tv_title;*/
    private RotateAnimation rotateAnimation;
    private PopupWindow mPopupWindow;
    private LinearLayout ll_title;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private Toolbar toolBar;
    private TabLayout tabLayout;
    private DecorateFragment decorateFragment;
    private EleProductFragment eleProductFragment;
    private OtherFragment otherFragment;
    private ViewPager tradeViewPager;
    private ArrayList<Fragment> fragmentList;
    private mFragmentPagerAdapter mFragmentPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        AbViewUtil.scaleContentView((ViewPager) findViewById(R.id.tradeViewPager));

        initView();
        initData();
        initViewPager();
        setListener();
    }

    public void initView() {
        /*tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("校园逛逛");
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);*/
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolBar);
        tradeViewPager = (ViewPager) findViewById(R.id.tradeViewPager);
        /*ll_title = (LinearLayout) findViewById(R.id.ll_title);
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_left.setVisibility(View.INVISIBLE);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);*/
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        /*rotateAnimation = new RotateAnimation(0,180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());//不停顿
        rotateAnimation.setFillAfter(true);//停在最后
        rotateAnimation.setDuration(300);*/
    }

    public void initData() {
        //initPopWindow();

        fragmentList = new ArrayList<>();
       // couponFragment = new CouponFragment();
        decorateFragment = new DecorateFragment();
        eleProductFragment = new EleProductFragment();
        otherFragment = new OtherFragment();

        //fragmentList.add(couponFragment);
        fragmentList.add(decorateFragment);
        fragmentList.add(eleProductFragment);
        fragmentList.add(otherFragment);

       // initColor();
        //tv_decorate.setTextColor(0xFFAAAAAA);
    }

    private void initViewPager(){
        title = new String[]{"装饰品","电子产品","其他"};
        mFragmentPagerAdapter = new mFragmentPagerAdapter(getSupportFragmentManager(),fragmentList,title);
        //tradeViewPager.setOffscreenPageLimit(1);
        tradeViewPager.setAdapter(mFragmentPagerAdapter);
        tradeViewPager.setOffscreenPageLimit(2);//设置缓存个数，这里缓存2个，正在显示的一个。就3个
        tabLayout.setupWithViewPager(tradeViewPager);
    }

    /*private void initPopWindow() {
            if(mPopupWindow==null){
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View contentview = inflater.inflate(R.layout.dialog_trade_choose,null);
            AbViewUtil.scaleContentView((LinearLayout) contentview.findViewById(R.id.root));
            //tv_coupon = (TextView) contentview.findViewById(R.id.tv_coupon);
            tv_decorate = (TextView) contentview.findViewById(R.id.tv_decorate);
            tv_ele_product = (TextView) contentview.findViewById(R.id.tv_ele_product);
            tv_other = (TextView) contentview.findViewById(R.id.tv_other);
            contentview.setFocusable(true);
            contentview.setFocusableInTouchMode(true);
            mPopupWindow = new PopupWindow(this);
            mPopupWindow.setContentView(contentview);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(000000));
            mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            //添加动画
            mPopupWindow.setAnimationStyle(R.style.categoryAnim);
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(false);

            contentview.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mPopupWindow.dismiss();
                    return true;
                }
            });

            mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    bt_title_left.startAnimation(rotateAnimation);
                }
            });
        }

        //mPopupWindow.showAsDropDown(ll_title);
    }*/

    public void setListener() {
        //tv_coupon.setOnClickListener(ChooseClickListener);
        /*tv_decorate.setOnClickListener(ChooseClickListener);
        tv_ele_product.setOnClickListener(ChooseClickListener);
        tv_other.setOnClickListener(ChooseClickListener);*/
        /*ll_title_left.setOnClickListener(this);
        ll_title_right.setOnClickListener(this);*/
        /*bt_title_left.setOnClickListener(this);
        bt_title_right.setOnClickListener(this);*/
      //  fab.setOnClickListener(this);
        /*tradeViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                initColor();
                if(position==0){
                    tv_decorate.setTextColor(0xFFAAAAAA);
                }else if(position==1){
                    tv_ele_product.setTextColor(0xFFAAAAAA);
                }else
                    tv_other.setTextColor(0xFFAAAAAA);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
                /*case R.id.ll_title_left:
                mPopupWindow.showAsDropDown(ll_title);
                bt_title_left.startAnimation(rotateAnimation);
                break;
            case R.id.bt_title_left:
                mPopupWindow.showAsDropDown(ll_title);
                bt_title_left.startAnimation(rotateAnimation);
                break;*/
            case R.id.bt_title_right:
                Intent intent = new Intent(TheApp.instance, ActivityMyShop.class);
                startActivity(intent);
                break;
            case R.id.ll_title_right:
                intent = new Intent(TheApp.instance, ActivityMyShop.class);
                startActivity(intent);
                break;
        }
    }

    /*public View.OnClickListener ChooseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_coupon:
                    tradeViewPager.setCurrentItem(0,false);
                    mPopupWindow.dismiss();
                    break;
                case R.id.tv_decorate:
                    initColor();
                    tradeViewPager.setCurrentItem(0,false);
                    tv_decorate.setTextColor(0xFFAAAAAA);
                    //mPopupWindow.dismiss();
                    break;
                case R.id.tv_ele_product:
                    initColor();
                    tradeViewPager.setCurrentItem(1,false);
                    tv_ele_product.setTextColor(0xFFAAAAAA);
                    //mPopupWindow.dismiss();
                    break;
                case R.id.tv_other:
                    initColor();
                    tradeViewPager.setCurrentItem(2,false);
                    tv_other.setTextColor(0xFFAAAAAA);
                    //mPopupWindow.dismiss();
                    break;
            }
        }
    };

    public void initColor(){
        tv_decorate.setTextColor(Color.BLACK);
        tv_ele_product.setTextColor(Color.BLACK);
        tv_other.setTextColor(Color.BLACK);
    }*/
}
