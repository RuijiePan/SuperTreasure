package com.supertreasure.eventbus;

/**
 * Created by prj on 2016/4/25.
 */
public class UpdateNewParams {

    public static UpdateNewParams instance;
    private String browserNum;
    private String praiseTimes;
    private String type;
    private boolean isPraised;
    private int position;

    public static UpdateNewParams getInstance() {
        if (instance == null)
            return new UpdateNewParams();
        return instance;
    }

    public UpdateNewParams(String browserNum, String praiseTimes, boolean isPraised,int position,String type) {
        this.browserNum = browserNum;
        this.praiseTimes = praiseTimes;
        this.isPraised = isPraised;
        this.position = position;
        this.type = type;
    }

    public UpdateNewParams() {
    }

    public String getBrowserNum() {
        return browserNum;
    }

    public void setBrowserNum(String browserNum) {
        this.browserNum = browserNum;
    }

    public String getPraiseTimes() {
        return praiseTimes;
    }

    public void setPraiseTimes(String praiseTimes) {
        this.praiseTimes = praiseTimes;
    }

    public boolean isPraised() {
        return isPraised;
    }

    public void setPraised(boolean praised) {
        isPraised = praised;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
