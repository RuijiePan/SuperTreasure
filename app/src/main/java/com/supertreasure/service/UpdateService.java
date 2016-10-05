package com.supertreasure.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.supertreasure.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by prj on 2016/5/19.
 */
/*public class UpdateService extends Service {
    // 安装包下载地址
    private String url = null;
    // 新的安装包本地存储路径
    private String filePath = null;
    // 通知管理器
    private NotificationManager updateNotificationManager = null;
    // 通知
    private Notification updateNotification = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 获取传值
        url = intent.getStringExtra("url");
        if (url != null) {
            String fileName = url.substring(url.lastIndexOf("/"));
            File uDir = new File(Environment.getExternalStorageDirectory() + "/test/download/");
            if (!uDir.exists() || !uDir.isDirectory()) {
                uDir.mkdirs();
            }
            // 本地目录存储路径
            filePath = uDir + fileName;
            // 使用AsyncTask执行下载请求
            new DownloadAsyncTask().execute(url);
        }
        return super.onStartCommand(intent, 0, 0);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}*/
