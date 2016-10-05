package com.supertreasure.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.supertreasure.R;
import com.supertreasure.bean.BrowserBean;
import com.supertreasure.bean.Praise;
import com.supertreasure.eventbus.UpdateNewParams;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.AlbumHelper;
import com.supertreasure.util.Config;
import com.supertreasure.util.ImageBucket;
import com.supertreasure.util.ImageBucketAdapter;
import com.supertreasure.util.SlideBackActivity;
import com.supertreasure.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/2/4.
 */
public class ActivityNewsDetail extends AbActivity implements View.OnClickListener{

    private String browsernum;
    private String priseNum;
    private String type;
    private boolean isPrise;
    private int position;
    private boolean isShowHistory;
    private ProgressBar progress_bar;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private LinearLayout ll_like;
    private LinearLayout ll_browser;
    private Button bt_title_left;
    private Button bt_title_right;
    private TextView tv_title;
    private String url;
    private String adminID;
    private String topId;
    private Context context;
    private LinearLayout root;
    private WebView webView;
    private TextView tv_browse;
    private TextView tv_like;
    private View iv_like;

    /*@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topnews_detail);

        //SwipeBackHelper.onCreate(this);
        initView();
        initData();
        setListener();
    }

    /*@Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }*/

    private void initView() {
        context = this;
        root = (LinearLayout) findViewById(R.id.root);
        webView = (WebView) findViewById(R.id.webView);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        progress_bar.setMax(100);
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        ll_browser = (LinearLayout) findViewById(R.id.ll_browse);
        ll_like = (LinearLayout) findViewById(R.id.ll_like);
        //ll_title_right.setVisibility(View.INVISIBLE);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        //bt_title_right.setBackgroundResource(R.drawable.ic_history);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_browse = (TextView) findViewById(R.id.tv_browse);
        tv_like = (TextView) findViewById(R.id.tv_like);
        iv_like =  findViewById(R.id.iv_like);

        tv_title.setText("头条详情");
        AbViewUtil.scaleContentView(root);
    }

    private void initData() {
        type = getIntent().getStringExtra(Config.Type);
        browsernum = getIntent().getStringExtra(Config.TopNewsBrowse);
        priseNum = getIntent().getStringExtra(Config.TopNewsPrise);
        isPrise = getIntent().getBooleanExtra(Config.TopNewsIsPrise,false);
        if(isPrise){
            iv_like.setBackgroundResource(R.drawable.like_press);
        }else {
            iv_like.setBackgroundResource(R.drawable.like_normal);
        }
        topId = getIntent().getStringExtra(Config.TopID);
        position = getIntent().getIntExtra(Config.Position,0);
        tv_browse.setText(browsernum==null?"获取失败":browsernum);
        tv_like.setText(priseNum==null?"获取失败":priseNum);
        /*if (isPrise!=null||isPrise)
        iv_like.setClickable();*/

        upDateBorwseNum();
        webView.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);//设置使支持缩放
        webView.getSettings().setDefaultFontSize(5);

        url = getIntent().getStringExtra(Config.URL);
        adminID = getIntent().getStringExtra(Config.AdminID);
        isShowHistory = getIntent().getBooleanExtra(Config.IsShowHistory,false);
        if (isShowHistory){
            ll_title_right.setVisibility(View.INVISIBLE);
            iv_like.setEnabled(false);
            tv_like.setEnabled(false);
            ll_like.setEnabled(false);
        }
        webView.loadUrl(url);
    }

    private void setListener(){

        ll_title_left.setOnClickListener(this);
        ll_title_right.setOnClickListener(this);
        bt_title_left.setOnClickListener(this);
        bt_title_right.setOnClickListener(this);
        ll_like.setOnClickListener(this);
        tv_like.setOnClickListener(this);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progress_bar.setProgress(newProgress);
                if(newProgress==100){
                    progress_bar.setVisibility(View.GONE);
                }else {
                    progress_bar.setVisibility(View.VISIBLE);
                }
                //Log.w("haha",newProgress+"");
                super.onProgressChanged(view, newProgress);
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);// 使用当前WebView处理跳转
                return true;//true表示此事件在此处被处理，不需要再广播
            }
            @Override   //转向错误时的处理
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                ToastUtil.showToast(context,"重定向失败");
            }
        });
    }

    private void upDateBorwseNum() {
        String url = Config.getAPI(Config.APITopNewsBrowser);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance,Config.Token));
        params.put(Config.TopID,topId);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                BrowserBean browser = gson.fromJson(content,BrowserBean.class);
                if(browser.getStatus().equals(Config.SUCCESS)){
                    browsernum = browser.getBrowserNum();
                    EventBus.getDefault().post(new UpdateNewParams(browsernum,priseNum,isPrise,position,type));
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

            }
        });
    }

    private void UpdatePraiseNume() {
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance, Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance, Config.Token));
        params.put(Config.TopID, topId);
        String url = Config.getAPI(Config.Praise);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //Logger.json("response content", content);
                Gson gson = new Gson();
                Praise praise = gson.fromJson(content, Praise.class);
                //Toast.makeText(TheApp.instance, praise.getPraiseTimes(), Toast.LENGTH_SHORT).show();
                if (Integer.parseInt(praise.getPraiseTimes()) - Integer.parseInt(tv_like.getText().toString()) < 0) {
                    iv_like.setBackgroundResource(R.drawable.like_normal);
                    EventBus.getDefault().post(new UpdateNewParams(browsernum,praise.getPraiseTimes(),false,position,type));
                } else {
                    iv_like.setBackgroundResource(R.drawable.like_press);
                    EventBus.getDefault().post(new UpdateNewParams(browsernum,praise.getPraiseTimes(),true,position,type));
                }
                tv_like.setText(praise.getPraiseTimes());
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Toast.makeText(TheApp.instance, statusCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
            case R.id.ll_title_right:
                Intent intent = new Intent(TheApp.instance,ActivityNewsHistory.class);
                intent.putExtra(Config.AdminID,adminID);
                startActivity(intent);
                break;
            case R.id.bt_title_right:
                intent = new Intent(TheApp.instance,ActivityNewsHistory.class);
                intent.putExtra(Config.AdminID,adminID);
                startActivity(intent);
                break;
            case R.id.ll_like:
                UpdatePraiseNume();
                break;
            case R.id.tv_like:
                UpdatePraiseNume();
        }
    }

}
