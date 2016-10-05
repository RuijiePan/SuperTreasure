package com.supertreasure.bean;

import android.content.ContentValues;

import java.util.List;

/**
 * Created by Administrator on 2016/1/23.
 */
public class Banner {//广告

    private String status;

    private List<me.itangqi.greendao.Banner> list;

    public List<me.itangqi.greendao.Banner> getList() {
        return list;
    }

    public void setList(List<me.itangqi.greendao.Banner> list) {
        this.list = list;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

/*
    private List<ListEntity> list;

    public List<ListEntity> getList() {
        return list;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }*/

    public static class ListEntity{
        private int _id;
        private int adId;
        private String photo;
        private String alink;

        public ListEntity(int adId, String photo, String alink) {
            this.adId = adId;
            this.photo = photo;
            this.alink = alink;
        }

        public ContentValues toContentValues(){
            ContentValues values = new ContentValues();
            values.put("adId"  ,adId);
            values.put("photo" ,photo);
            values.put("alink" ,alink);
            return values;
        }
        public int getAdId() {
            return adId;
        }

        public void setAdId(int adId) {
            this.adId = adId;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getAlink() {
            return alink;
        }

        public void setAlink(String alink) {
            this.alink = alink;
        }

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        @Override
        public String toString() {
            return "ListEntity{" +
                    "_id=" + _id +
                    ", adId=" + adId +
                    ", photo='" + photo + '\'' +
                    ", alink='" + alink + '\'' +
                    '}';
        }
    }
}
