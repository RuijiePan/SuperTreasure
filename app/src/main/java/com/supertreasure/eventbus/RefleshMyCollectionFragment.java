package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshMyCollectionFragment {
    public static RefleshMyCollectionFragment instance;

    public static RefleshMyCollectionFragment getInstance() {
        if (instance == null){
            instance = new RefleshMyCollectionFragment();
        }
        return instance;
    }
}
