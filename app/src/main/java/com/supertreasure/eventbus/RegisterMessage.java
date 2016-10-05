package com.supertreasure.eventbus;

/**
 * Created by prj on 2016/4/25.
 */
public class RegisterMessage {

    public  static RegisterMessage instance;
    private String userName;
    private String password;

    public static RegisterMessage getInstance(){
        if(instance == null)
            return new RegisterMessage();
        return instance;
    }

    public RegisterMessage(String userName,String password){
        this.userName = userName;
        this.password = password;
    }

    public RegisterMessage(){
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
