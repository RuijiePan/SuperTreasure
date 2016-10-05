package com.supertreasure.eventbus;

import java.util.ArrayList;

/**
 * Created by prj on 2016/4/24.
 */
public class RefreshMoodGridView {
    public static RefreshMoodGridView instance;
    private ArrayList<String> pathList;

    public static RefreshMoodGridView getInstance() {
        if (instance == null){
            instance = new RefreshMoodGridView();
        }
        return instance;
    }

    public RefreshMoodGridView(){
    }

    public RefreshMoodGridView(ArrayList<String> pathList){
        this.pathList = pathList;
    }

    public ArrayList<String> getPathList() {
        return pathList;
    }

    public void setPathList(ArrayList<String> pathList) {
        this.pathList = pathList;
    }
}
