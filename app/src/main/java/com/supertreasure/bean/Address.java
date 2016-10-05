package com.supertreasure.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/2/24.
 */
public class Address {

    /**
     * campus : ["北京大学","中国人民大学","清华大学","北京交通大学","北京科技大学","中国石油大学(北京)","中国矿业大学(北京)","中国地质大学(北京)","北京邮电大学","华北电力大学","北京化工大学","中国农业大学","中国科学院大学","北京林业大学","北京中医药大学","北京师范大学","北京外国语大学","北京语言大学","对外经济贸易大学","中央财经大学","中国政法大学","中央民族大学","中国人民公安大学","北京协和医学院","北京体育大学","北京理工大学","北京航空航天大学","北京信息科技大学","北京工商大学","北京联合大学","北京工业大学","北方工业大学","首都医科大学","首都师范大学","首都经济贸易大学","中国传媒大学","国际关系学院","中央美术学院","中央戏剧学院","中央音乐学院","北京电子科技学院","外交学院","中国劳动关系学院","中国青年政治学院","中华女子学院","北京建筑大学","北京服装学院","北京印刷学院","北京石油化工学院","首钢工学院","北京农学院","首都体育学院","北京第二外国语学院","北京物资学院","北京警察学院","中国音乐学院","北京舞蹈学院","中国戏曲学院","北京电影学院","北京城市学院","北京吉利学院"]
     * province : 北京
     * provinceID : 1
     */

    private List<ListEntity> list;

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private String province;
        private String provinceID;
        private List<String> campus;

        public void setProvince(String province) {
            this.province = province;
        }

        public void setProvinceID(String provinceID) {
            this.provinceID = provinceID;
        }

        public void setCampus(List<String> campus) {
            this.campus = campus;
        }

        public String getProvince() {
            return province;
        }

        public String getProvinceID() {
            return provinceID;
        }

        public List<String> getCampus() {
            return campus;
        }
    }
}
