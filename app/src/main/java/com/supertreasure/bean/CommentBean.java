package com.supertreasure.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/1/31.
 */
public class CommentBean {
    private String status;
    private List<Comment> list;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Comment> getList() {
        return list;
    }

    public void setList(List<Comment> list) {
        this.list = list;
    }

    public static class Comment{
        private String anonymous;//是否匿名
        private String content;
        private String commentTime;
        private String commentId;
        private FromUser fromUser;
        private ToUser toUser;

        public ToUser getToUser() {
            return toUser;
        }

        public void setToUser(ToUser toUser) {
            this.toUser = toUser;
        }

        public String getAnonymous() {
            return anonymous;
        }

        public void setAnonymous(String anonymous) {
            this.anonymous = anonymous;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getCommentTime() {
            return commentTime;
        }

        public void setCommentTime(String commentTime) {
            this.commentTime = commentTime;
        }

        public FromUser getFromUser() {
            return fromUser;
        }

        public void setFromUser(FromUser fromUser) {
            this.fromUser = fromUser;
        }
    }

    public static class FromUser{
        private int userID;
        private String nickName;
        private String userPic;

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getUserPic() {
            return userPic;
        }

        public void setUserPic(String userPic) {
            this.userPic = userPic;
        }

    }

    public static class ToUser{
        private int userID;
        private String nickName;
        private String userPic;

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getUserPic() {
            return userPic;
        }

        public void setUserPic(String userPic) {
            this.userPic = userPic;
        }
    }
}
