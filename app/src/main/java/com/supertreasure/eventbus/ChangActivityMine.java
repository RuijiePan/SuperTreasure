package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/13.
 */
public class ChangActivityMine {
    private String userPic;

    public ChangActivityMine(String userPic) {
        this.userPic = userPic;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
}
