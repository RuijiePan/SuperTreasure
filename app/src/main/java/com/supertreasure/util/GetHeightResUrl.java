package com.supertreasure.util;





/**
 * Created by Administrator on 2016/3/1.
 */
public class GetHeightResUrl {

    public static String getHeightUrl(String LowResUrl){
        int end = LowResUrl.lastIndexOf(".");
        //Log.i("haha","LowResUrl = " +LowResUrl);
        //Log.i("haha","end = " +end);
        String url = LowResUrl.substring(end-3,end);
        //Log.i("haha",url);
        if(url.equals("min")) {
            String UrlStrat = LowResUrl.substring(0, end - 3);
            String UrlEnd = LowResUrl.substring(end, LowResUrl.length());
            String HeightResUrl = UrlStrat + "max" + UrlEnd;
            //Log.i("haha","url");
            return HeightResUrl;
        }
        return LowResUrl;
    };

    public static boolean isGIF(String LowResUrl){
        int start = LowResUrl.lastIndexOf(".")+1;
        int end = LowResUrl.length();
        String Suffix = LowResUrl.substring(start,end);

        if(Suffix.equals("gif")){
            return true;
        }else {
            return false;
        }
    }
}
