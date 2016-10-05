package com.supertreasure.bean;

import java.util.List;

/**
 * Created by prj on 2016/3/29.
 */
public class MyMoodBean {
    private String status;
    private List<Mood> list;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Mood> getList() {
        return list;
    }

    public void setList(List<Mood> list) {
        this.list = list;
    }

    public static class Mood{
        private int moodId;
        private String content;
        private String paths;
        private String publishtime;
        private String commentCount;
        private boolean isPraised;
        private String praise;
        private People me;

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
            this.commentCount = commentCount;
        }

        public boolean isPraised() {
            return isPraised;
        }

        public void setPraised(boolean praised) {
            isPraised = praised;
        }

        public int getMoodId() {
            return moodId;
        }

        public void setMoodId(int moodId) {
            this.moodId = moodId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPaths() {
            return paths;
        }

        public void setPaths(String paths) {
            this.paths = paths;
        }

        public String getPublishtime() {
            return publishtime;
        }

        public void setPublishtime(String publishtime) {
            this.publishtime = publishtime;
        }

        public String getPraise() {
            return praise;
        }

        public void setPraise(String praise) {
            this.praise = praise;
        }

        public People getMe() {
            return me;
        }

        public void setMe(People me) {
            this.me = me;
        }

        public static class People{
            private String userPic;
            private String nickName;
            private String userId;

            public String getUserPic() {
                return userPic;
            }

            public void setUserPic(String userPic) {
                this.userPic = userPic;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }
    }
}
