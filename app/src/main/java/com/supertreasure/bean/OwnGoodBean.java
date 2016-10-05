package com.supertreasure.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/2/19.
 */
public class OwnGoodBean {
    private String paths;
    private List<OwnGood> list;

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }

    public List<OwnGood> getList() {
        return list;
    }

    public void setList(List<OwnGood> list) {
        this.list = list;
    }

    public static class OwnGood{
        private int sellID;
        private String introduction;
        private String pic;
        private String status;
        private double price;

        public int getSellID() {
            return sellID;
        }

        public void setSellID(int sellID) {
            this.sellID = sellID;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
