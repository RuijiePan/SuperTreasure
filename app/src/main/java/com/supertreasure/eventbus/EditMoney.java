package com.supertreasure.eventbus;

/**
 * Created by prj on 2016/5/2.
 */
public class EditMoney {
    private EditMoney instance;
    private String money;

    public EditMoney getInstance(){
        if(instance==null)
            instance = new EditMoney();
        return instance;
    }

    public EditMoney(String money){
        this.money = money;
    }

    public EditMoney(){
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
