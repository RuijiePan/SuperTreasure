package com.supertreasure.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
/*
class DownloadAsyncTask extends AsyncTask<String, Integer, Void> {

    private NotificationManager updateNotificationManager;
    private Notification updateNotification;
    //构造Notification
    @Override
    protected void onPreExecute() {

        updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        updateNotification = new Notification();
        updateNotification.icon = R.drawable.ic_launcher;
        updateNotification.contentView = new RemoteViews(getPackageName(), R.layout.view_notification_download);
        updateNotification.contentView.setProgressBar(R.id.notify_progress, 100, 0, false);
        updateNotification.contentView.setTextViewText(R.id.notify_title, "已下载:0%");
        updateNotification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);
        updateNotification.flags = Notification.FLAG_AUTO_CANCEL;
        updateNotificationManager.notify(101, updateNotification);
    }


    @Override
    protected Void doInBackground(String... params) {
        // 下载百分比
        int downPercentage = 0;
        // 上次缓存文件大小
        int cachedSize = 0;
        // 临时文件大小
        long tmpTotalSize = 0;
        // 待下载文件总大小
        int totalSize = 0;

        HttpURLConnection httpUrlConn = null;
        InputStream httpInputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            URL url = new URL(params[0]);
            httpUrlConn = (HttpURLConnection) url.openConnection();
            if (cachedSize > 0) {
                // 方便以后实现断点续传
                httpUrlConn.setRequestProperty("RANGE", "bytes=" + cachedSize + "-");
            }
            httpUrlConn.setConnectTimeout(500000);
            httpUrlConn.setReadTimeout(500000);
            // 获取文件总大小
            totalSize = httpUrlConn.getContentLength();
            if (httpUrlConn.getResponseCode() == 200) {
                httpInputStream = httpUrlConn.getInputStream();
                fileOutputStream = new FileOutputStream(new File(filePath));
                byte buffer[] = new byte[4096];
                int bufferSize = 0;
                while ((bufferSize = httpInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bufferSize);
                    tmpTotalSize += bufferSize;
                    int tmpDownPercentage = (int) (tmpTotalSize * 100 / totalSize);
                    if (tmpDownPercentage - downPercentage > 5) {
                        downPercentage += 5;
                        publishProgress(tmpDownPercentage);
                    }
                }
                // 下载结束
                publishProgress(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
            publishProgress(-1);
        } finally {
            try {
                if (httpUrlConn != null)
                    httpUrlConn.disconnect();
                if (httpInputStream != null)
                    httpInputStream.close();
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                publishProgress(-1);
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (values[0] != -1) {
            updateNotification.contentView.setTextViewText(R.id.notify_title, values[0] >= 100 ? "已完成下载" : "已下载:" + values[0] + "%");
            updateNotification.contentView.setProgressBar(R.id.notify_progress, 100, values[0] >= 100 ? 100 : values[0], false);
        } else {
            updateNotification.contentView.setTextViewText(R.id.notify_title, "下载失败!");
        }
        updateNotification.flags = Notification.FLAG_AUTO_CANCEL;
        updateNotificationManager.notify(101, updateNotification);
    }

    @Override
    protected void onPostExecute(Void result) {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
        startActivity(installIntent);
        stopSelf();
    }
}*/
