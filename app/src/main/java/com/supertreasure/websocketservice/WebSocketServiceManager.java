package com.supertreasure.websocketservice;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.supertreasure.bean.userLogin;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.websocket.MyWebSocketManager;

import java.util.List;

import me.itangqi.greendao.User;

/**
 * Created by yum on 2016/4/14.
 */
public class WebSocketServiceManager {

    public static void startService(final Context context){

        if(!isServiceRunning(context,WebSocketService.class.getName())) {
            Logger.i("websocket service", "第一次开启服务，现在开启");
            Intent serviceIntent = new Intent(context, WebSocketService.class);
            context.startService(serviceIntent);
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();

                        }
                        if (MyWebSocketManager.getInstance().isConneced())
                            Logger.i("websocket,", "长连接成功");
                        else {
                            Logger.i("websocket,", "长连接断开");
                        }

                    }
                }
            }).start();*/
        }
        else
            Logger.i("websocket service", "非第一次开启服务，不重新开启");
    }

    public static boolean isServiceRunning(Context mContext,String className) {//判断 某服务 是否在开启
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size()>0)) {
            return false;
        }
        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                //chatService = serviceList.get(i).service;
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

}
