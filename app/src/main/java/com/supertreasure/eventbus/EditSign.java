package com.supertreasure.eventbus;

/**
 * Created by prj on 2016/5/2.
 */
public class EditSign {
    private static EditSign instance;
    private String sign;

    public static EditSign getInstance(){
        if(instance==null){
            instance = new EditSign();
        }
        return instance;
    }

    public EditSign(){

    }

    public EditSign(String sign){
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
