package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshEleProFragment {
    public static RefleshEleProFragment instance;

    public static RefleshEleProFragment getInstance() {
        if (instance == null){
            instance = new RefleshEleProFragment();
        }
        return instance;
    }
}
