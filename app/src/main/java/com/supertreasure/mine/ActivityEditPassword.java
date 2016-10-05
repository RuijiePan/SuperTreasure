package com.supertreasure.mine;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.supertreasure.R;
import com.supertreasure.dialog.mSweetAlertDialog;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.util.MyUtils;
import com.supertreasure.util.ToastUtil;

public class ActivityEditPassword extends AbActivity implements View.OnClickListener{

    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private Button bt_title_left;
    private Button bt_title_right;
    private TextView tv_title;
    
    private EditText edit_oldpassword;
    private EditText edit_newpassword1;
    private EditText edit_newpassword2;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_changpassword);
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
        ll_title_left.setOnClickListener(this);
        ll_title_right.setOnClickListener(this);
        bt_title_left.setOnClickListener(this);
        bt_title_right.setOnClickListener(this);
    }

    private void initData() {

    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));
        
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("修改密码");
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        bt_title_right.setText("完成");

        edit_oldpassword = (EditText) findViewById(R.id.edit_oldpassword);
        edit_newpassword1 = (EditText) findViewById(R.id.edit_newpassword1);
        edit_newpassword2 = (EditText) findViewById(R.id.edit_newpassword2);
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
            case R.id.bt_title_right:
                commit();
                break;
            case R.id.ll_title_right:
                commit();
                break;
        }
    }

    private void commit() {
        String oldpassword = edit_oldpassword.getText().toString().trim().toLowerCase();
        String newPassword1 = edit_newpassword1.getText().toString().trim().toLowerCase();
        String newPassword2 = edit_newpassword2.getText().toString().trim().toLowerCase();
        if (oldpassword.equals("")||newPassword1.equals("")||newPassword2.equals("")){
            ToastUtil.showToast(ActivityEditPassword.this,"请填写完整");
            return;
        }else if(oldpassword.length()<6||newPassword1.length()<6||newPassword2.length()<6){
            ToastUtil.showToast(ActivityEditPassword.this,"密码少于6位");
            return;
        } else if (oldpassword.equals(newPassword1)){
            ToastUtil.showToast(ActivityEditPassword.this,"新密码不可和旧密码一样");
            return;
        } else if (!newPassword1.equals(newPassword2)){
            ToastUtil.showToast(ActivityEditPassword.this,"请重新确认新密码");
            return;
        }
        changPassword(oldpassword,newPassword1);
    }

    public void changPassword(final String oldpassword,final String newPassword){

        final mSweetAlertDialog sweetAlertDialog = new mSweetAlertDialog(this,mSweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("正在提交");
        sweetAlertDialog.setCanceledOnTouchOutside(true);
        sweetAlertDialog.show();

        String url = Config.getAPI(Config.APIModifyPassword);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this,Config.Token));
        params.put(Config.OldPassword, oldpassword);
        params.put(Config.Password, newPassword);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                EditPasswordBeen editPasswordBeen = gson.fromJson(content,EditPasswordBeen.class);
                if (editPasswordBeen.getStatus().equals(Config.SUCCESS)){
                    AbSharedUtil.putString(TheApp.instance,Config.Password,edit_newpassword1.getText().toString());
                    edit_oldpassword.setText("");
                    edit_newpassword1.setText("");
                    edit_newpassword2.setText("");
                    //ToastUtil.showToast(ActivityEditPassword.this,editPasswordBeen.getMsg());
                    sweetAlertDialog.changeAlertType(mSweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setTitleText("修改成功");
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
                    //sweetAlertDialog.show();
                    sweetAlertDialog.setCanceledOnTouchOutside(true);
                }else if (editPasswordBeen.getStatus().equals(Config.ERROR)){
                    sweetAlertDialog.cancel();
                    ToastUtil.showToast(TheApp.instance,"服务器连接失败");
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
                ToastUtil.showToast(ActivityEditPassword.this,"修改密码失败");
            }
        });
        //return oldpassword;
    }

    public static class EditPasswordBeen {
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


}
