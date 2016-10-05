package com.supertreasure.login;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbProgressDialogFragment;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbViewUtil;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.supertreasure.R;
import com.supertreasure.bean.Status;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.util.ToastUtil;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/3/14.
 */
public class ActivityConfirmPwd extends AbActivity implements View.OnClickListener,TextWatcher{

    private AbProgressDialogFragment dialogFragment;
    private Context context;
    private LinearLayout ll_title_left;
    private TextView tv_back;
    private TextView tv_title;
    private TextView tv_title_name;
    private EditText et_first_pwd;
    private EditText et_second_pwd;
    private Button bt_confirm;
    private Boolean isFirstPwdNotEmpty;
    private Boolean isSecondPwdNotEmpty;
    private Boolean isFirstPwd;
    private Boolean isSecondPwd;
    private Boolean isEqualPwd;
    private String changekey;
    private String telePhone;
    private String firstPwd;
    private String secondPwd;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_corfirm_pwd);

        SwipeBackHelper.onCreate(this);
        initView();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    private void initView() {
        context = this;
        AbViewUtil.scaleContentView((FrameLayout) findViewById(R.id.root));
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("修改密码");
        tv_title_name = (TextView) findViewById(R.id.tv_title_name);
        et_first_pwd = (EditText) findViewById(R.id.et_first_password);
        et_second_pwd = (EditText) findViewById(R.id.et_second_password);
        bt_confirm = (Button) findViewById(R.id.bt_confirm);

        bt_confirm.getBackground().setAlpha(Config.AlphaBlack);
    }

    private void setListener(){
        tv_back.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);
        bt_confirm.setOnClickListener(this);

        et_first_pwd.addTextChangedListener(this);
        et_second_pwd.addTextChangedListener(this);
    }

    private void confirmPwd() {
        if(!isFirstPwdNotEmpty||!isSecondPwdNotEmpty) {
            ToastUtil.showToast(context, "请输入密码");
            return;
        }

        if(!isFirstPwd||!isSecondPwd){
            ToastUtil.showToast(context, "密码需不小于6位");
            return;
        }

        if(!isEqualPwd){
            ToastUtil.showToast(context, "请确认两次密码是否相同");
            return;
        }

        dialogFragment = AbDialogUtil.showProgressDialog(context,0,"正在提交,请稍后...");
        String url = Config.getAPI(Config.APIChangePwd);
        changekey = getIntent().getStringExtra(Config.ChangeKey);
        telePhone = getIntent().getStringExtra(Config.TelePhone);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.TelePhone,telePhone);
        params.put(Config.ChangeKey,changekey);
        params.put(Config.ChangePwd,firstPwd);

        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                Status status = gson.fromJson(content,Status.class);
                if(status.getStatus().equals(Config.SUCCESS)){
                    ToastUtil.showToast(context,"成功");
                }else {
                    ToastUtil.showToast(TheApp.instance,"服务器连接失败");
                }
                AbDialogUtil.removeDialog(context);
                finish();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                ToastUtil.showToast(TheApp.instance,"服务器连接失败");
                AbDialogUtil.removeDialog(context);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.bt_confirm:
                confirmPwd();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        firstPwd = et_first_pwd.getText().toString();
        secondPwd = et_second_pwd.getText().toString();

        isFirstPwdNotEmpty = !TextUtils.isEmpty(firstPwd);
        isSecondPwdNotEmpty = !TextUtils.isEmpty(secondPwd);
        isFirstPwd = firstPwd.length()>= Config.MinPwdLenght;
        isSecondPwd = secondPwd.length()>=Config.MinPwdLenght;
        isEqualPwd = firstPwd.equals(secondPwd);

        if(isFirstPwd&&isSecondPwd&&isEqualPwd)
            bt_confirm.getBackground().setAlpha(Config.AlphaLight);
        else
            bt_confirm.getBackground().setAlpha(Config.AlphaBlack);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
