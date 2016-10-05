package com.supertreasure.websocketservice;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


import com.ab.util.AbSharedUtil;
import com.orhanobut.logger.Logger;
import com.supertreasure.util.Config;
import com.supertreasure.util.ToastUtil;
import com.supertreasure.websocket.MyWebSocketClient;
import com.supertreasure.websocket.MyWebSocketManager;

import org.java_websocket.drafts.Draft_17;

import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * 聊天服务.
 *
 */
public class WebSocketService extends Service {
	private WebSocketService service;
	private boolean isRunning =true;
	private ConnectivityManager connectivityManager;
	private Context context;
	private MyWebSocketManager myWebSocketManager;
	private NetworkInfo info;
	@Override
	public void onCreate() {
		Logger.d("websocket","WebSocketService.onCreate()");
		myWebSocketManager = MyWebSocketManager.getInstance();
		service = this;
		context = this;

		initChatManager();

		IntentFilter mFilter = new IntentFilter();
		mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mReceiver, mFilter);
	}

	private void initChatManager() {

		/*if (!myWebSocketManager.isConnecting()) {
		}*/
		//myWebSocketManager.connectServer();
		boolean isAutoLogin = AbSharedUtil.getBoolean(context,Config.IsAutoLogin,false);
		if (isAutoLogin) {
			Logger.i("websocket","检测连接");
			myWebSocketManager.reConnectServer();
		}
		else {//点击登录 登录进来的
			Logger.i("websocket","检测连接");
			myWebSocketManager.connectServer();
			AbSharedUtil.putBoolean(context,Config.IsAutoLogin,true);
		}


	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onDestroy() {
		unregisterReceiver(mReceiver);
		Logger.d("WebSocketService.onDestroy()");
		super.onDestroy();
	}
	public void stopService(){
		stopService();
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				Logger.d("websocket", "网络状态已经改变");
				connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
				info = connectivityManager.getActiveNetworkInfo();
				if(info != null && info.isAvailable()) {
					String name = info.getTypeName();
					Logger.d("websocket", "当前网络名称：" + name);
					//doSomething()
					MyWebSocketManager.getInstance().reConnectServer();

					//handler.sendEmptyMessage(0);

				} else {
					Logger.d("websocket", "没有可用网络");
					//doSomething()
				}
			}
}
	};
/*
	public static Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			MyWebSocketManager.getInstance().reConnectServer();
		}
	};*/
}
