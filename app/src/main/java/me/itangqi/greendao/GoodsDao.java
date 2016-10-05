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

import me.itangqi.greendao.Goods;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GOODS".
*/
public class GoodsDao extends AbstractDao<Goods, Long> {

    public static final String TABLENAME = "GOODS";

    /**
     * Properties of entity Goods.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property User_id = new Property(1, Long.class, "user_id", false, "USER_ID");
        public final static Property SellID = new Property(2, Integer.class, "sellID", false, "SELL_ID");
        public final static Property Width = new Property(3, Integer.class, "width", false, "WIDTH");
        public final static Property Height = new Property(4, Integer.class, "height", false, "HEIGHT");
        public final static Property UserName = new Property(5, String.class, "userName", false, "USER_NAME");
        public final static Property Pic = new Property(6, String.class, "pic", false, "PIC");
        public final static Property Price = new Property(7, String.class, "price", false, "PRICE");
        public final static Property GoodType = new Property(8, String.class, "goodType", false, "GOOD_TYPE");
    };

    private DaoSession daoSession;

    private Query<Goods> user_GoodsQuery;

    public GoodsDao(DaoConfig config) {
        super(config);
    }
    
    public GoodsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GOODS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_ID\" INTEGER," + // 1: user_id
                "\"SELL_ID\" INTEGER," + // 2: sellID
                "\"WIDTH\" INTEGER," + // 3: width
                "\"HEIGHT\" INTEGER," + // 4: height
                "\"USER_NAME\" TEXT," + // 5: userName
                "\"PIC\" TEXT," + // 6: pic
                "\"PRICE\" TEXT," + // 7: price
                "\"GOOD_TYPE\" TEXT);"); // 8: goodType
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GOODS\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Goods entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(2, user_id);
        }
 
        Integer sellID = entity.getSellID();
        if (sellID != null) {
            stmt.bindLong(3, sellID);
        }
 
        Integer width = entity.getWidth();
        if (width != null) {
            stmt.bindLong(4, width);
        }
 
        Integer height = entity.getHeight();
        if (height != null) {
            stmt.bindLong(5, height);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(6, userName);
        }
 
        String pic = entity.getPic();
        if (pic != null) {
            stmt.bindString(7, pic);
        }
 
        String price = entity.getPrice();
        if (price != null) {
            stmt.bindString(8, price);
        }
 
        String goodType = entity.getGoodType();
        if (goodType != null) {
            stmt.bindString(9, goodType);
        }
    }

    @Override
    protected void attachEntity(Goods entity) {
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
    public Goods readEntity(Cursor cursor, int offset) {
        Goods entity = new Goods( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // user_id
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // sellID
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // width
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // height
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // userName
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // pic
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // price
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // goodType
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Goods entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUser_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setSellID(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setWidth(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setHeight(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setUserName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPic(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPrice(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setGoodType(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Goods entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Goods entity) {
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
    
    /** Internal query to resolve the "goods" to-many relationship of User. */
    public List<Goods> _queryUser_Goods(Long user_id) {
        synchronized (this) {
            if (user_GoodsQuery == null) {
                QueryBuilder<Goods> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.User_id.eq(null));
                user_GoodsQuery = queryBuilder.build();
            }
        }
        Query<Goods> query = user_GoodsQuery.forCurrentThread();
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
            builder.append(" FROM GOODS T");
            builder.append(" LEFT JOIN USER T0 ON T.\"USER_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Goods loadCurrentDeep(Cursor cursor, boolean lock) {
        Goods entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        User user = loadCurrentOther(daoSession.getUserDao(), cursor, offset);
        entity.setUser(user);

        return entity;    
    }

    public Goods loadDeep(Long key) {
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
    public List<Goods> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Goods> list = new ArrayList<Goods>(count);
        
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
    
    protected List<Goods> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Goods> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
