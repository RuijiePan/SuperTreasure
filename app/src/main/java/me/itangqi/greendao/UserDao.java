package me.itangqi.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import me.itangqi.greendao.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Belongschool = new Property(1, String.class, "belongschool", false, "BELONGSCHOOL");
        public final static Property Sex = new Property(2, String.class, "sex", false, "SEX");
        public final static Property UserPic = new Property(3, String.class, "userPic", false, "USER_PIC");
        public final static Property NickName = new Property(4, String.class, "nickName", false, "NICK_NAME");
        public final static Property UserName = new Property(5, String.class, "userName", false, "USER_NAME");
    };

    private DaoSession daoSession;


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"BELONGSCHOOL\" TEXT," + // 1: belongschool
                "\"SEX\" TEXT," + // 2: sex
                "\"USER_PIC\" TEXT," + // 3: userPic
                "\"NICK_NAME\" TEXT," + // 4: nickName
                "\"USER_NAME\" TEXT);"); // 5: userName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String belongschool = entity.getBelongschool();
        if (belongschool != null) {
            stmt.bindString(2, belongschool);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(3, sex);
        }
 
        String userPic = entity.getUserPic();
        if (userPic != null) {
            stmt.bindString(4, userPic);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(5, nickName);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(6, userName);
        }
    }

    @Override
    protected void attachEntity(User entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // belongschool
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // sex
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // userPic
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // nickName
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // userName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setBelongschool(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSex(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserPic(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setNickName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUserName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
