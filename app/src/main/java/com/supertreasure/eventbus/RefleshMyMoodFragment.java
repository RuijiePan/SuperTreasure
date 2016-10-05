package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshMyMoodFragment {//更新校内说说
    public static RefleshMyMoodFragment instance;

    public static RefleshMyMoodFragment getInstance() {
        if (instance == null){
            instance = new RefleshMyMoodFragment();
        }
        return instance;
    }
}
