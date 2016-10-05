package com.supertreasure.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yum on 2016/1/29.
 */
public class MyUtils {
    public static void show(Context context, String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
        Log.i("toast",content);
    }
}
