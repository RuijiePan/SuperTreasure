package com.supertreasure.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.ab.global.AbAppConfig;
import com.ab.http.AbHttpUtil;
import com.ab.image.AbImageLoader;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.supertreasure.R;
import com.supertreasure.greenDaoUtils.GreenUtils;
import com.supertreasure.util.ImageLoader.FrescoImageLoader;
import com.supertreasure.util.ImageLoader.GlideImageLoader;
import com.supertreasure.websocket.MyWebSocketManager;

import de.greenrobot.dao.query.QueryBuilder;


/**
 *
 *
 * @author Administrator
 *
 */
public class TheApp  extends Application{
	public static int screenWidth;//屏幕宽度
	public static int screenHeight; //屏幕高度

	public static AbImageLoader mAbImageLoader;
	public static AbHttpUtil mAbHttpUtil = null;

	public static TheApp instance;

	// 用于存放倒计时时间
	public static Map<String, Long> map;
	// 用于存放手机号码
	public static Map<String, String> mapPhone;
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		getScreen();//获取屏幕宽高值
		setTheSize(); //  设置屏幕尺寸
		//LeakCanary.install(this);
		initImagePicker();
		initFresco();//初始化Facebook图片加载框架Fresco
		initImageLoader();//初始化ImageLodaer
		initHttpUtil();//初始化Http工具类
		QueryBuilder.LOG_SQL =  true   ;
		QueryBuilder.LOG_VALUES  = true   ;
		//----------------------------------
		GreenUtils.init(instance);
		MyWebSocketManager.init(instance);
		//----------------------------------
		}

	private void initImagePicker() {
		ImagePicker imagePicker = ImagePicker.getInstance();
		imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
		imagePicker.setShowCamera(true);  //显示拍照按钮
		imagePicker.setCrop(true);        //允许裁剪（单选才有效）
		imagePicker.setSaveRectangle(true); //是否按矩形区域保存
		imagePicker.setSelectLimit(9);    //选中数量限制
		imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
		int length = screenHeight>screenWidth?screenWidth:screenHeight;
		imagePicker.setFocusWidth(length/2);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
		imagePicker.setFocusHeight(length/2);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）*/
		imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
		imagePicker.setOutPutY(1000);
	}

	/*private void initGalleryFinal() {

		ThemeConfig theme = ThemeConfig.DARK;

		FunctionConfig functionConfig = new FunctionConfig.Builder()
				.setEnableRotate(true)
				.setEnableCamera(true)
				.setEnableEdit(false)
				.setEnableCrop(false)
				.setEnableRotate(true)
				.setCropSquare(false)
				.setEnablePreview(true)
				.setRotateReplaceSource(true)
				.setCropReplaceSource(false)
				.build();

		ImageLoader imageloader = new FrescoImageLoader(instance);
		CoreConfig coreConfig = new CoreConfig.Builder(instance, imageloader, theme)
				.setNoAnimcation(true)
				.setFunctionConfig(functionConfig)
				.build();
		GalleryFinal.init(coreConfig);
	}*/

	private void initFresco() {
		DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(instance)
				.setBaseDirectoryPath(new File(Environment.getExternalStorageDirectory().getAbsoluteFile(),"SuperTreasure"))
				.setBaseDirectoryName("SuperTreasure")
				.setMaxCacheSize(getMaxCacheSize())
				.build();
		ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(this)
				.setMainDiskCacheConfig(diskCacheConfig)
				.setDownsampleEnabled(true)
				.build();
		Fresco.initialize(instance,imagePipelineConfig);
	}

	/*private void initMap() {

		String longitude=AbSharedUtil.getString(this, "longitude");//经度
		String latitude=AbSharedUtil.getString(this, "latitude");//纬度
		if(longitude==null||latitude==null){//设置默认经纬度
			AbSharedUtil.putString(this, "longitude", "23.1923870384");//广东省广州市白云区机场路1257号
			AbSharedUtil.putString(this, "latitude", "113.2615202271");
		}
	}*/

	private void initHttpUtil() {
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(10000);
	}

	private void initImageLoader() {
		mAbImageLoader=AbImageLoader.getInstance(this);
		mAbImageLoader.setErrorImage(R.drawable.icon_failure);
		mAbImageLoader.setEmptyImage(R.drawable.image_placeholder);
	}

	private void getScreen() {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		if(screenWidth==0||screenHeight==0){
			screenWidth=720;
			screenHeight=1080;
		}
	}

	private void setTheSize() {
		AbAppConfig.UI_WIDTH = 720;
		AbAppConfig.UI_HEIGHT = 1280;
	}
//-------------------------------------------------------------
	public synchronized static TheApp getInstance() {
		if (null == instance) {
			instance = new TheApp();
		}
		return instance;
	}


	private List<Activity> activityList = new ArrayList<Activity>();
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}
	public void exit() {
		try {
			for (Activity activity : activityList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
	//-------------------------------------------------------

	private int getMaxCacheSize() {
		final int maxMemory = (int) Math.min(Runtime.getRuntime().maxMemory(), Integer.MAX_VALUE);
		if (maxMemory < 16 * ByteConstants.MB) {
			return ByteConstants.MB;
		} else if (maxMemory < 32 * ByteConstants.MB) {
			return 2 * ByteConstants.MB;
		} else {
			return 4 * ByteConstants.MB;
		}
	}
}
