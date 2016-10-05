package com.supertreasure.util;

/**
 * Created by prj on 2016/3/19.
 */
public class PrjBase64 {

    //解密
    public static String decode(String str){
        return new String(android.util.Base64.decode(str.getBytes(), android.util.Base64.DEFAULT));
    }

    //加密
    public static String encode(String str){
        return android.util.Base64.encodeToString(str.getBytes(), android.util.Base64.DEFAULT);
    }
}
