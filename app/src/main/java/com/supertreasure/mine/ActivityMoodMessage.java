package com.supertreasure.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.orhanobut.logger.Logger;
import com.supertreasure.R;
import com.supertreasure.bean.CouponBeen;
import com.supertreasure.eventbus.NotificationEvent;
import com.supertreasure.greenDaoUtils.GreenUtils;
import com.supertreasure.home.ActivityMoodInDeatil;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.util.DateUtils;
import com.supertreasure.util.MyUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.itangqi.greendao.MoodMessage;

public class ActivityMoodMessage extends AbActivity implements View.OnClickListener{

    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private Button bt_title_left;
    private TextView tv_title;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<MoodMessage> list_Moodmessage;
    private TextView nullContent;
    private MoodMessageAdapter adapter;
    //private CouponAdapter adapter_coupon;
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        SwipeBackHelper.onCreate(this);
        initView();
        initData();//不知为何post不能放到 子线程去执行，执行 就没反应
        setListener();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }
    private void setListener() {
        bt_title_left.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);

        adapter.setmOnItemClickListener(new MoodMessageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MoodMessageAdapter.MoodmessageViewHolder holder, int position) {
                //adapter.addData(position,new CouponBeen("new1.png","new1",new Date(System.currentTimeMillis()).toString(),new Date(System.currentTimeMillis()).toString()));
                Logger.i("",holder.textView_content.toString());
                GreenUtils.getInstance().updateMoodMessageToHadread(adapter.getList_moodmessage().get(position).getId());
                adapter.removeData(position);
                EventBus.getDefault().post(new NotificationEvent());
                //GreenUtils.getInstance().deleteAllMoodMessageByUserid();
                //GreenUtils.getInstance().getMoodMessageDao().deleteByKey(adapter.getList_moodmessage().get(position).getId());
                /*Intent intent = new Intent(ActivityMoodMessage.this, ActivityMoodInDeatil.class);
                //传递详情说说说需要的参数
                Bundle bundleMood = new Bundle();
                bundleMood.putInt(Config.MoodID, mood.getMoodId());
                bundleMood.putString("content", mood.getContent());
                bundleMood.putString("paths", mood.getPaths());
                bundleMood.putString("publishtime", DateUtils.fromToday(mood.getPublishtime()));
                bundleMood.putString("praiseTimes", mood.getPraiseTimes());
                bundleMood.putString("commentCount", mood.getCommentCount());
                bundleMood.putBoolean("isPraised", mood.getIsPraised());
                Bundle bundlePeople = new Bundle();
                bundlePeople.putString("belongschool", AbSharedUtil.getString(context, Config.Content_AboutMe_Belongschool));
                //bundlePeople.putString("sex", mood.getPeople().getSex());
                bundlePeople.putString("userPic", mood.getMe().getUserPic());
                bundlePeople.putString("nickName", mood.getMe().getNickName());
                //bundlePeople.putString("userName", mood.getPeople().getUserName());
                bundleMood.putBundle("people", bundlePeople);
                intent.putExtra("list", bundleMood);
                intent.putExtra("position", position);
                startActivityForResult(intent, RequestCode);*/
            }

            @Override
            public void onItemLongClick(MoodMessageAdapter.MoodmessageViewHolder holder, int position) {
                //adapter.removeData(position);
               /* Log.i("ActivityMyCoupon", "onItemLongClick position=" + position);
                MyUtils.show(ActivityMoodMessage.this, "onItemLongClick position=" + position + "    textView_content=" + holder.textView_content.getText().toString());
            */
            }
        });
    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));
        nullContent = (TextView) findViewById(R.id.nullcontent);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_coupon);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("我的评论");
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        ll_title_right.setVisibility(View.INVISIBLE);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
    }
    private void initData() {
        /*list_Moodmessage.add(new MoodMessage(1l,1l,"message","bodyType","date","fromUserName","fromNickName"));
        list_Moodmessage.add(new MoodMessage(2l,1l,"message","bodyType","date","fromUserName","fromNickName"));
        list_Moodmessage.add(new MoodMessage(3l,1l,"message","bodyType","date","fromUserName","fromNickName"));
        */


        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        swipeRefreshLayout.setColorSchemeColors(R.color.colorwhite, R.color.colorgray, R.color.grey_backgroud, R.color.colorwhite);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        adapter = new MoodMessageAdapter(ActivityMoodMessage.this,list_Moodmessage);

        new Thread(new Runnable() {
            @Override
            public void run() {
                list_Moodmessage = GreenUtils.getInstance().queryMoodMessageList(false);

                if (list_Moodmessage==null||list_Moodmessage.size()<=0){
                    showNullContent();
                }else{
                    showRecyclerView();
                }
                adapter.setList_moodmessage(list_Moodmessage);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);

                    }
                });
            }
        }).start();


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_title_left:
                //ToastUtil.showToast(this,"返回按钮");
                finish();
                break;
            case R.id.ll_title_left:
                //ToastUtil.showToast(this,"返回按钮");
                finish();
                break;
        }
    }
    public void showView(int number){
        if (number == 0)
            showNullContent();
        else
            showRecyclerView();
    }
    public void showNullContent(){
        swipeRefreshLayout.setVisibility(View.GONE);
        nullContent.setVisibility(View.VISIBLE);
    }
    public void showRecyclerView(){
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        nullContent.setVisibility(View.GONE);
    }

}
