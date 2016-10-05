package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/13.
 */
public class RefleshMineActivity {
    private String userPic;
    public static RefleshMineActivity instance;

    public static RefleshMineActivity getInstance() {
        if (instance == null){
            instance = new RefleshMineActivity();
        }
        return instance;
    }
    public RefleshMineActivity(){}
    public RefleshMineActivity(String userPic) {
        this.userPic = userPic;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
}
