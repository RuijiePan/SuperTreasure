package com.supertreasure.service;

import android.app.Activity;
import android.content.Intent;

import com.ab.util.AbSharedUtil;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.supertreasure.bean.GoodBean;
import com.supertreasure.bean.StatusBean;
import com.supertreasure.main.MainActivity;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;
import com.supertreasure.util.ToastUtil;
import com.supertreasure.websocket.MyWebSocketManager;

/**
 * Created by yum on 2016/4/24.
 */
public class HttpResultUtil {
    //未登录
    public static void onUnLoginError(Object module,Activity activity){
        Logger.i("httpresultutils", "onUnLoginError");
        MyWebSocketManager.getInstance().reConnectServer(module, activity);
    }
    //密码错误
    public static void onInputError(Activity activity){
        Logger.i("httpresultutils","onInputError");
        AbSharedUtil.putBoolean(activity, Config.IsOtherLogin, true);
        Intent intent = new Intent(activity,MainActivity.class);
        intent.putExtra(Config.Exit,true);
        activity.startActivity(intent);
        ToastUtil.showToast(TheApp.instance, "密码或账号错误");
        activity.finish();
    }
    /**
     *
     * @param content
     * @return true 有问题，false 没问题
     */
    public static boolean handleUnloginErrorStatus(String content, Object module, Activity activity){
        Gson gson = new Gson();
        StatusBean statusBean = gson.fromJson(content, StatusBean.class);
        String error_type = statusBean.getError_type();
        if (statusBean==null||error_type == null){
            return false;
        }
        switch (error_type){
            case Config.UnLoginError://没登录
                onUnLoginError(module, activity);
                return true;
        }
        return false;
    }
    public static boolean handleInputErrorStatus(String content, Activity activity){
        Gson gson = new Gson();
        StatusBean statusBean = gson.fromJson(content, StatusBean.class);
        String error_type = statusBean.getError_type();
        if (statusBean==null||error_type == null){
            return false;
        }
        switch (error_type){
            case Config.InputError://密码账号错误
                onInputError(activity);
                return true;
        }
        return false;
    }
}
