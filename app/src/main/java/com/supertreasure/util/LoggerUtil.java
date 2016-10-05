package com.supertreasure.util;

import com.orhanobut.logger.Logger;

/**
 * Created by yum on 2016/4/25.
 */
public class LoggerUtil{
    private static boolean isShow = true;

    public static void d(String tag,String message){
        if (isShow){
            Logger.d(tag,message);
        }
    }public static void json(String tag,String message){
        if (isShow){
            Logger.json(tag,message);
        }
    }


}
