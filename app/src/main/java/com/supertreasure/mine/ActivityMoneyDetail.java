package com.supertreasure.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.supertreasure.R;
import com.supertreasure.bean.CreditBean;
import com.supertreasure.bean.EditNameBean;
import com.supertreasure.bean.EditSignatureBean;
import com.supertreasure.bean.Status;
import com.supertreasure.eventbus.EditMoney;
import com.supertreasure.eventbus.EditNickName;
import com.supertreasure.eventbus.EditSign;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.util.ToastUtil;
import com.xw.repo.xedittext.XEditText;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by yum on 2016/4/24.
 */
public class ActivityMoneyDetail extends AbActivity implements View.OnClickListener{

    private View positiveAction;
    private XEditText noEmojiEditText;
    private EditText et_money;
    private LinearLayout ll_title_left;
    private Button bt_title_left;
    private Button bt_title_right;
    private View ll_title_right;
    private TextView tv_min;
    private TextView tv_max;
    private TextView tv_title;
    private TextView tv_money;
    private String money;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moneydetail);
        SwipeBackHelper.onCreate(this);

        initView();
        initData();
        setListener();
    }

    private void setListener() {
        bt_title_left.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);
        ll_title_right.setOnClickListener(this);
        bt_title_right.setOnClickListener(this);

    }

    private void initData() {
        money = getIntent().getStringExtra(Config.Content_Mine_money);
        tv_money.setText(money);
    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        ll_title_right = findViewById(R.id.ll_title_right);

        tv_money = (TextView) findViewById(R.id.money);
        bt_title_right.setText("兑换");
        tv_title.setText("我的元宝币");
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        et_money = (EditText) findViewById(R.id.et_money);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.bt_title_left:
                finish();
                break;
            case R.id.ll_title_right:
                exchangeMoney();
                break;
            case R.id.bt_title_right:
                exchangeMoney();
                break;
        }
    }

    private void exchangeMoney() {

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.exchange_money)
                .customView(R.layout.dialog_customview, true)
                .positiveText(R.string.submit)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String url = Config.getAPI(Config.APIExchangeMoney);
                        AbRequestParams params = new AbRequestParams();
                        params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance,Config.UserName));
                        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance,Config.Token));
                        params.put(Config.SerialNum,noEmojiEditText.getText().toString());
                        Log.w("haha",url);
                        Log.w("haha", AbSharedUtil.getString(TheApp.instance,Config.UserName));
                        Log.w("haha", AbSharedUtil.getString(TheApp.instance,Config.Token));
                        Log.w("haha", noEmojiEditText.getText().toString());
                        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
                            @Override
                            public void onSuccess(int statusCode, String content) {
                                Gson gson = new Gson();
                                Log.w("haha",content);
                                CreditBean credit = gson.fromJson(content,CreditBean.class);
                                if(credit.getStatus().equals(Config.SUCCESS)){
                                    String money = (credit.getCredit()+Integer.parseInt(tv_money.getText().toString()))+"";
                                    tv_money.setText(money);
                                    AbSharedUtil.putString(TheApp.instance, Config.Content_AboutMe_Money,money);
                                    EventBus.getDefault().post(new EditMoney(money));
                                    ToastUtil.showToast(TheApp.instance,"成功兑换"+credit.getCredit()+"个元宝币");
                                }else {
                                    ToastUtil.showToast(TheApp.instance,"兑换失败,请确认序列号");
                                }
                            }

                            @Override
                            public void onStart() {
                                hideKeyBoard();
                            }

                            @Override
                            public void onFinish() {

                            }

                            @Override
                            public void onFailure(int statusCode, String content, Throwable error) {
                                ToastUtil.showToast(TheApp.instance,"兑换失败");
                            }
                        });
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        hideKeyBoard();
                    }
                }).build();

        positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        //noinspection ConstantConditions
        noEmojiEditText = (XEditText) dialog.getCustomView().findViewById(R.id.edittext);
        //noEmojiEditText.setSeparator("-");
        //noEmojiEditText.setPattern(new int[]{4, 4, 4, 4});
        noEmojiEditText.setHint("请输入16位序列号");
        tv_min = (TextView) dialog.getCustomView().findViewById(R.id.tv_min);
        tv_max = (TextView) dialog.getCustomView().findViewById(R.id.tv_max);

        noEmojiEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.toString().length();
                positiveAction.setEnabled(length > 0);

                tv_min.setText(length+"");
                /*if(length<=4)
                    tv_min.setText(length+"");
                else if(length>4&&length<=9)
                    tv_min.setText(length-1+"");
                else if(length>9&&length<=14)
                    tv_min.setText(length-2+"");
                else
                    tv_min.setText(length-3+"");*/
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tv_min.setText("0");
        tv_max.setText("16");
        noEmojiEditText.setMaxLength(16);
        positiveAction.setEnabled(false);
        dialog.show();
        showKeyBoard();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    private void showKeyBoard() {
        InputMethodManager imm=(InputMethodManager) TheApp.instance.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(noEmojiEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void hideKeyBoard(){
        InputMethodManager imm = (InputMethodManager) TheApp.instance.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
