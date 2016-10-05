package com.supertreasure.bean;

import me.itangqi.greendao.MoodMessage;

/**
 * Created by yum on 2016/4/17.
 */
public class MoodMessageBean {
    private String status;
    private String msg;//errormsg
    private int type;
    private MoodMessage body;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MoodMessage getBody() {
        return body;
    }

    public void setBody(MoodMessage body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "MoodMessageBean{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                ", body=" + body +
                '}';
    }
}
