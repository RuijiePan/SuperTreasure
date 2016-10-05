package me.itangqi.greendao;

/**
 * Created by yum on 2016/4/9.
 */
public class People {
    private String belongschool;
    private String sex;
    private String userPic;
    private String nickName;
    private String userName;
    private String userId;//网络上的userId

    public People(String belongschool, String sex, String userPic, String nickName, String userName) {
        this.belongschool = belongschool;
        this.sex = sex;
        this.userPic = userPic;
        this.nickName = nickName;
        this.userName = userName;
    }

    public People(String userPic, String nickName, String userId) {
        this.userPic = userPic;
        this.nickName = nickName;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "People{" +
                "belongschool='" + belongschool + '\'' +
                ", sex='" + sex + '\'' +
                ", userPic='" + userPic + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
