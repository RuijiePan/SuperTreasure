package com.supertreasure.main;

import android.annotation.TargetApi;
import android.app.ActivityGroup;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;

import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.ab.util.AbWifiUtil;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.supertreasure.R;
import com.supertreasure.bean.VersionBean;
import com.supertreasure.login.ActivityLogin;
import com.supertreasure.service.AppUpdateService;
import com.supertreasure.util.Config;
import com.supertreasure.util.ToastUtil;
import com.supertreasure.websocketservice.WebSocketServiceManager;

public class MainActivity extends ActivityGroup implements View.OnClickListener{

    private VersionBean versionBean;
    private Dialog updateDialog;
    private static TabHost tabHost;
    private FrameLayout fl_home;
    private FrameLayout fl_coupon;
    private FrameLayout fl_good;
    private FrameLayout fl_mine;
    private FrameLayout tab;
    private static ImageView iv_home;
    private static ImageView iv_coupon;
    private static ImageView iv_good;
    private static ImageView iv_mine;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if(intent!=null){
            if(AbSharedUtil.getBoolean(TheApp.instance, Config.IsOtherLogin, false)&&
                    !AbSharedUtil.getBoolean(TheApp.instance, Config.IsFirstLogin, false)){
                Intent intent1 = new Intent(TheApp.instance, ActivityLogin.class);
                startActivity(intent1);
                finish();
            }
       }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwipeBackHelper.onCreate(this);
        
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        //tintManager.setTintColor(R.color.text_color_title);
        tintManager.setStatusBarTintColor(R.color.text_color_title);*/

        //WebSocketServiceManager.startService(this);

        initView();
        setListener();
        getUpdate();
        iv_home.setImageResource(R.drawable.tabbar_home_selected);
        tabHost.setCurrentTabByTag("a");
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void initView() {

        tabHost = (TabHost) findViewById(R.id.tabhost);
        AbViewUtil.scaleContentView(tabHost);
        fl_home=(FrameLayout) findViewById(R.id.fl_home);
        fl_coupon=(FrameLayout) findViewById(R.id.fl_coupon);
        fl_good=(FrameLayout) findViewById(R.id.fl_good);
        fl_mine=(FrameLayout) findViewById(R.id.fl_mine);
        tab=(FrameLayout) findViewById(R.id.tab);
        iv_home=(ImageView) findViewById(R.id.iv_home);
        iv_coupon=(ImageView) findViewById(R.id.iv_coupon);
        iv_good=(ImageView) findViewById(R.id.iv_good);
        iv_mine=(ImageView) findViewById(R.id.iv_mine);

        tabHost.setup(this.getLocalActivityManager());
        tabHost.addTab(tabHost.newTabSpec("a").setIndicator("A").setContent(new Intent(this, ActivityHome.class)));
        tabHost.addTab(tabHost.newTabSpec("b").setIndicator("B").setContent(new Intent(this, ActivityCoupon.class)));
        tabHost.addTab(tabHost.newTabSpec("c").setIndicator("C").setContent(new Intent(this, ActivityTrade.class)));
        tabHost.addTab(tabHost.newTabSpec("d").setIndicator("D").setContent(new Intent(this, ActivityMine.class)));
    }

    private void setListener() {
        fl_home.setOnClickListener(this);
        fl_coupon.setOnClickListener(this);
        fl_good.setOnClickListener(this);
        fl_mine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fl_home:
                toNormalNav();
                //填充点击的图片
                iv_home.setImageResource(R.drawable.tabbar_home_selected);
                /*fl_home.setBackgroundResource(R.color.tabbar_backgroud);*/
                tabHost.setCurrentTabByTag("a");
                break;
            case R.id.fl_coupon:
                toNormalNav();
                //填充点击的图片
                iv_coupon.setImageResource(R.drawable.tabbar_coupon_selected);
                /*fl_coupon.setBackgroundResource(R.color.tabbar_backgroud);*/
                tabHost.setCurrentTabByTag("b");
                break;
            case R.id.fl_good:
                toNormalNav();
                //填充点击的图片
                iv_good.setImageResource(R.drawable.tabbar_good_selected);
                /*fl_good.setBackgroundResource(R.color.tabbar_backgroud);*/
                tabHost.setCurrentTabByTag("c");
                break;
            case R.id.fl_mine:
                toNormalNav();
                //填充点击的图片
                iv_mine.setImageResource(R.drawable.tabbar_mine_selected);
                /*fl_mine.setBackgroundResource(R.color.tabbar_backgroud);*/
                tabHost.setCurrentTabByTag("d");
                break;
            default:
                break;
        }
    }

    private void toNormalNav() {
        iv_home.setImageResource(R.drawable.tabbar_home);
        iv_coupon.setImageResource(R.drawable.tabbar_coupon);
        iv_good.setImageResource(R.drawable.tabbar_good);
        iv_mine.setImageResource(R.drawable.tabbar_mine);
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void getUpdate() {

        String url = Config.getAPI(Config.APIVERSION);
        TheApp.mAbHttpUtil.post(url, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {

                Gson gson = new Gson();
                versionBean = gson.fromJson(content,VersionBean.class);
                try {
                    // 获取packagemanager的实例
                    PackageManager packageManager = getPackageManager();
                    // getPackageName()是你当前类的包名，0代表是获取版本信息
                    PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
                    String version = packInfo.versionName;
                    String getVersion = versionBean.getVersion();
                    if (!version.equals(getVersion)&&versionBean.getIsNeed().equals("Yes")) {  // 不同,则提示更新
                        showUpdataDialog();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
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

    private void showUpdataDialog() {

        String[] item = versionBean.getContent().split("&");
        updateDialog = new MaterialDialog.Builder(this)
                .title("更新("+versionBean.getUpdatetime()+")")
                .items(item)
                .positiveText(R.string.update)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        {
                            downLoadAPK(versionBean.getDownload());
                        }
                    }
                })
                .canceledOnTouchOutside(false)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                })
                .show();
    }

    private void downLoadAPK(String url) {
        Intent intent = new Intent(TheApp.instance ,AppUpdateService.class);
        intent.putExtra(Config.DOWNLOADURL,url);
        intent.putExtra(Config.VERSION,versionBean.getVersion());
        startService(intent);
    }
}
