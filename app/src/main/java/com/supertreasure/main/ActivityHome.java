package com.supertreasure.main;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.util.AbViewUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.supertreasure.R;
import com.supertreasure.base.mFragmentPagerAdapter;
import com.supertreasure.eventbus.NotificationEvent;
import com.supertreasure.eventbus.RefleshDecorFragment;
import com.supertreasure.fragment.SchoolFragment;
import com.supertreasure.fragment.TopnewsFragment;
import com.supertreasure.greenDaoUtils.GreenUtils;
import com.supertreasure.home.ActivityOutSchool;
import com.supertreasure.mine.ActivityMoodMessage;
import com.supertreasure.util.NetUtils;
import com.supertreasure.util.ToastUtil;
import com.supertreasure.util.photoview.log.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class ActivityHome extends AbActivity implements RadioGroup.OnCheckedChangeListener,View.OnClickListener{

    private String[] title;
    public ViewPager homeViewPager;
    public mFragmentPagerAdapter fragmentPagerAdapter;
    public ArrayList<Fragment> fragmentList;
    private RadioGroup rg_home;
    private RadioButton rb_topnews;
    private RadioButton rb_school;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private Fragment currentFragment;
    public SchoolFragment schoolFragment;
    public TopnewsFragment topnewsFragment;
    public FragmentManager fragmentManager;
    private Button btn_title_left;
    private Button btn_title_right;
    private ImageView btn_message;
    private TextView tv_notification_number;
    int number = 0;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EventBus.getDefault().register(this);

        initView();
        initData();
        setListener();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Subscribe
    public void onEventMainThread(NotificationEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String msg = "NotificationEvent";
                com.orhanobut.logger.Logger.d("eventbus", msg);
                //tv_notification_number.setVisibility(View.VISIBLE);
                number = GreenUtils.getInstance().getMoodMessageUnreadCount();
                //com.orhanobut.logger.Logger.i("eventbus", "getMoodMessageCount():" + number);

                if (number > 0) {
                    tv_notification_number.setVisibility(View.VISIBLE);
                    tv_notification_number.setText(number + "");
                } else
                    tv_notification_number.setVisibility(View.INVISIBLE);


            }
        });

    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));
        homeViewPager = (ViewPager) findViewById(R.id.vp_home);
        fragmentList = new ArrayList<>();
        schoolFragment = new SchoolFragment();
        topnewsFragment = new TopnewsFragment();

        fragmentList.add(topnewsFragment);
        fragmentList.add(schoolFragment);
        fragmentManager = getSupportFragmentManager();
        fragmentPagerAdapter = new mFragmentPagerAdapter(fragmentManager,fragmentList);
        homeViewPager.setAdapter(fragmentPagerAdapter);
        homeViewPager.setCurrentItem(0);

        btn_title_left  = (Button) findViewById(R.id.bt_title_left);
        btn_title_left.setVisibility(View.INVISIBLE);
        btn_title_right=(Button)findViewById(R.id.bt_title_right);
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        rg_home = (RadioGroup) findViewById(R.id.rg_home);
        rb_school = (RadioButton) findViewById(R.id.rb_school);
        rb_topnews = (RadioButton) findViewById(R.id.rb_topnews);
        tv_notification_number = (TextView) findViewById(R.id.tv_notification_number);
        tv_notification_number.setVisibility(View.INVISIBLE);
    }

    private void initData() {
        number = GreenUtils.getInstance().getMoodMessageUnreadCount();
        if (number > 0) {
            tv_notification_number.setVisibility(View.VISIBLE);
            tv_notification_number.setText(number + "");
        } else
            tv_notification_number.setVisibility(View.INVISIBLE);
    }

    private void setListener() {
        rg_home.setOnCheckedChangeListener(this);
        //btn_title_left.setOnClickListener(this);
        btn_title_right.setOnClickListener(this);
        //ll_title_left.setOnClickListener(this);
        ll_title_right.setOnClickListener(this);
        homeViewPager.setOnPageChangeListener(myOnPagerChangeListener);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==rb_topnews.getId()){
            homeViewPager.setCurrentItem(0,false);
        }
        else if(checkedId==rb_school.getId()){
            homeViewPager.setCurrentItem(1,false);
        }
    }

    ViewPager.OnPageChangeListener myOnPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if(position==0){
                rb_topnews.setChecked(true);
            }else {
                rb_school.setChecked(true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_title_right:
                Intent intent=new Intent(TheApp.instance, ActivityOutSchool.class);
                startActivity(intent);
                break;
            case R.id.bt_title_left:
                intent=new Intent(TheApp.instance, ActivityMoodMessage.class);
                startActivity(intent);
                break;
            case R.id.ll_title_right:
                intent=new Intent(TheApp.instance, ActivityOutSchool.class);
                startActivity(intent);
                break;
            case R.id.ll_title_left:
                intent=new Intent(TheApp.instance, ActivityMoodMessage.class);
                startActivity(intent);
                break;
        }
    }
}
