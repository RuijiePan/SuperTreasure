package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshOutschool {//更新校内说说
    public static RefleshOutschool instance;

    public static RefleshOutschool getInstance() {
        if (instance == null){
            instance = new RefleshOutschool();
        }
        return instance;
    }
}
