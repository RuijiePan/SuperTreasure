package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshOutschoolHead {//更新校内说说
    public static RefleshOutschoolHead instance;

    public static RefleshOutschoolHead getInstance() {
        if (instance == null){
            instance = new RefleshOutschoolHead();
        }
        return instance;
    }
}
