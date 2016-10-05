package com.supertreasure.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite数据库的帮助类
 * 
 * 该类属于扩展类,主要承担数据库初始化和版本升级使用,其他核心全由核心父类完成
 *
 *  //boolean 值 用geiInt获取。1标识true，0标识false
 */
public class DataBaseHelper extends SQLiteOpenHelper {

	public DataBaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE [_user] 		([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [account] NVARCHAR, [password]	 NVARCHAR,	[belongschool] NVARCHAR, 	[sex] 		NVARCHAR, 	[userPic] 		NVARCHAR, 	[nickName] 	 NVARCHAR,	[userName] 		NVARCHAR);");
		db.execSQL("CREATE TABLE [_new] 		([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [user_id] INTEGER,	[topId]		 NVARCHAR,	[adminID]      NVARCHAR, [adminName] 	NVARCHAR,	[title] 	NVARCHAR,	[label] 		NVARCHAR,	[summary] 		 NVARCHAR,	[pics] 	NVARCHAR,	[browserNum] 	NVARCHAR,	[praise] 	NVARCHAR,[url] NVARCHAR,[isPraised] bool);");
		db.execSQL("CREATE TABLE [_mood] 		([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [user_id] INTEGER,	[moodId] 	 INTEGER,	[content] 		NVARCHAR,	[paths]	 	NVARCHAR,	[publishtime] 	NVARCHAR,	[praiseTimes]	 NVARCHAR,	[commentCount] 	NVARCHAR,	[isPraised] 	bool,		[belongschool] 	NVARCHAR,	[sex] NVARCHAR,	[userPic] NVARCHAR,[nickName] NVARCHAR,[userName] NVARCHAR);");
		db.execSQL("CREATE TABLE [_coupon] 		([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [user_id] INTEGER,	[couponID]  INTEGER,	[couponPics] 	NVARCHAR,	[width] 	INTEGER,	[height] 		INTEGER,	[couponPraise] INTEGER,	[isPraise] 		bool,		[isCollect] 	bool,[intro]   NVARCHAR	,[beginTime]   NVARCHAR,[lastTime]  NVARCHAR,[shopName]  NVARCHAR,[shopID] INTEGER,[browserNum] 	INTEGER);");
		db.execSQL("CREATE TABLE [_good] 		([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [user_id] INTEGER,  [sellID] 	 INTEGER, 	[width] 		INTEGER, 	[height] 	INTEGER, 	[userName] 	NVARCHAR,	[pic] 			 NVARCHAR,	[price] 			NVARCHA, 	[goodType]		NVARCHA);");
		db.execSQL("CREATE TABLE [_banner] 		([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT, [adId] 	INTEGER,  [photo] 	 NVARCHAR,  	[alink] 	NVARCHAR);");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			//Enable foreign key constraints
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}
}
