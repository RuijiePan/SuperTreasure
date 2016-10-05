package com.supertreasure.bean;

import android.content.ContentValues;

import java.util.List;

import me.itangqi.greendao.Goods;

/**
 * Created by Administrator on 2016/2/19.
 */
public class GoodBean {

    private String status;
    private List<Goods> list;

    public List<Goods> getList() {
        return list;
    }

    public void setList(List<Goods> list) {
        this.list = list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
/*    private List<Good> list;

    public List<Good> getList() {
        return list;
    }

    public void setList(List<Good> list) {
        this.list = list;
    }*/

/*
    public static class Good{
        private int _id;
        private int user_id;
        private String goodType;
        public ContentValues toContentValues(){
            ContentValues values = new ContentValues();
            values.put("user_id"    ,user_id);
            values.put("sellID"     ,sellID);
            values.put("width"      ,width);
            values.put("height"     ,height);
            values.put("userName"   ,userName);
            values.put("pic"        ,pic);
            values.put("price"      ,price);
            values.put("goodType"     ,goodType);
            return values;
        }

        public Good(int user_id, int sellID, int width, int height, String userName, String pic, String price,String goodType) {
            this.user_id = user_id;
            this.sellID = sellID;
            this.width = width;
            this.height = height;
            this.userName = userName;
            this.pic = pic;
            this.price = price;
            this.goodType = goodType;
        }

        //------------------------------------------
        private int sellID;
        private int width;
        private int height;
        private String userName;
        private String pic;
        private String price;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getSellID() {
            return sellID;
        }

        public void setSellID(int sellID) {
            this.sellID = sellID;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getGoodType() {
            return goodType;
        }

        public void setGoodType(String goodType) {
            this.goodType = goodType;
        }
    }
*/
}
