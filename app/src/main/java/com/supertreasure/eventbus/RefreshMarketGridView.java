package com.supertreasure.eventbus;

import java.util.ArrayList;

/**
 * Created by prj on 2016/4/24.
 */
public class RefreshMarketGridView {
    public static RefreshMarketGridView instance;
    private ArrayList<String> pathList;

    public static RefreshMarketGridView getInstance() {
        if (instance == null){
            instance = new RefreshMarketGridView();
        }
        return instance;
    }

    public RefreshMarketGridView(){
    }

    public RefreshMarketGridView(ArrayList<String> pathList){
        this.pathList = pathList;
    }

    public ArrayList<String> getPathList() {
        return pathList;
    }

    public void setPathList(ArrayList<String> pathList) {
        this.pathList = pathList;
    }
}
