package com.supertreasure.mine;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbProgressDialogFragment;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.http.entity.mine.content.StringBody;
import com.ab.image.AbImageLoader;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.afollestad.materialdialogs.internal.MDTintHelper;
import com.afollestad.materialdialogs.internal.ThemeSingleton;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Response;
import com.supertreasure.R;
import com.supertreasure.bean.AboutMeBeen;
import com.supertreasure.bean.Address;
import com.supertreasure.bean.EditHobbyBean;
import com.supertreasure.bean.EditNameBean;
import com.supertreasure.bean.EditSelfdomBean;
import com.supertreasure.bean.EditSignatureBean;
import com.supertreasure.constant.Constant;

import com.supertreasure.eventbus.EditNickName;
import com.supertreasure.eventbus.EditSign;
import com.supertreasure.eventbus.RefleshMineActivity;
import com.supertreasure.login.CampusAdapter;
import com.supertreasure.login.ProvinceAdapter;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.AddressJson;
import com.supertreasure.util.Bimp;
import com.supertreasure.util.Config;
import com.supertreasure.util.CustomToolBar;
import com.supertreasure.util.HttpUtil;
import com.supertreasure.util.MyUtils;
import com.supertreasure.util.RoundProgressBar;
import com.supertreasure.util.ToastUtil;
import com.supertreasure.widget.CircleImageView;
import com.xw.repo.xedittext.XEditText;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActivityPersonData extends AppCompatActivity implements View.OnClickListener{
    private Context context;
    private final int IMAGE_PICKER = 100;
    private final int REQUEST_CODE_GALLERY = 1001;
    private final int MultiImageSelector_REQUEST_IMAGE = 1111;
    private ArrayList<String> defaultDataArray;
    private ArrayList<ImageItem> images;
    private XEditText noEmojiEditText;
    private LinearLayout root;
    private Toolbar toolbar;
    /*private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private Button bt_title_left;
    private TextView tv_title;*/

    private View vg_headImg;
    private View vg_currentAccount;
    private View vg_nickname;
    private View vg_signName;
    private View vg_personality;
    private View vg_hobby;
    private View vg_sex;
    private View vg_loveOffair;
    private View vg_belongSchool;
    private TextView tv_sex;
    private TextView tv_loveOffair;
    private TextView tv_nickname;
    private TextView tv_signature;
    private TextView tv_currentAccount;
    private TextView tv_personality;
    private TextView tv_hobby;
    private TextView tv_belongschool;
    private TextView tv_min;
    private TextView tv_max;
    private SimpleDraweeView draweeView;

    private CampusAdapter campusAdapter;
    private ProvinceAdapter provinceAdapter;
    //popWindow
    private PopupWindow mPopWindow;
    private Dialog schoolDialog;
    private TextView popTitle;
    private ListView mProvinceListView;
    private ListView mSchoolListView;
    private Address address;
    private List<String> campusList;
    private View parent;
    private View positiveAction;
    private Dialog roundProgressBarDialog;
    private RoundProgressBar progressBar;
    private MaterialDialog dialog;
    private int choose;
    private int max;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_edit);
        SwipeBackHelper.onCreate(this);
        context = getApplicationContext();
        initView();
        initToolbar();
        initData();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    private void initListener() {
        /*bt_title_left.setOnClickListener(this);
        ll_title_left.setOnClickListener(this);*/
        vg_headImg.setOnClickListener(this);
        vg_currentAccount.setOnClickListener(this);
        vg_nickname.setOnClickListener(this);
        vg_signName.setOnClickListener(this);
        vg_personality.setOnClickListener(this);
        vg_hobby.setOnClickListener(this);
        vg_sex.setOnClickListener(this);
        vg_loveOffair.setOnClickListener(this);
        //学校不可以改
        vg_belongSchool.setOnClickListener(this);
    }

    private void initData() {
        //-------------------先在本地获取数据----------------------------
        String hobbby           = AbSharedUtil.getString(TheApp.instance,Config.Content_AboutMe_Hobby);
        String personality      = AbSharedUtil.getString(TheApp.instance,Config.Content_AboutMe_Personality);
        String currentAccount   = AbSharedUtil.getString(TheApp.instance,Config.Content_AboutMe_CurrentAccount);
        String belongschool     = AbSharedUtil.getString(TheApp.instance,Config.Content_AboutMe_Belongschool);
        String sex              = AbSharedUtil.getString(TheApp.instance,Config.Content_AboutMe_Sex);
        String loveaffair       = AbSharedUtil.getString(TheApp.instance,Config.Content_AboutMe_LoveOffair);
        String nickname         = AbSharedUtil.getString(TheApp.instance,Config.Content_AboutMe_Nickname);
        String signature        = AbSharedUtil.getString(TheApp.instance,Config.Content_AboutMe_Signature);
        String userPic          = AbSharedUtil.getString(TheApp.instance,Config.Content_AboutMe_HeadImg);

        tv_hobby.setText(hobbby);
        tv_personality.setText(personality);
        tv_currentAccount.setText(currentAccount);
        tv_belongschool.setText(belongschool);
        tv_sex.setText(sex);
        tv_loveOffair.setText(loveaffair);
        tv_nickname.setText(nickname);
        tv_signature.setText(signature);

        if(userPic!=null) {
            int width = 120, height = 120;
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(userPic))
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(draweeView.getController())
                    .setImageRequest(request)
                    .build();
            draweeView.setController(controller);
        }
        //AbImageLoader.getInstance(ActivityPersonData.this).display(Iv_headImg,userPic);
        //draweeView.setImageURI(Uri.parse(userPic));

        //---------------------------再从网络获取-------------------------
        String url = Config.getAPI(Config.APIAboutMe);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this,Config.Token));
        TheApp.mAbHttpUtil.post(url,params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                AboutMeBeen aboutMeBeen = gson.fromJson(content, AboutMeBeen.class);
                if (aboutMeBeen.getStatus().equals(Config.SUCCESS)){

                    tv_hobby.setText(aboutMeBeen.getAboutMe().getHobby());
                    tv_personality.setText(aboutMeBeen.getAboutMe().getSelfdom());
                    tv_currentAccount.setText(aboutMeBeen.getAboutMe().getUserName());
                    tv_belongschool.setText(aboutMeBeen.getAboutMe().getBelongschool());
                    tv_sex.setText(aboutMeBeen.getAboutMe().getSex());
                    tv_loveOffair.setText(aboutMeBeen.getAboutMe().getLove());
                    tv_nickname.setText(aboutMeBeen.getAboutMe().getNickName());
                    tv_signature.setText(aboutMeBeen.getAboutMe().getSign());
                    int width = 120, height = 120;
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(
                            Uri.parse(aboutMeBeen.getAboutMe().getUserPic()))
                            .setResizeOptions(new ResizeOptions(width, height))
                            .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setOldController(draweeView.getController())
                            .setImageRequest(request)
                            .build();
                    draweeView.setController(controller);
                    //draweeView.setImageURI(Uri.parse(aboutMeBeen.getAboutMe().getUserPic()));
                    //TheApp.mAbImageLoader.display(Iv_headImg, aboutMeBeen.getAboutMe().getUserPic());
                    //AbImageLoader.getInstance(ActivityPersonData.this).display(Iv_headImg,aboutMeBeen.getAboutMe().getUserPic());

                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_Hobby,aboutMeBeen.getAboutMe().getHobby());
                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_Personality,aboutMeBeen.getAboutMe().getSelfdom());
                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_CurrentAccount,aboutMeBeen.getAboutMe().getUserName());
                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_Belongschool,aboutMeBeen.getAboutMe().getBelongschool());
                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_Sex,aboutMeBeen.getAboutMe().getSex());
                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_LoveOffair,aboutMeBeen.getAboutMe().getLove());
                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_Nickname,aboutMeBeen.getAboutMe().getNickName());
                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_Signature,aboutMeBeen.getAboutMe().getSign());
                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_HeadImg,aboutMeBeen.getAboutMe().getUserPic());
                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_Money,aboutMeBeen.getAboutMe().getMoney());

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
                Toast.makeText(ActivityPersonData.this, content, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void initView() {

        root                = (LinearLayout) findViewById(R.id.root);
        AbViewUtil.scaleContentView(root);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        vg_headImg          = findViewById(R.id.head_viewGroup);
        vg_currentAccount   = findViewById(R.id.btn_mine_currentAccount);
        vg_nickname         = findViewById(R.id.btn_mine_nickname);
        vg_signName         = findViewById(R.id.btn_mine_signName);
        vg_personality      = findViewById(R.id.btn_mine_personality);
        vg_hobby            = findViewById(R.id.btn_mine_hobby);
        vg_sex              = findViewById(R.id.btn_mine_sex);
        vg_loveOffair       = findViewById(R.id.btn_mine_loveOffair);
        vg_belongSchool     = findViewById(R.id.btn_mine_belongSchool);
        tv_sex              = (TextView) findViewById(R.id.tv_sex_content);
        tv_loveOffair       = (TextView) findViewById(R.id.tv_loveOffair_content);
        tv_nickname         = (TextView) findViewById(R.id.tv_nickname_content);
        tv_currentAccount   = (TextView) findViewById(R.id.tv_currentAccount_content);
        tv_personality      = (TextView) findViewById(R.id.tv_personality_content);
        tv_hobby            = (TextView) findViewById(R.id.tv_hobby_content);
        tv_signature        = (TextView) findViewById(R.id.signature);
        tv_belongschool     = (TextView) findViewById(R.id.tv_belongSchool_content);
        draweeView          = (SimpleDraweeView) findViewById(R.id.headImg);

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(Constant.RequestCode_ActivityPersonData);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.bt_title_left:
                //ToastUtil.showToast(this,"返回键");
                setResult(Constant.RequestCode_ActivityPersonData);
                finish();
                break;
            case R.id.ll_title_left:
                //ToastUtil.showToast(this,"返回键");
                setResult(Constant.RequestCode_ActivityPersonData);
                finish();
                break;
            case R.id.head_viewGroup:
                //ToastUtil.showToast(this,"点击头像");
                ImagePicker.getInstance().setMultiMode(false);
                ImagePicker.getInstance().setCrop(true);
                intent = new Intent(TheApp.instance,ImageGridActivity.class);
                startActivityForResult(intent,IMAGE_PICKER);
                break;
            case R.id.btn_mine_currentAccount:
                //ToastUtil.showToast(this,"点击当前账号");
                break;
            case R.id.btn_mine_nickname:
                /*intent = new Intent(this,ActivityEditNickname.class);
                intent.putExtra(Config.NickName,tv_nickname.getText().toString());
                ToastUtil.showToast(this,"点击昵称");*/
                changeNickName();
                //startActivityForResult(intent, Constant.RequestCode_ActivityPersonData);
                break;
            case R.id.btn_mine_signName:
                changeSignName();
                //ToastUtil.showToast(this,"点击签名");
                /*intent = new Intent(this,ActivityEditSignature.class);
                intent.putExtra(Config.Sign,tv_signature.getText().toString());
                startActivityForResult(intent,Constant.RequestCode_ActivityPersonData);*/
                break;
            case R.id.btn_mine_personality:
                changePersonality();
                /*intent = new Intent(this,ActivityEditSelfdom.class);
                intent.putExtra(Config.Selfdom,tv_personality.getText().toString());
                //ToastUtil.showToast(this,"点击个性");
                startActivityForResult(intent,Constant.RequestCode_ActivityPersonData);*/
                break;
            case R.id.btn_mine_hobby:
                changeHobby();
                /*intent = new Intent(this,ActivityEditHobby.class);
                intent.putExtra(Config.Hobby,tv_hobby.getText().toString());
                //ToastUtil.showToast(this,"点击爱好");
                startActivityForResult(intent,Constant.RequestCode_ActivityPersonData);*/
                break;
            case R.id.btn_mine_sex:
                //ToastUtil.showToast(this,"点击性别");
                showSexSettingDialog();
                break;
            case R.id.btn_mine_loveOffair:
                //ToastUtil.showToast(this,"点击恋爱关系");
                showLoveStatusSettingDialog();
                break;
            case R.id.btn_mine_belongSchool:
                //ToastUtil.showToast(this,"点击所属学校");
               // showPopWindow();
                showSchoolDialog();
                //ToastUtil.showToast(this,"如需修改请联系开发人员");
                break;

        }
    }

    private void changeHobby() {

        dialog = new MaterialDialog.Builder(this)
                .title(R.string.hobby)
                .customView(R.layout.dialog_customview, true)
                .positiveText(R.string.submit)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //showToast("Password: " + passwordInput.getText().toString());
                        String url = Config.getAPI(Config.APIEditHobby);
                        AbRequestParams params = new AbRequestParams();
                        params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance,Config.UserName));
                        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance,Config.Token));
                        params.put(Config.Hobby,noEmojiEditText.getText().toString());
                        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
                            @Override
                            public void onSuccess(int statusCode, String content) {
                                Gson gson = new Gson();
                                EditHobbyBean editNameBeen = gson.fromJson(content,EditHobbyBean.class);
                                if (editNameBeen.getStatus().equals(Config.SUCCESS)){
                                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_Hobby,editNameBeen.getHobby());
                                    tv_hobby.setText(editNameBeen.getHobby());
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
                                ToastUtil.showToast(TheApp.instance,"修改爱好失败");
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
        tv_min = (TextView) dialog.getCustomView().findViewById(R.id.tv_min);
        tv_max = (TextView) dialog.getCustomView().findViewById(R.id.tv_max);

        noEmojiEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    positiveAction.setEnabled(s.toString().trim().length() > 0);
                    tv_min.setText(s.toString().trim().length()+"");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        max = 20;
        tv_min.setText(tv_hobby.getText().toString().length()+"");
        tv_max.setText(max+"");
        noEmojiEditText.setMaxLength(max);
        noEmojiEditText.setText(tv_hobby.getText().toString());
        noEmojiEditText.setSelection(tv_hobby.getText().toString().length());
        dialog.show();
        showKeyBoard();
    }

    private void changePersonality() {
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.myself)
                .customView(R.layout.dialog_customview, true)
                .positiveText(R.string.submit)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String url = Config.getAPI(Config.APIEditSelfdom);
                        AbRequestParams params = new AbRequestParams();
                        params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance,Config.UserName));
                        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance,Config.Token));
                        params.put(Config.Selfdom,noEmojiEditText.getText().toString());
                        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
                            @Override
                            public void onSuccess(int statusCode, String content) {
                                Gson gson = new Gson();
                                EditSelfdomBean editNameBeen = gson.fromJson(content,EditSelfdomBean.class);
                                if (editNameBeen.getStatus().equals(Config.SUCCESS)){
                                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_Personality,editNameBeen.getSelfdom());
                                    tv_personality.setText(editNameBeen.getSelfdom());
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
                                ToastUtil.showToast(TheApp.instance,"修改个性失败");
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
        tv_min = (TextView) dialog.getCustomView().findViewById(R.id.tv_min);
        tv_max = (TextView) dialog.getCustomView().findViewById(R.id.tv_max);

        noEmojiEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    positiveAction.setEnabled(s.toString().trim().length() > 0);
                    tv_min.setText(s.toString().trim().length()+"");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        max = 20;
        tv_min.setText(tv_personality.getText().toString().length()+"");
        tv_max.setText(max+"");
        noEmojiEditText.setMaxLength(max);
        noEmojiEditText.setText(tv_personality.getText().toString());
        noEmojiEditText.setSelection(tv_personality.getText().toString().length());
        dialog.show();
        showKeyBoard();
    }

    private void changeSignName() {
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.sing_name)
                .backgroundColor(Color.WHITE)
                .customView(R.layout.dialog_customview, true)
                .positiveText(R.string.submit)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String url = Config.getAPI(Config.APIEditSign);
                        AbRequestParams params = new AbRequestParams();
                        params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance,Config.UserName));
                        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance,Config.Token));
                        params.put(Config.Sign,noEmojiEditText.getText().toString());
                        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
                            @Override
                            public void onSuccess(int statusCode, String content) {
                                Gson gson = new Gson();
                                EditSignatureBean editNameBeen = gson.fromJson(content,EditSignatureBean.class);
                                if (editNameBeen.getStatus().equals(Config.SUCCESS)){
                                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_Signature,editNameBeen.getSign());
                                    tv_signature.setText(editNameBeen.getSign());
                                    EventBus.getDefault().post(new EditSign(editNameBeen.getSign()));
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
                                ToastUtil.showToast(TheApp.instance,"修改签名失败");
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
        tv_min = (TextView) dialog.getCustomView().findViewById(R.id.tv_min);
        tv_max = (TextView) dialog.getCustomView().findViewById(R.id.tv_max);

        noEmojiEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    positiveAction.setEnabled(s.toString().trim().length() > 0);
                    tv_min.setText(s.toString().trim().length()+"");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        max = 30;
        tv_min.setText(tv_signature.getText().toString().length()+"");
        tv_max.setText(max+"");
        noEmojiEditText.setText(tv_signature.getText().toString());
        noEmojiEditText.setMaxLength(max);
        noEmojiEditText.setSelection(tv_signature.getText().toString().length());
        dialog.show();
        showKeyBoard();
    }

    private void changeNickName() {

        dialog = new MaterialDialog.Builder(this)
                .title(R.string.nick_name)
                .customView(R.layout.dialog_customview, true)
                .positiveText(R.string.submit)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String url = Config.getAPI(Config.APIEditNickName);
                        AbRequestParams params = new AbRequestParams();
                        params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance,Config.UserName));
                        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance,Config.Token));
                        params.put(Config.NickName,noEmojiEditText.getText().toString());
                        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
                            @Override
                            public void onSuccess(int statusCode, String content) {
                                Gson gson = new Gson();
                                EditNameBean editNameBeen = gson.fromJson(content,EditNameBean.class);
                                if (editNameBeen.getStatus().equals(Config.SUCCESS)){
                                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_Nickname,editNameBeen.getNickName());
                                    tv_nickname.setText(editNameBeen.getNickName());
                                    EventBus.getDefault().post(new EditNickName(editNameBeen.getNickName()));
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
                                ToastUtil.showToast(TheApp.instance,"修改昵称失败");
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
        tv_min = (TextView) dialog.getCustomView().findViewById(R.id.tv_min);
        tv_max = (TextView) dialog.getCustomView().findViewById(R.id.tv_max);

        noEmojiEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(s.toString().trim().length() > 0);
                 tv_min.setText(s.toString().trim().length()+"");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        max = 9;
        tv_min.setText(tv_nickname.getText().toString().length()+"");
        tv_max.setText(max+"");
        noEmojiEditText.setMaxLength(max);
        noEmojiEditText.setText(tv_nickname.getText().toString());
        noEmojiEditText.setSelection(tv_nickname.getText().toString().length());
        dialog.show();
        showKeyBoard();
        /*new MaterialDialog.Builder(this)
                .title(R.string.nick_name)
                .content(R.string.nick_name_notification)
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(1, 9)
                .positiveText(R.string.submit)
                .negativeText(R.string.cancel)
                .input(R.string.nick_name_hint, R.string.empty, false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        String url = Config.getAPI(Config.APIEditNickName);
                        AbRequestParams params = new AbRequestParams();
                        params.put(Config.UserName, AbSharedUtil.getString(TheApp.instance,Config.UserName));
                        params.put(Config.Token, AbSharedUtil.getString(TheApp.instance,Config.Token));
                        params.put(Config.NickName,input.toString());
                        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
                            @Override
                            public void onSuccess(int statusCode, String content) {
                                Gson gson = new Gson();
                                EditNameBean editNameBeen = gson.fromJson(content,EditNameBean.class);
                                if (editNameBeen.getStatus().equals(Config.SUCCESS)){
                                    AbSharedUtil.putString(TheApp.instance,Config.Content_AboutMe_Nickname,editNameBeen.getNickName());
                                    tv_nickname.setText(editNameBeen.getNickName());
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
                                ToastUtil.showToast(TheApp.instance,"修改昵称失败");
                            }
                        });
                    }
                }).show();*/
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    protected void onRestart() {
        super.onRestart();

        //uploadPic();

    }
    //----------------------------------------------
    /*Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    if (Bimp.bmp!=null) {
                        for (Bitmap bitmap:Bimp.bmp)
                            Iv_headImg.setImageBitmap(bitmap);
                    }

                    Bimp.clear();
                    //FileUtils.deleteDir();

                    break;
                case 2:
                    draweeView.setImageURI(Uri.parse(AbSharedUtil.getString(TheApp.instance,Config.Content_AboutMe_HeadImg)));
                    //TheApp.mAbImageLoader.display(Iv_headImg,AbSharedUtil.getString(ActivityPersonData.this,Config.Content_AboutMe_HeadImg));//��ʾ��ַ
                    break;
            }
            super.handleMessage(msg);
        }
    };*/


    //-----------------------------------------------------------
    private void showSexSettingDialog() {
        if(tv_sex.getText().toString().equals(Config.Girl)){
            choose = 1;
        }else{
            choose = 0;
        }
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.sex)
                .items(R.array.sex_array)
/*                .widgetColorRes(R.color.text_color_title)
                .negativeColorRes(R.color.text_color_title)
                .positiveColorRes(R.color.text_color_title)*/
                .itemsCallbackSingleChoice(choose, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if(which==0){
                            changSex("男");
                        }else {
                            changSex("女");
                        }
                        return true; // allow selection
                    }
                })
                .positiveText(R.string.confrim)
                .negativeText(R.string.cancel)
                .show();
        /*View view = getLayoutInflater().inflate(R.layout.dialog_mine_sex, null);
        AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.root));

        View btn_boy = view.findViewById(R.id.btn_mine_boy);
        View btn_girl = view.findViewById(R.id.btn_mine_girl);
        View btn_cancel = view.findViewById(R.id.btn_mine_cancel);

        final Dialog dialog = new Dialog(this, R.style.photoChooseDialog);
        dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
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
                    case R.id.btn_mine_girl:
                        ToastUtil.showToast(ActivityPersonData.this,"女");
                        changSex("女",dialog);
                        break;
                    case R.id.btn_mine_boy:
                        ToastUtil.showToast(ActivityPersonData.this,"男");
                        changSex("男",dialog);
                        break;
                    case R.id.btn_mine_cancel:
                        ToastUtil.showToast(ActivityPersonData.this,"取消");
                        dialog.dismiss();
                        break;
                }
            }
        };
        btn_boy.setOnClickListener(onClickListener);
        btn_girl.setOnClickListener(onClickListener);
        btn_cancel.setOnClickListener(onClickListener);*/
    }

    private void showLoveStatusSettingDialog() {
        if(tv_loveOffair.getText().toString().equals("单身")){
            choose = 0;
        }else if(tv_loveOffair.getText().toString().equals("初恋")){
            choose = 1;
        }else if(tv_loveOffair.getText().toString().equals("热恋")){
            choose = 2;
        }else{
            choose = 3;
        }
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.love)
                .items(R.array.love_array)
/*                .widgetColorRes(R.color.text_color_title)
                .negativeColorRes(R.color.text_color_title)
                .positiveColorRes(R.color.text_color_title)*/
                .itemsCallbackSingleChoice(choose, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if(which==0){
                            changLove("单身");
                        }else if(which==1){
                            changLove("初恋");
                        }else if(which==2){
                            changLove("热恋");
                        }else {
                            changLove("已婚");
                        }
                        return true; // allow selection
                    }
                })
                .positiveText(R.string.confrim)
                .negativeText(R.string.cancel)
                .show();
        /*View view = getLayoutInflater().inflate(R.layout.dialog_mine_lovestatus, null);
        AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.root));

        View btn_single = view.findViewById(R.id.btn_mine_single);
        View btn_firstlove = view.findViewById(R.id.btn_mine_tx_firstlove);
        View btn_heartlove = view.findViewById(R.id.btn_mine_heartlove);
        View btn_married = view.findViewById(R.id.btn_mine_married);
        View btn_cancel = view.findViewById(R.id.btn_mine_cancel);

        final Dialog dialog = new Dialog(this, R.style.photoChooseDialog);
        dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
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
                    case R.id.btn_mine_single:
                        ToastUtil.showToast(ActivityPersonData.this,"单身");
                        changLove("单身",dialog);
                        break;
                    case R.id.btn_mine_tx_firstlove:
                        ToastUtil.showToast(ActivityPersonData.this,"初恋");
                        changLove("初恋",dialog);
                        break;
                    case R.id.btn_mine_heartlove:
                        ToastUtil.showToast(ActivityPersonData.this,"热恋");
                        changLove("热恋",dialog);
                        break;
                    case R.id.btn_mine_married:
                        ToastUtil.showToast(ActivityPersonData.this,"已婚");
                        changLove("已婚",dialog);
                        break;
                    case R.id.btn_mine_cancel:
                        ToastUtil.showToast(ActivityPersonData.this,"取消");
                        dialog.dismiss();
                        break;
                }
            }
        };
        btn_single.setOnClickListener(onClickListener);
        btn_married.setOnClickListener(onClickListener);
        btn_heartlove.setOnClickListener(onClickListener);
        btn_firstlove.setOnClickListener(onClickListener);
        btn_cancel.setOnClickListener(onClickListener);*/
    }


    public String changSex(final String sex){

        String url = Config.getAPI(Config.APIEditSex);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this,Config.Token));
        params.put(Config.Sex, sex);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                EditSexBeen editNameBeen = gson.fromJson(content,EditSexBeen.class);
                if (editNameBeen.getStatus().equals(Config.SUCCESS)){
                    AbSharedUtil.putString(ActivityPersonData.this,Config.Content_AboutMe_Sex,sex);
                    tv_sex.setText(sex);
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
                ToastUtil.showToast(ActivityPersonData.this,"修改性别失败");
            }
        });
        return sex;
    }

    public String changLove(final String love){

        String url = Config.getAPI(Config.APIEditLove);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this,Config.Token));
        params.put(Config.Love, love);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                EditLoveBeen editNameBeen = gson.fromJson(content,EditLoveBeen.class);
                if (editNameBeen.getStatus().equals(Config.SUCCESS)){
                    AbSharedUtil.putString(ActivityPersonData.this,Config.Content_AboutMe_LoveOffair,love);
                    tv_loveOffair.setText(love);
                    //dialog.dismiss();
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
                ToastUtil.showToast(ActivityPersonData.this,"修改恋爱关系失败");
            }
        });
        return love;
    }
    private void showUploadDialog() {
        if(roundProgressBarDialog==null){
            View view = getLayoutInflater().inflate(R.layout.dialog_round_progressbar, null);
            AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.root));
            progressBar = (RoundProgressBar) view.findViewById(R.id.roundProgressBar);
            roundProgressBarDialog = new Dialog(this, R.style.picUploadDialogStyle);
            roundProgressBarDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            Window window = roundProgressBarDialog.getWindow();
            // 设置显示动画
            window.setWindowAnimations(R.style.address_choose_animstyle);
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = 0;
            // 以下这两句是为了保证按钮可以水平满屏
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            // 设置显示位置
            roundProgressBarDialog.onWindowAttributesChanged(wl);
            // 设置点击外围解散
            roundProgressBarDialog.setCanceledOnTouchOutside(false);
            roundProgressBarDialog.show();
        }
        roundProgressBarDialog.show();
        /*if(runningManDialog==null) {
            runningManDialog = new RunningManDialog(this,"正在上传",R.drawable.frame);
            runningManDialog.setCanceledOnTouchOutside(false);
        }
        runningManDialog.show();*/
    }
    private void uploadPic(File file) {
        //Logger.i("","uploadPic() "+file.getName());
        //if (Bimp.drr.size()==0)//==============*****================
        //    return;
        showUploadDialog();

        //abDialog = AbDialogUtil.showProgressDialog(TheApp.instance, 0, "正在上传头像，请稍后...");

        //String url = "http://119.29.146.227:8080/data2" + Config.APIUserpicturePutIn;
        String url = Config.getAPI(Config.APIUserpicturePutIn);
        HttpUtil.HttpUploadRequest request = new HttpUtil.HttpUploadRequest(url);
        request.addHeaderParam(Config.UserName, AbSharedUtil.getString(ActivityPersonData.this, Config.UserName));
        request.addHeaderParam(Config.Token, AbSharedUtil.getString(ActivityPersonData.this, Config.Token));
        request.uploadFile("files", file, new HttpUtil.UIchangeListener() {
            @Override
            public void progressUpdate(long bytesWrite, long contentLength, boolean done, int position, int all) {
                if (position == all && done) {
                    progressBar.setProgress(100);
                } else {
                    progressBar.setProgress((int) (bytesWrite*100/contentLength));
                    //ToastUtil.showToast(TheApp.instance,bytesWrite+"!!"+contentLength);
                    //runningManDialog.setProgressBar(position + "/" + all + "         " + bytesWrite * 100 / contentLength + "%");
                }
            }
        });
        HttpUtil.CallBack callBack = new HttpUtil.CallBack() {

            @Override
            public void onSuccess(String body) {
                //Log.w("haha", body + "");
               // Logger.i("response content", body);

                Gson gson = new Gson();
                EditHeadImgBeen editHeadImgBeen = gson.fromJson(body, EditHeadImgBeen.class);
                if (editHeadImgBeen.getStatus().equals(Config.SUCCESS)){
                    UserHeadImgPutIn(editHeadImgBeen.getPaths());
                    int width = 120, height = 120;
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse("file://"+images.get(0).path))
                            .setResizeOptions(new ResizeOptions(width, height))
                            .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setOldController(draweeView.getController())
                            .setImageRequest(request)
                            .build();
                    draweeView.setController(controller);
                    //draweeView.setImageURI(Uri.parse("file://"+images.get(0).path));
                }
                roundProgressBarDialog.cancel();
                //
            }

            @Override
            public void onFailure(Response response) {
                //Logger.i("response content", response.toString());

                //Log.w("hahashibai", response + "");
                //AbDialogUtil.removeDialog(ActivityPersonData.this);
                ToastUtil.showToast(ActivityPersonData.this, "头像上传失败");
                roundProgressBarDialog.cancel();

                //uploadDialog.cancel();
                //FileUtils.deleteDir();
            }
        };
        HttpUtil.uploadFiles(request, callBack);

    }

    private void UserHeadImgPutIn(String paths) {
        String url = Config.getAPI(Config.APIEditChangHeadImg);
        HttpUtil.HttpRequest request = new HttpUtil.HttpRequest(url);
        request.addParam(Config.UserName, AbSharedUtil.getString(ActivityPersonData.this, Config.UserName));
        request.addParam(Config.Token, AbSharedUtil.getString(ActivityPersonData.this, Config.Token));
        request.addParam(Config.Paths,paths);
        HttpUtil.CallBack callBack = new HttpUtil.CallBack() {

            @Override
            public void onSuccess(String body) {
                //Log.w("haha", body + "");

                Gson gson = new Gson();
                EditHeadImgBeen editHeadImgBeen = gson.fromJson(body,EditHeadImgBeen.class);
                if(editHeadImgBeen.getStatus().equals(Config.SUCCESS)){
                    AbSharedUtil.putString(TheApp.instance, Config.Content_AboutMe_HeadImg, editHeadImgBeen.getPaths());
                    //handler.sendEmptyMessage(2);
                    String userPic = AbSharedUtil.getString(TheApp.instance, Config.Content_AboutMe_HeadImg);
                    EventBus.getDefault().post(new RefleshMineActivity(userPic));
                    //uploadDialog.cancel();
                    //finish();
                    //loading();//上传成功了，就把图片显示出来*****
                    //AbDialogUtil.removeDialog(ActivityPersonData.this);
                    ToastUtil.showToast(ActivityPersonData.this,"头像上传成功");

                }
            }

            @Override
            public void onFailure(Response response) {
                //uploadDialog.cancel();
                //  FileUtils.deleteDir();
                AbDialogUtil.removeDialog(ActivityPersonData.this);
                ToastUtil.showToast(ActivityPersonData.this, "头像上传失败");


            }


        };
        HttpUtil.httpPostGetData(request,callBack);
    }

    public String changSchool(final String school){

        String url = Config.getAPI(Config.APIEditBelongSchool);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this,Config.Token));
        params.put(Config.BelongSchool, school);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                EditBelongSchoolBeen editBelongSchoolBeen = gson.fromJson(content,EditBelongSchoolBeen.class);
                if (editBelongSchoolBeen.getStatus().equals(Config.SUCCESS)){
                    AbSharedUtil.putString(ActivityPersonData.this,Config.Content_AboutMe_Belongschool,school);
                    tv_belongschool.setText(school);
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
                ToastUtil.showToast(ActivityPersonData.this,"修改学校失败");
            }
        });
        return school;
    }
    public static class EditHeadImgBeen {
        private String status;
        private String paths;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPaths() {
            return paths;
        }

        public void setPaths(String paths) {
            this.paths = paths;
        }
    }
    public static class EditBelongSchoolBeen {
        private String status;
        private String belongSchool;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBelongSchool() {
            return belongSchool;
        }

        public void setBelongSchool(String belongSchool) {
            this.belongSchool = belongSchool;
        }
    }
    public static class EditLoveBeen {
        private String status;
        private String love;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLove() {
            return love;
        }

        public void setLove(String love) {
            this.love = love;
        }
    }
    public static class EditSexBeen {
        private String status;
        private String sex;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }

    //------------------------------------------------------------
    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(parent==mProvinceListView){
                popTitle.setText("请选择学校");
                mProvinceListView.setVisibility(View.GONE);
                mSchoolListView.setVisibility(View.VISIBLE);
                loadCampus(position);
            }else if(parent==mSchoolListView){
                changSchool(campusList.get(position));
                //tv_belongschool.setText();
                schoolDialog.dismiss();
            }
        }
    };
    public void loadProvince(){
        Gson gson = new Gson();
        address = gson.fromJson(AddressJson.addressJson,Address.class);
        provinceAdapter.setList(address.getList());
    }
    public void loadCampus(int position){
        campusList = address.getList().get(position).getCampus();
        campusAdapter.setList(campusList);
    }
    /*private void showPopWindow() {
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

    }*/

    private void showSchoolDialog() {
        if(schoolDialog==null) {
            parent = this.getWindow().getDecorView();
            View shoolView = View.inflate(this, R.layout.view_select_province_list, null);
            AbViewUtil.scaleContentView((LinearLayout) shoolView.findViewById(R.id.root));
            popTitle = (TextView) shoolView.findViewById(R.id.list_title);
            mProvinceListView = (ListView) shoolView.findViewById(R.id.province);
            mSchoolListView = (ListView) shoolView.findViewById(R.id.school);
            mProvinceListView.setOnItemClickListener(itemListener);
            mSchoolListView.setOnItemClickListener(itemListener);

            provinceAdapter = new ProvinceAdapter(this);
            campusAdapter = new CampusAdapter(this);
            mProvinceListView.setAdapter(provinceAdapter);
            mSchoolListView.setAdapter(campusAdapter);

            int width = getResources().getDisplayMetrics().widthPixels * 3 / 4;
            int height = getResources().getDisplayMetrics().heightPixels * 3 / 5;
            schoolDialog = new Dialog(this,R.style.photoChooseDialog);
            schoolDialog.setContentView(shoolView,new LinearLayout.LayoutParams(width,height));
            schoolDialog.setCanceledOnTouchOutside(true);

            loadProvince();
            schoolDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    popTitle.setText("请选择地区");
                    mProvinceListView.setVisibility(View.VISIBLE);
                    campusAdapter.setList(new ArrayList<String>());
                    mSchoolListView.setVisibility(View.GONE);
                }
            });
        }

        schoolDialog.show();
        /*dialog.setAnimationStyle(R.style.address_choose_animstyle);
        mPopWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);*/

    }

    private void showCameraDialog() {

        View view = View.inflate(TheApp.instance, R.layout.dialog_photo_choose, null);
        AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.root));

        Button openCamera = (Button) view.findViewById(R.id.openCamera);
        Button openPhones = (Button) view.findViewById(R.id.openPhones);
        Button cancel = (Button) view.findViewById(R.id.cancel);

        final Dialog dialog = new Dialog(this, R.style.photoChooseDialog);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = TheApp.screenHeight;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        dialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openCamra();

                dialog.cancel();
            }
        });
        openPhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GalleryChoosePic();
                //MultiImageSelectorChoosePic();
                //openPhotosForBig();
                //Bimp.clear();
                //ImageGridAdapter.canSelectNum = 1;
                //Intent intent = new Intent(ActivityPersonData.this,ActivityChoosePic.class);
                //startActivity(intent);
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

/*    public void MultiImageSelectorChoosePic(){
        defaultDataArray = new ArrayList<>();
        Intent intent = new Intent(TheApp.instance, MultiImageSelectorActivity.class);
// 是否显示调用相机拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
// 最大图片选择数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
// 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
// 默认选择图片,回填选项(支持String ArrayList)
        intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultDataArray);
        startActivityForResult(intent, MultiImageSelector_REQUEST_IMAGE);
    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode){
            case Constant.RequestCode_ActivityEditNickname:
                String nickName = data.getStringExtra("nickName");
                tv_nickname.setText(nickName);
                break;
            case Constant.RequestCode_ActivityEditSignature:
                String signature = data.getStringExtra("signature");
                tv_signature.setText(signature);
                break;
            case Constant.RequestCode_ActivityEditSelfdom:
                String selfdom = data.getStringExtra("selfdom");
                tv_personality.setText(selfdom);
                break;
            case Constant.RequestCode_ActivityEditHobby:
                String hobby = data.getStringExtra("hobby");
                tv_hobby.setText(hobby);
                break;
        }

        switch (requestCode){
            case IMAGE_PICKER:
                if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                    if (data != null && requestCode == IMAGE_PICKER) {
                        images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        if(images != null){
                            uploadPic(new File(images.get(0).path));
                            //TheApp.mAbImageLoader.display(Iv_headImg,path);
                        }
                    } else {
                        Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
                    }
                }
        }
        /*switch (requestCode) {
            case Constant.CHOOSE_BIG_PICTURE:
                Log.d(TAG, "CHOOSE_BIG_PICTURE: data = " + data);//it seems to be null
                if(imageUri != null){
                    Bitmap bitmap = decodeUriAsBitmap(imageUri);//decode bitmap
                    //Iv_headImg.setImageBitmap(bitmap);
                    File file = null;
                    try {
                        file = saveFile(bitmap,new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA))  + ".jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    uploadPic(file);
                }
                break;
            case Constant.CHOOSE_SMALL_PICTURE:
                if(data != null){
                    Bitmap bitmap = data.getParcelableExtra("data");
                    File file = null;
                    try {
                        file = saveFile(bitmap,new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA))  + ".jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    uploadPic(file);
                    //Iv_headImg.setImageBitmap(bitmap);
                }else{
                    Log.e(TAG, "CHOOSE_SMALL_PICTURE: data = " + data);
                }
                break;
            case Constant.TAKE_BIG_PICTURE:
                Log.d(TAG, "TAKE_BIG_PICTURE: data = " + data);//it seems to be null
                cropImageUri(imageUri, 200, 200, Constant.CROP_BIG_PICTURE);
                break;
            case Constant.CROP_BIG_PICTURE://from crop_big_picture
                Log.d(TAG, "CROP_BIG_PICTURE: data = " + data);//it seems to be null
                if(imageUri != null){
                    Bitmap bitmap = decodeUriAsBitmap(imageUri);
                    File file = null;
                    try {
                        file = saveFile(bitmap,new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA))  + ".jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    uploadPic(file);
                    //Iv_headImg.setImageBitmap(bitmap);
                }
        }

        if(requestCode == MultiImageSelector_REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....

                for (String s : path) {
                    uploadPic(new File(s));

                }
            }
        }*/

    }

    public File saveFile(Bitmap bm, String fileName) throws IOException {
        String path = getSDPath() +"/revoeye/";
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }
    public static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }
    //-----------------------------------------------------------------------------------------
    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";//temp file
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
    private static String TAG = "ActivityEditNickname";

    public void openPhotosForBig(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, Constant.CHOOSE_BIG_PICTURE);
    }

    public void openPhotosForSmall(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, Constant.CHOOSE_SMALL_PICTURE);
    }
    public void openCamra(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, Constant.TAKE_BIG_PICTURE);//or TAKE_SMALL_PICTURE
    }
    private Bitmap decodeUriAsBitmap(Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;

    }
    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }
    /*public void GalleryChoosePic(){

        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        cn.finalteam.galleryfinal.ImageLoader imageLoader = new FrescoImageLoader(context);
        functionConfigBuilder.setMutiSelectMaxSize(1)
                .setEnableCamera(true)
                .setRotateReplaceSource(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnablePreview(true)
                .setSelected((ArrayList<String>) Bimp.drr);//添加过滤集合
        Log.w("hahasize", Bimp.drr.size() + "");
        final FunctionConfig functionConfig = functionConfigBuilder.build();
        CoreConfig coreConfig = new CoreConfig.Builder(context, imageLoader, ThemeConfig.DARK)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(null)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, functionConfig, mOnHanlderResultCallback);
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null && resultList.size()>0) {
                uploadPic(new File(resultList.get(0).getPhotoPath()));
                //adapter.update();
                GalleryFinal.cleanCacheFile();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtil.showToast(context, "获取图片路径失败");
        }
    };*/
    //-------------------------------------------------------------------------------------------
  /*  public String changHeadImg(final String paths,File imgFile){

        String url = Config.getAPI(Config.APIEditChangHeadImg);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this,Config.Token));
        params.put(Config.Paths, imgFile);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                EditHeadImgBeen editHeadImgBeen = gson.fromJson(content,EditHeadImgBeen.class);
                if (editHeadImgBeen.getStatus().equals(Config.SUCCESS)){

                    //loading();//上传成功了，就把图片显示出来*****
                    AbSharedUtil.putString(ActivityPersonData.this,Config.Content_AboutMe_HeadImg,paths);
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
                ToastUtil.showToast(ActivityPersonData.this,"修改头像失败");
            }
        });
        return paths;
    }*/
        /*public void loading(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(Bimp.max == Bimp.drr.size()){
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    }else {
                        try {
                            String path = Bimp.drr.get(Bimp.max);
                            System.out.print(path);
                            Bitmap bm = Bimp.revitionImageSize(path);
                            Bimp.bmp.add(bm);
                            String newStr = path.substring(path.lastIndexOf("/")+1,path.lastIndexOf("."));
                            FileUtils.saveBitmap(bm,""+newStr);
                            Bimp.max++;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }

            }
        }).start();
    }*/

    //----------------------------------------------------
   /* public void takePhoto(){
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        StringBuffer sDir = new StringBuffer();
        if(hasSDcard()){
            sDir.append(Environment.getExternalStorageDirectory()+"/SuperTreasurePic/");
        }else {
            String dataPath = Environment.getRootDirectory().getPath();
            sDir.append(dataPath+"/SuperTreasurePic/");
        }

        File fileDir = new File(sDir.toString());
        if(!fileDir.exists()){
            fileDir.mkdir();
        }
        File file = new File(fileDir,String.valueOf(System.currentTimeMillis())+".jpg");

        path = file.getPath();
        Uri imageUrl = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUrl);
        //ImageGridAdapter.canSelectNum = 1;
        startActivityForResult(openCameraIntent,TAKE_PICTURE);
    }
    public static boolean hasSDcard(){
        String status = Environment.getExternalStorageState();
        if(status.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else {
            return false;
        }
    }*/

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
