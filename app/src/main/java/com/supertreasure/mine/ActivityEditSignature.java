package com.supertreasure.mine;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.supertreasure.constant.Constant;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.util.MyUtils;

public class ActivityEditSignature extends AbActivity implements View.OnClickListener{

    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private Button bt_title_left;
    private Button bt_title_right;
    private TextView tv_title;
    private EditText edittext;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_signname);
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
        bt_title_right.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);
        ll_title_right.setOnClickListener(this);
    }

    private void initData() {
        edittext.setText(getIntent().getStringExtra(Config.Sign));
        edittext.setSelection(edittext.getText().length());

    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));

        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("个性签名");
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        bt_title_right.setText("提交");
        edittext = (EditText) findViewById(R.id.edittext);
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
                changSignature(edittext.getText().toString());
                break;
            case R.id.ll_title_right:
                changSignature(edittext.getText().toString());
                break;
        }
    }
    public String changSignature(final String sign){
        if (sign==null||sign.equals("")){
            MyUtils.show(this,"个人签名不可为空");
            return null;
        }
        String url = Config.getAPI(Config.APIEditSign);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this,Config.Token));
        params.put(Config.Sign, sign);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                EditSignatureBeen editNameBeen = gson.fromJson(content,EditSignatureBeen.class);
                if (editNameBeen.getStatus().equals(Config.SUCCESS)){
                    AbSharedUtil.putString(ActivityEditSignature.this,Config.Content_AboutMe_Signature,sign);
                    Intent intent = new Intent();
                    intent.putExtra("signature",sign);
                    setResult(Constant.RequestCode_ActivityEditSignature,intent);
                    finish();
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
                MyUtils.show(ActivityEditSignature.this,"修改个性签名失败");
            }
        });
        return sign;
    }
    public static class EditSignatureBeen {
        private String status;
        private String sign;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }


}
