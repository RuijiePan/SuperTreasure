package com.supertreasure.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * SQLite数据库管理类
 * 
 * 主要负责数据库资源的初始化,开启,关闭,以及获得DatabaseHelper帮助类操作
 * 
 */
public class DBManager {
	private int version = 1;
	private String databaseName;
	// 本地Context对象
	private Context mContext = null;
	private static DBManager dBManager = null;
	/**
	 * 构造函数
	 * 
	 * @param mContext
	 */
	private DBManager(Context mContext) {
		super();
		this.mContext = mContext;
	}
	public static DBManager getInstance(Context Context, String databaseName) {//单例管理类
		if (null == dBManager) {
			dBManager = new DBManager(Context);
		}
		dBManager.databaseName = databaseName;
		return dBManager;
	}
	/**
	 * 关闭数据库 注意:当事务成功或者一次性操作完毕时候再关闭
	 */
	public void closeDatabase(SQLiteDatabase dataBase, Cursor cursor) {
		if (null != dataBase) {
			dataBase.close();
		}
		if (null != cursor) {
			cursor.close();
		}
	}
	/**
	 * 打开数据库 注:SQLiteDatabase资源一旦被关闭,该底层会重新产生一个新的SQLiteDatabase
	 */
	public synchronized SQLiteDatabase openDatabase() {//
		return new DataBaseHelper(mContext, this.databaseName, null, this.version).getWritableDatabase();
	}
	/**
	 * 获取DataBaseHelper
	 * 
	 * @return
	 */
	public DataBaseHelper getDatabaseHelper() {//每次调用都是重新new，因为 这个openhelper 创建的时候，
	// 如果存在数据库 就不会调用onCreate()，不会有问题，而数据库名称在 new Manager的时候指定。
		return new DataBaseHelper(mContext, this.databaseName, null, this.version);
	}

}
