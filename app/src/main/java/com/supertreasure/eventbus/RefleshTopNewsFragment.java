package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshTopNewsFragment {//更新校内说说
    public static RefleshTopNewsFragment instance;

    public static RefleshTopNewsFragment getInstance() {
        if (instance == null){
            instance = new RefleshTopNewsFragment();
        }
        return instance;
    }
}
