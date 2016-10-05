package com.supertreasure.bean;

import android.content.ContentValues;

import java.util.List;

import me.itangqi.greendao.New;

/**
 * Created by Administrator on 2016/1/25.
 */
public class Topnews {//新闻
    private String status;
    private List<New> list;

    @Override
    public String toString() {
        return "Topnews{" +
                "status='" + status + '\'' +
                ", list=" + list +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<New> getList() {
        return list;
    }

    public void setList(List<New> list) {
        this.list = list;
    }

/*
    public static class News{
        private int _id;
        private int user_id;
        public ContentValues toContentValues(){
            ContentValues values = new ContentValues();
            values.put("user_id"  ,user_id);
            values.put("topId" ,topId);
            values.put("adminID",adminID);
            values.put("adminName" ,adminName);
            values.put("title" ,title);
            values.put("label" ,label);
            values.put("summary",summary);
            values.put("pics",pics);
            values.put("browserNum",browserNum);
            values.put("praise",praise);
            values.put("url",url);
            values.put("isPraised",0);
            return values;
        }

        public News(int user_id, String topId, String adminID,String adminName, String title, String label, String summary, String pics, String browserNum, String praise, String url,int isPraised) {
            this.user_id = user_id;
            this.topId = topId;
            this.adminID = adminID;
            this.adminName = adminName;
            this.title = title;
            this.label = label;
            this.summary = summary;
            this.pics = pics;
            this.browserNum = browserNum;
            this.praise = praise;
            this.url = url;
            this.isPraised = isPraised>0;
        }

        //------------------------------------------
        private String topId;
        private String adminName;
        private String adminID;
        private String title;
        private String label;
        private String summary;
        private String pics;
        private String browserNum;
        private String praise;
        private String url;
        private boolean isPraised;

        public String getAdminID() {
            return adminID;
        }

        public void setAdminID(String adminID) {
            this.adminID = adminID;
        }

        public String getBrowserNum() {
            return browserNum;
        }

        public void setBrowserNum(String browserNum) {
            this.browserNum = browserNum;
        }

        public String getTopId() {
            return topId;
        }

        public void setTopId(String topId) {
            this.topId = topId;
        }

        public String getAdminName() {
            return adminName;
        }

        public void setAdminName(String adminName) {
            this.adminName = adminName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getPics() {
            return pics;
        }

        public void setPics(String pics) {
            this.pics = pics;
        }

        public String getPraise() {
            return praise;
        }

        public void setPraise(String praise) {
            this.praise = praise;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public boolean isPraised() {
            return isPraised;
        }

        public void setPraised(boolean praised) {
            isPraised = praised;
        }
    }
*/
}
