package com.supertreasure.bean;

import android.content.ContentValues;

import java.util.List;

/**
 * Created by yum on 2016/1/29.
 */
public class GoodsBeen {
    public static final int HadSold = 1;
    public static final int HadPublish = 2;
    public static final int HadRemove = 3;

    private String status;
    private List<Goods> list;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Goods> getList() {
        return list;
    }

    public void setList(List<Goods> list) {
        this.list = list;
    }

    public static class Goods{
        private int soldStatus = HadPublish;

        private String status;
        private String sellID;
        private String introduction;
        private String pic;
        private String price;

        public ContentValues toContentValues(){
            ContentValues values = new ContentValues();
            values.put("sellID",sellID);
            values.put("status",status);
            values.put("introduction",introduction);
            values.put("pic",pic);
            values.put("price",price);
            return values;
        }

        public String getSellID() {
            return sellID;
        }

        public void setSellID(String sellID) {
            this.sellID = sellID;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
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

        public int getSoldStatus() {
            return soldStatus;
        }

        public void setSoldStatus(int soldStatus) {
            this.soldStatus = soldStatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return "Goods{" +
                    "soldStatus=" + soldStatus +
                    ", status='" + status + '\'' +
                    ", sellID='" + sellID + '\'' +
                    ", introduction='" + introduction + '\'' +
                    ", pic='" + pic + '\'' +
                    ", price='" + price + '\'' +
                    '}';
        }

    }

}
