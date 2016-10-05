package com.supertreasure.home;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.activity.AbActivity;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbViewUtil;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.orhanobut.logger.Logger;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.squareup.okhttp.Response;
import com.supertreasure.R;
import com.supertreasure.bean.MarketPic;
import com.supertreasure.bean.Status;
import com.supertreasure.eventbus.RefleshMineActivity;
import com.supertreasure.eventbus.RefleshSchoolInMoodFragment;
import com.supertreasure.eventbus.RefleshSchoolOutMoodFragment;
import com.supertreasure.eventbus.RefreshMoodGridView;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Bimp;
import com.supertreasure.util.BitmapUtil;
import com.supertreasure.util.Config;
import com.supertreasure.util.FileUtils;
import com.supertreasure.util.GetHeightResUrl;
import com.supertreasure.util.HttpUtil;
import com.supertreasure.util.PrjBase64;
import com.supertreasure.util.RoundProgressBar;
import com.supertreasure.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/2/3.
 */
public class ActivityMoodInPut extends AbActivity implements TextWatcher{

    private RoundProgressBar progressBar;
    private ArrayList<ImageItem> images;
    private List<String> list = new ArrayList<String>();
    private List<String> paths = new ArrayList<>();
    private final int IMAGE_PICKER = 100;
    private boolean isInMoodInPut;
    private GridView noScrollgridView;
    private PhotoPreAdapter adapter;
    private Dialog roundProgressBarDialog;
    private Button bt_title_left;
    private View ll_title_left;
    private Button bt_title_right;//发布按钮
    private TextView tv_title;
    private EmojiconEditText et_mood;
    private String mood;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_mood_put);
        SwipeBackHelper.onCreate(this);
        initView();
        setListener();
    }

    @Override
    protected void onDestroy() {
        adapter.clear();
        //EventBus.getDefault().post(new RefreshMoodGridView(new ArrayList<String>()));
        EventBus.getDefault().unregister(this);
        SwipeBackHelper.onDestroy(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEventMainThread(RefreshMoodGridView event) {
        paths = event.getPathList();
        adapter.update(paths);
    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));
        noScrollgridView = (GridView) findViewById(R.id.NoScrollgridview);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        bt_title_left = (Button) findViewById(R.id.bt_title_left);
        ll_title_left =  findViewById(R.id.ll_title_left);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        et_mood = (EmojiconEditText) findViewById(R.id.et_mood);
        tv_title.setText("发布说说");
        bt_title_right.setText("发表");
        bt_title_right.setVisibility(View.VISIBLE);
        noScrollgridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new PhotoPreAdapter(this);
        noScrollgridView.setAdapter(adapter);
        adapter.update(paths);

        isInMoodInPut = getIntent().getBooleanExtra(Config.IsInMoodInput,true);
    }

    private void setListener() {
        noScrollgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == paths.size()) {
                    ImagePicker.getInstance().setMultiMode(true);
                    ImagePicker.getInstance().setCrop(false);
                    Intent intent = new Intent(TheApp.instance, ImageGridActivity.class);
                    startActivityForResult(intent, IMAGE_PICKER);
                    adapter.clear();
                } else {
                    Intent intent = new Intent(TheApp.instance, ActivityPhoto.class);
                    intent.putExtra("ID", position);
                    String[] urls = new String[paths.size()];
                    for (int i=0;i<paths.size();i++){
                        urls[i] = paths.get(i);
                    }
                    intent.putExtra("urls", urls);
                    intent.putExtra(Config.Type,Config.Mood);
                    startActivity(intent);
                }
            }
        });

        bt_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bt_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_mood.getText().toString().length() == 0) {
                    ToastUtil.showToast(TheApp.instance, "说说不能为空哦！");
                    return;
                }else if (paths.size()<=0){
                    ToastUtil.showToast(TheApp.instance, "至少上传1张照片哦！");
                    return;
                }
                list.clear();
                for (int i = 0; i < paths.size(); i++) {
                    String Str = paths.get(i).substring(
                            paths.get(i).lastIndexOf("/") + 1,
                            paths.get(i).lastIndexOf("."));
                    list.add(FileUtils.SDPATH + Str + ".JPEG");
                }

                uploadPic();
                showUploadDialog();
                // 高清的压缩图片全部就在  list 路径里面了
                // 高清的压缩过的 bmp 对象  都在 Bimp.bmp里面
                // 完成上传服务器后 .........
            }
        });

        et_mood.addTextChangedListener(this);

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
    }


    private void  uploadPic() {

            String url = Config.getAPI(Config.APIUploadMoodPicture);
            HttpUtil.HttpUploadRequest request = new HttpUtil.HttpUploadRequest(url);
            request.addHeaderParam(Config.UserName, AbSharedUtil.getString(TheApp.instance, Config.UserName));
            request.addHeaderParam(Config.Token, AbSharedUtil.getString(TheApp.instance, Config.Token));
            ArrayList<File> files = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if(!GetHeightResUrl.isGIF(images.get(i).path))
                    files.add(new File(list.get(i)));
                else
                    files.add(new File(images.get(i).path));
            }
            request.addUploadFile("files", files, new HttpUtil.UIchangeListener() {
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
                    Logger.i("onSuccess content "+body);
                    Gson gson = new Gson();
                    MarketPic marketPic = gson.fromJson(body, MarketPic.class);
                    if (marketPic.getPaths()!=null)
                        MoodPutIn(marketPic.getPaths());
                    else {
                      //  ToastUtil.showToast(TheApp.instance, "null");
                    }
                }

                @Override
                public void onFailure(Response response) {
                    //Log.w("hahashibai", response + "");
                    roundProgressBarDialog.cancel();
                }
            };
            HttpUtil.uploadFiles(request, callBack);

    }

    private synchronized void MoodPutIn(String paths) {

        String url ;
        if(isInMoodInPut)
            url = Config.getAPI(Config.APIMoodPutIn);
        else
            url = Config.getAPI(Config.APIMoodPutOut);
        HttpUtil.HttpRequest request = new HttpUtil.HttpRequest(url);
        request.addParam(Config.UserName, AbSharedUtil.getString(TheApp.instance, Config.UserName));
        request.addParam(Config.Token, AbSharedUtil.getString(TheApp.instance, Config.Token));
        request.addParam("content", PrjBase64.encode(et_mood.getText().toString()));
        request.addParam("paths", paths);
        Log.w("haha1",PrjBase64.encode(et_mood.getText().toString()));
        /*Log.w("hahaurl", Config.getAPI(Config.APIMoodPutIn));
        Log.w("haha", paths);
        Log.w("haha1",et_mood.getText().toString());
        Log.w("haha2", AbSharedUtil.getString(TheApp.instance, Config.UserName));
        Log.w("haha3", AbSharedUtil.getString(TheApp.instance, Config.Token));*/
        HttpUtil.CallBack callBack = new HttpUtil.CallBack() {
            @Override
            public void onSuccess(String body) {
                //Logger.i("onSuccess content "+body);

                Gson gson = new Gson();
                Status status = gson.fromJson(body,Status.class);
                roundProgressBarDialog.cancel();
                if(status.getStatus().equals(Config.SUCCESS)){
                    //runningManDialog.cancel();
                    //上传说说成功,
                    EventBus.getDefault().post(RefleshMineActivity.getInstance());
                    ToastUtil.showToast(TheApp.instance,"元宝币+"+Config.sendMoodmoney);
                    FileUtils.deleteDir();
                    finish();
                    new CountDownTimer(Config.EVENTBUS_REFLESH_TIME,Config.EVENTBUS_REFLESH_TIME){

                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            if (isInMoodInPut)
                                EventBus.getDefault().post(new RefleshSchoolInMoodFragment());
                            else
                                EventBus.getDefault().post(new RefleshSchoolOutMoodFragment());

                        }
                    }.start();


                }else {
                    ToastUtil.showToast(TheApp.instance,"服务器连接失败");
                    roundProgressBarDialog.cancel();
                }
            }

            @Override
            public void onFailure(Response response) {
                  roundProgressBarDialog.cancel();
            }

        };
        HttpUtil.httpPostGetData(request,callBack);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mood = et_mood.getText().toString();
        Resources resources = this.getResources();
        ColorStateList csl = resources.getColorStateList(R.color.blue_btn_bg_color);
        if(mood.length()!=0){
            bt_title_right.setTextColor(csl);
        }else {
            bt_title_right.setTextColor(Color.WHITE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /*@Override
    protected void onRestart() {
        adapter.update(paths);
        super.onRestart();
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IMAGE_PICKER:
                if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                    if (data != null && requestCode == IMAGE_PICKER) {
                        images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                        for (int i = 0; i < images.size(); i++)
                            paths.add(images.get(i).path);
                        adapter.update(paths);
                        try {
                            createSmallPic(paths);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(TheApp.instance, "没有数据", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    private void createSmallPic(List<String> paths) throws IOException {
        for (int i=0;i<paths.size();i++)
        {
            Bitmap bm = revitionImageSize(paths.get(i));
            int degree = BitmapUtil.getBitmapDegree(paths.get(i));
            Bitmap newbitmap = BitmapUtil.rotateBitmapByDegree(bm, degree);
            String newStr = paths.get(i).substring(paths.get(i).lastIndexOf("/")+1,paths.get(i).lastIndexOf("."));
            FileUtils.saveBitmap(newbitmap,""+newStr);
        }
    }

    public Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }
}
