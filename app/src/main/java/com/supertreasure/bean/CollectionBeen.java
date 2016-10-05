package com.supertreasure.bean;

import com.supertreasure.mine.CollectionAdapter;
import com.supertreasure.mine.CouponAdapter;

import java.util.List;

/**
 * Created by yum on 2016/1/29.
 */
public class CollectionBeen {
    private String status;
    private List<Collection> publish;
    private List<Collection> remove;
    private List<Collection> sold;

    public List<Collection> getPublish() {
        return publish;
    }

    public void setPublish(List<Collection> publish) {
        this.publish = publish;
    }

    public List<Collection> getRemove() {
        return remove;
    }

    public void setRemove(List<Collection> remove) {
        this.remove = remove;
    }

    public List<Collection> getSold() {
        return sold;
    }

    public void setSold(List<Collection> sold) {
        this.sold = sold;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Collection{
        private int type = CollectionAdapter.TYPE_CONTENT;


        private int sellID;
        private String pics;//(图片组地址【逗号隔开】)
        private String pic;

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        private String introduction;
        private String price;

        public int getSellID() {
            return sellID;
        }

        public void setSellID(int sellID) {
            this.sellID = sellID;
        }

        public String getPics() {
            return pics;
        }

        public void setPics(String pics) {
            this.pics = pics;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Collection{" +
                    "type=" + type +
                    ", sellID=" + sellID +
                    ", pics='" + pics + '\'' +
                    ", introduction='" + introduction + '\'' +
                    ", price='" + price + '\'' +
                    '}';
        }
    }
}
