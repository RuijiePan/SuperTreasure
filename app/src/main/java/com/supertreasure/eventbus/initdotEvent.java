package com.supertreasure.eventbus;

import android.support.v4.view.ViewPager;

/**
 * Created by prj on 2016/5/2.
 */
public class initdotEvent {
    private static initdotEvent instance;
    private int index;

    public static initdotEvent getInstance(){
        if(instance==null){
            instance = new initdotEvent();
        }
        return instance;
    }

    public initdotEvent(){

    }

    public initdotEvent(int index){
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
