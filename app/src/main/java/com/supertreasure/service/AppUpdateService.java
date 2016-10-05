package com.supertreasure.service;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbViewUtil;
import com.squareup.okhttp.OkHttpClient;
import com.supertreasure.R;
import com.supertreasure.dialog.RoundProgressBarDialog;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.util.DownloadUtils;
import com.supertreasure.util.RoundProgressBar;
import com.supertreasure.util.ToastUtil;

import java.io.File;
import java.net.URLEncoder;

/**
 * Created by prj on 2016/5/5.
 */
public class AppUpdateService extends Service {

    private boolean showNotification=true;
    private boolean isForce;
    private NotificationManager notificationManager = null;
    private Notification notification = null;
    private PendingIntent pendingIntent = null;

    public ButtonBroadcastReceiver bReceiver;
    /** 通知栏按钮点击事件对应的ACTION */
    public final static String ACTION_BUTTON = "com.notifications.intent.action.ButtonClick";
    public final static String INTENT_BUTTONID_TAG = "ButtonId";
    public final static int BUTTON_DELETE = 1;

    private String downloadUrl;
    private String version;
    private File destDir = null;
    private File destFile = null;

    public static final String downloadPath = "/superxyb";
    public static final int notificationId = 111;
    private static final int DOWNLOAD_FAIL = -1;
    private static final int DOWNLOAD_SUCCESS = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_SUCCESS:
                    ToastUtil.showToast(TheApp.instance,"下载成功");
                    install(destFile);
                    notificationManager.cancel(notificationId);
                    break;
                case DOWNLOAD_FAIL:
                    ToastUtil.showToast(TheApp.instance,"下载失败");
                    notificationManager.cancel(notificationId);
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null){
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        initButtonReceiver();
        downloadUrl = intent.getStringExtra(Config.DOWNLOADURL);
        version = intent.getStringExtra(Config.VERSION);
        isForce = intent.getBooleanExtra(Config.ISFORCE,false);
        if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            destDir = new File(Environment.getExternalStorageDirectory().getPath() +"/"+ downloadPath);
            if(destDir.exists()){
                File destFile = new File(destDir.getPath() + "/"+version+"/" + URLEncoder.encode(downloadUrl));
                if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
                    install(destFile);
                    stopSelf();
                    return super.onStartCommand(intent, flags, startId);
                }
            }
        }else {
            ToastUtil.showToast(TheApp.instance,"开始下载");
            return super.onStartCommand(intent, flags, startId);
        }

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = new Notification();
        Intent completingIntent = new Intent();
        completingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        completingIntent.setClass(TheApp.instance, AppUpdateService.class);

        // 创建Notifcation对象，设置图标，提示文字,策略
        pendingIntent = PendingIntent.getActivity(TheApp.instance, R.string.app_name, completingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        notification.icon = R.drawable.ic_logo;
        notification.tickerText = "开始下载";
        notification.contentIntent = pendingIntent;
        
        notification.contentView = new RemoteViews(getPackageName(), R.layout.app_update_notification);
        notification.contentView.setProgressBar(R.id.progressbar, 100, 0, false);
        notification.contentView.setTextViewText(R.id.tv_title, "超级校园宝");
        notification.contentView.setTextViewText(R.id.tv_progress, "当前进度:0%");

        //点击的事件处理
        Intent buttonIntent = new Intent(ACTION_BUTTON);
        //删除按钮
        buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_DELETE);
        //这里加了广播，所及INTENT的必须用getBroadcast方法
        PendingIntent intent_delete = PendingIntent.getBroadcast(TheApp.instance, 1, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentView.setOnClickPendingIntent(R.id.bt_clear, intent_delete);

        notificationManager.cancel(notificationId);
        notificationManager.notify(notificationId, notification);
        new AppUpdateThread().start();

        return super.onStartCommand(intent, flags, startId);
    }

    /** 带按钮的通知栏点击广播接收 */
    public void initButtonReceiver(){
        bReceiver = new ButtonBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_BUTTON);
        registerReceiver(bReceiver, intentFilter);
    }

    /**
     * apk文件安装
     *
     * @param apkFile
     */
    public void install(File apkFile) {
        Uri uri = Uri.fromFile(apkFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class AppUpdateThread extends Thread {
        @Override
        public void run() {
            if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                if (destDir == null) {
                    destDir = new File(Environment.getExternalStorageDirectory().getPath() + downloadPath);
                }
                if (destDir.exists() || destDir.mkdirs()) {
                    destFile = new File(destDir.getPath() + "/" + URLEncoder.encode(downloadUrl));
                    if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
                        install(destFile);
                        notificationManager.cancel(notificationId);
                    } else {
                        try {
                            DownloadUtils.download(downloadUrl, destFile, false, downloadListener);
                            /*TheApp.mAbHttpUtil.post(downloadUrl, new AbStringHttpResponseListener() {
                                @Override
                                public void onSuccess(int statusCode, String content) {

                                }

                                @Override
                                public void onStart() {

                                }

                                @Override
                                public void onFinish() {
                                    notification.contentView.setViewVisibility(R.id.progressbar, View.GONE);
                                    notification.defaults = Notification.DEFAULT_SOUND;
                                    notification.contentIntent = pendingIntent;
                                    notification.contentView.setTextViewText(R.id.tv_title, "下载完成");
                                    notificationManager.notify(notificationId, notification);
                                    if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
                                        Message msg = mHandler.obtainMessage();
                                        msg.what = DOWNLOAD_SUCCESS;
                                        mHandler.sendMessage(msg);
                                    }
                                    notificationManager.cancel(notificationId);

                                }

                                @Override
                                public void onFailure(int statusCode, String content, Throwable error) {
                                    ToastUtil.showToast(TheApp.instance,"下载失败");
                                    notificationManager.cancel(notificationId);
                                }

                                @Override
                                public void onProgress(long bytesWritten, long totalSize) {
                                    if(showNotification) {
                                        notification.contentView.setProgressBar(R.id.progressbar, 100, (int) (bytesWritten / totalSize), false);
                                        notification.contentView.setTextViewText(R.id.tv_progress, "当前进度：" + (int) (bytesWritten / totalSize) + "%");
                                        notificationManager.notify(notificationId, notification);
                                    }
                                    super.onProgress(bytesWritten, totalSize);
                                }

                            });*/
                        } catch (Exception e) {
                            Message msg = mHandler.obtainMessage();
                            msg.what = DOWNLOAD_FAIL;
                            mHandler.sendMessage(msg);
                            e.printStackTrace();
                        }
                    }
                }
            }
            stopSelf();
        }
    }

    /**
     * 用于监听文件下载 
     */
    private DownloadUtils.DownloadListener downloadListener = new DownloadUtils.DownloadListener() {
        @Override
        public void downloading(int progress) {
            //System.out.println(progress);
            if(showNotification) {
                notification.contentView.setProgressBar(R.id.progressbar, 100, progress, false);
                notification.contentView.setTextViewText(R.id.tv_progress, "当前进度：" + progress + "%");
                notificationManager.notify(notificationId, notification);
            }
        }

        @Override
        public void downloaded() {
            notification.contentView.setViewVisibility(R.id.progressbar, View.GONE);
            notification.defaults = Notification.DEFAULT_SOUND;
            notification.contentIntent = pendingIntent;
            notification.contentView.setTextViewText(R.id.tv_title, "下载完成");
            notificationManager.notify(notificationId, notification);
            if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
                Message msg = mHandler.obtainMessage();
                msg.what = DOWNLOAD_SUCCESS;
                mHandler.sendMessage(msg);
            }
            notificationManager.cancel(notificationId);
        }
    };

    /**
     * 判断文件是否完整
     *
     * @param apkFilePath
     * @return
     */
    public boolean checkApkFile(String apkFilePath) {
        boolean result = false;
        try {
            PackageManager pManager = getPackageManager();
            PackageInfo pInfo = pManager.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
            if (pInfo == null) {
                result = false;
            } else {
                result = true;
            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    /**
     *	 广播监听按钮点击时间
     */
    public class ButtonBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(action.equals(ACTION_BUTTON)){
                //通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
                int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
                switch (buttonId) {
                    case BUTTON_DELETE:
                        showNotification = false;
                        notificationManager.cancel(notificationId);
                        unregisterReceiver(bReceiver);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if(bReceiver != null){
            unregisterReceiver(bReceiver);
        }
        super.onDestroy();
    }
}
