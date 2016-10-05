package me.itangqi.greendao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import me.itangqi.greendao.Mood;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MOOD".
*/
public class MoodDao extends AbstractDao<Mood, Long> {

    public static final String TABLENAME = "MOOD";

    /**
     * Properties of entity Mood.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property User_id = new Property(1, Long.class, "user_id", false, "USER_ID");
        public final static Property MoodId = new Property(2, Integer.class, "moodId", false, "MOOD_ID");
        public final static Property Content = new Property(3, String.class, "content", false, "CONTENT");
        public final static Property Type = new Property(4, String.class, "type", false, "TYPE");
        public final static Property Paths = new Property(5, String.class, "paths", false, "PATHS");
        public final static Property Publishtime = new Property(6, String.class, "publishtime", false, "PUBLISHTIME");
        public final static Property PraiseTimes = new Property(7, String.class, "praiseTimes", false, "PRAISE_TIMES");
        public final static Property CommentCount = new Property(8, String.class, "commentCount", false, "COMMENT_COUNT");
        public final static Property IsPraised = new Property(9, Boolean.class, "isPraised", false, "IS_PRAISED");
        public final static Property Belongschool = new Property(10, String.class, "belongschool", false, "BELONGSCHOOL");
        public final static Property Sex = new Property(11, String.class, "sex", false, "SEX");
        public final static Property UserPic = new Property(12, String.class, "userPic", false, "USER_PIC");
        public final static Property NickName = new Property(13, String.class, "nickName", false, "NICK_NAME");
        public final static Property UserName = new Property(14, String.class, "userName", false, "USER_NAME");
        public final static Property Me = new Property(15, String.class, "me", false, "ME");
    };

    private DaoSession daoSession;

    private final PeoplePropertyConverter meConverter = new PeoplePropertyConverter();
    private Query<Mood> user_MoodsQuery;

    public MoodDao(DaoConfig config) {
        super(config);
    }
    
    public MoodDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MOOD\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_ID\" INTEGER," + // 1: user_id
                "\"MOOD_ID\" INTEGER," + // 2: moodId
                "\"CONTENT\" TEXT," + // 3: content
                "\"TYPE\" TEXT," + // 4: type
                "\"PATHS\" TEXT," + // 5: paths
                "\"PUBLISHTIME\" TEXT," + // 6: publishtime
                "\"PRAISE_TIMES\" TEXT," + // 7: praiseTimes
                "\"COMMENT_COUNT\" TEXT," + // 8: commentCount
                "\"IS_PRAISED\" INTEGER," + // 9: isPraised
                "\"BELONGSCHOOL\" TEXT," + // 10: belongschool
                "\"SEX\" TEXT," + // 11: sex
                "\"USER_PIC\" TEXT," + // 12: userPic
                "\"NICK_NAME\" TEXT," + // 13: nickName
                "\"USER_NAME\" TEXT," + // 14: userName
                "\"ME\" TEXT);"); // 15: me
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MOOD\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Mood entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(2, user_id);
        }
 
        Integer moodId = entity.getMoodId();
        if (moodId != null) {
            stmt.bindLong(3, moodId);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(4, content);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(5, type);
        }
 
        String paths = entity.getPaths();
        if (paths != null) {
            stmt.bindString(6, paths);
        }
 
        String publishtime = entity.getPublishtime();
        if (publishtime != null) {
            stmt.bindString(7, publishtime);
        }
 
        String praiseTimes = entity.getPraiseTimes();
        if (praiseTimes != null) {
            stmt.bindString(8, praiseTimes);
        }
 
        String commentCount = entity.getCommentCount();
        if (commentCount != null) {
            stmt.bindString(9, commentCount);
        }
 
        Boolean isPraised = entity.getIsPraised();
        if (isPraised != null) {
            stmt.bindLong(10, isPraised ? 1L: 0L);
        }
 
        String belongschool = entity.getBelongschool();
        if (belongschool != null) {
            stmt.bindString(11, belongschool);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(12, sex);
        }
 
        String userPic = entity.getUserPic();
        if (userPic != null) {
            stmt.bindString(13, userPic);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(14, nickName);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(15, userName);
        }
 
        People me = entity.getMe();
        if (me != null) {
            stmt.bindString(16, meConverter.convertToDatabaseValue(me));
        }
    }

    @Override
    protected void attachEntity(Mood entity) {
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
    public Mood readEntity(Cursor cursor, int offset) {
        Mood entity = new Mood( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // user_id
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // moodId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // content
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // type
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // paths
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // publishtime
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // praiseTimes
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // commentCount
            cursor.isNull(offset + 9) ? null : cursor.getShort(offset + 9) != 0, // isPraised
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // belongschool
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // sex
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // userPic
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // nickName
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // userName
            cursor.isNull(offset + 15) ? null : meConverter.convertToEntityProperty(cursor.getString(offset + 15)) // me
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Mood entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUser_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setMoodId(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setContent(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setType(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPaths(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPublishtime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPraiseTimes(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setCommentCount(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setIsPraised(cursor.isNull(offset + 9) ? null : cursor.getShort(offset + 9) != 0);
        entity.setBelongschool(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setSex(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setUserPic(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setNickName(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setUserName(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setMe(cursor.isNull(offset + 15) ? null : meConverter.convertToEntityProperty(cursor.getString(offset + 15)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Mood entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Mood entity) {
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
    
    /** Internal query to resolve the "moods" to-many relationship of User. */
    public List<Mood> _queryUser_Moods(Long user_id) {
        synchronized (this) {
            if (user_MoodsQuery == null) {
                QueryBuilder<Mood> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.User_id.eq(null));
                user_MoodsQuery = queryBuilder.build();
            }
        }
        Query<Mood> query = user_MoodsQuery.forCurrentThread();
        query.setParameter(0, user_id);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getUserDao().getAllColumns());
            builder.append(" FROM MOOD T");
            builder.append(" LEFT JOIN USER T0 ON T.\"USER_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Mood loadCurrentDeep(Cursor cursor, boolean lock) {
        Mood entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        User user = loadCurrentOther(daoSession.getUserDao(), cursor, offset);
        entity.setUser(user);

        return entity;    
    }

    public Mood loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<Mood> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Mood> list = new ArrayList<Mood>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<Mood> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Mood> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}