package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshMyShopPublish {//发布
    public static RefleshMyShopPublish instance;

    public static RefleshMyShopPublish getInstance() {
        if (instance == null){
            instance = new RefleshMyShopPublish();
        }
        return instance;
    }
}
