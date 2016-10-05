package com.supertreasure.mine;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbProgressDialogFragment;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.ab.util.AbWifiUtil;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.supertreasure.R;
import com.supertreasure.bean.VersionBean;
import com.supertreasure.dialog.mSweetAlertDialog;
import com.supertreasure.greenDaoUtils.GreenUtils;
import com.supertreasure.login.ActivityLogin;
import com.supertreasure.main.MainActivity;
import com.supertreasure.main.TheApp;
import com.supertreasure.service.AppUpdateService;
import com.supertreasure.util.Config;
import com.supertreasure.util.FileUtil;
import com.supertreasure.util.HttpUtil;
import com.supertreasure.util.MyUtils;
import com.supertreasure.util.RoundProgressBar;
import com.supertreasure.util.ToastUtil;
import com.supertreasure.websocket.MyWebSocketManager;
import com.supertreasure.websocketservice.WebSocketService;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActivitySetting extends AbActivity implements View.OnClickListener{

    private mSweetAlertDialog sweetAlertDialog;
    private Dialog updateDialog;
    private VersionBean versionBean;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private Button bt_title_left;
    private TextView tv_title;
    private View vg_changPassword;
    private View vg_clearCache;
    private View vg_tips;
    private View vg_help;
    private View vg_update;
    private View btn_quit;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_setting);
        SwipeBackHelper.onCreate(this);

        initView();
        initListener();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    private void initListener() {
        bt_title_left.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);
        vg_changPassword.setOnClickListener(this);
        vg_clearCache.setOnClickListener(this);
        vg_tips.setOnClickListener(this);
        vg_help.setOnClickListener(this);
        vg_update.setOnClickListener(this);
        btn_quit.setOnClickListener(this);
    }

    private void initData() {

    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));

        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("设置");
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        ll_title_right.setVisibility(View.INVISIBLE);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        vg_changPassword = findViewById(R.id.btn_mine_changpassword);
        vg_clearCache = findViewById(R.id.btn_mine_clearCache);
        vg_tips = findViewById(R.id.btn_mine_tips);
        vg_help = findViewById(R.id.mine_help);
        vg_update = findViewById(R.id.tv_update);
        btn_quit = findViewById(R.id.btn_mine_quit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_title_left:
                //ToastUtil.showToast(this,"返回键");
                finish();
                break;
            case R.id.ll_title_left:
                //ToastUtil.showToast(this,"返回键");
                finish();
                break;
            case R.id.btn_mine_changpassword:
                //ToastUtil.showToast(this,"点击改密码");
                startActivity(new Intent(TheApp.instance,ActivityEditPassword.class));
                break;
            case R.id.btn_mine_clearCache:
                //ToastUtil.showToast(this,"清除缓存");
                showClearCacheDialog();
                break;
            case R.id.btn_mine_tips:
                ToastUtil.showToast(this,"尚未开通");
                break;
            case R.id.mine_help:
                ToastUtil.showToast(this,"尚未开通");
                //ToastUtil.showToast(this,"帮助反馈");
                break;
            case R.id.tv_update:
                getUpdate();
                //ToastUtil.showToast(this,"版本更新");
                break;
            case R.id.btn_mine_quit:
                //ToastUtil.showToast(this,"退出");
                showQuitDialog();
                break;
        }
    }

    private void showClearCacheDialog() {

        sweetAlertDialog = new mSweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.show();
        sweetAlertDialog.setTitleText("确定清除缓存吗？")
                .setConfirmText("确定")
                .setCancelText("取消")
                .setConfirmClickListener(new mSweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(mSweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        ImagePipeline imagePipeline = Fresco.getImagePipeline();
                        /*imagePipeline.clearMemoryCaches();
                        imagePipeline.clearDiskCaches();*/
                        imagePipeline.clearCaches();
                        GreenUtils.getInstance().clearCache();
                    }
                });

        /*View view = getLayoutInflater().inflate(R.layout.dialog_mine_clearcache, null);
        AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.root));
        View btn_confirm = view.findViewById(R.id.btn_confirm);
        View btn_cancel = view.findViewById(R.id.btn_cancel);


        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight()/10;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        View.OnClickListener onClickListener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_confirm:
                        ToastUtil.showToast(ActivitySetting.this,"确认");
                        dialog.dismiss();
                        //changSex("女",dialog);
                        break;
                    case R.id.btn_cancel:
                        ToastUtil.showToast(ActivitySetting.this,"取消");
                        dialog.dismiss();
                        break;
                }
            }
        };
        btn_confirm.setOnClickListener(onClickListener);
        btn_cancel.setOnClickListener(onClickListener);*/
    }
    private void showQuitDialog() {
        sweetAlertDialog = new mSweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.show();
        sweetAlertDialog.setTitleText("确定要注销吗？")
                .setConfirmText("确定")
                .setCancelText("取消")
                .setConfirmClickListener(new mSweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(mSweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        quit();
                    }
                });
        /*View view = getLayoutInflater().inflate(R.layout.dialog_mine_quit, null);
        AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.root));

        View btn_confirm = view.findViewById(R.id.btn_confirm);
        View btn_cancel = view.findViewById(R.id.btn_cancel);


        final Dialog dialog = new Dialog(this, R.style.transparentFrameWindowStyle);
        dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight()/10;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        View.OnClickListener onClickListener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btn_confirm:
                        ToastUtil.showToast(ActivitySetting.this,"确认");
                        dialog.dismiss();
                        quit();
                        //changSex("女",dialog);
                        break;
                    case R.id.btn_cancel:
                        ToastUtil.showToast(ActivitySetting.this,"取消");
                        dialog.dismiss();
                        break;
                }
            }
        };
        btn_confirm.setOnClickListener(onClickListener);
        btn_cancel.setOnClickListener(onClickListener);*/
    }
    public void quit(){

        AbProgressDialogFragment dialogFragment = AbDialogUtil.showProgressDialog(this,0,"正在退出，请稍后...");
        String url = Config.getAPI(Config.APIQuit);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this,Config.Token));
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //Logger.i("response content",content);
                Gson gson = new Gson();
                QuitBeen quitBeen = gson.fromJson(content,QuitBeen.class);
                /*if (quitBeen.getStatus().equals(Config.SUCCESS)){
                    //ToastUtil.showToast(TheApp.instance,"服务器连接失败");
                }else if (quitBeen.getStatus().equals(Config.ERROR)){
                    ToastUtil.showToast(TheApp.instance,"服务器连接失败");
                }*/

                AbDialogUtil.removeDialog(TheApp.instance);
                AbSharedUtil.putBoolean(TheApp.instance,Config.IsAutoLogin,false);
                AbSharedUtil.putBoolean(TheApp.instance,Config.IsOtherLogin,true);
                Intent intent = new Intent(TheApp.instance,MainActivity.class);
                intent.putExtra(Config.Exit,true);
                startActivity(intent);
                finish();
            }

            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
                MyWebSocketManager.getInstance().closeConnect();
                //stopService(new Intent(ActivitySetting.this, WebSocketService.class));
                //Logger.i("websocket", "stopService()");
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                AbDialogUtil.removeDialog(ActivitySetting.this);
                //ToastUtil.showToast(ActivitySetting.this,"退出失败");
                AbSharedUtil.putBoolean(TheApp.instance,Config.IsOtherLogin,true);
                Intent intent = new Intent(TheApp.instance,MainActivity.class);
                intent.putExtra(Config.Exit,true);
                startActivity(intent);
                finish();
            }
        });
    }

    public static class QuitBeen {
        private String status;
        private String msg;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
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
                    if (!version.equals(getVersion)) {  // 不同,则提示更新
                        showUpdataDialog();
                    }else {
                        ToastUtil.showToast(TheApp.instance,"已经是最新版本了");
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
                //.content(versionBean.getUpdatetime()+":"+versionBean.getContent())
                .positiveText(R.string.update)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if(!AbWifiUtil.isConnectivity(TheApp.instance)){
                            ToastUtil.showToast(TheApp.instance,"亲，请打开网络哦");
                        }else if(!AbWifiUtil.isWifiConnectivity(TheApp.instance)){
                            showWarningDialog();
                        }else {
                            downLoadAPK(versionBean.getDownload());
                        }
                    }
                })
                .show();
    }

    private void showWarningDialog() {
        sweetAlertDialog = new mSweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.show();
        sweetAlertDialog.setTitleText("当前为2/3/4G网络")
                .setConfirmText("我是壕")
                .setCancelText("稍后说")
                .setConfirmClickListener(new mSweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(mSweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        ToastUtil.showToast(TheApp.instance,"开始下载");
                        downLoadAPK(versionBean.getDownload());
                    }
                });
    }

    private void downLoadAPK(String url) {
        Intent intent = new Intent(TheApp.instance ,AppUpdateService.class);
        intent.putExtra(Config.DOWNLOADURL,url);
        intent.putExtra(Config.VERSION,versionBean.getVersion());
        startService(intent);
    }

}
