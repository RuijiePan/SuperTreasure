package com.supertreasure.bean;

import com.supertreasure.mine.CouponAdapter;

import java.util.List;

/**
 * Created by yum on 2016/1/29.
 */
public class CouponBeen {
    private String status;
    private List<Coupon> out;//out:[{优惠卷1},{优惠卷2},{优惠卷3}...]【无效】
    private List<Coupon> in;//in:[{优惠卷1},{优惠卷2},{优惠卷3}...]【有效】

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Coupon> getOut() {
        return out;
    }

    public void setOut(List<Coupon> out) {
        this.out = out;
    }

    public List<Coupon> getIn() {
        return in;
    }

    public void setIn(List<Coupon> in) {
        this.in = in;
    }

    public static class Coupon{



        private int type = CouponAdapter.TYPE_CONTENT;
        private String intro;

        private int couponID;
        private String couponPics;
        private int width;
        private int height;
        private int couponPraise;
        private boolean isPraise;
        private boolean isCollect;
        private int browserNum;
        private String beginTime;
        private String lastTime;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public boolean isPraise() {
            return isPraise;
        }

        public void setPraise(boolean praise) {
            isPraise = praise;
        }

        public boolean isCollect() {
            return isCollect;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public void setIsPraise(boolean isPraise) {
            this.isPraise = isPraise;
        }

        public void setIsCollect(boolean isCollect) {
            this.isCollect = isCollect;
        }

        public void setCollect(boolean collect) {
            isCollect = collect;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getCouponID() {
            return couponID;
        }

        public void setCouponID(int couponID) {
            this.couponID = couponID;
        }

        public String getCouponPics() {
            return couponPics;
        }

        public void setCouponPics(String couponPics) {
            this.couponPics = couponPics;
        }

        public int getBrowserNum() {
            return browserNum;
        }

        public void setBrowserNum(int browserNum) {
            this.browserNum = browserNum;
        }

        public int getCouponPraise() {
            return couponPraise;
        }

        public void setCouponPraise(int couponPraise) {
            this.couponPraise = couponPraise;
        }

        public String getBeginTime() {
            return beginTime;
        }

        public void setBeginTime(String beginTime) {
            this.beginTime = beginTime;
        }

        public String getLastTime() {
            return lastTime;
        }

        public void setLastTime(String lastTime) {
            this.lastTime = lastTime;
        }

        @Override
        public String toString() {
            return "Coupon{" +
                    "type=" + type +
                    ", intro='" + intro + '\'' +
                    ", couponID=" + couponID +
                    ", couponPics='" + couponPics + '\'' +
                    ", width=" + width +
                    ", height=" + height +
                    ", couponPraise=" + couponPraise +
                    ", isPraise=" + isPraise +
                    ", isCollect=" + isCollect +
                    ", browserNum=" + browserNum +
                    ", beginTime='" + beginTime + '\'' +
                    ", lastTime='" + lastTime + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CouponBeen{" +
                "status='" + status + '\'' +
                ", out=" + out +
                ", in=" + in +
                '}';
    }
}
