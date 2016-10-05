package com.supertreasure.login;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbDialogFragment;
import com.ab.fragment.AbProgressDialogFragment;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.supertreasure.R;
import com.supertreasure.bean.CouponBean;
import com.supertreasure.bean.GoodBean;
import com.supertreasure.bean.MoodBean;
import com.supertreasure.bean.NowTime;
import com.supertreasure.bean.StatusBean;
import com.supertreasure.bean.Topnews;
import com.supertreasure.bean.userLogin;
import com.supertreasure.constant.Constant;
import com.supertreasure.database.DBTool;
import com.supertreasure.dialog.RefreshDialog;
import com.supertreasure.dialog.RunningManDialog;
import com.supertreasure.eventbus.RefreshMoodGridView;
import com.supertreasure.eventbus.RegisterMessage;
import com.supertreasure.greenDaoUtils.GreenUtils;
import com.supertreasure.main.ActivityHome;
import com.supertreasure.main.MainActivity;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.util.NetUtils;
import com.supertreasure.util.ToastUtil;
import com.supertreasure.util.ValidationUtil;
import com.supertreasure.websocketservice.WebSocketService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.smssdk.SMSSDK;
import me.itangqi.greendao.User;
import me.itangqi.greendao.UserDao;


/**
 * Created by Administrator on 2016/1/24.
 */
public class ActivityLogin extends AbActivity implements View.OnClickListener,TextWatcher{

    private GreenUtils greenUtils;
    //private AbProgressDialogFragment abDialog;
    private RefreshDialog refreshDialog;
    private EditText et_user;
    private EditText et_pwd;
    private Button bt_login;
    private Button bt_register;
    private Button bt_forget_pwd;

    private String userName;
    private String pwd;
    private boolean isUserNameNotEmpty;
    private boolean isPwdNotEmpty;
    private boolean isUserNameLength;
    private boolean isPwdLength;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (!AbSharedUtil.getBoolean(TheApp.instance,Config.IsOtherLogin,false)&&!AbSharedUtil.getBoolean(TheApp.instance,Config.IsFirstLogin,true)){
            startActivity(new Intent(TheApp.instance,MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EventBus.getDefault().register(this);

        greenUtils = GreenUtils.getInstance();
        initView();
        setListener();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEventMainThread(RegisterMessage event) {
        //adapter.clear();
        et_user.setText(event.getUserName());
        et_pwd.setText(event.getPassword());
    }

    private void initView() {
        AbViewUtil.scaleContentView((FrameLayout) findViewById(R.id.root));
        et_user = (EditText) findViewById(R.id.et_user);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_forget_pwd = (Button) findViewById(R.id.bt_forgot_pwd);
        bt_register = (Button) findViewById(R.id.bt_register);

        bt_login.getBackground().setAlpha(204);
        refreshDialog = new RefreshDialog(this);
        refreshDialog.setTitle("正在登陆，请稍后");
    }

    private void setListener() {
        bt_login.setOnClickListener(this);
        bt_forget_pwd.setOnClickListener(this);
        bt_register.setOnClickListener(this);

        et_user.addTextChangedListener(this);
        et_pwd.addTextChangedListener(this);

        et_user.setText(AbSharedUtil.getString(TheApp.instance,Config.UserName));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_login:
                doLogin();
                break;
            case R.id.bt_forgot_pwd:
                Intent intent = new Intent(TheApp.instance,ActivityForgetPwd.class);
                startActivity(intent);
                break;
            case R.id.bt_register:
                intent = new Intent(TheApp.instance, ActivityRegister.class);
                startActivity(intent);
                break;
        }
    }

    private void doLogin(){
        if (!NetUtils.isConnected(TheApp.instance)) {
            ToastUtil.showToast(TheApp.instance, "请先打开网络");
            return;
        }

       /* dialog = new mSweetAlertDialog(this,mSweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("宝宝正在登陆中");
        dialog.show();*/

        //获取服务器当前时间
        String url = Config.getAPI(Config.NowTime);

        final String userName = et_user.getText().toString();
        final String password = et_pwd.getText().toString();

        if(!isUserNameNotEmpty){
            Toast.makeText(this,"请输入账号",Toast.LENGTH_SHORT).show();
            return;
        }else if(!isPwdNotEmpty){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }else if(!isUserNameLength){
            Toast.makeText(this,"请输入正确账号",Toast.LENGTH_SHORT).show();
            return;
        }else if(!isPwdLength) {
            Toast.makeText(this, "密码必须不小于6位", Toast.LENGTH_SHORT).show();
            return;
        }

        refreshDialog.show();
        //abDialog = AbDialogUtil.showProgressDialog(ActivityLogin.this,0,"正在登陆，请稍后...");
        TheApp.mAbHttpUtil.post(url, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //Logger.json("response content", content);

                Gson gson = new Gson();
                NowTime nowTime = gson.fromJson(content,NowTime.class);
                AbSharedUtil.putString(TheApp.instance,Config.TopNewsTime,nowTime.getNowtime());
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Toast.makeText(TheApp.instance,"获取服务器当前时间失败",Toast.LENGTH_SHORT).show();
            }
        });

        url = Config.getAPI(Config.UserLogin);
        AbRequestParams params = new AbRequestParams();
        params.put("userName",userName);
        params.put("password",password);

        TheApp.mAbHttpUtil.setTimeout(5000);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
                @Override
            public void onSuccess(int statusCode, String content) {
                    //Logger.json("response content",content);
                    Gson gson = new Gson();
                    StatusBean statusBean = gson.fromJson(content, StatusBean.class);
                    String error_type = statusBean.getError_type();
                    if (statusBean!=null &&error_type!=null&& error_type.equals(Config.InputError)){
                        ToastUtil.showToast(TheApp.instance,"密码或账号错误");
                        //return;
                    }
                   refreshDialog.dismiss();
               // Toast.makeText(context,content,Toast.LENGTH_LONG).show();
                userLogin userLogin = gson.fromJson(content,userLogin.class);

                if(userLogin.getStatus().equals(Config.SUCCESS)){
                    //登陆后设置为false,表示你重新登陆
                    AbSharedUtil.putBoolean(TheApp.instance, Config.IsOtherLogin, false);
                    AbSharedUtil.putBoolean(TheApp.instance, Config.IsFirstLogin, false);
                    User user = userLogin.getUser();
                    AbSharedUtil.putBoolean(TheApp.instance, Config.IsOtherLogin, false);
                    AbSharedUtil.putBoolean(TheApp.instance, Config.IsFirstLogin, false);
                    // 保存个人信息
                    AbSharedUtil.putString(TheApp.instance,Config.UserName,userName);
                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_Belongschool,userLogin.getUser().getBelongschool());
                    AbSharedUtil.putString(TheApp.instance, Config.Token, userLogin.getToken());
                    AbSharedUtil.putString(TheApp.instance,Config.UserName,user.getUserName());
                    AbSharedUtil.putString(TheApp.instance,Config.Password,password);
                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_Belongschool,userLogin.getUser().getBelongschool());
                    AbSharedUtil.putString(TheApp.instance, Config.Token, userLogin.getToken());
                    AbSharedUtil.putBoolean(TheApp.instance, Config.IsAutoLogin, false);

                    long current_user_id = 0 ;

                    //User user = userLogin.getUser();
                    greenUtils.insertOrReplaceUser(user);
                    Log.i("insert", user.toString());
                    current_user_id = greenUtils.queryCurrentUserId(userName);
                    AbSharedUtil.putInt(TheApp.instance, Config.Current_user_id, (int) current_user_id);

                    refreshDialog.dismiss();
                    startActivity(new Intent(TheApp.instance,MainActivity.class));
                    //startService(new Intent(context, WebSocketService.class));
                    finish();
                }



            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {
                //AbDialogUtil.removeDialog(TheApp.instance);
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                Toast.makeText(TheApp.instance,"服务器连接失败",Toast.LENGTH_SHORT).show();
               // dialog.cancel();
                refreshDialog.dismiss();
            }
        });
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        userName = et_user.getText().toString();
        pwd = et_pwd.getText().toString();

        isUserNameNotEmpty = !TextUtils.isEmpty(userName);
        isPwdNotEmpty = !TextUtils.isEmpty(pwd);
        isUserNameLength = ValidationUtil.isMobile(userName);
        isPwdLength = pwd.length()>=Config.MinPwdLenght;

        if(isUserNameLength&&isPwdLength)
            bt_login.getBackground().setAlpha(204);
        else
            bt_login.getBackground().setAlpha(102);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


}
