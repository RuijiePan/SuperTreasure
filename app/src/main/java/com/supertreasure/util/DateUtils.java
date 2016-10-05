package com.supertreasure.util;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2016/2/23.
 */
public class DateUtils {

    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;

    public static Calendar calendar = Calendar.getInstance();

    /**
     *
     * @param timestr
     * @return 距离现在
     */
    public static String fromToday(String timestr){

        timestr = timestr.replace("T"," ");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(timestr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "未知时间";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        long time = date.getTime() / 1000;
        long now = new Date().getTime() / 1000;
        long ago = now - time;
        /*Log.w("time1","str="+timestr);
        Log.w("time1","time="+time);
        Log.w("time1","now="+now);
        Log.w("time1","ago="+ago);*/
        if(ago<=ONE_MINUTE){
            return "刚刚";
        }
        else if(ago <= ONE_HOUR){
            return ago / ONE_MINUTE + "分钟前";
        }
        else if(ago <= ONE_DAY){
            return ago / ONE_HOUR + "小时前" ; /*+ (ago % ONE_HOUR / ONE_MINUTE)+ "分钟前";*/
        }
        else if(ago <= ONE_DAY * 2){
            return "昨天" ;/*+ calendar.get(Calendar.HOUR_OF_DAY) + "点"+ calendar.get(Calendar.MINUTE) + "分";*/
        }
        else if (ago <= ONE_DAY * 3){
            return "前天" ;/*+ calendar.get(Calendar.HOUR_OF_DAY) + "点" + calendar.get(Calendar.MINUTE) + "分";*/
        }
        else if (ago <= ONE_MONTH){
            long day = ago / ONE_DAY;
            if(day < 7)
                return day + "天前" ;
            else if (day >= 7 && day <14)
                return "1周前";
            else if (day >=14 && day <21)
                return "2周前";
            else if (day >= 21 && day <28)
                return "3周前";
            else
                return "4周前";
        }
        else if (ago <= ONE_YEAR){
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / ONE_DAY;
            return month + "个月前" ;/*+ day + "天前";*/
        }
        else {
            long year = ago / ONE_YEAR;
            int month = calendar.get(Calendar.MONTH) + 1;// JANUARY which is 0 so month+1
            return year + "年前";/* + month + "月" + calendar.get(Calendar.DATE)
                    + "日";*/
        }
    }

}
