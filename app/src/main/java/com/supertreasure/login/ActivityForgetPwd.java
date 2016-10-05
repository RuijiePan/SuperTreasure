package com.supertreasure.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;

import com.ab.fragment.AbProgressDialogFragment;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.supertreasure.R;
import com.supertreasure.bean.ChangeKeyBean;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.util.SlideBackActivity;
import com.supertreasure.util.ToastUtil;
import com.supertreasure.util.ValidationUtil;
import com.supertreasure.widget.TimeButton;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by prj on 2016/3/14.
 */
public class ActivityForgetPwd extends AbActivity implements View.OnClickListener, TextWatcher {

    private AbProgressDialogFragment dialogFragment;
    private LinearLayout ll_title_left;
    private TextView tv_back;
    private TextView tv_title;
    private EditText et_phone_number;
    private EditText et_codes;
    private Button bt_next;
    private TimeButton tb_getcode;
    private Context context;
    private boolean isPhoneLength = false;
    private boolean isCodeLength = false;
    private final int PhoneLength = 11;
    private final int CodeLength = 4;
    private EventHandler eh;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_pwd);

        SwipeBackHelper.onCreate(this);
        tb_getcode = (TimeButton) findViewById(R.id.tb_getcode);
        tb_getcode.onCreate(savedInstanceState);

        initView();
        setListener();
    }

    @Override
    protected void onDestroy() {

        if(tb_getcode.getText().toString().contains("秒后重新获取")){
            if(TheApp.mapPhone==null){
                TheApp.mapPhone = new HashMap<>();
            }
            if(et_phone_number==null){
                TheApp.mapPhone.put("phone",et_phone_number.getText().toString());
            }
        }else {
            if (TheApp.mapPhone ==null){
                TheApp.mapPhone = new HashMap<>();
            }
            if (et_phone_number!=null){
                TheApp.mapPhone.put("phone","");
            }
        }

        SMSSDK.unregisterEventHandler(eh);
        tb_getcode.onDestroy();
        SwipeBackHelper.onDestroy(this);
        super.onDestroy();

    }

    private void initView() {
        context = this;
        AbViewUtil.scaleContentView((FrameLayout) findViewById(R.id.root));
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("忘记密码");
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_codes = (EditText) findViewById(R.id.et_code);
        bt_next = (Button) findViewById(R.id.bt_next);
        bt_next.getBackground().setAlpha(102);//alpha=0.4，资料合格变为0.8

        SMSSDK.initSDK(this,Config.APPKEY,Config.APPSECRET);
    }

    private void setListener() {
        ll_title_left.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        tb_getcode.setOnClickListener(this);
        bt_next.setOnClickListener(this);
        et_phone_number.addTextChangedListener(this);
        et_codes.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.tb_getcode:
                getCode();
                break;
            case R.id.bt_next:
                checkCode();
                break;
        }
    }

    private void checkCode() {
        final String phone_number = et_phone_number.getText().toString();
        String code = et_codes.getText().toString();

        if(TextUtils.isEmpty(phone_number)){
            ToastUtil.showToast(this,"请输入手机号");
            return;
        }else if(!ValidationUtil.isMobile(phone_number)){
            ToastUtil.showToast(this,"请输入正确的手机号");
            return;
        }else if(TextUtils.isEmpty(code)) {
            ToastUtil.showToast(this, "请输入验证码");
            return;
        }else if(code.length()!=CodeLength) {
            ToastUtil.showToast(this, "请输入正确的验证码");
            return;
        }

        dialogFragment = AbDialogUtil.showProgressDialog(context,0,"正在提交,请稍后...");
        String url = Config.getAPI(Config.APIGetKey);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.TelePhone,phone_number);
        params.put(Config.SafetyCode,code);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                ChangeKeyBean changeKeyBean = gson.fromJson(content,ChangeKeyBean.class);
                if(changeKeyBean.getStatus().equals(Config.SUCCESS)) {
                    Intent intent = new Intent(ActivityForgetPwd.this, ActivityConfirmPwd.class);
                    intent.putExtra(Config.TelePhone, phone_number);
                    intent.putExtra(Config.ChangeKey,changeKeyBean.getChangekey());
                    startActivity(intent);
                }else {
                    ToastUtil.showToast(TheApp.instance,"服务器连接失败");
                }
                AbDialogUtil.removeDialog(context);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                //ToastUtil.showToast(context,content);
                ToastUtil.showToast(TheApp.instance,"服务器连接失败");
                AbDialogUtil.removeDialog(context);
            }

        });

    }

    public void getCode() {
        String phone = et_phone_number.getText().toString();
        if(phone.length()!=11){
            ToastUtil.showToast(this,"请输入正确的手机号");
            tb_getcode.setTextAfter("秒重新获取").setTextBefore("").setLenght(0);
            return;
        }

        tb_getcode.setTextAfter("秒重新获取").setTextBefore("").setLenght(60*1000);

        eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {
                /*Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                Log.w("mysend",data.toString());*/
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                    }else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                        //获取验证码成功
                    }else if (event ==SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES){
                        //返回支持发送验证码的国家列表
                    }
                }else{
                    ((Throwable)data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh);
        SMSSDK.getVerificationCode("86",phone);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        String code = et_codes.getText().toString();
        String phone_number = et_phone_number.getText().toString();
        isPhoneLength = phone_number.length() == Config.PhoneLength;
        isCodeLength = code.length() == Config.CodeLengh;

        //Log.w("haha",isPhoneLength+"!!"+isCodeLength+phone_number.length()+"!!"+et_codes.length());
        if(isPhoneLength&&isCodeLength){
            bt_next.getBackground().setAlpha(204);
        }else {
            bt_next.getBackground().setAlpha(102);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
