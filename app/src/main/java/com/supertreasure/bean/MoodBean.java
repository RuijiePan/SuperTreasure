package com.supertreasure.bean;

import android.content.ContentValues;

import java.util.List;

import me.itangqi.greendao.Mood;

/**
 * Created by Administrator on 2016/1/28.
 */
public class MoodBean {
    private String status;

    public String getStatus() {
        return status;
    }
    private List<me.itangqi.greendao.Mood> list;

    public List<me.itangqi.greendao.Mood> getList() {
        return list;
    }

    public void setList(List<me.itangqi.greendao.Mood> list) {
        this.list = list;
    }

    public void setStatus(String status) {
        this.status = status;
    }
/*    private List<Mood> list;

    public List<Mood> getList() {
        return list;
    }

    public void setList(List<Mood> list) {
        this.list = list;
    }*/

/*
    public static class Mood{//说说
        private int _id;
        private int user_id;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public ContentValues toContentValues(){
            ContentValues values = new ContentValues();
            values.put("user_id"  ,user_id);
            values.put("moodId" ,moodId);
            values.put("content" ,content);
            values.put("paths" ,paths);
            values.put("publishtime" ,publishtime);
            values.put("praiseTimes",praiseTimes);
            values.put("commentCount",commentCount);
            values.put("isPraised",isPraised);
            values.put("belongschool",me.getBelongschool());
            values.put("sex",me.getSex());
            values.put("userPic",me.getUserPic());
            values.put("nickName",me.getNickName());
            values.put("userName",me.getUserName());

            return values;
        }

        public Mood(int user_id, int moodId, String content, String paths, String publishtime, String praiseTimes, String commentCount, boolean isPraised, String belongschool, String sex, String userPic, String nickName, String userName) {
            this.user_id = user_id;
            this.moodId = moodId;
            this.content = content;
            this.paths = paths;
            this.publishtime = publishtime;
            this.praiseTimes = praiseTimes;
            this.commentCount = commentCount;
            this.isPraised = isPraised;
            this.me = new People();
            this.me.belongschool = belongschool;
            this.me.sex = sex;
            this.me.userPic = userPic;
            this.me.nickName = nickName;
            this.me.userName = userName;
        }

        //------------------------------------------
        private int moodId;
        private String content;
        private String paths;//图片组
        private String publishtime;//(发表时间)
        private String praiseTimes;
        private String commentCount;
        private boolean isPraised;
        private People me;

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

        public String getPraiseTimes() {
            return praiseTimes;
        }

        public void setPraiseTimes(String praiseTimes) {
            this.praiseTimes = praiseTimes;
        }

        public String getCommentCount() {
            return commentCount;
        }

        public void setCommentCount(String commentCount) {
            this.commentCount = commentCount;
        }

        public People getPeople() {
            return me;
        }

        public void setPeople(People people) {
            this.me = people;
        }

        public static class People{
            private String belongschool;
            private String sex;
            private String userPic;
            private String nickName;
            private String userName;


            public String getBelongschool() {
                return belongschool;
            }

            public void setBelongschool(String belongschool) {
                this.belongschool = belongschool;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

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

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }
    }
*/
}
