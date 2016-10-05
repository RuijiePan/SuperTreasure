package com.supertreasure.util;

import com.google.gson.Gson;
import com.google.gson.JsonNull;

import java.lang.reflect.Type;

public class JsonUtils {

	private static Gson gson = new Gson();
	
	private JsonUtils() {
		
	}

	public static String toJson(Object obj){
		if(obj == null){
			return gson.toJson(JsonNull.INSTANCE);
		}
		return gson.toJson(obj);
	}
	
	public static <T> T fromJson(String json, Class<T> classOfT){
		return gson.fromJson(json, classOfT);
	}
	
	public static <T> T fromJson(String json, Type typeOfT){
		return gson.fromJson(json, typeOfT);
	}
}
