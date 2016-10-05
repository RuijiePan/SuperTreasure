package com.supertreasure.bean;

import java.util.List;

/**
 * Created by yum on 2016/2/26.
 */
public class MeBeen {
    private String status;
    private Me mydata;

    public Me getMydata() {
        return mydata;
    }

    public void setMydata(Me mydata) {
        this.mydata = mydata;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public static class Me{
        private String userId;
        private String userPic;
        private String nickName;
        private String sign;
        private String money;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
