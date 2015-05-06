package com.esoft.core.util;

import java.util.Map;

import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * <p>
 * GSON工具类
 * </p>
 * 
 * @author Wallis Wang
 * @version $Id: GsonUtil.java, v 0.1 2014年5月15日 下午5:41:20 wangqiang Exp $
 */
public class GsonUtil {

	private static Gson gson = null;
	private static JsonParser jsonParser = null;
	public static final GsonBuilder HibernatePBuilder=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY).serializeNulls();
	
	static {
		gson = new GsonBuilder().create();
		jsonParser = new JsonParser();
	}
	
	public static JsonObject stringToJsonObject(String str){
		return (JsonObject) jsonParser.parse(str);
	}

	/**
	 * 小写下划线的格式解析JSON字符串到对象
	 * <p>
	 * 例如 is_success->isSuccess
	 * </p>
	 * 
	 * @param json
	 * @param classOfT
	 * @return
	 */
	public static <T> T fromJsonUnderScoreStyle(String json, Class<T> classOfT) {
		return gson.fromJson(json, classOfT);
	}

	/**
	 * JSON字符串转为Map<String,String>
	 * 
	 * @param json
	 * @return
	 */
	@SuppressWarnings("all")
	public static Map<String, String> fromJson2Map(String json) {
		return gson.fromJson(json, new TypeToken<Map<String, String>>() {
		}.getType());
	}

	/**
	 * Map<String,String>转为JSON字符串
	 * 
	 * @param json
	 * @return
	 */
	@SuppressWarnings("all")
	public static String fromMap2Json(Map<String, String> map) {
		return gson.toJson(map, new TypeToken<Map<String, String>>() {
		}.getType());
	}

	/**
	 * 小写下划线的格式将对象转换成JSON字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String toJson(Object src) {
		return gson.toJson(src);
	}
}
