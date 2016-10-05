package com.supertreasure.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.supertreasure.util.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;

public class ActivityEditNickname extends AbActivity implements View.OnClickListener{

    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private Button bt_title_left;
    private Button bt_title_right;
    private TextView tv_title;
    private EditText edittext;

    //private ImageView image_head;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nickname);
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
        //image_head.setOnClickListener(this);

    }

    private void initData() {
        edittext.setText(getIntent().getStringExtra(Config.NickName));
        edittext.setSelection(edittext.getText().length());
    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));

        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("昵称");
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        bt_title_right.setText("提交");
        edittext = (EditText) findViewById(R.id.edittext);


        //image_head = (ImageView) findViewById(R.id.image_head);
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
                changNickName(edittext.getText().toString().trim());
                break;
            case R.id.ll_title_right:
                changNickName(edittext.getText().toString().trim());
                break;
        }
    }
    public String changNickName(final String nickName){

        if (nickName==null||nickName.equals("")){
            MyUtils.show(this,"名称不可为空");
            return null;
        }

        String url = Config.getAPI(Config.APIEditNickName);
        AbRequestParams params = new AbRequestParams();
        params.put(Config.UserName, AbSharedUtil.getString(this,Config.UserName));
        params.put(Config.Token, AbSharedUtil.getString(this,Config.Token));
        params.put(Config.NickName, nickName);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Gson gson = new Gson();
                EditNameBeen editNameBeen = gson.fromJson(content,EditNameBeen.class);
                if (editNameBeen.getStatus().equals(Config.SUCCESS)){
                    AbSharedUtil.putString(ActivityEditNickname.this,Config.Content_AboutMe_Nickname,nickName);
                    Intent intent = new Intent();
                    intent.putExtra("nickName",nickName);
                    setResult(Constant.RequestCode_ActivityEditNickname,intent);
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
                MyUtils.show(ActivityEditNickname.this,"修改名字失败");
            }
        });
        return nickName;
    }
    public static class EditNameBeen{
        private String status;
        private String nickName;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }

   /* private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";//temp file
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap
    private static String TAG = "ActivityEditNickname";
    private static final int CHOOSE_BIG_PICTURE = 10;
    private static final int CHOOSE_SMALL_PICTURE = 11;
    private static final int TAKE_BIG_PICTURE = 12;
    private static final int CROP_BIG_PICTURE = 13;
    public void openPhotosForBig(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image*//*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, CHOOSE_BIG_PICTURE);
    }
    public void openPhotosForSmall(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image*//*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 100);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, CHOOSE_SMALL_PICTURE);
    }
    public void openCamra(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_BIG_PICTURE);//or TAKE_SMALL_PICTURE
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_BIG_PICTURE:
                Log.d(TAG, "CHOOSE_BIG_PICTURE: data = " + data);//it seems to be null
                if(imageUri != null){
                    Bitmap bitmap = decodeUriAsBitmap(imageUri);//decode bitmap
                    image_head.setImageBitmap(bitmap);
                }
                break;
            case CHOOSE_SMALL_PICTURE:
                if(data != null){
                    Bitmap bitmap = data.getParcelableExtra("data");
                    image_head.setImageBitmap(bitmap);
                }else{
                    Log.e(TAG, "CHOOSE_SMALL_PICTURE: data = " + data);
                }
                break;
            case TAKE_BIG_PICTURE:
                Log.d(TAG, "TAKE_BIG_PICTURE: data = " + data);//it seems to be null
                cropImageUri(imageUri, 800, 400, CROP_BIG_PICTURE);
                break;
            case CROP_BIG_PICTURE://from crop_big_picture
                Log.d(TAG, "CROP_BIG_PICTURE: data = " + data);//it seems to be null
                if(imageUri != null){
                    Bitmap bitmap = decodeUriAsBitmap(imageUri);
                    image_head.setImageBitmap(bitmap);
                }
        }
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
        intent.setDataAndType(uri, "image*//*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);
    }*/
}
