package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshMyShopSold {//已出售
    public static RefleshMyShopSold instance;

    public static RefleshMyShopSold getInstance() {
        if (instance == null){
            instance = new RefleshMyShopSold();
        }
        return instance;
    }
}
