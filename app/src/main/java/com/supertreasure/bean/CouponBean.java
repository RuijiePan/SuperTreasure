package com.supertreasure.bean;

import android.content.ContentValues;

import java.util.List;

import me.itangqi.greendao.Coupon;

/**
 * Created by Administrator on 2016/2/13.
 */
public class CouponBean {

    private String status;
    private List<me.itangqi.greendao.Coupon> list;

    public List<me.itangqi.greendao.Coupon> getList() {
        return list;
    }

    public void setList(List<me.itangqi.greendao.Coupon> list) {
        this.list = list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
/*
    private List<Coupon> list;
    public List<Coupon> getList() {
        return list;
    }

    public void setList(List<Coupon> list) {
        this.list = list;
    }*/

/*
    public static class Coupon{
        private int _id;
        private int user_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public ContentValues toContentValues(){
            ContentValues values = new ContentValues();
            values.put("user_id"  ,user_id);
            values.put("couponID" ,couponID);
            values.put("couponPics" ,couponPics);
            values.put("width" ,width);
            values.put("height" ,height);
            values.put("couponPraise",couponPraise);
            values.put("isPraise",isPraise);
            values.put("isCollect",isCollect);
            values.put("intro",intro);
            //values.put("isCollect",isCollect);
            values.put("beginTime",beginTime);
            values.put("lastTime",lastTime);
            values.put("shopName",shopName);
            values.put("shopID",shopID);
            values.put("browserNum",browserNum);
            return values;
        }

        public Coupon(int user_id, int couponID, String couponPics, int width, int height,
                      int couponPraise, boolean isPraise, boolean isCollect,
                      String intro,String beginTime,String lastTime,String shopName,int shopID,int browserNum) {
            this.user_id = user_id;
            this.couponID = couponID;
            this.couponPics = couponPics;
            this.width = width;
            this.height = height;
            this.couponPraise = couponPraise;
            this.isPraise = isPraise;
            this.isCollect = isCollect;
            this.intro = intro;
            this.beginTime = beginTime;
            this.lastTime = lastTime;
            this.shopName = shopName;
            this.shopID = shopID;
            this.browserNum = browserNum;
        }

        //------------------------------------------
        private int couponID;
        private String couponPics;
        private int width;
        private int height;
        private int couponPraise;
        private boolean isPraise;
        private boolean isCollect;
        private String intro;
        private String beginTime;
        private String lastTime;
        private String shopName;
        private int shopID;
        private int browserNum;

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getLastTime() {
            return lastTime;
        }

        public void setLastTime(String lastTime) {
            this.lastTime = lastTime;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public int getShopID() {
            return shopID;
        }

        public void setShopID(int shopID) {
            this.shopID = shopID;
        }

        public boolean isPraise() {
            return isPraise;
        }

        public void setPraise(boolean praise) {
            isPraise = praise;
        }

        public boolean isCollect() {
            return isCollect;
        }

        public void setCollect(boolean collect) {
            isCollect = collect;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getCouponID() {
            return couponID;
        }

        public void setCouponID(int couponID) {
            this.couponID = couponID;
        }

        public String getCouponPics() {
            return couponPics;
        }

        public void setCouponPics(String couponPics) {
            this.couponPics = couponPics;
        }

        public int getBrowserNum() {
            return browserNum;
        }

        public void setBrowserNum(int browserNum) {
            this.browserNum = browserNum;
        }

        public int getCouponPraise() {
            return couponPraise;
        }

        public void setCouponPraise(int couponPraise) {
            this.couponPraise = couponPraise;
        }
    }
*/
}
