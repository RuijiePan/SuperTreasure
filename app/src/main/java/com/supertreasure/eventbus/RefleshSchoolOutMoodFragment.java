package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshSchoolOutMoodFragment {
    public static RefleshSchoolOutMoodFragment instance;

    public static RefleshSchoolOutMoodFragment getInstance() {
        if (instance == null){
            instance = new RefleshSchoolOutMoodFragment();
        }
        return instance;
    }
}
