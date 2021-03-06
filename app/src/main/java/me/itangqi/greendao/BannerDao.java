package me.itangqi.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import me.itangqi.greendao.Banner;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BANNER".
*/
public class BannerDao extends AbstractDao<Banner, Long> {

    public static final String TABLENAME = "BANNER";

    /**
     * Properties of entity Banner.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property AdID = new Property(1, Integer.class, "adID", false, "AD_ID");
        public final static Property Photo = new Property(2, String.class, "photo", false, "PHOTO");
        public final static Property Alink = new Property(3, String.class, "alink", false, "ALINK");
        public final static Property Type = new Property(4, String.class, "type", false, "TYPE");
    };


    public BannerDao(DaoConfig config) {
        super(config);
    }
    
    public BannerDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BANNER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"AD_ID\" INTEGER," + // 1: adID
                "\"PHOTO\" TEXT," + // 2: photo
                "\"ALINK\" TEXT," + // 3: alink
                "\"TYPE\" TEXT);"); // 4: type
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BANNER\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Banner entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer adID = entity.getAdID();
        if (adID != null) {
            stmt.bindLong(2, adID);
        }
 
        String photo = entity.getPhoto();
        if (photo != null) {
            stmt.bindString(3, photo);
        }
 
        String alink = entity.getAlink();
        if (alink != null) {
            stmt.bindString(4, alink);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(5, type);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Banner readEntity(Cursor cursor, int offset) {
        Banner entity = new Banner( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // adID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // photo
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // alink
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // type
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Banner entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAdID(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setPhoto(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAlink(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setType(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Banner entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Banner entity) {
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
