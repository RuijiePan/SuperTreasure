package com.supertreasure.login;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.supertreasure.R;
import com.supertreasure.bean.Address;
import com.supertreasure.bean.ErrorBean;
import com.supertreasure.bean.userRegister;
import com.supertreasure.eventbus.RegisterMessage;
import com.supertreasure.login.CampusAdapter;
import com.supertreasure.login.ProvinceAdapter;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.AddressJson;
import com.supertreasure.util.Config;
import com.supertreasure.util.SlideBackActivity;
import com.supertreasure.util.ToastUtil;
import com.supertreasure.dialog.mSweetAlertDialog;
import com.supertreasure.util.ValidationUtil;
import com.supertreasure.widget.TimeButton;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class ActivityRegister extends AbActivity implements View.OnClickListener,TextWatcher {

    private int TIME_OUT = 5000;
    private Context context;
    private TextView tv_back;
    private LinearLayout ll_title_left;
    private EditText et_phone_number;
    private EditText et_first_password;
    private EditText et_query_password;
    private EditText et_nick_name;
    private TextView tv_belong_school;
    private EditText et_code;
    private TimeButton btn_getcode;
    private Button btn_register;
    private CampusAdapter campusAdapter;
    private ProvinceAdapter provinceAdapter;
    private EventHandler eh;
    //popWindow
    private PopupWindow mPopWindow;
    private TextView popTitle;
    private ListView mProvinceListView;
    private ListView mSchoolListView;
    private Address address;
    private List<String> campusList;
    private View parent;

    //判断注册按钮颜色
    private boolean isRegister;
    private boolean isPhoneNumer;
    private boolean isPhoneNoEmpty;
    private boolean isFirstPassword;
    private boolean isFirstPasswordNoEmpty;
    private boolean isSecondPassword;
    private boolean isSecondPasswordNoEmpty;
    private boolean isEqualPassword;
    private boolean isNickName;
    private boolean isSchool;
    private boolean isCodeNoEmpty;
    private boolean isCodeLength;
    private String phone_number;
    private String first_password;
    private String query_password;
    private String nick_name;
    private String belong_school;
    private String code;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SwipeBackHelper.onCreate(this);
        btn_getcode = (TimeButton) findViewById(R.id.btn_getcode);
        btn_getcode.onCreate(savedInstanceState);

        initView();
        setListener();
    }

    @Override
    protected void onDestroy() {
        if(btn_getcode.getText().toString().contains("秒后重新获取")){
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

        btn_getcode.onDestroy();
        SMSSDK.unregisterAllEventHandler();
        SwipeBackHelper.onDestroy(this);
        super.onDestroy();
    }

    private void setListener() {
        btn_getcode.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        tv_belong_school.setOnClickListener(this);

        et_phone_number.addTextChangedListener(this);
        et_first_password.addTextChangedListener(this);
        et_query_password.addTextChangedListener(this);
        et_nick_name.addTextChangedListener(this);
        tv_belong_school.addTextChangedListener(this);
        et_code.addTextChangedListener(this);
    }

    private void initView() {
        context = this;
        AbViewUtil.scaleContentView((FrameLayout) findViewById(R.id.root));
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_first_password = (EditText) findViewById(R.id.et_first_password);
        et_query_password = (EditText) findViewById(R.id.et_query_password);
        et_nick_name = (EditText) findViewById(R.id.et_nick_name);
        tv_belong_school = (TextView) findViewById(R.id.tv_belong_school);
        et_code = (EditText) findViewById(R.id.et_code);
        tv_back = (TextView) findViewById(R.id.tv_back);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.getBackground().setAlpha(102);
        SMSSDK.initSDK(this,Config.APPKEY,Config.APPSECRET);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.tv_back:
                finish();
                break;
            case R.id.btn_getcode:
                getCode();
                break;
            case R.id.btn_register:
                doRegister();
                break;
            case R.id.tv_belong_school:
                showPopWindow();
                break;
        }
    }

    private void showPopWindow() {
        if(mPopWindow==null) {
            parent = this.getWindow().getDecorView();
            View popWindow = View.inflate(this, R.layout.view_select_province_list, null);
            AbViewUtil.scaleContentView((LinearLayout) popWindow.findViewById(R.id.root));
            popTitle = (TextView) popWindow.findViewById(R.id.list_title);
            mProvinceListView = (ListView) popWindow.findViewById(R.id.province);
            mSchoolListView = (ListView) popWindow.findViewById(R.id.school);
            mProvinceListView.setOnItemClickListener(itemListener);
            mSchoolListView.setOnItemClickListener(itemListener);

            provinceAdapter = new ProvinceAdapter(this);
            campusAdapter = new CampusAdapter(this);
            mProvinceListView.setAdapter(provinceAdapter);
            mSchoolListView.setAdapter(campusAdapter);

            int width = getResources().getDisplayMetrics().widthPixels * 3 / 4;
            int height = getResources().getDisplayMetrics().heightPixels * 3 / 5;
            mPopWindow = new PopupWindow(popWindow, width, height);
            ColorDrawable dw = new ColorDrawable(0x00000000);
            mPopWindow.setBackgroundDrawable(dw);
            mPopWindow.setFocusable(true);
            mPopWindow.setTouchable(true);
            mPopWindow.setOutsideTouchable(true);//允许在外侧点击取消

            loadProvince();
            mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popTitle.setText("请选择地区");
                    mProvinceListView.setVisibility(View.VISIBLE);
                    campusAdapter.setList(new ArrayList<String>());
                    mSchoolListView.setVisibility(View.GONE);
                }
            });
        }

        mPopWindow.setAnimationStyle(R.style.address_choose_animstyle);
        mPopWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public void loadProvince(){
        Gson gson = new Gson();
        address = gson.fromJson(AddressJson.addressJson,Address.class);
        provinceAdapter.setList(address.getList());
    }

    public void loadCampus(int position){
        campusList = address.getList().get(position).getCampus();
        campusAdapter.setList(campusList);
    }

    /**
     * ListView Item点击事件
     */
    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(parent==mProvinceListView){
                popTitle.setText("请选择学校");
                mProvinceListView.setVisibility(View.GONE);
                mSchoolListView.setVisibility(View.VISIBLE);
                loadCampus(position);
            }else if(parent==mSchoolListView){
                tv_belong_school.setText(campusList.get(position));
                mPopWindow.dismiss();
            }
        }
    };

    private void getCode() {

        if(!isPhoneNumer){
            ToastUtil.showToast(this,"请输入正确的手机号");
            btn_getcode.setTextAfter("秒重新获取").setTextBefore("").setLenght(0);
            return;
        }

        btn_getcode.setTextAfter("秒重新获取").setTextBefore("").setLenght(60*1000);

        eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {

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
        SMSSDK.getVerificationCode("86",phone_number);
    }


    private void doRegister() {

        if(!isPhoneNoEmpty){
            ToastUtil.showToast(this,"请输入手机号");
            return;
        }else if(!isPhoneNumer){
            ToastUtil.showToast(this,"请输入正确的手机号");
            return;
        }else if(!isFirstPasswordNoEmpty){
            ToastUtil.showToast(this,"请输入密码");
            return;
        }else if(!isSecondPasswordNoEmpty){
            ToastUtil.showToast(this,"请输入确认密码");
            return;
        }else if(!isNickName){
            ToastUtil.showToast(this,"请输入昵称");
            return;
        }else if(!isSchool){
            ToastUtil.showToast(this,"请选择学校");
            return;
        }else if(!isCodeNoEmpty){
            ToastUtil.showToast(this,"请输入验证码");
            return;
        }else if(!isFirstPassword) {
            ToastUtil.showToast(this,"密码长度需不小于6位");
            return;
        }else if(!isEqualPassword){
            ToastUtil.showToast(this,"两次密码不一样，请重新输入");
            return;
        }else if(!isCodeLength){
            ToastUtil.showToast(this,"请输入正确的验证码");
            return;
        }

        final mSweetAlertDialog sweetAlertDialog = new mSweetAlertDialog(this,mSweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("正在提交");
        sweetAlertDialog.setCanceledOnTouchOutside(true);
        sweetAlertDialog.show();

        String url = Config.getAPI(Config.UserRegister);
        AbRequestParams params = new AbRequestParams();
        params.put("reg_phone",phone_number);
        params.put("reg_password",first_password);
        params.put("reg_nickname",nick_name);
        params.put("reg_school",belong_school);
        params.put("reg_code",code);
        TheApp.mAbHttpUtil.setTimeout(TIME_OUT);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                userRegister register = gson.fromJson(content,userRegister.class);
                String status=register.getStatus();
                if(status.equals(Config.SUCCESS)){
                    sweetAlertDialog.changeAlertType(mSweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setTitleText("注册成功");
                    EventBus.getDefault().post(new RegisterMessage(phone_number,first_password));
                    new CountDownTimer(1500,500){
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            sweetAlertDialog.cancel();
                            finish();
                        }
                    }.start() ;
                    sweetAlertDialog.show();
                    sweetAlertDialog.setCanceledOnTouchOutside(true);
                }else {
                    ErrorBean error = gson.fromJson(content,ErrorBean.class);
                    ToastUtil.showToast(TheApp.instance,"注册失败");
                    sweetAlertDialog.cancel();
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
                sweetAlertDialog.cancel();
            }
        });

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        phone_number=et_phone_number.getText().toString();
        first_password=et_first_password.getText().toString();
        query_password=et_query_password.getText().toString();
        nick_name=et_nick_name.getText().toString();
        belong_school= tv_belong_school.getText().toString();
        code=et_code.getText().toString();

      /*  Log.w("haha",first_password+"!!"+query_password);*/

        isPhoneNoEmpty = !TextUtils.isEmpty(phone_number);
        isPhoneNumer = ValidationUtil.isMobile(phone_number);
        isFirstPasswordNoEmpty = !TextUtils.isEmpty(first_password);
        isFirstPassword = first_password.length() >= Config.MinPwdLenght;
        isSecondPasswordNoEmpty = !TextUtils.isEmpty(query_password);
        isSecondPassword = query_password.length() >= Config.MinPwdLenght;
        isNickName = !TextUtils.isEmpty(nick_name);
        isSchool = !TextUtils.isEmpty(belong_school);
        isCodeNoEmpty = !TextUtils.isEmpty(code);
        isCodeLength = code.length() == Config.CodeLengh;
        isEqualPassword = first_password.equals(query_password);

        if(isPhoneNumer&&isPhoneNoEmpty&&isFirstPassword&&isSecondPassword&&isEqualPassword
                &&isFirstPasswordNoEmpty&&isSecondPasswordNoEmpty&&isNickName&&isSchool&&isCodeNoEmpty&&isCodeLength)
            btn_register.getBackground().setAlpha(204);
        else
            btn_register.getBackground().setAlpha(102);
       /* Log.w("haha",isPhoneNumer+"!!"+isPhoneNoEmpty+"!!"+isFirstPassword+"!!"+isSecondPassword+"!!"+isEqualPassword+"!!"+
                isFirstPasswordNoEmpty+"!!"+isSecondPasswordNoEmpty+"!!"+isNickName+"!!"+isSchool+"!!"+isCodeNoEmpty+"!!"+isCodeLength);*/
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
