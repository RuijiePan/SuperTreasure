package me.itangqi.greendao;

import com.google.gson.Gson;



import de.greenrobot.dao.converter.PropertyConverter;

/**
 * Created by yum on 2016/4/9.
 */
public class PeoplePropertyConverter implements PropertyConverter<People,String> {

    @Override
    public People convertToEntityProperty(String databaseValue) {
        return new Gson().fromJson(databaseValue,People.class);
    }

    @Override
    public String convertToDatabaseValue(People entityProperty) {
        return new Gson().toJson(entityProperty,People.class);
    }
}
