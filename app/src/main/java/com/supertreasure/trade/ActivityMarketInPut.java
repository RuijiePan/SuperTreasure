package com.supertreasure.trade;

import android.app.Dialog;
import android.content.Context;
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
import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;
import com.google.gson.Gson;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.squareup.okhttp.Response;
import com.supertreasure.R;
import com.supertreasure.bean.MarketPic;
import com.supertreasure.bean.Status;
import com.supertreasure.eventbus.RefreshMarketGridView;
import com.supertreasure.eventbus.RefleshDecorFragment;
import com.supertreasure.eventbus.RefleshEleProFragment;
import com.supertreasure.eventbus.RefleshOtherProFragment;
import com.supertreasure.home.ActivityPhoto;
import com.supertreasure.home.PhotoPreAdapter;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Bimp;
import com.supertreasure.util.BitmapUtil;
import com.supertreasure.util.Config;
import com.supertreasure.util.FileUtils;
import com.supertreasure.util.GetHeightResUrl;
import com.supertreasure.util.HttpUtil;
import com.supertreasure.util.RoundProgressBar;
import com.supertreasure.util.ToastUtil;
import com.supertreasure.util.ValidationUtil;
import com.xw.repo.xedittext.XEditText;

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
public class ActivityMarketInPut extends AbActivity implements TextWatcher {

    private final int IMAGE_PICKER = 100;
    private GridView noScrollgridView;
    private PhotoPreAdapter adapter;
    private ArrayList<ImageItem> images;
    private List<String> list = new ArrayList<String>();
    private List<String> paths = new ArrayList<>();
    private Dialog dialog;
    private Dialog typeDialog;
    private RoundProgressBar progressBar;
    private Dialog roundProgressBarDialog;
    //private RunningManDialog runningManDialog;
    private Context context;
    private Button bt_title_right;
    private Button bt_delete;
    private LinearLayout ll_title_left;
    private LinearLayout ll_title_right;
    private LinearLayout ll_type;
    private XEditText et_introduction;
    private EditText et_price;
    private EditText et_old_price;
    private XEditText et_linker;
    private EditText et_ele_phone;
    private TextView tv_type;
    private TextView tv_title;
    private Boolean isIntroductuonNotEmpty;
    private Boolean isOldPriceNotEmpty;
    private Boolean isPriceNotEmpty;
    private Boolean isLinkerNotEmpty;
    private Boolean isPhoneNotEmpty;
    private Boolean isTypeNotEmpty;
    private Boolean isPhotoNotEmpty;
    private Boolean isPhoneNumber;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_market_put);
        EventBus.getDefault().register(this);

        SwipeBackHelper.onCreate(this);
        /*SwipeBackHelper.getCurrentPage(this)//获取当前页面
                .setSwipeBackEnable(true)//设置是否可滑动
                .setSwipeEdge(200)//可滑动的范围。px。200表示为左边200px的屏幕
                .setSwipeEdgePercent(0.2f)//可滑动的范围。百分比。0.2表示为左边20%的屏幕
                .setSwipeSensitivity(0.5f)//对横向滑动手势的敏感程度。0为迟钝 1为敏感
                //.setScrimColor(Color.WHITE)底层阴影颜色
                .setClosePercent(0.5f)//触发关闭Activity百分比
                .setSwipeRelateEnable(true)//是否与下一级activity联动(微信效果)。默认关
                .setSwipeRelateOffset(200)//activity联动时的偏移量。默认500px。
                .addListener(new SwipeListener() {//滑动监听

                    @Override
                    public void onScroll(float percent, int px) {//滑动的百分比与距离
                    }

                    @Override
                    public void onEdgeTouch() {//当开始滑动
                    }

                    @Override
                    public void onScrollToClose() {//当滑动关闭

                    }
                });*/

        initView();
        setListener();
    }

    @Override
    protected void onDestroy() {
        adapter.clear();
        EventBus.getDefault().unregister(this);
        SwipeBackHelper.onDestroy(this);
        super.onDestroy();
    }

    @Subscribe
    public void onEventMainThread(RefreshMarketGridView event) {
        //adapter.clear();
        paths = event.getPathList();
        adapter.update(paths);
    }

    private void initView() {
        AbViewUtil.scaleContentView((LinearLayout) findViewById(R.id.root));
        context = this;
        noScrollgridView = (GridView) findViewById(R.id.NoScrollgridview);
        tv_title = (TextView) findViewById(R.id.tv_title_name);
        tv_title.setText("发布宝贝");
        tv_type = (TextView) findViewById(R.id.tv_type);
        ll_type = (LinearLayout) findViewById(R.id.ll_type);
        ll_title_left = (LinearLayout) findViewById(R.id.ll_title_left);
        ll_title_right = (LinearLayout) findViewById(R.id.ll_title_right);
        bt_title_right = (Button) findViewById(R.id.bt_title_right);
        bt_delete = (Button) findViewById(R.id.bt_delete);
        et_introduction = (XEditText) findViewById(R.id.et_introduction);
        et_introduction.setMaxLength(140);
        et_price = (EditText) findViewById(R.id.et_price);
        et_old_price = (EditText) findViewById(R.id.et_old_price);
        et_linker = (XEditText) findViewById(R.id.et_linker);
        et_linker.setMaxLength(9);
        et_ele_phone = (EditText) findViewById(R.id.et_phone);
        noScrollgridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new PhotoPreAdapter(this);
        noScrollgridView.setAdapter(adapter);
        adapter.update(paths);

    }

    private void setListener() {

        et_introduction.addTextChangedListener(this);
        et_old_price.addTextChangedListener(this);
        et_price.addTextChangedListener(this);
        et_linker.addTextChangedListener(this);
        et_ele_phone.addTextChangedListener(this);
        tv_type.addTextChangedListener(this);

        noScrollgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == paths.size()) {
                    ImagePicker.getInstance().setMultiMode(true);
                    ImagePicker.getInstance().setCrop(false);
                    Intent intent = new Intent(TheApp.instance,ImageGridActivity.class);
                    startActivityForResult(intent,IMAGE_PICKER);
                    adapter.clear();
                } else {
                    Intent intent = new Intent(TheApp.instance, ActivityPhoto.class);
                    intent.putExtra("ID", position);
                    String[] urls = new String[paths.size()];
                    for (int i=0;i<paths.size();i++){
                        urls[i] = paths.get(i);
                    }
                    intent.putExtra("urls", urls);
                    intent.putExtra(Config.Type,Config.Market);
                    startActivity(intent);
                }
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
                if (!isIntroductuonNotEmpty) {
                    ToastUtil.showToast(context, "产品简介不能为空哦");
                    return;
                } else if (!isPriceNotEmpty) {
                    ToastUtil.showToast(context, "价格还没填写呢~");
                    return;
                } else if (!isOldPriceNotEmpty) {
                    ToastUtil.showToast(context, "原价不能为空哦");
                    return;
                } else if (!isLinkerNotEmpty) {
                    ToastUtil.showToast(context, "请填写联系人");
                    return;
                } else if (!isPhoneNotEmpty) {
                    ToastUtil.showToast(context, "请填写联系方式");
                    return;
                } else if (!isTypeNotEmpty) {
                    ToastUtil.showToast(context, "请输入产品类型");
                    return;
                } else if (!isPhotoNotEmpty) {
                    ToastUtil.showToast(context, "请添加至少一张产品图片");
                    return;
                } else if (!isPhoneNumber) {
                    ToastUtil.showToast(context, "请输入正确的联系方式");
                    return;
                }
                if (paths.size() == 0) {
                    ToastUtil.showToast(context, "请上传至少一张图片");
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

        ll_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTypeDialog();
            }
        });
    }

    private void showTypeDialog(){
        if(typeDialog==null){
            View view = View.inflate(TheApp.instance, R.layout.dialog_type_choose, null);
            AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.root));

            final Button bt_life = (Button) view.findViewById(R.id.bt_life);
            final Button bt_ele_product = (Button) view.findViewById(R.id.bt_ele_product);
            final Button bt_other = (Button) view.findViewById(R.id.bt_other);
            Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

            typeDialog = new Dialog(this, R.style.photoChooseDialog);
            typeDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            Window window = typeDialog.getWindow();
            // 设置显示动画
            window.setWindowAnimations(R.style.main_menu_animstyle);
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = 0;
            wl.y = TheApp.screenHeight;
            // 以下这两句是为了保证按钮可以水平满屏
            wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
            wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            // 设置显示位置
            typeDialog.onWindowAttributesChanged(wl);
            // 设置点击外围解散
            typeDialog.setCanceledOnTouchOutside(true);
            typeDialog.show();

            bt_life.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_type.setText(bt_life.getText().toString());
                    typeDialog.cancel();
                }
            });
            bt_ele_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_type.setText(bt_ele_product.getText().toString());
                    typeDialog.cancel();
                }
            });
            bt_other.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_type.setText(bt_other.getText().toString());
                    typeDialog.cancel();
                }
            });
            bt_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeDialog.cancel();
                }
            });
        }else {
            typeDialog.show();
        }
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


    private void uploadPic() {

            String url = Config.getAPI(Config.APIUploadGoodImg);
            HttpUtil.HttpUploadRequest request = new HttpUtil.HttpUploadRequest(url);
            request.addHeaderParam(Config.UserName, AbSharedUtil.getString(ActivityMarketInPut.this, Config.UserName));
            request.addHeaderParam(Config.Token, AbSharedUtil.getString(ActivityMarketInPut.this, Config.Token));
            ArrayList<File> files = new ArrayList<>();
            for (int i = 0; i < paths.size(); i++) {
                if(!GetHeightResUrl.isGIF(images.get(i).path))
                    files.add(new File(list.get(i)));
                else
                    files.add(new File(images.get(i).path));
            }

            request.addUploadFile("files", files, new HttpUtil.UIchangeListener() {
                @Override
                public void progressUpdate(long bytesWrite, long contentLength, boolean done,int position,int all) {
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

                    Gson gson = new Gson();
                    MarketPic marketPic = gson.fromJson(body, MarketPic.class);
                    roundProgressBarDialog.cancel();
                    if(marketPic.getStatus().equals(Config.SUCCESS))
                        GoodPutIn(marketPic.getPaths(), marketPic.getWidth(), marketPic.getHeight());
                }

                @Override
                public void onFailure(Response response) {
                    roundProgressBarDialog.cancel();
                }
            };
            HttpUtil.uploadFiles(request, callBack);

    }

    private void GoodPutIn(String paths,int width,int height) {

        String url = Config.getAPI(Config.APIAddGood);
        HttpUtil.HttpRequest request = new HttpUtil.HttpRequest(url);
        request.addParam(Config.UserName, AbSharedUtil.getString(ActivityMarketInPut.this, Config.UserName));
        request.addParam(Config.Token, AbSharedUtil.getString(ActivityMarketInPut.this, Config.Token));
        request.addParam(Config.Paths,paths);
        request.addParam(Config.Introduction, et_introduction.getText().toString());
        request.addParam(Config.Linker, et_linker.getText().toString());
        request.addParam(Config.Price, et_price.getText().toString());
        request.addParam(Config.TelePhone, et_ele_phone.getText().toString());
        request.addParam(Config.Cost, et_old_price.getText().toString());
        request.addParam(Config.Width, width + "");
        request.addParam(Config.Height, height + "");
        if(tv_type.getText().toString().equals("生活用品")){
            request.addParam(Config.Type,Config.Life);
        }else if(tv_type.getText().toString().equals("电子产品")){
            request.addParam(Config.Type,Config.Elect);
        }else if(tv_type.getText().toString().equals("其他")){
            request.addParam(Config.Type,Config.Other);
        }
        HttpUtil.CallBack callBack = new HttpUtil.CallBack() {
            @Override
            public void onSuccess(String body) {
                Gson gson = new Gson();
                Status status = gson.fromJson(body,Status.class);
                if(status.getStatus().equals(Config.SUCCESS)){
                    roundProgressBarDialog.cancel();
                    FileUtils.deleteDir();
                    finish();
                    new CountDownTimer(Config.EVENTBUS_REFLESH_TIME,Config.EVENTBUS_REFLESH_TIME){

                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            String type = tv_type.getText().toString();

                            switch (type) {
                                case "生活用品":
                                    EventBus.getDefault().post(new RefleshDecorFragment());
                                    break;
                                case "电子产品":
                                    EventBus.getDefault().post(new RefleshEleProFragment() );
                                    break;
                                case "其他":
                                    EventBus.getDefault().post(new RefleshOtherProFragment() );
                                    break;
                            }
                        }
                    }.start();


                }

            }

            @Override
            public void onFailure(Response response) {
                roundProgressBarDialog.cancel();
                //  FileUtils.deleteDir();
            }

        };
        HttpUtil.httpPostGetData(request, callBack);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        isIntroductuonNotEmpty = et_introduction.getText().length() != 0;
        isOldPriceNotEmpty = et_old_price.getText().length()!=0;
        isPriceNotEmpty = et_price.getText().length()!=0;
        isLinkerNotEmpty = et_linker.getText().length()!=0;
        isPhoneNotEmpty = et_ele_phone.getText().length()!=0;
        isTypeNotEmpty = tv_type.getText().length()!=0;
        isPhotoNotEmpty = adapter.getCount()!=0;
        isPhoneNumber = ValidationUtil.isMobile(et_ele_phone.getText().toString());

        bt_delete.setText(140 - et_introduction.getText().length() + "");
        if(isIntroductuonNotEmpty&&isOldPriceNotEmpty&&isPriceNotEmpty&&isLinkerNotEmpty&&
                isPhoneNotEmpty&&isTypeNotEmpty&&isPhotoNotEmpty&&isPhoneNumber){
            Resources resources = this.getResources();
            ColorStateList csl = resources.getColorStateList(R.color.blue_btn_bg_color);
            bt_title_right.setTextColor(csl);
        }else {
            bt_title_right.setTextColor(Color.WHITE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /*public class GridAdapter extends BaseAdapter{

        private LayoutInflater inflater;// 视图容器
        private int selectedPosition = -1;// 选中的位置
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
        }

        @Override
        public int getCount() {
            return (Bimp.bmp.size()+1);
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int coord = position;
            ViewHolder holder = null;

            if(convertView==null){
                convertView = inflater.inflate(R.layout.mood_item_gridview,parent,false);
                AbViewUtil.scaleContentView((LinearLayout) convertView.findViewById(R.id.root));
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.album_image);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            if(position == Bimp.drr.size()){
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(),R.drawable.icon_addpic_unfocused));
                if(position==9){
                    holder.image.setVisibility(View.GONE);
                }
            }else {
                Glide.with(TheApp.instance)
                        //配置上下文
                        .load(Bimp.drr.get(position))
                        .thumbnail(0.1f)
                        //设置图片路径
                        .placeholder(com.lzy.imagepicker.R.mipmap.default_image)
                        .error(com.lzy.imagepicker.R.mipmap.default_image)           //设置错误图片
                        .diskCacheStrategy(DiskCacheStrategy.NONE)//缓存全尺寸
                        .into(holder.image);
            }

            return convertView;
        }

        public class ViewHolder{
                public ImageView image;
        }

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public GridAdapter(Context context){
            inflater = LayoutInflater.from(context);
        }

        public void update(){
            loading();
        }

        public void loading(){
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
                                Bimp.max++;
                                Bitmap bm = Bimp.revitionImageSize(path);
                                //Bimp.bmp.add(bm);
                                String newStr = path.substring(path.lastIndexOf("/")+1,path.lastIndexOf("."));
                                FileUtils.saveBitmap(bm,""+newStr);

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
        }
    }

    public String getString(String s){
        String path = null;
        if(s==null)
            return "";
        for (int i=s.length()-1;i>0;i++){
             s.charAt(i);
        }
        return path;
    }

    @Override
    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    *//*private void showDialog() {

        View view = View.inflate(TheApp.instance, R.layout.dialog_photo_choose, null);
        AbViewUtil.scaleContentView((LinearLayout) view.findViewById(R.id.root));

        Button openCamera = (Button) view.findViewById(R.id.openCamera);
        Button openPhones = (Button) view.findViewById(R.id.openPhones);
        Button cancel = (Button) view.findViewById(R.id.cancel);

        dialog = new Dialog(this, R.style.photoChooseDialog);
        dialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
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
                takePhoto();
                dialog.cancel();
            }
        });
        openPhones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageGridAdapter.canSelectNum = 9;
                *//**//*Intent intent = new Intent(ActivityMarketInPut.this,ActivityChoosePic.class);
                startActivity(intent);*//**//*
                GalleryChoosePic();
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

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    public void takePhoto(){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
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

    /*public void GalleryChoosePic(){

        FunctionConfig.Builder functionConfigBuilder = new FunctionConfig.Builder();
        cn.finalteam.galleryfinal.ImageLoader imageLoader = new FrescoImageLoader(ActivityMarketInPut.this);
        functionConfigBuilder.setMutiSelectMaxSize(1)
                             .setEnableCamera(true)
                             .setRotateReplaceSource(true)
                             .setEnableEdit(false)
                             .setEnableCrop(false)
                             .setEnablePreview(true)
                             .setSelected((ArrayList<String>) Bimp.drr);//添加过滤集合
        final FunctionConfig functionConfig = functionConfigBuilder.build();
        CoreConfig coreConfig = new CoreConfig.Builder(ActivityMarketInPut.this, imageLoader, ThemeConfig.DARK)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(null)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY,functionConfig, mOnHanlderResultCallback);
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                Bimp.max = 0;
                Bimp.drr.clear();
                Bimp.bmp.clear();
                for (int i=0;i<resultList.size();i++)
                    Bimp.drr.add(resultList.get(i).getPhotoPath());
                adapter.update();
                GalleryFinal.cleanCacheFile();
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            ToastUtil.showToast(ActivityMarketInPut.this,"获取图片路径失败");
        }
    };*/
}
