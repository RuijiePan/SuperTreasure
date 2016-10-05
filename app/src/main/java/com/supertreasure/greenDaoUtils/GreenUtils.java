package com.supertreasure.greenDaoUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ab.util.AbSharedUtil;
import com.orhanobut.logger.Logger;
import com.supertreasure.util.Config;

import java.util.List;

import me.itangqi.greendao.Banner;
import me.itangqi.greendao.BannerDao;
import me.itangqi.greendao.Coupon;
import me.itangqi.greendao.CouponDao;
import me.itangqi.greendao.DaoMaster;
import me.itangqi.greendao.DaoSession;
import me.itangqi.greendao.Goods;
import me.itangqi.greendao.GoodsDao;
import me.itangqi.greendao.Mood;
import me.itangqi.greendao.MoodDao;
import me.itangqi.greendao.MoodMessage;
import me.itangqi.greendao.MoodMessageDao;
import me.itangqi.greendao.New;
import me.itangqi.greendao.NewDao;
import me.itangqi.greendao.User;
import me.itangqi.greendao.UserDao;

/**
 * Created by yum on 2016/4/7.
 */
public class GreenUtils {
    private static GreenUtils instance;
    private static Context context;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private DaoMaster.DevOpenHelper helper;
    public static void init(Context context){
        GreenUtils.context = context;
    }
    private GreenUtils (){
        helper = new DaoMaster.DevOpenHelper(context, "notes-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static GreenUtils getInstance(){
        if (context == null){
            Logger.e("GreenUtils error", "请先初始化GreenUtils.init()");
            return null;
        }
        if (instance != null){
            return instance;
        }
        instance = new GreenUtils();
        Logger.i("GreenUtils success", "成功初始化GreenUtils.init()");

        return instance;
    }

    public List<Coupon> queryCouponList(Context context,int page,long newestCouponId){
        int index = (page-1)*Config.DATABASE_LODING_NUM;
        int current_user_id = AbSharedUtil.getInt(context, Config.Current_user_id);
        //Cursor cursor = rawQuery("select * from " + Config.DATABASE_TABLE_COUPON + " where user_id=" + current_user_id + " and _id<=" + newestCouponId + " order by couponID desc limit " + index + "," + Config.DATABASE_LODING_NUM);
        List<Coupon> couponList = getCouponDao().queryBuilder()
                .where(CouponDao.Properties.User_id.eq(current_user_id))
                .where(CouponDao.Properties.Id.le(newestCouponId))
                .orderDesc(CouponDao.Properties.CouponID)
                .offset(index)
                .limit(Config.DATABASE_LODING_NUM)
                .list();
        StringBuilder builder = new StringBuilder();
        for (Coupon coupon : couponList) {
            builder.append(coupon.toString());
            builder.append("\n");
        }
        Logger.i("query", builder.toString());

        return couponList;
    }
    public List<Goods>  queryGoodList(Context context,String goodType,int page,long newestGoodId){
        int index = (page-1)*Config.DATABASE_LODING_NUM;
        int current_user_id = AbSharedUtil.getInt(context, Config.Current_user_id);
        List<Goods> goodList = null;
       // Cursor cursor = rawQuery("select * from "+Config.DATABASE_TABLE_GOOD+" where user_id="+current_user_id+" and goodType='"+goodType+"' and _id<="+newestGoodId+" order by sellID desc limit "+index+","+Config.DATABASE_LODING_NUM);
        goodList = getGoodsDao().queryBuilder()
                .where(GoodsDao.Properties.User_id.eq(current_user_id))
                .where(GoodsDao.Properties.Id.le(newestGoodId))
                .where(GoodsDao.Properties.GoodType.eq(goodType))
                .orderDesc(GoodsDao.Properties.SellID)
                .offset(index)
                .limit(Config.DATABASE_LODING_NUM)
                .list();

        StringBuilder builder = new StringBuilder();
        for (Goods goods : goodList) {
            builder.append(goods.toString());
            builder.append("\n");
        }
        Logger.i("query", builder.toString());

        return goodList;
    }
    public List<Mood>   queryMoodList(Context context,int page,long newestMoodId,String type){
        int index = (page-1)*Config.DATABASE_LODING_NUM;
        int current_user_id = AbSharedUtil.getInt(context, Config.Current_user_id);
        List<Mood> moodList = null;
        //Cursor cursor = rawQuery("select * from "+Config.DATABASE_TABLE_MOOD+" where user_id="+current_user_id+" and _id<="+newestMoodId+" order by moodId desc limit "+index+","+Config.DATABASE_LODING_NUM);
        moodList = getMoodDao().queryBuilder()
                .where(MoodDao.Properties.User_id.eq(current_user_id))
                .where(MoodDao.Properties.Id.le(newestMoodId))
                .where(MoodDao.Properties.Type.eq(type))
                .orderDesc(MoodDao.Properties.MoodId)
                .offset(index)
                .limit(Config.DATABASE_LODING_NUM)
                .list();
        StringBuilder builder = new StringBuilder();
        for (Mood mood : moodList) {
            builder.append(mood.toString());
            builder.append("\n");
        }
        Logger.i("query", builder.toString());

        return moodList;
    }
    public List<Mood>   queryMyOwnMoodList(Context context,int page,long newestMoodId){
        int index = (page-1)*Config.DATABASE_LODING_NUM;
        int current_user_id = AbSharedUtil.getInt(context, Config.Current_user_id);
        int Current_user_name = AbSharedUtil.getInt(context, Config.UserName);
        List<Mood> moodList = null;
        //Cursor cursor = rawQuery("select * from "+Config.DATABASE_TABLE_MOOD+" where user_id="+current_user_id+" and _id<="+newestMoodId+" order by moodId desc limit "+index+","+Config.DATABASE_LODING_NUM);
        moodList = getMoodDao().queryBuilder()
                .where(MoodDao.Properties.User_id.eq(current_user_id))//当前账号  所有说说
                .where(MoodDao.Properties.UserName.eq(Current_user_name))//自己发的说说
                .where(MoodDao.Properties.Id.le(newestMoodId))
                .orderDesc(MoodDao.Properties.MoodId)
                .offset(index)
                .limit(Config.DATABASE_LODING_NUM)
                .list();

        return moodList;
    }

    public List<Banner> queryBannerList(int page,long newestBannerId,String type){
        int index = (page-1)*Config.DATABASE_LODING_AD_NUM;
        List<Banner> bannerList = null;
        //Cursor cursor = rawQuery("select * from "+Config.DATABASE_TABLE_BANNER+" where  _id<="+newestBannerId+" order by adId desc limit "+index+","+Config.DATABASE_LODING_AD_NUM);
        bannerList = getBannerDao().queryBuilder()
               .where(BannerDao.Properties.Id.le(newestBannerId))
               .where(BannerDao.Properties.Type.eq(type))
               .orderDesc(BannerDao.Properties.AdID)
               .offset(index)
               .limit(Config.DATABASE_LODING_NUM)
               .list();
        StringBuilder builder = new StringBuilder();
        for (Banner banner : bannerList) {
            builder.append(banner.toString());
            builder.append("\n");
        }
        Logger.i("query", builder.toString());

        return bannerList;
    }
    public List<Banner> queryBannerList(String type){
        List<Banner> bannerList = null;
        //Cursor cursor = rawQuery("select * from "+Config.DATABASE_TABLE_BANNER+" where  _id<="+newestBannerId+" order by adId desc limit "+index+","+Config.DATABASE_LODING_AD_NUM);
        bannerList = getBannerDao().queryBuilder()
                .where(BannerDao.Properties.Type.eq(type))
                .list();

        StringBuilder builder = new StringBuilder();
        for (Banner banner : bannerList) {
            builder.append(banner.toString());
            builder.append("\n");
        }
        Logger.i("query", builder.toString());

        return bannerList;
    }
    public List<New> queryNewsList(Context context,int page,long newestNewId,String type){
        int index = (page-1)*Config.DATABASE_LODING_NUM;
        int current_user_id = AbSharedUtil.getInt(context, Config.Current_user_id);
        List<New> newsList = getNewDao().queryBuilder()
                .where(NewDao.Properties.User_id.eq(current_user_id))//当前账号的新闻
                .where(NewDao.Properties.Id.le(newestNewId))//并且《= 参考的最大id
                .where(NewDao.Properties.Type.eq(type))
                .orderDesc(NewDao.Properties.TopId)//按 topid逆序
                .offset(index)//分页
                .limit(Config.DATABASE_LODING_NUM)
                .list();
        StringBuilder builder = new StringBuilder();
        for (New aNew : newsList) {
            builder.append(aNew.toString());
            builder.append("\n");
        }
        Logger.i("query", builder.toString());
        return newsList;
    }
    public List<MoodMessage>   queryMoodMessageList(boolean hadRead){
        int current_user_id = AbSharedUtil.getInt(context, Config.Current_user_id);
        List<MoodMessage> moodMessageList = null;
        //Cursor cursor = rawQuery("select * from "+Config.DATABASE_TABLE_MOOD+" where user_id="+current_user_id+" and _id<="+newestMoodId+" order by moodId desc limit "+index+","+Config.DATABASE_LODING_NUM);
        moodMessageList = getMoodMessageDao().queryBuilder()
                .where(MoodMessageDao.Properties.User_id.eq(current_user_id))
                .where(MoodMessageDao.Properties.HadRead.eq(hadRead))
                .orderDesc(MoodMessageDao.Properties.Id)
                .list();
        StringBuilder builder = new StringBuilder();
        for (MoodMessage moodMessage : moodMessageList) {
            builder.append(moodMessage.toString());
            builder.append("\n");
        }
        Logger.i("query", builder.toString());

        return moodMessageList;
    }

    public long queryNewCurrentNewestId(Context context,String type){
        int user_id = AbSharedUtil.getInt(context, Config.Current_user_id);
        long _id = -1;
        Cursor cursor = db.rawQuery("select max(_id) as max_id from " + NewDao.TABLENAME + " where " + NewDao.Properties.User_id.columnName + "=" + user_id + " and " + NewDao.Properties.Type.columnName + "='" + type + "'", null);
        while(cursor.moveToNext()){
            _id = cursor.getInt(cursor.getColumnIndex("max_id"));
        }
        Logger.i("query","queryNewCurrentNewestId:" + _id+"   user_id:"+user_id);

        return _id;
    }
    public long queryCouponCurrentNewestId(Context context){
        int user_id = AbSharedUtil.getInt(context,Config.Current_user_id);
        long _id = -1;
        Cursor cursor = db.rawQuery("select max(_id) as max_id from " + CouponDao.TABLENAME + " where "+ CouponDao.Properties.User_id.columnName+"=" + user_id, null);
        while(cursor.moveToNext()){
            _id = cursor.getInt(cursor.getColumnIndex("max_id"));
        }
        Logger.i("query","queryCouponCurrentNewestId:" + _id+"   user_id:"+user_id);

        return _id;
    }
    public long queryGoodCurrentNewestId(Context context,String goodType){
        int user_id = AbSharedUtil.getInt(context,Config.Current_user_id);

        long _id = -1;
        Cursor cursor = db.rawQuery("select max(_id) as max_id from " + GoodsDao.TABLENAME + " where "+GoodsDao.Properties.User_id.columnName+"=" + user_id + " and "+GoodsDao.Properties.GoodType.columnName+"='" + goodType + "'",null);
        while(cursor.moveToNext()){
            _id = cursor.getInt(cursor.getColumnIndex("max_id"));
        }
        Logger.i("query","queryGoodCurrentNewestId:" + _id+"   user_id:"+user_id);

        return _id;
    }
    public long queryMoodCurrentNewestId(Context context,String type){
        int user_id = AbSharedUtil.getInt(context,Config.Current_user_id);
        long _id = -1;
        Cursor cursor = db.rawQuery("select max(_id) as max_id from " + MoodDao.TABLENAME + " where "+MoodDao.Properties.User_id.columnName+"=" + user_id+" and "+MoodDao.Properties.Type.columnName+" = '"+type+"'",null);
        while(cursor.moveToNext()){
            _id = cursor.getInt(cursor.getColumnIndex("max_id"));
        }
        Logger.i("query","queryMoodCurrentNewestId:" + _id+"   user_id:"+user_id);

        return _id;
    }
    public long queryMyOwnMoodCurrentNewestId(Context context){
        int user_id = AbSharedUtil.getInt(context,Config.Current_user_id);
        int userName = AbSharedUtil.getInt(context,Config.UserName);
        long _id = -1;
        Cursor cursor = db.rawQuery("select max(_id) as max_id from " + MoodDao.TABLENAME + " where "+MoodDao.Properties.User_id.columnName+"=" + user_id+" and "+ MoodDao.Properties.UserName.columnName+"="+userName,null);
        while(cursor.moveToNext()){
            _id = cursor.getInt(cursor.getColumnIndex("max_id"));
        }
        return _id;
    }

    public long queryBannerCurrentNewestId(String type){
        long _id = -1;
        Cursor cursor = db.rawQuery("select max(_id) as max_id from " + BannerDao.TABLENAME + " where " + BannerDao.Properties.Type.columnName + "='" + type + "'", null);
        while(cursor.moveToNext()){
            _id = cursor.getInt(cursor.getColumnIndex("max_id"));
        }
        Logger.i("query","queryBannerCurrentNewestId:" + _id);

        return _id;
    }
    public long queryCurrentUserId(String userName){
        long current_user_id  = getUserDao().queryBuilder()
                .where(UserDao.Properties.UserName.eq(userName))
                .list().get(0).getId();
        Logger.i("query", "current_user_id"+current_user_id);
        return current_user_id;

    }

    public UserDao getUserDao(){
        return daoSession.getUserDao();
    }
    public BannerDao getBannerDao(){
        return daoSession.getBannerDao();
    }
    public CouponDao getCouponDao(){
        return daoSession.getCouponDao();
    }
    public GoodsDao getGoodsDao(){
        return daoSession.getGoodsDao();
    }
    public MoodDao getMoodDao(){
        return daoSession.getMoodDao();
    }
    public NewDao getNewDao(){
        return daoSession.getNewDao();
    }
    public MoodMessageDao getMoodMessageDao(){
        return daoSession.getMoodMessageDao();
    }

    public DaoMaster.DevOpenHelper getHelper() {
        return helper;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void deleteGoodTable(Integer sellID) {
        String sql = "delete from "+GoodsDao.TABLENAME+" where "+ GoodsDao.Properties.SellID.columnName+" = '"+sellID+"'";
        getDb().execSQL(sql);
        Logger.i("delete", sql);

    }

    public void insertOrReplaceCoupon(Coupon coupon) {

        long user_id = AbSharedUtil.getInt(context, Config.Current_user_id);
        coupon.setUser_id(user_id);
        List list = getCouponDao().queryBuilder()
                .where(CouponDao.Properties.CouponID.eq(coupon.getCouponID()))
                .where(CouponDao.Properties.User_id.eq(user_id))
                .list();
        int size = list==null?0:list.size();

        if (size <= 0){
            getCouponDao().insertOrReplace(coupon);
            Logger.i("insert", "coupon 不存在，执行插入\n"+coupon.toString());
        }else{
            Logger.i("insert", "coupon 已存在，不执行插入\n"+coupon.toString());

        }
    }

    public void insertOrReplaceGoods(Goods goods) {

        long user_id = AbSharedUtil.getInt(context,Config.Current_user_id);
        goods.setUser_id(user_id);
        List list = getGoodsDao().queryBuilder()
                .where(GoodsDao.Properties.SellID.eq(goods.getSellID()))
                .where(GoodsDao.Properties.User_id.eq(user_id))
                .list();
        int size = list==null?0:list.size();

        if (size <= 0){
            getGoodsDao().insertOrReplace(goods);
            Logger.i("insert", "good 不存在，执行插入\n"+goods.toString());
        }else{
            Logger.i("insert", "good 已存在，不执行插入\n"+goods.toString());

        }
    }

    public void insertOrReplaceMood(Mood mood,String type) {
        mood.setType(type);
        mood.setSex(mood.getMe().getSex());
        mood.setBelongschool(mood.getMe().getBelongschool());
        mood.setUserPic(mood.getMe().getUserPic());
        mood.setNickName(mood.getMe().getNickName());
        mood.setUserName(mood.getMe().getUserName());
        long user_id = AbSharedUtil.getInt(context,Config.Current_user_id);
        mood.setUser_id(user_id);
        List list = getMoodDao().queryBuilder()
                .where(MoodDao.Properties.MoodId.eq(mood.getMoodId()))
                .where(MoodDao.Properties.User_id.eq(user_id))
                .list();
        int size = list==null?0:list.size();

        if (size <= 0){
            getMoodDao().insertOrReplace(mood);
            Logger.i("insert", "mood 不存在，执行插入\n"+ mood.toString());
        }else{
            Logger.i("insert", "mood 已存在，不执行插入\n"+ mood.toString());

        }
    }

    public void insertOrReplaceNew(New newtemp,String type) {

        long user_id = AbSharedUtil.getInt(context,Config.Current_user_id);
        newtemp.setUser_id(user_id);
        List list = getNewDao().queryBuilder()
                .where(NewDao.Properties.TopId.eq(newtemp.getTopId()))
                .where(NewDao.Properties.User_id.eq(user_id))
                .list();
        int size = list==null?0:list.size();

        newtemp.setType(type);
        if (size <= 0){
            getNewDao().insertOrReplace(newtemp);
            Logger.i("insert", "news 不存在，执行插入\n"+newtemp.toString());
        }else{
            Logger.i("insert", "news 已存在，不执行插入\n"+newtemp.toString());

        }
    }
    public void insertOrReplaceUser(User user) {
        List list = getUserDao().queryBuilder()
                .where(UserDao.Properties.UserName.eq(user.getUserName()))
                .list();
        int size = list==null?0:list.size();

        if (size <= 0){
            getUserDao().insert(user);
            Logger.i("insert", "user 不存在，执行插入\n"+user.toString());
        }else{
            Logger.i("insert", "user 已存在，不执行插入\n"+user.toString());
        }
    }
    public void insertOrReplaceBanner(Banner banner1,String type) {

        List list = getBannerDao().queryBuilder()
                .where(BannerDao.Properties.AdID.eq(banner1.getAdID()))
                .where(BannerDao.Properties.Type.eq(type))
                .list();
        int size = list==null?0:list.size();

        banner1.setType(type);
        if (size <= 0) {
            getBannerDao().insertOrReplace(banner1);
            Logger.i("insert", "banner 不存在，执行插入\n"+banner1.toString());
        }else{
            Logger.i("insert", "banner 已存在，不执行插入\n"+banner1.toString());

        }

    }
    public void insertOrReplaceMoodMessage(MoodMessage moodMessage,boolean hadRead) {
        moodMessage.setHadRead(hadRead);
        long user_id = AbSharedUtil.getInt(context,Config.Current_user_id);
        moodMessage.setUser_id(user_id);
        getMoodMessageDao().insertOrReplace(moodMessage);
    }

    public void deleteAllBannerByType(String type){
        String sql = "delete from "+BannerDao.TABLENAME+" where "+ BannerDao.Properties.Type.columnName+" = '"+type+"'";
        getDb().execSQL(sql);
        Logger.i("delete", sql);

    }
    public void deleteAllGoodsByType(String goodType){
        String sql = "delete from "+GoodsDao.TABLENAME+" where "+ GoodsDao.Properties.GoodType.columnName+" = '"+goodType+"'";
        getDb().execSQL(sql);
        Logger.i("delete", sql);

    }
    public void deleteAllNewsByType(String type){

        String sql = "delete from "+NewDao.TABLENAME+" where "+ NewDao.Properties.Type.columnName+" = '"+type+"'";
        getDb().execSQL(sql);
        Logger.i("delete", sql);

    }
    public void deleteAllMoodByType(String type){
        long user_id = AbSharedUtil.getInt(context,Config.Current_user_id);
        String sql = "delete from "+MoodDao.TABLENAME+" where "+ MoodDao.Properties.Type.columnName+" = '"+type+"'"+" and "+MoodDao.Properties.User_id.columnName+"="+user_id;
        getDb().execSQL(sql);
        Logger.i("delete", sql);
    }
    public void deleteAllMoodMessageByUserid(String type){
        long user_id = AbSharedUtil.getInt(context,Config.Current_user_id);

        String sql = "delete from "+ MoodMessageDao.TABLENAME+" where "+ MoodMessageDao.Properties.User_id.columnName+" = "+user_id;
        getDb().execSQL(sql);
        Logger.i("delete", sql);

    }
    public int getMoodMessageUnreadCount(){
        long user_id = AbSharedUtil.getInt(context,Config.Current_user_id);
        int count = getMoodMessageDao().queryBuilder()
                .where(MoodMessageDao.Properties.User_id.eq(user_id))
                .where(MoodMessageDao.Properties.HadRead.eq(false))
                .list()
                .size();
        return count;

    }
    public void updateMoodMessageToHadread(long _id){
        long user_id = AbSharedUtil.getInt(context,Config.Current_user_id);
        String sql = "update "+MoodMessageDao.TABLENAME+" set "+MoodMessageDao.Properties.HadRead.columnName+" = '"+true+"' where "+ MoodMessageDao.Properties.User_id.columnName+" = "+user_id+" and "+ MoodMessageDao.Properties.Id.columnName+" = "+_id;
        getDb().execSQL(sql);
        Logger.i("delete", sql);
    }
    public void clearCache(){
        getBannerDao().deleteAll();
        getCouponDao().deleteAll();
        getGoodsDao().deleteAll();
        getMoodMessageDao().deleteAll();
        getUserDao().deleteAll();
        getNewDao().deleteAll();
        getMoodDao().deleteAll();

    }
}
