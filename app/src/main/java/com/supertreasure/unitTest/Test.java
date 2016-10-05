package com.supertreasure.unitTest;

import android.content.Context;
import android.test.AndroidTestCase;

import com.ab.util.AbSharedUtil;
import com.supertreasure.bean.CouponBean;
import com.supertreasure.bean.GoodBean;
import com.supertreasure.bean.MoodBean;
import com.supertreasure.bean.Topnews;
import com.supertreasure.bean.userLogin;
import com.supertreasure.constant.Constant;
import com.supertreasure.database.DBTool;
import com.supertreasure.util.Config;

/**
 * Created by yum on 2016/3/13.
 */
public class Test extends AndroidTestCase{
/*
    public void test1() throws Throwable {
        Context context = getContext();
        //DBTool tool = DBTool.getInstance(getContext());
        *//*userLogin.User user = new userLogin.User("15622625081","123456","肇庆学院","男","dsadsadsads.jpg","一鸣","yummy");
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
        *//*tool.showUserTable();
        String UPDATE_USER = "update "+ Config.DATABASE_TABLE_USER+" set belongschool=? ,nickName=? where _id=?";
        String EXIST_USER = "select * from "+Config.DATABASE_TABLE_USER +" where _id=?";
        if (tool.ifExist(EXIST_USER,new String[]{"1"}))
            tool.exeSql(UPDATE_USER,new String[]{"肇庆大学","肇庆昵称","1"});
        tool.showUserTable();*//*

        AbSharedUtil.putInt(getContext(),Config.Current_user_id,1);//登录的时候，就要把这个更新

        //DBTool tool = DBTool.getInstance(context);


        userLogin.User user = new userLogin.User("15622625081","123456","肇庆学院","男","dsadsadsads.jpg","一鸣","yummy");
        //插入用户信息，内部判断，存在则更新，不存在则插入
        tool.insertUserTable(user.toContentValues());

        //插入头条信息，内部判断，存在则不处理，不存在则插入
        Topnews.News news = new Topnews.News(1,"2","管理ID","管理名字","标题","标签","总结","dsadsadsa.jpg","20","点钟","dsadsadsadUrl",0);
        tool.insertNewTable(news.toContentValues());

        //插入说说信息，内部判断，存在则不处理，不存在则插入
        MoodBean.Mood mood = new MoodBean.Mood(1,1,"content","dsadsa.jpg","2015-1-1","2016-1-1","50",false,"肇庆学院","男","dsadadsad.jpg","昵称","用户名");
        tool.insertMoodTable(mood.toContentValues());

        //插入商品信息，内部判断，存在则不处理，不存在则插入
        GoodBean.Good good = new GoodBean.Good(1,2,300,400,"用户名","dsadsadsa.jpg","20", Config.GoodType_Decorate);
        tool.inserTGoodTable(good.toContentValues());

        //插入优惠卷信息，内部判断，存在则不处理，不存在则插入
        CouponBean.Coupon coupon = new CouponBean.Coupon(1,2,"dsadsa.jpg",300,400,20,false,true,"介绍","2016-04-30 10:11:16","2016-04-37 10:11:16","点名",1,90);
        tool.insertCouponTable(coupon.toContentValues());
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


    }
    public void test2(){
        System.out.println("");
    }*/
}
