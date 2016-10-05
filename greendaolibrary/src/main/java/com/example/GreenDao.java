package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class GreenDao {
    public static void main(String[] args) throws Exception {
        // 正如你所见的，你创建了一个用于添加实体（Entity）的模式（Schema）对象。
        // 两个参数分别代表：数据库版本号与自动生成代码的包路径。
        Schema schema = new Schema(1, "me.itangqi.greendao");
//      当然，如果你愿意，你也可以分别指定生成的 Bean 与 DAO 类所在的目录，只要如下所示：
//      Schema schema = new Schema(1, "me.itangqi.bean");
//      schema.setDefaultJavaPackageDao("me.itan gqi.dao");

        // 模式（Schema）同时也拥有两个默认的 flags，分别用来标示 entity 是否是 activie 以及是否使用 keep sections。
        // schema2.enableActiveEntitiesByDefault();
        // schema2.enableKeepSectionsByDefault();

        // 一旦你拥有了一个 Schema 对象后，你便可以使用它添加实体（Entities）了。
        addNote(schema);

        // 最后我们将使用 DAOGenerator 类的 generateAll() 方法自动生成代码，此处你需要根据自己的情况更改输出目录（既之前创建的 java-gen)。
        // 其实，输出目录的路径可以在 build.gradle 中设置，有兴趣的朋友可以自行搜索，这里就不再详解。
        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }

    /**
     * @param schema
     */
    private static void addNote(Schema schema) {
        Entity userEntity = schema.addEntity("User");
        // 接下来你便可以设置表中的字段：
        userEntity.addIdProperty().primaryKey().autoincrement().getProperty();
        userEntity.addStringProperty("account");
        userEntity.addStringProperty("password");
        userEntity.addStringProperty("belongschool");
        userEntity.addStringProperty("sex");
        userEntity.addStringProperty("userPic");
        userEntity.addStringProperty("nickName");
        userEntity.addStringProperty("userName");

        Entity newsEntity = schema.addEntity("New");
        newsEntity.addIdProperty().primaryKey().autoincrement();
        Property userId = newsEntity.addLongProperty("user_id").getProperty();
        userEntity.addToMany(newsEntity,userId).setName("news");//一个用户 有多条新闻。用户添加一个属性 ：new表的,userId属性。
        newsEntity.addToOne(userEntity,userId);//一条新闻，只对应一个用户
        //newsEntity.addToOne(userEntity, userId);
        //newsEntity.addIntProperty("user_id");//外键
        newsEntity.addStringProperty("topId");
        newsEntity.addStringProperty("adminID");
        newsEntity.addStringProperty("adminName");
        newsEntity.addStringProperty("title");
        newsEntity.addStringProperty("label");
        newsEntity.addStringProperty("summary");
        newsEntity.addStringProperty("pics");
        newsEntity.addStringProperty("browserNum");
        newsEntity.addStringProperty("praise");
        newsEntity.addStringProperty("url");
        newsEntity.addBooleanProperty("isPraised");

        Entity moodEntity = schema.addEntity("Mood");
        moodEntity.addIdProperty().primaryKey().autoincrement();
        //moodEntity.addToOne(userEntity, userId);
        Property userId1 = moodEntity.addLongProperty("user_id").getProperty();
        userEntity.addToMany(moodEntity, userId1).setName("moods");
        moodEntity.addToOne(userEntity, userId1);
        moodEntity.addIntProperty("moodId");
        moodEntity.addStringProperty("content");
        moodEntity.addStringProperty("paths");
        moodEntity.addStringProperty("publishtime");
        moodEntity.addStringProperty("praiseTimes");
        moodEntity.addStringProperty("commentCount");
        moodEntity.addBooleanProperty("isPraised");
        moodEntity.addStringProperty("belongschool");
        moodEntity.addStringProperty("sex");
        moodEntity.addStringProperty("userPic");
        moodEntity.addStringProperty("nickName");
        moodEntity.addStringProperty("userName");
        moodEntity.addStringProperty("me").customType("me.itangqi.greendao.People", "me.itangqi.greendao.PeoplePropertyConverter");


        Entity couponEntity = schema.addEntity("Coupon");
        couponEntity.addIdProperty().primaryKey().autoincrement();
        //couponEntity.addToOne(userEntity, userId);
        Property userId2 = couponEntity.addLongProperty("user_id").getProperty();
        userEntity.addToMany(couponEntity, userId2).setName("coupons");
        couponEntity.addToOne(userEntity,userId2);
        couponEntity.addIntProperty("couponID");
        couponEntity.addStringProperty("couponPics");
        couponEntity.addIntProperty("width");
        couponEntity.addIntProperty("height");
        couponEntity.addIntProperty("couponPraise");
        couponEntity.addBooleanProperty("isPraise");
        couponEntity.addBooleanProperty("isCollect");
        couponEntity.addStringProperty("intro");
        couponEntity.addStringProperty("beginTime");
        couponEntity.addStringProperty("lastTime");
        couponEntity.addStringProperty("shopName");
        couponEntity.addIntProperty("shopID");
        couponEntity.addIntProperty("browserNum");

        Entity goodEntity = schema.addEntity("Goods");
        goodEntity.addIdProperty().primaryKey().autoincrement();
        //goodEntity.addToOne(userEntity,userId);
        Property userId3 =  goodEntity.addLongProperty("user_id").getProperty();
        userEntity.addToMany(goodEntity, userId3).setName("goods");
        goodEntity.addToOne(userEntity, userId3);
        goodEntity.addIntProperty("sellID");
        goodEntity.addIntProperty("width");
        goodEntity.addIntProperty("height");
        goodEntity.addStringProperty("userName");
        goodEntity.addStringProperty("pic");
        goodEntity.addStringProperty("price");
        goodEntity.addStringProperty("goodType");

        Entity bannerEntity = schema.addEntity("Banner");
        bannerEntity.addIdProperty().primaryKey().autoincrement();
        bannerEntity.addIntProperty("adID");
        bannerEntity.addStringProperty("photo");
        bannerEntity.addStringProperty("alink");

    }
}
