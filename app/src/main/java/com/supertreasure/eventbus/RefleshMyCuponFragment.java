package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshMyCuponFragment {
    public static RefleshMyCuponFragment instance;

    public static RefleshMyCuponFragment getInstance() {
        if (instance == null){
            instance = new RefleshMyCuponFragment();
        }
        return instance;
    }
}
