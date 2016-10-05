package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshDecorFragment {
    public static RefleshDecorFragment instance;

    public static RefleshDecorFragment getInstance() {
        if (instance == null){
            instance = new RefleshDecorFragment();
        }
        return instance;
    }
}
