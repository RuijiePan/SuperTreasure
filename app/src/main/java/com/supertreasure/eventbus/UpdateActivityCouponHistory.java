package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/5/16.
 */
public class UpdateActivityCouponHistory {
    private boolean isPraise;
    private boolean isCollect;
    private int praiseNum;
    private int browseNum;
    private int position;
    private int couponID;
    public UpdateActivityCouponHistory(){}
    public UpdateActivityCouponHistory(boolean isPraise, boolean isCollect, int praiseNum, int browseNum, int position, int couponID) {
        this.isPraise = isPraise;
        this.isCollect = isCollect;
        this.praiseNum = praiseNum;
        this.browseNum = browseNum;
        this.position = position;
        this.couponID = couponID;
    }

    public boolean isPraise() {
        return isPraise;
    }

    public void setIsPraise(boolean isPraise) {
        this.isPraise = isPraise;
    }

    public boolean isCollect() {
        return isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getBrowseNum() {
        return browseNum;
    }

    public void setBrowseNum(int browseNum) {
        this.browseNum = browseNum;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCouponID() {
        return couponID;
    }

    public void setCouponID(int couponID) {
        this.couponID = couponID;
    }
}
