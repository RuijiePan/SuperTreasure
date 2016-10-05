package com.supertreasure.bean;

/**
 * Created by yum on 2016/2/26.
 */
public class AboutMeBeen {
    private String status;
    private AboutMe mydata;

    public AboutMe getAboutMe() {
        return mydata;
    }

    public void setAboutMe(AboutMe mydata) {
        this.mydata = mydata;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class AboutMe{
        private String userId;
        private String userPic;
        private String nickName;
        private String sign;
        private String belongschool;
        private String sex;
        private String selfdom;
        private String hobby;
        private String money;
        private String love;
        private String userName;

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

        public String getSelfdom() {
            return selfdom;
        }

        public void setSelfdom(String selfdom) {
            this.selfdom = selfdom;
        }

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getLove() {
            return love;
        }

        public void setLove(String love) {
            this.love = love;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
