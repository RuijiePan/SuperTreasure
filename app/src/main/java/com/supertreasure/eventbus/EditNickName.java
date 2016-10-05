package com.supertreasure.eventbus;

/**
 * Created by prj on 2016/4/28.
 */
public class EditNickName {
    private static EditNickName instance;
    private String nickName;

    public static EditNickName getInstance(){
        if(instance==null){
            instance = new EditNickName();
        }
        return instance;
    }

    public EditNickName(){

    }

    public EditNickName(String nickName){
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
