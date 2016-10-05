package com.supertreasure.eventbus;

import android.app.Notification;

/**
 * Created by prj on 2016/4/20.
 */
public class NotificationEvent {

    public static NotificationEvent instance;

    public static NotificationEvent getInstance() {
        if (instance == null){
            instance = new NotificationEvent();
        }
        return instance;
    }

}
