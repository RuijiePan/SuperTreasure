package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshOtherProFragment {
    public static RefleshOtherProFragment instance;

    public static RefleshOtherProFragment getInstance() {
        if (instance == null){
            instance = new RefleshOtherProFragment();
        }
        return instance;
    }
}
