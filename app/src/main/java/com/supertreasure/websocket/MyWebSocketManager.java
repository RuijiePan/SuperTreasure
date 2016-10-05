package com.supertreasure.websocket;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbSharedUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.supertreasure.bean.NowTime;
import com.supertreasure.bean.userLogin;
import com.supertreasure.eventbus.RefleshMineActivity;

import com.supertreasure.eventbus.RefleshCuponFragment;
import com.supertreasure.eventbus.RefleshDecorFragment;
import com.supertreasure.eventbus.RefleshEleProFragment;
import com.supertreasure.eventbus.RefleshMyCollectionFragment;
import com.supertreasure.eventbus.RefleshMyCuponFragment;
import com.supertreasure.eventbus.RefleshMyMoodFragment;
import com.supertreasure.eventbus.RefleshMyShopPublish;
import com.supertreasure.eventbus.RefleshMyShopRemove;
import com.supertreasure.eventbus.RefleshMyShopSold;
import com.supertreasure.eventbus.RefleshOtherProFragment;
import com.supertreasure.eventbus.RefleshOutschool;
import com.supertreasure.eventbus.RefleshOutschoolHead;
import com.supertreasure.eventbus.RefleshSchoolInMoodFragment;
import com.supertreasure.eventbus.RefleshSchoolOutMoodFragment;
import com.supertreasure.main.TheApp;
import com.supertreasure.service.HttpResultUtil;
import com.supertreasure.util.Config;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.drafts.Draft_17;

import java.net.URI;
import java.net.URISyntaxException;

import me.itangqi.greendao.User;

/**
 * Created by yum on 2016/4/14.
 */
public class MyWebSocketManager {
    String Tag = "eventbus post";
    private static MyWebSocketManager instance = null;
    private MyWebSocketClient webSocketClient = null;
    private static Context context;
    public static void init(Context context){
        MyWebSocketManager.context = context;
    }
    private MyWebSocketManager(){
    }
    public static MyWebSocketManager getInstance(){
        if (instance == null){
            instance = new MyWebSocketManager();
        }
        return instance;
    }
    public synchronized void connectServer() {

        if (context == null) {
            Logger.e("error", "请先初始化MyWebSocketManager.init()");
            return ;
        }

        String token = AbSharedUtil.getString(context, Config.Token);
        String userName = AbSharedUtil.getString(context,Config.UserName);
        try {

            webSocketClient = new MyWebSocketClient(context,new URI("ws://112.74.54.18:8080/chat/ws/"+userName+"/"+token), new Draft_17());
            if (!isConneced()) {
                webSocketClient.connectBlocking();
                Logger.i("websocket,", "连接成功");
            }else{
                Logger.i("websocket,", "连接正常，无需重连");

            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Logger.i("websocket,", "webSocketClient.isConnect():" + isConnecting());
        //webSocketClient.connect();
    }
    public MyWebSocketClient getWebSocketClient(){
        return webSocketClient;
    }
    public boolean isConneced(){
        if (webSocketClient!=null)
            return webSocketClient.getConnection().isOpen();
        return false;
    }
    public synchronized void reConnectServer(){
        Logger.i("websocket,", "尝试重连接");

        if (webSocketClient!=null && isConneced()) {
            Logger.i("websocket,", "连接正常，无需重连");
            return;
        }

        //webSocketClient == null 的时候 或则  没有连接的时候。去执行连接

        //获取服务器当前时间
        String url = Config.getAPI(Config.NowTime);
        final String userName = AbSharedUtil.getString(context,Config.UserName);
        final String password = AbSharedUtil.getString(context,Config.Password);
        final String token = AbSharedUtil.getString(context, Config.Token);

        TheApp.mAbHttpUtil.post(url, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                //Logger.json("response content", content);

                Gson gson = new Gson();
                NowTime nowTime = gson.fromJson(content,NowTime.class);
                AbSharedUtil.putString(context, Config.TopNewsTime, nowTime.getNowtime());
            }
            @Override
            public void onStart() {}
            @Override
            public void onFinish() {}
            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                //Toast.makeText(context, "获取服务器当前时间失败", Toast.LENGTH_SHORT).show();
            }
        });

        url = Config.getAPI(Config.UserLogin);
        AbRequestParams params = new AbRequestParams();
        params.put("userName",userName);
        params.put("password", password);

        TheApp.mAbHttpUtil.setTimeout(5000);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Logger.json("response content", content);

                Gson gson = new Gson();
                // Toast.makeText(context,content,Toast.LENGTH_LONG).show();
                userLogin userLogin = gson.fromJson(content, userLogin.class);

                if (userLogin.getStatus().equals(Config.SUCCESS)) {
                    //登陆后设置为false,表示你重新登陆
                    AbSharedUtil.putBoolean(context, Config.IsOtherLogin, false);
                    AbSharedUtil.putBoolean(context, Config.IsFirstLogin, false);
                    // 保存个人信息
                    AbSharedUtil.putString(context, Config.UserName, userName);
                    AbSharedUtil.putString(context, Config.Content_AboutMe_Belongschool, userLogin.getUser().getBelongschool());
                    AbSharedUtil.putString(context, Config.Token, userLogin.getToken());
                    AbSharedUtil.putBoolean(context, Config.IsAutoLogin, true);

                    long current_user_id = 0;

                    User user = userLogin.getUser();
                    Log.i("insert", user.toString());
                    AbSharedUtil.putInt(context, Config.Current_user_id, (int) current_user_id);
                    //startService(new Intent(context, WebSocketService.class));
                    //EventBus.getDefault().post(new InitBanner1FromServer());
                    //EventBus.getDefault().post(new InitTopNewsFromServer());
                    Logger.i("websocket", "重新登录成功");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            connectServer();
                        }
                    }).start();
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
                //Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public synchronized void reConnectServer(final Object module){
        Logger.i("websocket,", "尝试重连接");

        if (webSocketClient!=null && isConneced()) {
            Logger.i("websocket,", "连接正常，无需重连");
            return;
        }

        //webSocketClient == null 的时候 或则  没有连接的时候。去执行连接

        //获取服务器当前时间
        String url = Config.getAPI(Config.NowTime);
        final String userName = AbSharedUtil.getString(context,Config.UserName);
        final String password = AbSharedUtil.getString(context,Config.Password);
        final String token = AbSharedUtil.getString(context, Config.Token);

        TheApp.mAbHttpUtil.post(url, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Logger.json("response content", content);

                Gson gson = new Gson();
                NowTime nowTime = gson.fromJson(content,NowTime.class);
                AbSharedUtil.putString(context, Config.TopNewsTime, nowTime.getNowtime());
            }
            @Override
            public void onStart() {}
            @Override
            public void onFinish() {}
            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                //Toast.makeText(context, "获取服务器当前时间失败", Toast.LENGTH_SHORT).show();
            }
        });

        url = Config.getAPI(Config.UserLogin);
        AbRequestParams params = new AbRequestParams();
        params.put("userName",userName);
        params.put("password", password);

        TheApp.mAbHttpUtil.setTimeout(5000);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Logger.json("onSuccess content", content);
               /* if (HttpResultUtil.handleUnloginErrorStatus(content)) {
                    return;
                }*/
                Gson gson = new Gson();
                // Toast.makeText(context,content,Toast.LENGTH_LONG).show();
                userLogin userLogin = gson.fromJson(content, userLogin.class);

                if (userLogin.getStatus().equals(Config.SUCCESS)) {
                    //登陆后设置为false,表示你重新登陆
                    AbSharedUtil.putBoolean(context, Config.IsOtherLogin, false);
                    AbSharedUtil.putBoolean(context, Config.IsFirstLogin, false);
                    // 保存个人信息
                    AbSharedUtil.putString(context, Config.Token, userLogin.getToken());
                    //startService(new Intent(context, WebSocketService.class));
                    //EventBus.getDefault().post(new InitBanner1FromServer());
                    //EventBus.getDefault().post(new InitTopNewsFromServer());
                    Logger.i("websocket", "重新登录成功");

                    updateModule(module);
//-------------------------------推送打开---------------------------------------
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            connectServer();
                        }
                    }).start();*/
//----------------------------------------------------------------------

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
                //Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public synchronized void reConnectServer(final Object module, final Activity activity){
        Logger.i("websocket,", "尝试重连接");

        if (webSocketClient!=null && isConneced()) {
            Logger.i("websocket,", "连接正常，无需重连");
            return;
        }

        //webSocketClient == null 的时候 或则  没有连接的时候。去执行连接

        //获取服务器当前时间
        String url = Config.getAPI(Config.NowTime);
        final String userName = AbSharedUtil.getString(context,Config.UserName);
        final String password = AbSharedUtil.getString(context,Config.Password);
        final String token = AbSharedUtil.getString(context, Config.Token);

        TheApp.mAbHttpUtil.post(url, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Logger.json("response content", content);

                Gson gson = new Gson();
                NowTime nowTime = gson.fromJson(content,NowTime.class);
                AbSharedUtil.putString(context, Config.TopNewsTime, nowTime.getNowtime());
            }
            @Override
            public void onStart() {}
            @Override
            public void onFinish() {}
            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                //Toast.makeText(context, "获取服务器当前时间失败", Toast.LENGTH_SHORT).show();
            }
        });

        url = Config.getAPI(Config.UserLogin);
        AbRequestParams params = new AbRequestParams();
        params.put("userName",userName);
        params.put("password", password);

        TheApp.mAbHttpUtil.setTimeout(5000);
        TheApp.mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                Logger.json("onSuccess content", content);
                if (HttpResultUtil.handleInputErrorStatus(content, activity)) {
                    return;
                }
                Gson gson = new Gson();
                // Toast.makeText(context,content,Toast.LENGTH_LONG).show();
                userLogin userLogin = gson.fromJson(content, userLogin.class);

                if (userLogin.getStatus().equals(Config.SUCCESS)) {
                    //登陆后设置为false,表示你重新登陆
                    AbSharedUtil.putBoolean(context, Config.IsOtherLogin, false);
                    AbSharedUtil.putBoolean(context, Config.IsFirstLogin, false);
                    // 保存个人信息
                    AbSharedUtil.putString(context, Config.Token, userLogin.getToken());
                    //startService(new Intent(context, WebSocketService.class));
                    //EventBus.getDefault().post(new InitBanner1FromServer());
                    //EventBus.getDefault().post(new InitTopNewsFromServer());
                    Logger.i("websocket", "重新登录成功");

                    updateModule(module);
//-------------------------------推送打开---------------------------------------
                    /*new Thread(new Runnable() {
                        @Override
                        public void run() {
                            connectServer();
                        }
                    }).start();*/
//----------------------------------------------------------------------

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
                //Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void closeConnect(){
        Logger.i("websocket","closeConnect()");
        if (isConneced()) {
            webSocketClient.closeConnection(1006,"客户端关闭连接");
        }
    }
    public void updateModule(Object module){
        if (module instanceof RefleshDecorFragment) {
            //饰品 商品fragement
            Logger.i(Tag,"饰品 商品fragement");
            EventBus.getDefault().post(RefleshDecorFragment.getInstance());
        }
        else if (module instanceof RefleshSchoolOutMoodFragment) {
            //外校说说fragement
            Logger.i(Tag,"外校说说fragement");
            EventBus.getDefault().post(RefleshSchoolOutMoodFragment.getInstance());
        }
        else if (module instanceof RefleshSchoolInMoodFragment) {
            //校内说说fragement
            Logger.i(Tag,"校内说说fragement");
            EventBus.getDefault().post(RefleshSchoolInMoodFragment.getInstance());
        }
        else if (module instanceof RefleshOtherProFragment){
            //其他 商品fragement
            Logger.i(Tag,"其他 商品fragement");
            EventBus.getDefault().post(RefleshOtherProFragment.getInstance());
        }
        else if (module instanceof RefleshMyShopSold){
            //我的小店
            Logger.i(Tag,"我的小店 RefleshMyShopSold");
            EventBus.getDefault().post(RefleshMyShopSold.getInstance());
        }
        else if (module instanceof RefleshMyShopRemove){
            //我的小店
            Logger.i(Tag,"我的小店 RefleshMyShopRemove");
            EventBus.getDefault().post(RefleshMyShopRemove.getInstance());
        }
        else if (module instanceof RefleshMyShopPublish){
            //我的小店
            Logger.i(Tag,"我的小店RefleshMyShopPublish");
            EventBus.getDefault().post(RefleshMyShopPublish.getInstance());
        }
        else if (module instanceof RefleshEleProFragment){
            //电器 商品fragement
            Logger.i(Tag,"电器 商品fragement");
            EventBus.getDefault().post(RefleshEleProFragment.getInstance());
        }
        else if (module instanceof RefleshMineActivity){
            //我的
            Logger.i(Tag,"我的mine");
            EventBus.getDefault().post(RefleshMineActivity.getInstance());
        }
        /*else if (module instanceof InitBanner1FromServer){
            Logger.i(Tag,"广告1");
            EventBus.getDefault().post(InitBanner1FromServer.getInstance());
        }*/
        else if (module instanceof RefleshMyCollectionFragment){
            //我的收藏activity
            Logger.i(Tag,"我的收藏activity");
            EventBus.getDefault().post(RefleshMyCollectionFragment.getInstance());
        }else if (module instanceof RefleshMyCuponFragment){
            //我的优惠卷activity
            Logger.i(Tag,"我的优惠卷activity");
            EventBus.getDefault().post(RefleshMyCuponFragment.getInstance());
        }else if (module instanceof RefleshCuponFragment){
            //优惠卷activity
            Logger.i(Tag,"优惠卷activity");
            EventBus.getDefault().post(RefleshCuponFragment.getInstance());
        }else if (module instanceof RefleshOutschool){
            //校外activity
            Logger.i(Tag,"校外activity");
            EventBus.getDefault().post(RefleshOutschool.getInstance());
        }else if (module instanceof RefleshOutschoolHead){
            //校外头条activity
            Logger.i(Tag,"校外头条activity");
            EventBus.getDefault().post(RefleshOutschoolHead.getInstance());
        }else if (module instanceof RefleshMyMoodFragment){
            //校外头条activity
            Logger.i(Tag,"我的说说activity");
            EventBus.getDefault().post(RefleshMyMoodFragment.getInstance());
        }

    }
}
