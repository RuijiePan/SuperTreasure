package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshMyShopRemove {//下架
    public static RefleshMyShopRemove instance;

    public static RefleshMyShopRemove getInstance() {
        if (instance == null){
            instance = new RefleshMyShopRemove();
        }
        return instance;
    }
}
