package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/4/21.
 */
public class RefleshSchoolInMoodFragment {//更新校内说说
    public static RefleshSchoolInMoodFragment instance;

    public static RefleshSchoolInMoodFragment getInstance() {
        if (instance == null){
            instance = new RefleshSchoolInMoodFragment();
        }
        return instance;
    }
}
