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

import me.itangqi.greendao.New;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NEW".
*/
public class NewDao extends AbstractDao<New, Long> {

    public static final String TABLENAME = "NEW";

    /**
     * Properties of entity New.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property User_id = new Property(1, Long.class, "user_id", false, "USER_ID");
        public final static Property TopId = new Property(2, String.class, "topId", false, "TOP_ID");
        public final static Property AdminID = new Property(3, String.class, "adminID", false, "ADMIN_ID");
        public final static Property AdminName = new Property(4, String.class, "adminName", false, "ADMIN_NAME");
        public final static Property Title = new Property(5, String.class, "title", false, "TITLE");
        public final static Property Label = new Property(6, String.class, "label", false, "LABEL");
        public final static Property Summary = new Property(7, String.class, "summary", false, "SUMMARY");
        public final static Property Pics = new Property(8, String.class, "pics", false, "PICS");
        public final static Property BrowserNum = new Property(9, String.class, "browserNum", false, "BROWSER_NUM");
        public final static Property Praise = new Property(10, String.class, "praise", false, "PRAISE");
        public final static Property Url = new Property(11, String.class, "url", false, "URL");
        public final static Property Type = new Property(12, String.class, "type", false, "TYPE");
        public final static Property IsPraised = new Property(13, Boolean.class, "isPraised", false, "IS_PRAISED");
    };

    private DaoSession daoSession;

    private Query<New> user_NewsQuery;

    public NewDao(DaoConfig config) {
        super(config);
    }
    
    public NewDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NEW\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_ID\" INTEGER," + // 1: user_id
                "\"TOP_ID\" TEXT," + // 2: topId
                "\"ADMIN_ID\" TEXT," + // 3: adminID
                "\"ADMIN_NAME\" TEXT," + // 4: adminName
                "\"TITLE\" TEXT," + // 5: title
                "\"LABEL\" TEXT," + // 6: label
                "\"SUMMARY\" TEXT," + // 7: summary
                "\"PICS\" TEXT," + // 8: pics
                "\"BROWSER_NUM\" TEXT," + // 9: browserNum
                "\"PRAISE\" TEXT," + // 10: praise
                "\"URL\" TEXT," + // 11: url
                "\"TYPE\" TEXT," + // 12: type
                "\"IS_PRAISED\" INTEGER);"); // 13: isPraised
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NEW\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, New entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(2, user_id);
        }
 
        String topId = entity.getTopId();
        if (topId != null) {
            stmt.bindString(3, topId);
        }
 
        String adminID = entity.getAdminID();
        if (adminID != null) {
            stmt.bindString(4, adminID);
        }
 
        String adminName = entity.getAdminName();
        if (adminName != null) {
            stmt.bindString(5, adminName);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(6, title);
        }
 
        String label = entity.getLabel();
        if (label != null) {
            stmt.bindString(7, label);
        }
 
        String summary = entity.getSummary();
        if (summary != null) {
            stmt.bindString(8, summary);
        }
 
        String pics = entity.getPics();
        if (pics != null) {
            stmt.bindString(9, pics);
        }
 
        String browserNum = entity.getBrowserNum();
        if (browserNum != null) {
            stmt.bindString(10, browserNum);
        }
 
        String praise = entity.getPraise();
        if (praise != null) {
            stmt.bindString(11, praise);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(12, url);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(13, type);
        }
 
        Boolean isPraised = entity.getIsPraised();
        if (isPraised != null) {
            stmt.bindLong(14, isPraised ? 1L: 0L);
        }
    }

    @Override
    protected void attachEntity(New entity) {
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
    public New readEntity(Cursor cursor, int offset) {
        New entity = new New( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // user_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // topId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // adminID
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // adminName
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // title
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // label
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // summary
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // pics
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // browserNum
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // praise
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // url
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // type
            cursor.isNull(offset + 13) ? null : cursor.getShort(offset + 13) != 0 // isPraised
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, New entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUser_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setTopId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAdminID(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAdminName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTitle(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setLabel(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setSummary(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setPics(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setBrowserNum(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setPraise(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setUrl(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setType(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setIsPraised(cursor.isNull(offset + 13) ? null : cursor.getShort(offset + 13) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(New entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(New entity) {
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
    
    /** Internal query to resolve the "news" to-many relationship of User. */
    public List<New> _queryUser_News(Long user_id) {
        synchronized (this) {
            if (user_NewsQuery == null) {
                QueryBuilder<New> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.User_id.eq(null));
                user_NewsQuery = queryBuilder.build();
            }
        }
        Query<New> query = user_NewsQuery.forCurrentThread();
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
            builder.append(" FROM NEW T");
            builder.append(" LEFT JOIN USER T0 ON T.\"USER_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected New loadCurrentDeep(Cursor cursor, boolean lock) {
        New entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        User user = loadCurrentOther(daoSession.getUserDao(), cursor, offset);
        entity.setUser(user);

        return entity;    
    }

    public New loadDeep(Long key) {
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
    public List<New> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<New> list = new ArrayList<New>(count);
        
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
    
    protected List<New> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<New> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
