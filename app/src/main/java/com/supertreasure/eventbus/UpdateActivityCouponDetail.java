package com.supertreasure.eventbus;

/**
 * Created by yum on 2016/5/16.
 */
public class UpdateActivityCouponDetail {
    private boolean isPraise;
    private boolean isCollect;
    private int praiseNum;
    private int browseNum;
    public UpdateActivityCouponDetail(){}
    public UpdateActivityCouponDetail(boolean isPraise, boolean isCollect, int praiseNum, int browseNum) {

        this.isPraise = isPraise;
        this.isCollect = isCollect;
        this.praiseNum = praiseNum;
        this.browseNum = browseNum;
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
}
