package com.supertreasure.bean;


import android.content.ContentValues;

import java.util.List;

import me.itangqi.greendao.User;

/**
 * Created by Administrator on 2016/1/24.
 */
public class userLogin {

    private String status;
    private String token;
    private me.itangqi.greendao.User user;

    public me.itangqi.greendao.User getUser() {
        return user;
    }

    public void setUser(me.itangqi.greendao.User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
/*    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/

/*
    public static class User{
        private int _id;
        private String account;
        private String password;
        public ContentValues toContentValues(){
            ContentValues values = new ContentValues();
            values.put("account"  ,account);
            values.put("password" ,password);
            values.put("belongschool" ,belongschool);
            values.put("sex" ,sex);
            values.put("userPic" ,userPic);
            values.put("nickName",nickName);
            values.put("userName",userName);
            return values;
        }

        public User(String account, String password, String belongschool, String sex, String userPic, String nickName, String userName) {
            this.account = account;
            this.password = password;
            this.belongschool = belongschool;
            this.sex = sex;
            this.userPic = userPic;
            this.nickName = nickName;
            this.userName = userName;
        }

        //------------------------------------------------
        private String belongschool;
        private String sex;
        private String userPic;
        private String nickName;
        private String userName;

        public String getBelongschool() {
            return belongschool;
        }

        public void setBelongschool(String belongschool) {
            this.belongschool = belongschool;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getUserPic() {
            return userPic;
        }

        public void setUserPic(String userPic) {
            this.userPic = userPic;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int get_id() {
            return _id;
        }

        public void set_id(int _id) {
            this._id = _id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
*/
}
