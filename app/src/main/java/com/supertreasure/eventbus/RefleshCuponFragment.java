package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshCuponFragment {
    public static RefleshCuponFragment instance;

    public static RefleshCuponFragment getInstance() {
        if (instance == null){
            instance = new RefleshCuponFragment();
        }
        return instance;
    }
}
