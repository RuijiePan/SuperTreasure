package com.supertreasure.websocket;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.supertreasure.bean.MoodMessageBean;
import com.supertreasure.eventbus.NotificationEvent;
import com.supertreasure.greenDaoUtils.GreenUtils;
import com.supertreasure.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.zip.GZIPOutputStream;

public class MyWebSocketClient extends org.java_websocket.client.WebSocketClient {
	private Context context;
/*	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String message = msg.getData().getString("content");
			switch (msg.what){
				case 1:
					ToastUtil.showToast(context, message);
					break;
			}
		}
	};*/
	public MyWebSocketClient(Context context, URI serverUri, Draft draft) {
		super(serverUri, draft);
		this.context = context;
	}

	public MyWebSocketClient(Context context, URI serverURI) {
		super(serverURI);
		this.context = context;

	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("opened connection");
		// if you plan to refuse connection based on ip or httpfields overload:
		// onWebsocketHandshakeReceivedAsClient
	}

	@Override
	public void onMessage(String message) {
		/*System.out.println("received: " + message);
		final String m = message;
		Message mess = new Message();
		Bundle bundle=new Bundle();
		bundle.putString("content", message);
		mess.setData(bundle);
		mess.what=1;
		handler.sendMessage(mess);*/
		Logger.i("websocket", "onMessage()接收到:" + message);
		MoodMessageBean moodMessageBean = new Gson().fromJson(message, MoodMessageBean.class);
		GreenUtils.getInstance().insertOrReplaceMoodMessage(moodMessageBean.getBody(),false);
		Logger.i("websocket", "MoodMessageBean:" + moodMessageBean.toString());
		EventBus.getDefault().post(new NotificationEvent());
	}

	@Override
	public void onFragment(Framedata fragment) {
		System.out.println("received fragment: " + new String(fragment.getPayloadData().array()));
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		// The codecodes are documented in class
		// org.java_websocket.framing.CloseFrame
		System.out.println("Connection closed by " + (remote ? "remote peer" : "us"));
	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();
		// if the error is fatal then onClose will be called additionally
	}

}
