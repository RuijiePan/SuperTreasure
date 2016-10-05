package com.supertreasure.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ab.util.AbSharedUtil;
import com.supertreasure.bean.Banner;
import com.supertreasure.bean.CouponBean;
import com.supertreasure.bean.GoodBean;
import com.supertreasure.bean.MoodBean;
import com.supertreasure.bean.Topnews;
import com.supertreasure.util.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yum on 2015/10/7.
 */
public class DBTool {
    /*DBManager dbManager;
    SQLiteDatabase database;

    private DBTool() {}
    private static DBTool dbTool;
    public DBTool(Context context) {
        dbManager = DBManager.getInstance(context, Config.DATABASE_NAME);
    }
    //单例模式,获取了这个，数据库使用默认的，不用每次都传参数
    public static DBTool getInstance(Context context){
        if (dbTool != null){
            return dbTool;
        }
        dbTool = new DBTool(context);
        return dbTool;
    }
    //插入
    public long insert(String table,ContentValues values){
        database = dbManager.openDatabase();
        long i = 0;
        try {
             i =  database.insert(table,null,values);
            if (i<=0){
                Log.i("SHOWTABLE",table+ "-------插入失败-----返回值："+i);
            }
        }catch (Exception e){
            Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }
        return  i;
    }
    public long insertUserTable(ContentValues values){
        long i = 0;
        //如果存在，则更新
        if (ifExistUser(values.getAsString("account"))) {
            Log.i("SHOWTABLE",Config.DATABASE_TABLE_USER+ "-------已存在"+values.getAsString("account")+" 做更新操作");
            database = dbManager.openDatabase();
            try {
                database.update(Config.DATABASE_TABLE_USER,values,"account=?",new String[]{values.getAsString("account")});
            }catch (Exception e){
                Log.i("SHOWTABLE", e.getMessage());
            }
            finally {
                database.close();
            }

            return i;
        }
        database = dbManager.openDatabase();
        //不存在则插入
        try {
            i =  database.insert(Config.DATABASE_TABLE_USER,null,values);
            if (i<=0){
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_USER+ "-------插入失败-----返回值："+i);
            }else{
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_USER+ "-------插入成功-----返回值："+i);

            }
        }catch (Exception e){
            Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }
        return  i;
    }
    public long insertNewTable(ContentValues values){
        long i = 0;
        if (ifExistNew(values.getAsString("topId"))) {
            Log.i("SHOWTABLE",Config.DATABASE_TABLE_NEW+ "-------已存在"+values.getAsString("topId")+" 不做处理");
            return i;
        }
        database = dbManager.openDatabase();

        try {
            i =  database.insert(Config.DATABASE_TABLE_NEW,null,values);
            if (i<=0){
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_NEW+ "-------插入失败-----返回值："+i);
            }else{
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_NEW+ "-------插入成功-----返回值："+i);

            }
        }catch (Exception e){
            Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }
        return  i;
    }
    public long insertNewTable(Context context,List<Topnews.News> newsList){
        ContentValues values;
        int user_id = AbSharedUtil.getInt(context,Config.Current_user_id);
        long i = 0;
        database = dbManager.openDatabase();
        try {
            for (Topnews.News news:newsList){
                news.setUser_id(user_id);
                values = news.toContentValues();
                if (ExistNew(values.getAsString("topId"))) {
                    Log.i("SHOWTABLE",Config.DATABASE_TABLE_NEW+ "-------已存在"+values.getAsString("topId")+" 不做处理");
                    continue;
                }
                i = 0;

                    i =  database.insert(Config.DATABASE_TABLE_NEW,null,values);
                    if (i<=0){
                        Log.i("SHOWTABLE",Config.DATABASE_TABLE_NEW+ "-------插入失败-----返回值："+i);
                    }else{
                        Log.i("SHOWTABLE",Config.DATABASE_TABLE_NEW+ "-------插入成功-----返回值："+i);

                    }
            }
        }catch (Exception e){
                Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }

        return  i;
    }
    public long insertMoodTable(ContentValues values){
        long i = 0;
        if (ifExistMood(values.getAsInteger("moodId"))){
            Log.i("SHOWTABLE",Config.DATABASE_TABLE_MOOD+ "-------已存在"+values.getAsString("moodId")+" 不做处理");
            return i;
        }
        database = dbManager.openDatabase();

        try {
            i =  database.insert(Config.DATABASE_TABLE_MOOD,null,values);
            if (i<=0){
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_MOOD+ "-------插入失败-----返回值："+i);
            }else{
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_MOOD+ "-------插入成功-----返回值："+i);

            }
        }catch (Exception e){
            Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }
        return  i;
    }
    public long insertMoodTable(Context context,List<MoodBean.Mood> moodList){//避免开关数据库太频繁
        ContentValues values;
        int user_id = AbSharedUtil.getInt(context,Config.Current_user_id);

        long i = 0;
        database = dbManager.openDatabase();

        try {
            for (MoodBean.Mood mood:moodList) {
                mood.setUser_id(user_id);
                values = mood.toContentValues();
                if (ExistMood(values.getAsInteger("moodId"))) {
                    Log.i("SHOWTABLE", Config.DATABASE_TABLE_MOOD + "-------已存在" + values.getAsString("moodId") + " 不做处理");
                    continue;
                }
                i = 0;
                i = database.insert(Config.DATABASE_TABLE_MOOD, null, values);
                if (i <= 0) {
                    Log.i("SHOWTABLE", Config.DATABASE_TABLE_MOOD + "-------插入失败-----返回值：" + i);
                } else {
                    Log.i("SHOWTABLE", Config.DATABASE_TABLE_MOOD + "-------插入成功-----返回值：" + i);

                }
            }
        } catch (Exception e) {
            Log.i("SHOWTABLE", e.getMessage());
        } finally {
            database.close();
        }

        return  i;
    }
    public long insertCouponTable(ContentValues values){
        long i = 0;
        if (ifExistCoupon(values.getAsInteger("couponID"))) {
            Log.i("SHOWTABLE",Config.DATABASE_TABLE_COUPON+ "-------已存在"+values.getAsString("couponID")+" 不做处理");
            return i;
        }
        database = dbManager.openDatabase();

        try {
            i =  database.insert(Config.DATABASE_TABLE_COUPON,null,values);
            if (i<=0){
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_COUPON+ "-------插入失败-----返回值："+i);
            }else{
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_COUPON+ "-------插入成功-----返回值："+i);

            }
        }catch (Exception e){
            Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }
        return  i;
    }
    public long insertCouponTable(Context context,List<CouponBean.Coupon> couponList){
        int user_id = AbSharedUtil.getInt(context,Config.Current_user_id);

        ContentValues values;
        long i = 0;
        database = dbManager.openDatabase();

        try {
            for (CouponBean.Coupon coupon:couponList) {
                coupon.setUser_id(user_id);
                values = coupon.toContentValues();
                if (ExistCoupon(values.getAsInteger("couponID"))) {
                    Log.i("SHOWTABLE",Config.DATABASE_TABLE_COUPON+ "-------已存在"+values.getAsString("couponID")+" 不做处理");
                    continue;
                }
                i = 0;
                i =  database.insert(Config.DATABASE_TABLE_COUPON,null,values);
                if (i<=0){
                    Log.i("SHOWTABLE",Config.DATABASE_TABLE_COUPON+ "-------插入失败-----返回值："+i);
                }else{
                    Log.i("SHOWTABLE",Config.DATABASE_TABLE_COUPON+ "-------插入成功-----返回值："+i);

                }
            }
        } catch (Exception e) {
            Log.i("SHOWTABLE", e.getMessage());
        } finally {
            database.close();
        }
        return  i;
    }
    public long inserTGoodTable(ContentValues values){
        long i = 0;
        if (ifExistGood(values.getAsInteger("sellID"))) {
            Log.i("SHOWTABLE",Config.DATABASE_TABLE_GOOD+ "-------已存在"+values.getAsString("sellID")+" 不做处理");
            return i;
        }

        database = dbManager.openDatabase();
        try {
            i =  database.insert(Config.DATABASE_TABLE_GOOD,null,values);
            if (i<=0){
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_GOOD+ "-------插入失败-----返回值："+i);
            }else{
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_GOOD+ "-------插入成功-----返回值："+i);

            }
        }catch (Exception e){
            Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }
        return  i;
    }
    public long inserTGoodTable(Context context,List<GoodBean.Good> goodList,String goodType){
        ContentValues values;
        int user_id = AbSharedUtil.getInt(context,Config.Current_user_id);

        long i = 0;
        database = dbManager.openDatabase();
        try {
            for (GoodBean.Good good:goodList){
                good.setUser_id(user_id);
                good.setGoodType(goodType);
                values = good.toContentValues();
                if (ExistGood(values.getAsInteger("sellID"))) {
                    Log.i("SHOWTABLE",Config.DATABASE_TABLE_GOOD+ "-------已存在"+values.getAsString("sellID")+" 不做处理"+    "goodType="+goodType);
                    continue;
                }
                i = 0;

                i =  database.insert(Config.DATABASE_TABLE_GOOD,null,values);
                if (i<=0){
                    Log.i("SHOWTABLE",Config.DATABASE_TABLE_GOOD+ "-------插入失败-----返回值："+i+" goodType="+goodType);
                }else{
                    Log.i("SHOWTABLE",Config.DATABASE_TABLE_GOOD+ "-------插入成功-----返回值："+i+" goodType="+goodType);

                }
            }
        }catch (Exception e){
            Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }
        return  i;
    }
    public long inserTBannerTable(ContentValues values){
        long i = 0;
        if (ExistBanner(values.getAsInteger("adId"))) {
            Log.i("SHOWTABLE",Config.DATABASE_TABLE_BANNER+ "-------已存在"+values.getAsString("adId")+" 不做处理");
            return i;
        }

        database = dbManager.openDatabase();
        try {
            i =  database.insert(Config.DATABASE_TABLE_BANNER,null,values);
            if (i<=0){
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_BANNER+ "-------插入失败-----返回值："+i);
            }else{
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_BANNER+ "-------插入成功-----返回值："+i);

            }
        }catch (Exception e){
            Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }
        return  i;
    }
    public long inserTBannerTable(List<Banner.ListEntity> bannerList){
        ContentValues values;

        long i = 0;
        database = dbManager.openDatabase();
        try {
            for (Banner.ListEntity  banner:bannerList){
                values = banner.toContentValues();
                if (ExistBanner(values.getAsInteger("adId"))) {
                    Log.i("SHOWTABLE",Config.DATABASE_TABLE_BANNER+ "-------已存在"+values.getAsString("adId")+" 不做处理");
                    continue;
                }
                i = 0;

                i =  database.insert(Config.DATABASE_TABLE_BANNER,null,values);
                if (i<=0){
                    Log.i("SHOWTABLE",Config.DATABASE_TABLE_BANNER+ "-------插入失败-----返回值："+i);
                }else{
                    Log.i("SHOWTABLE",Config.DATABASE_TABLE_BANNER+ "-------插入成功-----返回值："+i);

                }
            }
        }catch (Exception e){
            Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }
        return  i;
    }

    //移除
    public long deleteNewTable(String topId){//网络的topId
        long i = 0;
        if (!ifExistNew(topId)) {
            Log.i("SHOWTABLE",Config.DATABASE_TABLE_NEW+ "-------不已存在"+topId+" 不做移除处理");
            return i;
        }
        database = dbManager.openDatabase();
        try {
            i =  database.delete(Config.DATABASE_TABLE_NEW, "topId=?", new String[]{topId});
            if (i<=0){
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_NEW+ "-------移除失败-----返回值："+i);
            }else{
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_NEW+ "-------移除成功-----返回值："+i);
            }
        }catch (Exception e){
            Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }
        return  i;
    }
    public long deleteMoodTable(int moodId){//网络的topId
        long i = 0;
        if (!ifExistMood(moodId)) {
            Log.i("SHOWTABLE",Config.DATABASE_TABLE_MOOD+ "-------不已存在"+moodId+" 不做移除处理");
            return i;
        }
        database = dbManager.openDatabase();
        try {
            i =  database.delete(Config.DATABASE_TABLE_MOOD,"moodId=?",new String[]{moodId+""});
            if (i<=0){
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_MOOD+ "-------移除失败-----返回值："+i);
            }else{
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_MOOD+ "-------移除成功-----返回值："+i);
            }
        }catch (Exception e){
            Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }
        return  i;
    }
    public long deleteCouponTable(int couponID){//网络的topId
        long i = 0;
        if (!ifExistCoupon(couponID)) {
            Log.i("SHOWTABLE",Config.DATABASE_TABLE_COUPON+ "-------不已存在"+couponID+" 不做移除处理");
            return i;
        }
        database = dbManager.openDatabase();
        try {
            i =  database.delete(Config.DATABASE_TABLE_COUPON,"couponID=?",new String[]{couponID+""});
            if (i<=0){
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_COUPON+ "-------移除失败-----返回值："+i);
            }else{
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_COUPON+ "-------移除成功-----返回值："+i);
            }
        }catch (Exception e){
            Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }
        return  i;
    }
    public long deleteGoodTable(int sellID){
        long i = 0;
        if (!ifExistGood(sellID)) {
            Log.i("SHOWTABLE",Config.DATABASE_TABLE_GOOD+ "-------不已存在"+sellID+" 不做移除处理");
            return i;
        }
        database = dbManager.openDatabase();
        try {
            i =  database.delete(Config.DATABASE_TABLE_GOOD,"sellID=?",new String[]{sellID+""});
            if (i<=0){
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_GOOD+ "-------移除失败-----返回值："+i);
            }else{
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_GOOD+ "-------移除成功-----返回值："+i);
            }
        }catch (Exception e){
            Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }
        return  i;
    }
    public long deleteBannerTable(int adId){
        long i = 0;
        if (!ifExistBanner(adId)) {
            Log.i("SHOWTABLE",Config.DATABASE_TABLE_BANNER+ "-------不已存在"+adId+" 不做移除处理");
            return i;
        }
        database = dbManager.openDatabase();
        try {
            i =  database.delete(Config.DATABASE_TABLE_BANNER,"adId=?",new String[]{adId+""});
            if (i<=0){
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_BANNER+ "-------移除失败-----返回值："+i);
            }else{
                Log.i("SHOWTABLE",Config.DATABASE_TABLE_BANNER+ "-------移除成功-----返回值："+i);
            }
        }catch (Exception e){
            Log.i("SHOWTABLE", e.getMessage());
        }
        finally{
            database.close();
        }
        return  i;
    }

    //更新
    public int update(String table,ContentValues updateValues,String whereClause,String[] whereArgs){
        database = dbManager.openDatabase();
        try {
            return database.update(table,updateValues,whereClause,whereArgs);
        }finally {
            database.close();
        }
    }
    //判断是否存在
    public boolean ifExist(String sql){
        database = dbManager.openDatabase();
        try {
            Cursor cursor = database.rawQuery(sql, null);
            if (cursor!=null&&cursor.getCount()>0){
                cursor.close();
                return true;
            }else {
                cursor.close();
                return false;
            }
        }finally {
            database.close();
        }
    }
    public boolean ifExist(String sql,String[] args){
        database = dbManager.openDatabase();
        try {
            Cursor cursor = database.rawQuery(sql, args);
            if (cursor!=null&&cursor.getCount()>0){
                cursor.close();
                return true;
            }else {
                cursor.close();
                return false;
            }
        }finally {
            database.close();
        }
    }
    public boolean ifExistUser(String account){
        String sql = "select * from "+Config.DATABASE_TABLE_USER+" where account="+account;
        database = dbManager.openDatabase();
        try {
            Cursor cursor = database.rawQuery(sql,null);
            if (cursor!=null&&cursor.getCount()>0){
                cursor.close();
                Log.i("SHOWTABLE", "ifExistUser:" + account + "     true");
                return true;
            }else {
                cursor.close();
                Log.i("SHOWTABLE", "ifExistUser:" + account + "     false");
                return false;
            }
        }finally {
            database.close();
        }
    }
    //这里判断的存在，都是指 服务器 返回的id
    //ifExist  独立使用   ，ExistNew内部private使用
    public boolean ifExistNew(String topId){
        String sql = "select * from "+Config.DATABASE_TABLE_NEW+" where topId="+topId;
        database = dbManager.openDatabase();
        try {
            Cursor cursor = database.rawQuery(sql,null);
            if (cursor!=null&&cursor.getCount()>0){
                cursor.close();
                Log.i("SHOWTABLE","ifExistNew:" + topId+"     true");

                return true;
            }else {
                cursor.close();
                Log.i("SHOWTABLE","ifExistNew:" + topId+"     false");

                return false;
            }
        }finally {
            database.close();
        }
    }
    private boolean ExistNew(String topId){
        String sql = "select * from "+Config.DATABASE_TABLE_NEW+" where topId="+topId;
            Cursor cursor = database.rawQuery(sql,null);
            if (cursor!=null&&cursor.getCount()>0){
                cursor.close();
                Log.i("SHOWTABLE","ifExistNew:" + topId+"     true");

                return true;
            }else {
                cursor.close();
                Log.i("SHOWTABLE","ifExistNew:" + topId+"     false");

                return false;
            }

    }
    public boolean ifExistMood(int moodId){
        String sql = "select * from "+Config.DATABASE_TABLE_MOOD+" where moodId="+moodId;
        database = dbManager.openDatabase();
        try {
            Cursor cursor = database.rawQuery(sql,null);
            if (cursor!=null&&cursor.getCount()>0){
                cursor.close();
                Log.i("SHOWTABLE","ifExistMood:" + moodId+"     true");

                return true;
            }else {
                cursor.close();
                Log.i("SHOWTABLE","ifExistMood:" + moodId+"     false");

                return false;
            }
        }finally {
            database.close();
        }
    }
    private boolean ExistMood(int moodId){//避免开关数据库太频繁
        String sql = "select * from "+Config.DATABASE_TABLE_MOOD+" where moodId="+moodId;
        Cursor cursor = database.rawQuery(sql,null);
        if (cursor!=null&&cursor.getCount()>0){
            cursor.close();
            Log.i("SHOWTABLE","ifExistMood:" + moodId+"     true");
            return true;
        }else {
            cursor.close();
            Log.i("SHOWTABLE","ifExistMood:" + moodId+"     false");
            return false;
        }

    }
    public boolean ifExistCoupon(int couponID){
        String sql = "select * from "+Config.DATABASE_TABLE_COUPON+" where couponID="+couponID;
        database = dbManager.openDatabase();
        try {
            Cursor cursor = database.rawQuery(sql,null);
            if (cursor!=null&&cursor.getCount()>0){
                cursor.close();
                Log.i("SHOWTABLE","ifExistCoupon:" + couponID+"     true");

                return true;
            }else {
                cursor.close();
                Log.i("SHOWTABLE","ifExistCoupon:" + couponID+"     false");

                return false;
            }
        }finally {
            database.close();
        }
    }
    private boolean ExistCoupon(int couponID){
        String sql = "select * from "+Config.DATABASE_TABLE_COUPON+" where couponID="+couponID;
            Cursor cursor = database.rawQuery(sql,null);
            if (cursor!=null&&cursor.getCount()>0){
                cursor.close();
                Log.i("SHOWTABLE","ifExistCoupon:" + couponID+"     true");

                return true;
            }else {
                cursor.close();
                Log.i("SHOWTABLE","ifExistCoupon:" + couponID+"     false");

                return false;
            }

    }
    public boolean ifExistGood(int sellID){
        String sql = "select * from "+Config.DATABASE_TABLE_GOOD+" where sellID="+sellID;
        database = dbManager.openDatabase();
        try {
            Cursor cursor = database.rawQuery(sql,null);
            if (cursor!=null&&cursor.getCount()>0){
                cursor.close();
                Log.i("SHOWTABLE","ifExistGood:" + sellID+"     true");

                return true;
            }else {
                cursor.close();
                Log.i("SHOWTABLE","ifExistGood:" + sellID+"     false");

                return false;
            }
        }finally {
            database.close();
        }
    }
    private boolean ExistGood(int sellID){
        String sql = "select * from "+Config.DATABASE_TABLE_GOOD+" where sellID="+sellID;
            Cursor cursor = database.rawQuery(sql,null);
            if (cursor!=null&&cursor.getCount()>0){
                cursor.close();
                Log.i("SHOWTABLE","ifExistGood:" + sellID+"     true");

                return true;
            }else {
                cursor.close();
                Log.i("SHOWTABLE","ifExistGood:" + sellID+"     false");

                return false;
            }

    }
    private boolean ExistBanner(int adId){
        String sql = "select * from "+Config.DATABASE_TABLE_BANNER+" where adId="+adId;
        Cursor cursor = database.rawQuery(sql,null);
        if (cursor!=null&&cursor.getCount()>0){
            cursor.close();
            Log.i("SHOWTABLE","ifExistBanner:" + adId+"     true");

            return true;
        }else {
            cursor.close();
            Log.i("SHOWTABLE","ifExistBanner:" + adId+"     false");

            return false;
        }

    }
    public boolean ifExistBanner(int adId){
        String sql = "select * from "+Config.DATABASE_TABLE_BANNER+" where adId="+adId;
        database = dbManager.openDatabase();
        try {
            Cursor cursor = database.rawQuery(sql,null);
            if (cursor!=null&&cursor.getCount()>0){
                cursor.close();
                Log.i("SHOWTABLE","ifExistBanner:" + adId+"     true");

                return true;
            }else {
                cursor.close();
                Log.i("SHOWTABLE","ifExistBanner:" + adId+"     false");

                return false;
            }
        }finally {
            database.close();
        }
    }

    //查询
    public Cursor rawQuery(String sql){
        database = dbManager.openDatabase();
        Cursor cursor =  database.rawQuery(sql, null);
        //database.close();
        return cursor;
    }
    public Cursor rawQuery(String sql,String[] args){
        database = dbManager.openDatabase();
        Cursor cursor =database.rawQuery(sql,args);
        //database.close();
        return cursor;
    }
    //执行
    public void exeSql(String sql){
        database = dbManager.openDatabase();
        database.execSQL(sql);
        database.close();
    }
    public void exeSql(String sql,Object[] args){
        database = dbManager.openDatabase();
        database.execSQL(sql,args);
        database.close();
    }
    //控制台输出数据库表
    public void showUserTable(){
        Log.i("SHOWTABLE", "----------------------打印_user 账户表单:------------------------");
        database = dbManager.openDatabase();
        try {
            Cursor cursor = database.rawQuery("select * from "+Config.DATABASE_TABLE_USER, null);
            int i = 0;
            while(cursor.moveToNext()){
                i++;
                Log.i("SHOWTABLE","_id:" + cursor.getInt(cursor.getColumnIndex("_id")));
                Log.i("SHOWTABLE","account:" + cursor.getString(cursor.getColumnIndex("account")));
                Log.i("SHOWTABLE","password:" + cursor.getString(cursor.getColumnIndex("password")));
                Log.i("SHOWTABLE","belongschool:" + cursor.getString(cursor.getColumnIndex("belongschool")));
                Log.i("SHOWTABLE","sex:" + cursor.getString(cursor.getColumnIndex("sex")));
                Log.i("SHOWTABLE","userPic:" + cursor.getString(cursor.getColumnIndex("userPic")));
                Log.i("SHOWTABLE","nickName:" + cursor.getString(cursor.getColumnIndex("nickName")));
                Log.i("SHOWTABLE","userName:" + cursor.getString(cursor.getColumnIndex("userName")));
                Log.i("SHOWTABLE","-----------------------------第"+i+"个记录---------------------------------------");
            }
            Log.i("SHOWTABLE", "---------------------------打印_user 账户表单完毕-----------------------");
        }finally {
            database.close();
        }
    }
    public void showNewsTable(){
        Log.i("SHOWTABLE", "----------------------打印_news 账户表单:------------------------");
        database = dbManager.openDatabase();
        try {
            Cursor cursor = database.rawQuery("select * from "+Config.DATABASE_TABLE_NEW, null);
            int i = 0;
            while(cursor.moveToNext()){
                i++;
                Log.i("SHOWTABLE","_id:" + cursor.getInt(cursor.getColumnIndex("_id")));
                Log.i("SHOWTABLE","user_id:" + cursor.getInt(cursor.getColumnIndex("user_id")));
                Log.i("SHOWTABLE","topId:" + cursor.getString(cursor.getColumnIndex("topId")));
                Log.i("SHOWTABLE","adminID:" + cursor.getString(cursor.getColumnIndex("adminID")));
                Log.i("SHOWTABLE","adminName:" + cursor.getString(cursor.getColumnIndex("adminName")));
                Log.i("SHOWTABLE","title:" + cursor.getString(cursor.getColumnIndex("title")));
                Log.i("SHOWTABLE","label:" + cursor.getString(cursor.getColumnIndex("label")));
                Log.i("SHOWTABLE","summary:" + cursor.getString(cursor.getColumnIndex("summary")));
                Log.i("SHOWTABLE","pics:" + cursor.getString(cursor.getColumnIndex("pics")));
                Log.i("SHOWTABLE","browserNum:" + cursor.getString(cursor.getColumnIndex("browserNum")));
                Log.i("SHOWTABLE","praise:" + cursor.getString(cursor.getColumnIndex("praise")));
                Log.i("SHOWTABLE","url:" + cursor.getString(cursor.getColumnIndex("url")));
                //Log.i("SHOWTABLE","isPraised:" + cursor.getInt(cursor.getColumnIndex("isPraised"))==1?true:false);
                Log.i("SHOWTABLE","-----------------------------第"+i+"个记录---------------------------------------");
            }
            Log.i("SHOWTABLE", "---------------------------打印_news 账户表单完毕-----------------------");
        }finally {
            database.close();
        }
    }
    public void showMoodTable(){
        Log.i("SHOWTABLE", "----------------------打印_mood 账户表单:------------------------");
        database = dbManager.openDatabase();
        try {
            Cursor cursor = database.rawQuery("select * from "+Config.DATABASE_TABLE_MOOD, null);
            int i = 0;
            while(cursor.moveToNext()){
                i++;
                Log.i("SHOWTABLE","_id:" + cursor.getInt(cursor.getColumnIndex("_id")));
                Log.i("SHOWTABLE","user_id:" + cursor.getInt(cursor.getColumnIndex("user_id")));
                Log.i("SHOWTABLE","moodId:" + cursor.getInt(cursor.getColumnIndex("moodId")));
                Log.i("SHOWTABLE","content:" + cursor.getString(cursor.getColumnIndex("content")));
                Log.i("SHOWTABLE","paths:" + cursor.getString(cursor.getColumnIndex("paths")));
                Log.i("SHOWTABLE","publishtime:" + cursor.getString(cursor.getColumnIndex("publishtime")));
                Log.i("SHOWTABLE","praiseTimes:" + cursor.getString(cursor.getColumnIndex("praiseTimes")));
                Log.i("SHOWTABLE","commentCount:" + cursor.getString(cursor.getColumnIndex("commentCount")));
                Log.i("SHOWTABLE","isPraised:" + cursor.getInt(cursor.getColumnIndex("isPraised")));
                Log.i("SHOWTABLE","belongschool:" + cursor.getString(cursor.getColumnIndex("belongschool")));
                Log.i("SHOWTABLE","sex:" + cursor.getString(cursor.getColumnIndex("sex")));
                Log.i("SHOWTABLE","userPic:" + cursor.getString(cursor.getColumnIndex("userPic")));
                Log.i("SHOWTABLE","nickName:" + cursor.getString(cursor.getColumnIndex("nickName")));
                Log.i("SHOWTABLE","userName:" + cursor.getString(cursor.getColumnIndex("userName")));
                Log.i("SHOWTABLE","-----------------------------第"+i+"个记录---------------------------------------");
            }
            Log.i("SHOWTABLE", "---------------------------打印_mood 账户表单完毕-----------------------");
        }finally {
            database.close();
        }
    }
    public void showGoodTable(){
        Log.i("SHOWTABLE", "----------------------打印_good 账户表单:------------------------");
        database = dbManager.openDatabase();
        try {
            Cursor cursor = database.rawQuery("select * from "+Config.DATABASE_TABLE_GOOD, null);
            int i = 0;
            while(cursor.moveToNext()){
                i++;
                Log.i("SHOWTABLE","_id:" + cursor.getInt(cursor.getColumnIndex("_id")));
                Log.i("SHOWTABLE","user_id:" + cursor.getInt(cursor.getColumnIndex("user_id")));
                Log.i("SHOWTABLE","sellID:" + cursor.getInt(cursor.getColumnIndex("sellID")));
                Log.i("SHOWTABLE","width:" + cursor.getInt(cursor.getColumnIndex("width")));
                Log.i("SHOWTABLE","height:" + cursor.getInt(cursor.getColumnIndex("height")));
                Log.i("SHOWTABLE","userName:" + cursor.getString(cursor.getColumnIndex("userName")));
                Log.i("SHOWTABLE","pic:" + cursor.getString(cursor.getColumnIndex("pic")));
                Log.i("SHOWTABLE","price:" + cursor.getString(cursor.getColumnIndex("price")));
                Log.i("SHOWTABLE","goodType:" + cursor.getString(cursor.getColumnIndex("goodType")));
                Log.i("SHOWTABLE","-----------------------------第"+i+"个记录---------------------------------------");
            }
            Log.i("SHOWTABLE", "---------------------------打印_good 账户表单完毕-----------------------");
        }finally {
            database.close();
        }
    }
    public void showCouponTable(){
        Log.i("SHOWTABLE", "----------------------打印_coupon 账户表单:------------------------");
        database = dbManager.openDatabase();
        try {
            Cursor cursor = database.rawQuery("select * from "+Config.DATABASE_TABLE_COUPON, null);
            int i = 0;
            while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
                int couponID = cursor.getInt(cursor.getColumnIndex("couponID"));
                String couponPics = cursor.getString(cursor.getColumnIndex("couponPics"));
                int width =cursor.getInt(cursor.getColumnIndex("width"));
                int height =cursor.getInt(cursor.getColumnIndex("height"));
                int couponPraise =cursor.getInt(cursor.getColumnIndex("couponPraise"));
                boolean isPraise =cursor.getInt(cursor.getColumnIndex("isPraise"))==1?true:false;
                boolean isCollect =cursor.getInt(cursor.getColumnIndex("isCollect"))==1?true:false;
                String intro = cursor.getString(cursor.getColumnIndex("intro"));
                String beginTime = cursor.getString(cursor.getColumnIndex("beginTime"));
                String lastTime = cursor.getString(cursor.getColumnIndex("lastTime"));
                String shopName = cursor.getString(cursor.getColumnIndex("shopName"));
                int shopID =cursor.getInt(cursor.getColumnIndex("shopID"));
                int browserNum =cursor.getInt(cursor.getColumnIndex("browserNum"));

                i++;
                Log.i("SHOWTABLE","_id:" + id);
                Log.i("SHOWTABLE","user_id:" + user_id);
                Log.i("SHOWTABLE","couponID:" + couponID);
                Log.i("SHOWTABLE","couponPics:" + couponPics);
                Log.i("SHOWTABLE","width:" + width);
                Log.i("SHOWTABLE","height:" + height);
                Log.i("SHOWTABLE","couponPraise:" + couponPraise);
                Log.i("SHOWTABLE","isPraise:" + isPraise);
                Log.i("SHOWTABLE","isCollect:" + isCollect);
                Log.i("SHOWTABLE","intro:" + intro);
                Log.i("SHOWTABLE","beginTime:" + beginTime);
                Log.i("SHOWTABLE","lastTime:" + lastTime);
                Log.i("SHOWTABLE","shopName:" + shopName);
                Log.i("SHOWTABLE","shopID:" + shopID);
                Log.i("SHOWTABLE","browserNum:" + browserNum);
                Log.i("SHOWTABLE","-----------------------------第"+i+"个记录---------------------------------------");
            }
            Log.i("SHOWTABLE", "---------------------------打印_coupon 账户表单完毕-----------------------");
        }finally {
            database.close();
        }
    }
    //查询并获取数据newestCouponId  指的是 本地的_id
    public List<CouponBean.Coupon> queryCouponList(Context context,int page,int newestCouponId){
        int index = (page-1)*Config.DATABASE_LODING_NUM;
        int current_user_id = AbSharedUtil.getInt(context, Config.Current_user_id);
        List<CouponBean.Coupon> couponList = null;                                      //当前账号曾加载过的优惠卷          倒序
        Cursor cursor = rawQuery("select * from "+Config.DATABASE_TABLE_COUPON+" where user_id="+current_user_id+" and _id<="+newestCouponId+" order by couponID desc limit "+index+","+Config.DATABASE_LODING_NUM);
        Log.i("SHOWTABLE", "select * from "+Config.DATABASE_TABLE_COUPON+" where user_id="+current_user_id+" and _id<="+newestCouponId+" order by couponID desc limit "+index+","+Config.DATABASE_LODING_NUM);
        Log.i("SHOWTABLE", "----------------------查询结果_coupon 账户表单:------------------------");
        try {
            int i = 0;
            if (cursor!=null&&cursor.getCount()>0)
                couponList = new ArrayList<>();
            while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
                int couponID = cursor.getInt(cursor.getColumnIndex("couponID"));
                String couponPics = cursor.getString(cursor.getColumnIndex("couponPics"));
                int width =cursor.getInt(cursor.getColumnIndex("width"));
                int height =cursor.getInt(cursor.getColumnIndex("height"));
                int couponPraise =cursor.getInt(cursor.getColumnIndex("couponPraise"));
                boolean isPraise =cursor.getInt(cursor.getColumnIndex("isPraise"))==1?true:false;
                boolean isCollect =cursor.getInt(cursor.getColumnIndex("isCollect"))==1?true:false;
                String intro = cursor.getString(cursor.getColumnIndex("intro"));
                String beginTime = cursor.getString(cursor.getColumnIndex("beginTime"));
                String lastTime = cursor.getString(cursor.getColumnIndex("lastTime"));
                String shopName = cursor.getString(cursor.getColumnIndex("shopName"));
                int shopID =cursor.getInt(cursor.getColumnIndex("shopID"));
                int browserNum =cursor.getInt(cursor.getColumnIndex("browserNum"));
                couponList.add(new CouponBean.Coupon(user_id,couponID,couponPics,width,height,
                        couponPraise,isPraise,isCollect,intro,beginTime,lastTime,shopName,shopID,browserNum));
                i++;
                Log.i("SHOWTABLE","_id:" + id);
                Log.i("SHOWTABLE","user_id:" + user_id);
                Log.i("SHOWTABLE","couponID:" + couponID);
                Log.i("SHOWTABLE","couponPics:" + couponPics);
                Log.i("SHOWTABLE","width:" + width);
                Log.i("SHOWTABLE","height:" + height);
                Log.i("SHOWTABLE","couponPraise:" + couponPraise);
                Log.i("SHOWTABLE","isPraise:" + isPraise);
                Log.i("SHOWTABLE","isCollect:" + isCollect);
                Log.i("SHOWTABLE","intro:" + intro);
                Log.i("SHOWTABLE","beginTime:" + beginTime);
                Log.i("SHOWTABLE","lastTime:" + lastTime);
                Log.i("SHOWTABLE","shopName:" + shopName);
                Log.i("SHOWTABLE","shopID:" + shopID);
                Log.i("SHOWTABLE","browserNum:" + browserNum);
                Log.i("SHOWTABLE","-----------------------------第"+i+"个记录---------------------------------------");
            }
            Log.i("SHOWTABLE", "---------------------------查询结果 账户表单完毕-----------------------");
        }finally {
            database.close();
        }
        return couponList;
    }
    public List<GoodBean.Good> queryGoodList(Context context,String goodType,int page,int newestCouponId){
        int index = (page-1)*Config.DATABASE_LODING_NUM;
        int current_user_id = AbSharedUtil.getInt(context, Config.Current_user_id);
        List<GoodBean.Good> goodList = null;
        Cursor cursor = rawQuery("select * from "+Config.DATABASE_TABLE_GOOD+" where user_id="+current_user_id+" and goodType='"+goodType+"' and _id<="+newestCouponId+" order by sellID desc limit "+index+","+Config.DATABASE_LODING_NUM);
        Log.i("SHOWTABLE", "----------------------查询结果_good 账户表单:------------------------");
        try {
            int i = 0;
            if (cursor!=null&&cursor.getCount()>0)
                goodList = new ArrayList<>();
            while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
                int sellID = cursor.getInt(cursor.getColumnIndex("sellID"));
                int width = cursor.getInt(cursor.getColumnIndex("width"));
                int height = cursor.getInt(cursor.getColumnIndex("height"));
                String userName = cursor.getString(cursor.getColumnIndex("userName"));
                String pic = cursor.getString(cursor.getColumnIndex("pic"));
                String price = cursor.getString(cursor.getColumnIndex("price"));
                String _goodType = cursor.getString(cursor.getColumnIndex("goodType"));

                goodList.add(new GoodBean.Good(user_id,sellID,width,height,userName,pic,price,_goodType));
                i++;
                Log.i("SHOWTABLE","_id:" + id);
                Log.i("SHOWTABLE","user_id:" + user_id);
                Log.i("SHOWTABLE","sellID:" + sellID);
                Log.i("SHOWTABLE","width:" + width);
                Log.i("SHOWTABLE","height:" + height);
                Log.i("SHOWTABLE","userName:" + userName);
                Log.i("SHOWTABLE","pic:" + pic);
                Log.i("SHOWTABLE","price:" + price);
                Log.i("SHOWTABLE","goodType:" + _goodType);
                Log.i("SHOWTABLE","-----------------------------第"+i+"个记录---------------------------------------");
            }
            Log.i("SHOWTABLE", "---------------------------查询结果_good 账户表单完毕-----------------------");
        }finally {
            database.close();
        }
        return goodList;
    }
    public List<MoodBean.Mood> queryMoodList(Context context,int page,int newestCouponId){
        int index = (page-1)*Config.DATABASE_LODING_NUM;

        int current_user_id = AbSharedUtil.getInt(context, Config.Current_user_id);
        List<MoodBean.Mood> moodList = null;
        Cursor cursor = rawQuery("select * from "+Config.DATABASE_TABLE_MOOD+" where user_id="+current_user_id+" and _id<="+newestCouponId+" order by moodId desc limit "+index+","+Config.DATABASE_LODING_NUM);
        Log.i("SHOWTABLE", "----------------------查询结果_mood 账户表单:------------------------");
        try {
            int i = 0;
            if (cursor!=null&&cursor.getCount()>0)
                moodList = new ArrayList<>();
            while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
                int moodId = cursor.getInt(cursor.getColumnIndex("moodId"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String paths = cursor.getString(cursor.getColumnIndex("paths"));
                String publishtime = cursor.getString(cursor.getColumnIndex("publishtime"));
                String praiseTimes = cursor.getString(cursor.getColumnIndex("praiseTimes"));
                String commentCount = cursor.getString(cursor.getColumnIndex("commentCount"));

                boolean isPraised = cursor.getInt(cursor.getColumnIndex("isPraised"))==1?true:false;
                String belongschool = cursor.getString(cursor.getColumnIndex("belongschool"));
                String sex = cursor.getString(cursor.getColumnIndex("sex"));
                String userPic = cursor.getString(cursor.getColumnIndex("userPic"));
                String nickName = cursor.getString(cursor.getColumnIndex("nickName"));
                String userName = cursor.getString(cursor.getColumnIndex("userName"));

                moodList.add(new MoodBean.Mood(user_id,moodId,content,paths,publishtime,praiseTimes,commentCount,isPraised,belongschool,sex,userPic,nickName,userName));
                i++;
                Log.i("SHOWTABLE","_id:" + id);
                Log.i("SHOWTABLE","user_id:" + user_id);
                Log.i("SHOWTABLE","moodId:" + moodId);
                Log.i("SHOWTABLE", "content:" + content);
                Log.i("SHOWTABLE","paths:" + paths);
                Log.i("SHOWTABLE","publishtime:" + publishtime);
                Log.i("SHOWTABLE","praiseTimes:" + praiseTimes);
                Log.i("SHOWTABLE","commentCount:" + commentCount);
                Log.i("SHOWTABLE","isPraised:" + isPraised);
                Log.i("SHOWTABLE","belongschool:" + belongschool);
                Log.i("SHOWTABLE","sex:" + sex);
                Log.i("SHOWTABLE","userPic:" + userPic);
                Log.i("SHOWTABLE","nickName:" + nickName);
                Log.i("SHOWTABLE","userName:" + userName);
                Log.i("SHOWTABLE","-----------------------------第"+i+"个记录---------------------------------------");
            }
            Log.i("SHOWTABLE", "---------------------------查询结果_mood 账户表单完毕-----------------------");
        }finally {
            database.close();
        }
        return moodList;
    }
    public List<Topnews.News> queryNewsList(Context context,int page,int newestCouponId){
        int index = (page-1)*Config.DATABASE_LODING_NUM;

        int current_user_id = AbSharedUtil.getInt(context, Config.Current_user_id);
        List<Topnews.News> newsList = null;
        Cursor cursor = rawQuery("select * from "+Config.DATABASE_TABLE_NEW+" where user_id="+current_user_id+" and _id<="+newestCouponId+" order by topId desc limit "+index+","+Config.DATABASE_LODING_NUM);
        Log.i("SHOWTABLE", "----------------------查询结果_new 账户表单:------------------------");
        try {
            int i = 0;
            if (cursor!=null&&cursor.getCount()>0)
                newsList = new ArrayList<>();
            while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int user_id = cursor.getInt(cursor.getColumnIndex("user_id"));
                String topId = cursor.getString(cursor.getColumnIndex("topId"));
                String adminID = cursor.getString(cursor.getColumnIndex("adminID"));
                String adminName = cursor.getString(cursor.getColumnIndex("adminName"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String label = cursor.getString(cursor.getColumnIndex("label"));
                String summary = cursor.getString(cursor.getColumnIndex("summary"));
                String pics = cursor.getString(cursor.getColumnIndex("pics"));

                String browserNum = cursor.getString(cursor.getColumnIndex("browserNum"));
                String praise = cursor.getString(cursor.getColumnIndex("praise"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                int isPraised = cursor.getInt(cursor.getColumnIndex("isPraised"));
                newsList.add(new Topnews.News(user_id,topId,adminID,adminName,title,label,summary,pics,browserNum,praise,url,isPraised));
                i++;
                Log.i("SHOWTABLE","_id:" + id);
                Log.i("SHOWTABLE","user_id:" + user_id);
                Log.i("SHOWTABLE","topId:" + topId);
                Log.i("SHOWTABLE","adminID:" + adminID);
                Log.i("SHOWTABLE","adminName:" + adminName);
                Log.i("SHOWTABLE","title:" + title);
                Log.i("SHOWTABLE","label:" + label);
                Log.i("SHOWTABLE","summary:" + summary);
                Log.i("SHOWTABLE","pics:" + pics);
                Log.i("SHOWTABLE","browserNum:" + browserNum);
                Log.i("SHOWTABLE","praise:" + praise);
                Log.i("SHOWTABLE","url:" + url);
                Log.i("SHOWTABLE","isPraised:" + isPraised);
                Log.i("SHOWTABLE","-----------------------------第"+i+"个记录---------------------------------------");
            }
            Log.i("SHOWTABLE", "---------------------------查询结果_new 账户表单完毕-----------------------");
        }finally {
            database.close();
        }
        return newsList;
    }
    public List<Banner.ListEntity> queryBannerList(int page,int newestBannerId){
        int index = (page-1)*Config.DATABASE_LODING_AD_NUM;
        List<Banner.ListEntity> bannerList = null;
        Cursor cursor = rawQuery("select * from "+Config.DATABASE_TABLE_BANNER+" where  _id<="+newestBannerId+" order by adId desc limit "+index+","+Config.DATABASE_LODING_AD_NUM);
        Log.i("SHOWTABLE", "----------------------查询结果_banner 账户表单:------------------------");
        try {
            int i = 0;
            if (cursor!=null&&cursor.getCount()>0)
                bannerList = new ArrayList<>();
            while(cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                int adId = cursor.getInt(cursor.getColumnIndex("adId"));
                String photo = cursor.getString(cursor.getColumnIndex("photo"));
                String alink = cursor.getString(cursor.getColumnIndex("alink"));
                bannerList.add(new Banner.ListEntity(adId, photo, alink));
                i++;
                Log.i("SHOWTABLE","_id:" + id);
                Log.i("SHOWTABLE","adId:" + adId);
                Log.i("SHOWTABLE","photo:" + photo);
                Log.i("SHOWTABLE","-----------------------------第"+i+"个记录---------------------------------------");
            }
            Log.i("SHOWTABLE", "---------------------------查询结果_banner 账户表单完毕-----------------------");
        }finally {
            database.close();
        }
        return bannerList;
    }

    //获取当前用户本地_id
    public int queryCurrentUserId(String userName){
        int _id = -1;
        Cursor cursor = rawQuery("select * from "+Config.DATABASE_TABLE_USER+" where account="+userName);
        try {
            while(cursor.moveToNext()){
                _id = cursor.getInt(cursor.getColumnIndex("_id"));
            }
        }finally {
            database.close();
        }
        Log.i("SHOWTABLE","queryCurrentUserId:" + _id+"   userName:"+userName);

        return _id;
    }
    //查询获取最新的本地_id
    public int queryCouponCurrentNewestId(Context context){
        int user_id = AbSharedUtil.getInt(context,Config.Current_user_id);

        int _id = -1;
        Cursor cursor = rawQuery("select max(_id) as max_id from "+Config.DATABASE_TABLE_COUPON+" where user_id="+user_id);
        try {
            while(cursor.moveToNext()){
                _id = cursor.getInt(cursor.getColumnIndex("max_id"));
            }
        }finally {
            database.close();
        }
        Log.i("SHOWTABLE","queryCouponCurrentNewestId:" + _id+"   user_id:"+user_id);
        return _id;
    }
    public int queryGoodCurrentNewestId(Context context,String goodType){
        int user_id = AbSharedUtil.getInt(context,Config.Current_user_id);

        int _id = -1;
        Cursor cursor = rawQuery("select max(_id) as max_id from "+Config.DATABASE_TABLE_GOOD+" where user_id="+user_id+" and goodType='"+goodType+"'");
        try {
            while(cursor.moveToNext()){
                _id = cursor.getInt(cursor.getColumnIndex("max_id"));
            }
        }finally {
            database.close();
        }
        Log.i("SHOWTABLE","queryGoodCurrentNewestId:" + _id+"   user_id:"+user_id);

        return _id;
    }
    public int queryNewCurrentNewestId(Context context){
        int user_id = AbSharedUtil.getInt(context,Config.Current_user_id);

        int _id = -1;
        Cursor cursor = rawQuery("select max(_id) as max_id from "+Config.DATABASE_TABLE_NEW+" where user_id="+user_id);
        try {
            while(cursor.moveToNext()){
                _id = cursor.getInt(cursor.getColumnIndex("max_id"));
            }
        }finally {
            database.close();
        }
        Log.i("SHOWTABLE","queryNewCurrentNewestId:" + _id+"   user_id:"+user_id);

        return _id;
    }
    public int queryMoodCurrentNewestId(Context context){
        int user_id = AbSharedUtil.getInt(context,Config.Current_user_id);
        int _id = -1;
        Cursor cursor = rawQuery("select max(_id) as max_id from "+Config.DATABASE_TABLE_MOOD+" where user_id="+user_id);
        try {
            while(cursor.moveToNext()){
                _id = cursor.getInt(cursor.getColumnIndex("max_id"));
            }
        }finally {
            database.close();
        }
        Log.i("SHOWTABLE","queryMoodCurrentNewestId:" + _id+"   user_id:"+user_id);

        return _id;
    }
    public int queryBannerCurrentNewestId(){
        int _id = -1;
        Cursor cursor = rawQuery("select max(_id) as max_id from "+Config.DATABASE_TABLE_BANNER);
        try {
            while(cursor.moveToNext()){
                _id = cursor.getInt(cursor.getColumnIndex("max_id"));
            }
        }finally {
            database.close();
        }
        Log.i("SHOWTABLE","queryBannerCurrentNewestId:" + _id);

        return _id;
    }*/
}
