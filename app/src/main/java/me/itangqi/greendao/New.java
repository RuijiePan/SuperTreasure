package me.itangqi.greendao;

import me.itangqi.greendao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "NEW".
 */
public class New {

    private Long id;
    private Long user_id;
    private String topId;
    private String adminID;
    private String adminName;
    private String title;
    private String label;
    private String summary;
    private String pics;
    private String browserNum;
    private String praise;
    private String url;
    private String type;
    private Boolean isPraised;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient NewDao myDao;

    private User user;
    private Long user__resolvedKey;


    public New() {
    }

    public New(Long id) {
        this.id = id;
    }

    public New(Long id, Long user_id, String topId, String adminID, String adminName, String title, String label, String summary, String pics, String browserNum, String praise, String url, String type, Boolean isPraised) {
        this.id = id;
        this.user_id = user_id;
        this.topId = topId;
        this.adminID = adminID;
        this.adminName = adminName;
        this.title = title;
        this.label = label;
        this.summary = summary;
        this.pics = pics;
        this.browserNum = browserNum;
        this.praise = praise;
        this.url = url;
        this.type = type;
        this.isPraised = isPraised;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNewDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getTopId() {
        return topId;
    }

    public void setTopId(String topId) {
        this.topId = topId;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getBrowserNum() {
        return browserNum;
    }

    public void setBrowserNum(String browserNum) {
        this.browserNum = browserNum;
    }

    public String getPraise() {
        return praise;
    }

    public void setPraise(String praise) {
        this.praise = praise;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsPraised() {
        return isPraised;
    }

    public void setIsPraised(Boolean isPraised) {
        this.isPraised = isPraised;
    }

    /** To-one relationship, resolved on first access. */
    public User getUser() {
        Long __key = this.user_id;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
            	user__resolvedKey = __key;
            }
        }
        return user;
    }

    public void setUser(User user) {
        synchronized (this) {
            this.user = user;
            user_id = user == null ? null : user.getId();
            user__resolvedKey = user_id;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    @Override
    public String toString() {
        return "New{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", topId='" + topId + '\'' +
                ", adminID='" + adminID + '\'' +
                ", adminName='" + adminName + '\'' +
                ", title='" + title + '\'' +
                ", label='" + label + '\'' +
                ", summary='" + summary + '\'' +
                ", pics='" + pics + '\'' +
                ", browserNum='" + browserNum + '\'' +
                ", praise='" + praise + '\'' +
                ", url='" + url + '\'' +
                ", type='" + type + '\'' +
                ", isPraised=" + isPraised +
                ", daoSession=" + daoSession +
                ", myDao=" + myDao +
                ", user=" + user +
                ", user__resolvedKey=" + user__resolvedKey +
                '}';
    }
}