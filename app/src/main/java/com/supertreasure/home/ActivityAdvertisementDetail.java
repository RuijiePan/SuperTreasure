package com.supertreasure.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.util.AbViewUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.supertreasure.R;
import com.supertreasure.util.Config;
import com.supertreasure.util.ToastUtil;

/**
 * Created by Administrator on 2016/2/4.
 */
public class ActivityAdvertisementDetail extends AbActivity implements View.OnClickListener{
    private ProgressBar progress_bar;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private Button bt_title_left;
    private Button bt_title_right;
    private TextView tv_title;
    private String url;
    private Context context;
    private LinearLayout root;
    private WebView webView;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topnews_detail);

        SwipeBackHelper.onCreate(this);
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    private void initView() {
        context = this;
        root = (LinearLayout) findViewById(R.id.root);
        webView = (WebView) findViewById(R.id.webView);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        progress_bar.setMax(100);
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        ll_title_right.setVisibility(View.INVISIBLE);
        //ll_title_right.setVisibility(View.INVISIBLE);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        bt_title_right.setVisibility(View.INVISIBLE);
        //bt_title_right.setBackgroundResource(R.drawable.ic_history);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("广告详情");
        AbViewUtil.scaleContentView(root);
    }

    private void initData() {
        webView.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);//设置使支持缩放
        webView.getSettings().setDefaultFontSize(5);

        url = getIntent().getStringExtra(Config.URL);
        webView.loadUrl(url);
    }

    private void setListener(){

        ll_title_left.setOnClickListener(this);
        ll_title_right.setOnClickListener(this);
        bt_title_left.setOnClickListener(this);
        bt_title_right.setOnClickListener(this);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progress_bar.setProgress(newProgress);
                if(newProgress==100){
                    progress_bar.setVisibility(View.GONE);
                }else {
                    progress_bar.setVisibility(View.VISIBLE);
                }
                Log.w("haha",newProgress+"");
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
        }
    }
}
