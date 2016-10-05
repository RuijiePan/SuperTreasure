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

import me.itangqi.greendao.Coupon;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COUPON".
*/
public class CouponDao extends AbstractDao<Coupon, Long> {

    public static final String TABLENAME = "COUPON";

    /**
     * Properties of entity Coupon.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property User_id = new Property(1, Long.class, "user_id", false, "USER_ID");
        public final static Property CouponID = new Property(2, Integer.class, "couponID", false, "COUPON_ID");
        public final static Property CouponPics = new Property(3, String.class, "couponPics", false, "COUPON_PICS");
        public final static Property Width = new Property(4, Integer.class, "width", false, "WIDTH");
        public final static Property Height = new Property(5, Integer.class, "height", false, "HEIGHT");
        public final static Property CouponPraise = new Property(6, Integer.class, "couponPraise", false, "COUPON_PRAISE");
        public final static Property IsPraise = new Property(7, Boolean.class, "isPraise", false, "IS_PRAISE");
        public final static Property IsCollect = new Property(8, Boolean.class, "isCollect", false, "IS_COLLECT");
        public final static Property Intro = new Property(9, String.class, "intro", false, "INTRO");
        public final static Property BeginTime = new Property(10, String.class, "beginTime", false, "BEGIN_TIME");
        public final static Property LastTime = new Property(11, String.class, "lastTime", false, "LAST_TIME");
        public final static Property ShopName = new Property(12, String.class, "shopName", false, "SHOP_NAME");
        public final static Property ShopID = new Property(13, Integer.class, "shopID", false, "SHOP_ID");
        public final static Property BrowserNum = new Property(14, Integer.class, "browserNum", false, "BROWSER_NUM");
    };

    private DaoSession daoSession;

    private Query<Coupon> user_CouponsQuery;

    public CouponDao(DaoConfig config) {
        super(config);
    }
    
    public CouponDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COUPON\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_ID\" INTEGER," + // 1: user_id
                "\"COUPON_ID\" INTEGER," + // 2: couponID
                "\"COUPON_PICS\" TEXT," + // 3: couponPics
                "\"WIDTH\" INTEGER," + // 4: width
                "\"HEIGHT\" INTEGER," + // 5: height
                "\"COUPON_PRAISE\" INTEGER," + // 6: couponPraise
                "\"IS_PRAISE\" INTEGER," + // 7: isPraise
                "\"IS_COLLECT\" INTEGER," + // 8: isCollect
                "\"INTRO\" TEXT," + // 9: intro
                "\"BEGIN_TIME\" TEXT," + // 10: beginTime
                "\"LAST_TIME\" TEXT," + // 11: lastTime
                "\"SHOP_NAME\" TEXT," + // 12: shopName
                "\"SHOP_ID\" INTEGER," + // 13: shopID
                "\"BROWSER_NUM\" INTEGER);"); // 14: browserNum
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COUPON\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Coupon entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(2, user_id);
        }
 
        Integer couponID = entity.getCouponID();
        if (couponID != null) {
            stmt.bindLong(3, couponID);
        }
 
        String couponPics = entity.getCouponPics();
        if (couponPics != null) {
            stmt.bindString(4, couponPics);
        }
 
        Integer width = entity.getWidth();
        if (width != null) {
            stmt.bindLong(5, width);
        }
 
        Integer height = entity.getHeight();
        if (height != null) {
            stmt.bindLong(6, height);
        }
 
        Integer couponPraise = entity.getCouponPraise();
        if (couponPraise != null) {
            stmt.bindLong(7, couponPraise);
        }
 
        Boolean isPraise = entity.getIsPraise();
        if (isPraise != null) {
            stmt.bindLong(8, isPraise ? 1L: 0L);
        }
 
        Boolean isCollect = entity.getIsCollect();
        if (isCollect != null) {
            stmt.bindLong(9, isCollect ? 1L: 0L);
        }
 
        String intro = entity.getIntro();
        if (intro != null) {
            stmt.bindString(10, intro);
        }
 
        String beginTime = entity.getBeginTime();
        if (beginTime != null) {
            stmt.bindString(11, beginTime);
        }
 
        String lastTime = entity.getLastTime();
        if (lastTime != null) {
            stmt.bindString(12, lastTime);
        }
 
        String shopName = entity.getShopName();
        if (shopName != null) {
            stmt.bindString(13, shopName);
        }
 
        Integer shopID = entity.getShopID();
        if (shopID != null) {
            stmt.bindLong(14, shopID);
        }
 
        Integer browserNum = entity.getBrowserNum();
        if (browserNum != null) {
            stmt.bindLong(15, browserNum);
        }
    }

    @Override
    protected void attachEntity(Coupon entity) {
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
    public Coupon readEntity(Cursor cursor, int offset) {
        Coupon entity = new Coupon( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // user_id
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // couponID
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // couponPics
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // width
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // height
            cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // couponPraise
            cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0, // isPraise
            cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0, // isCollect
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // intro
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // beginTime
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // lastTime
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // shopName
            cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13), // shopID
            cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14) // browserNum
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Coupon entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUser_id(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setCouponID(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setCouponPics(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setWidth(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setHeight(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setCouponPraise(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setIsPraise(cursor.isNull(offset + 7) ? null : cursor.getShort(offset + 7) != 0);
        entity.setIsCollect(cursor.isNull(offset + 8) ? null : cursor.getShort(offset + 8) != 0);
        entity.setIntro(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setBeginTime(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setLastTime(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setShopName(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setShopID(cursor.isNull(offset + 13) ? null : cursor.getInt(offset + 13));
        entity.setBrowserNum(cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Coupon entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Coupon entity) {
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
    
    /** Internal query to resolve the "coupons" to-many relationship of User. */
    public List<Coupon> _queryUser_Coupons(Long user_id) {
        synchronized (this) {
            if (user_CouponsQuery == null) {
                QueryBuilder<Coupon> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.User_id.eq(null));
                user_CouponsQuery = queryBuilder.build();
            }
        }
        Query<Coupon> query = user_CouponsQuery.forCurrentThread();
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
            builder.append(" FROM COUPON T");
            builder.append(" LEFT JOIN USER T0 ON T.\"USER_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Coupon loadCurrentDeep(Cursor cursor, boolean lock) {
        Coupon entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        User user = loadCurrentOther(daoSession.getUserDao(), cursor, offset);
        entity.setUser(user);

        return entity;    
    }

    public Coupon loadDeep(Long key) {
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
    public List<Coupon> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Coupon> list = new ArrayList<Coupon>(count);
        
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
    
    protected List<Coupon> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Coupon> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}