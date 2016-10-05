package com.supertreasure;

import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.ab.util.AbSharedUtil;
import com.supertreasure.bean.GoodBean;
import com.supertreasure.bean.MoodBean;
import com.supertreasure.bean.Topnews;
import com.supertreasure.bean.userLogin;
import com.supertreasure.database.DBTool;
import com.supertreasure.greenDaoUtils.GreenUtils;
import com.supertreasure.main.TheApp;
import com.supertreasure.util.Config;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.Property;
import de.greenrobot.dao.query.QueryBuilder;
import me.itangqi.greendao.Banner;
import me.itangqi.greendao.Coupon;
import me.itangqi.greendao.DaoMaster;
import me.itangqi.greendao.DaoSession;
import me.itangqi.greendao.Goods;
import me.itangqi.greendao.Mood;
import me.itangqi.greendao.New;
import me.itangqi.greendao.NewDao;
import me.itangqi.greendao.User;
import me.itangqi.greendao.UserDao;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
 /*   public void test1() throws Exception {
        System.out.println("test");
        Context context = getContext();

        DBTool tool = DBTool.getInstance(context);
        userLogin.User user = new userLogin.User("15622625081","123456","肇庆学院","男","dsadsadsads.jpg","一鸣","yummy");
        DBTool.getInstance(getContext()).insert(Config.DATABASE_TABLE_USER, user.toContentValues());
        DBTool.getInstance(getContext()).showUserTable();

        Topnews.News news = new Topnews.News(1,"2","管理名字","标题","标签","总结","dsadsadsa.jpg","20","点钟","dsadsadsadUrl");
        DBTool.getInstance(getContext()).insert(Config.DATABASE_TABLE_NEW, news.toContentValues());
        DBTool.getInstance(getContext()).showNewsTable();


        MoodBean.Mood mood = new MoodBean.Mood(1,1,"content","dsadsa.jpg","2015-1-1","2016-1-1","50",false,"肇庆学院","男","dsadadsad.jpg","昵称","用户名");
        DBTool.getInstance(getContext()).insert(Config.DATABASE_TABLE_MOOD, mood.toContentValues());
        DBTool.getInstance(getContext()).showMoodTable();

        GoodBean.Good good = new GoodBean.Good(1,2,300,400,"用户名","dsadsadsa.jpg","20", Config.GoodType_Decorate);
        DBTool.getInstance(getContext()).insert(Config.DATABASE_TABLE_GOOD, good.toContentValues());
        DBTool.getInstance(getContext()).showGoodTable();

        CouponBean.Coupon coupon = new CouponBean.Coupon(1,2,"dsadsa.jpg",300,400,20,false,true,90);
        DBTool.getInstance(getContext()).insert(Config.DATABASE_TABLE_COUPON, coupon.toContentValues());
        DBTool.getInstance(getContext()).showCouponTable();*//*

        //DBTool tool = DBTool.getInstance(this);
        tool.showUserTable();
        String UPDATE_USER = "update "+ Config.DATABASE_TABLE_USER+" set belongschool=? ,nickName=? where _id=?";
        String EXIST_USER = "select * from "+Config.DATABASE_TABLE_USER +" where _id=?";
        if (tool.ifExist(EXIST_USER,new String[]{"1"}))
            tool.exeSql(UPDATE_USER,new String[]{"肇庆大学","肇庆昵称","1"});
        tool.showUserTable();

        AbSharedUtil.putInt(context, Config.Current_user_id,1);//登录的时候，就要把这个更新

        //DBTool tool = DBTool.getInstance(context);


        userLogin.User user = new userLogin.User("15622625081","123456","肇庆学院","男","dsadsadsads.jpg","一鸣","yummy");
        //插入用户信息，内部判断，存在则更新，不存在则插入
        tool.insertUserTable(user.toContentValues());

        //插入头条信息，内部判断，存在则不处理，不存在则插入
       // Topnews.News news = new Topnews.News(1,"2","1","管理名字","标题","标签","总结","dsadsadsa.jpg","20","点钟","dsadsadsadUrl");
       // tool.insertNewTable(news.toContentValues());

        //插入说说信息，内部判断，存在则不处理，不存在则插入
        MoodBean.Mood mood = new MoodBean.Mood(1,1,"content","dsadsa.jpg","2015-1-1","2016-1-1","50",false,"肇庆学院","男","dsadadsad.jpg","昵称","用户名");
        tool.insertMoodTable(mood.toContentValues());

        //插入商品信息，内部判断，存在则不处理，不存在则插入
        GoodBean.Good good = new GoodBean.Good(1,2,300,400,"用户名","dsadsadsa.jpg","20", Config.GoodType_Decorate);
        tool.inserTGoodTable(good.toContentValues());

        //插入优惠卷信息，内部判断，存在则不处理，不存在则插入
       // CouponBean.Coupon coupon = new CouponBean.Coupon(1,2,"dsadsa.jpg",300,400,20,false,true,90);
       // tool.insertCouponTable(coupon.toContentValues());
        //展示用户表所有信息
        tool.showUserTable();

        //获取该用户的最新的ID
        tool.queryCurrentUserId("15622625081");

        //展示优惠卷表所有信息
        tool.showCouponTable();

        //分页查询优惠卷信息
        tool.queryCouponList(context,1,tool.queryCouponCurrentNewestId(context));

        //展示商品表所有信息
        tool.showGoodTable();
        //分页查询商品信息
        tool.queryGoodList(context,Config.GoodType_Decorate,1,tool.queryGoodCurrentNewestId(context,Config.GoodType_Eleproduct));

        //展示说说表所有信息
        tool.showMoodTable();

        //分页查询说说信息
        tool.queryMoodList(context,1,tool.queryMoodCurrentNewestId(context));

        //展示头条表所有信息
        tool.showNewsTable();
        //分页查询头条信息
        tool.queryNewsList(context,1,tool.queryNewCurrentNewestId(context));

    }*/
 /*   public void test2() throws Exception{
        Context context = getContext();
        DBTool tool = DBTool.getInstance(context);
        tool.deleteGoodTable(183);
        tool.deleteGoodTable(183);
        tool.deleteNewTable("4");
        tool.deleteNewTable("4");
        tool.deleteCouponTable(11);
        tool.deleteCouponTable(11);
        tool.deleteMoodTable(76);
        tool.showMoodTable();
        List<Topnews.News> newsList = new ArrayList<>();
        Topnews.News news = new Topnews.News(1,"2","管理名字","标题","标签","总结","dsadsadsa.jpg","20","点钟","dsadsadsadUrl","",1);

        newsList.add(news);
        newsList.add(news);
        newsList.add(news);
        tool.insertNewTable(context,newsList);
    }*/
    public void test3() throws Exception{
        SQLiteDatabase db;
        DaoMaster daoMaster;
        DaoSession daoSession;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "notes-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        //daoSession.getUserDao().insert(new User(null, "account", "password", "belongschool", "sex", "userPic", "nickName", "userName"));
        //daoSession.getUserDao().insert(new User(null, "account", "password", "belongschool", "sex", "userPic", "nickName", "userName"));
        //daoSession.getGoodsDao().insert(new Goods(null, 2l, 1, 200, 300, "userName", "pic", "price2", "goodType"));
        //daoSession.getGoodsDao().insert(new Goods(null, 2l, 1, 200, 300, "userName", "pic", "price2", "goodType"));
        //daoSession.getGoodsDao().insert(new Goods(null, 3l, 1, 200, 300, "userName", "pic", "price3", "goodType"));
        //daoSession.getGoodsDao().insert(new Goods(null, 3l, 1, 200, 300, "userName", "pic", "price3", "goodType"));
        QueryBuilder qb = daoSession.getUserDao().queryBuilder();
        qb.whereOr(UserDao.Properties.Id.eq(1), UserDao.Properties.Id.eq(3));
        //qb.where(UserDao.Properties.Account.eq("account3"));
        qb.orderDesc(UserDao.Properties.Id);

        //qb.where(UserDao.Properties.Id.eq(2));
        List<User> userlist = qb.list();
        for (User user:userlist) {
            //Log.i("test3","user_id = "+user.getId()+"  account = "+user.getAccount());
            List<Goods> goodsList = user.getGoods();
            for (Goods goods:goodsList) {
                Log.i("test3","good_id = "+goods.getId()+"  price = "+ goods.getPrice());

            }
        }


    }
    public void testUpdate() {
        SQLiteDatabase db;
        DaoMaster daoMaster;
        DaoSession daoSession;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "notes-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        //daoSession.getUserDao().update(new User(3l, "account3", "password3", "belongschool3", "sex3", "userPic3", "nickName3", "userName3"));
    }
    public void test4(){
        GreenUtils.init(getContext());
        GreenUtils greenUtils = GreenUtils.getInstance();
        Context context = getContext();
/*
        long referenceNewsId = greenUtils.queryMoodCurrentNewestId(getContext());
        List<New> newsList = greenUtils.getNewDao().queryBuilder()
                .where(NewDao.Properties.User_id.eq(1))
                .where(NewDao.Properties.Id.le(referenceNewsId))
                .orderDesc(NewDao.Properties.Topid)
                .offset((1-1)*Config.DATABASE_LODING_NUM)
                .limit(Config.DATABASE_LODING_NUM)
                .list();

        Log.i("test4","newsList= " + newsList.toString());
        Log.i("test4","referenceNewsId= " + greenUtils.queryNewCurrentNewestId(getContext()));
        Log.i("test4","referenceNewsId= " + greenUtils.queryCouponCurrentNewestId(getContext()));
        Log.i("test4","referenceNewsId= " + greenUtils.queryGoodCurrentNewestId(getContext(), Config.GoodType_Decorate));
        Log.i("test4","referenceNewsId= " + greenUtils.queryMoodCurrentNewestId(getContext()));
*/
        Log.d("test4","=================goods=====================");

        int currentreferencePage  = 2;
        long referenceoId = greenUtils.queryGoodCurrentNewestId(context, Config.GoodType_Decorate);
        List<Goods> decorateList = greenUtils.queryGoodList(context,Config.GoodType_Decorate,currentreferencePage,referenceoId);
        Log.d("test4","referenceoId:"+referenceoId);

        for (Goods goods : decorateList) {
            Log.d("test4",goods.toString());
        }
        Log.d("test4","=================mood=====================");

        referenceoId = greenUtils.queryMoodCurrentNewestId(context,Config.DATABASE_MOOD_TYPE_IN);
        Log.d("test4","referenceoId:"+referenceoId);

        List<Mood> moodList = greenUtils.queryMoodList(context, currentreferencePage, referenceoId,Config.DATABASE_MOOD_TYPE_IN);
        for (Mood mood : moodList) {
            Log.d("test4",mood.toString());
        }
        Log.d("test4","=================news=====================");

        referenceoId = greenUtils.queryNewCurrentNewestId(context,Config.DATABASE_NEWS_TYPE_IN);
        Log.d("test4","referenceoId:"+referenceoId);

        List<New> newsList = greenUtils.queryNewsList(context, currentreferencePage, referenceoId,Config.DATABASE_NEWS_TYPE_IN);
        for (New aNew : newsList) {
            Log.d("test4",aNew.toString());

        }
        Log.d("test4","=================banner=====================");

        referenceoId = greenUtils.queryBannerCurrentNewestId(Config.DATABASE_BANNER_TYPE_IN);
        Log.d("test4","referenceoId:"+referenceoId);

        //List<Banner> bannerList = greenUtils.queryBannerList(currentreferencePage, referenceoId,Config.DATABASE_BANNER_TYPE_IN);
        List<Banner> bannerList = greenUtils.queryBannerList(Config.DATABASE_BANNER_TYPE_IN);
        for (Banner banner : bannerList) {
            Log.d("test4",banner.toString());

        }
        Log.d("test4","=================coupon=====================");

        referenceoId = greenUtils.queryCouponCurrentNewestId(context);
        Log.d("test4","referenceoId:"+referenceoId);

        List<Coupon> couponList = greenUtils.queryCouponList(context,currentreferencePage,referenceoId);
        for (Coupon coupon : couponList) {
            Log.d("test4",coupon.toString());

        }

    }
    public void test5(){
        Log.i("test5",getContext().getFilesDir().getAbsolutePath());
    }


}