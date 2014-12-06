package com.cyou.mrd.pengyou.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.cyou.mrd.pengyou.entity.base.MyGameBase;
import com.cyou.mrd.pengyou.log.CYLog;

public class JsonUtils {
	private CYLog cyLog = CYLog.getInstance();
	private static final ObjectMapper mapper = new ObjectMapper();
	public static final String DATA = "data";

	private JsonUtils() {
		// 忽略没有set方法的字段
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	}

	public <T> ArrayList<T> getListByJson(String content, Class<T> t) {
		ArrayList<T> list = null;
		try {
			JSONObject jsonObj = new JSONObject(content);
			list = new ArrayList<T>();
			JSONArray jsonArray = jsonObj.getJSONArray(DATA);
			for (int index = 0; index < jsonArray.length(); index++) {
				JSONObject jsonItemObject = jsonArray.getJSONObject(index);
				T tInfo = (T) JsonUtils.fromJson(jsonItemObject.toString(), t);
				list.add(tInfo);
			}
		} catch (Exception e) {
			cyLog.e(e);
		}
		return list;
	}

	/**
	 * @param <T>
	 *            泛型声明
	 * @param bean
	 *            类的实例
	 * @return JSON字符串
	 */
	public static <T> String toJson(T bean) {
		// 忽略没有set方法的字段
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		if (null == bean) {
			return null;
		}
		StringWriter sw = new StringWriter();
		try {
			JsonGenerator gen = new JsonFactory().createJsonGenerator(sw);
			mapper.writeValue(gen, bean);
			gen.close();
			return sw.toString();
		} catch (JsonGenerationException e) {
			CYLog.getInstance().e(e);
		} catch (JsonMappingException e) {
			CYLog.getInstance().e(e);
		} catch (IOException e) {
			CYLog.getInstance().e(e);
		}
		return null;
	}

	/**
	 * @param <T>
	 *            泛型声明
	 * @param json
	 *            JSON字符串
	 * @param clzz
	 *            要转换对象的类型
	 * @return
	 */
	public static <T> T fromJson(String json, Class<T> clzz) {
		// 忽略没有set方法的字段
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		if (TextUtils.isEmpty(json)) {
			return null;
		}
		T t = null;
		try {
			t = mapper.readValue(json, clzz);
		} catch (JsonParseException e) {
			CYLog.getInstance().e(e);
		} catch (JsonMappingException e) {
			CYLog.getInstance().e(e);
		} catch (IOException e) {
			CYLog.getInstance().e(e);
		}
		return t;
	}

	public static String getJsonValue(String jsonText, String key) {
		// 忽略没有set方法的字段
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		String value = "";
		if (TextUtils.isEmpty(jsonText)) {
			return null;
		}
		try {
			JSONObject jsonObj = new JSONObject(jsonText);
			value = jsonObj.getString(key);
		} catch (Exception e) {
			CYLog.getInstance().e(e);
		}
		return value;
	}

	/**
	 * @param json
	 *            JSON字符串,请保持key是加了双引号的
	 * @return Map对象,默认为HashMap
	 */
	public static Map<?, ?> fromJson(String json) {
		// 忽略没有set方法的字段
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		if (TextUtils.isEmpty(json)) {
			return null;
		}
		try {
			return mapper.readValue(json, HashMap.class);
		} catch (JsonParseException e) {
			CYLog.getInstance().e(e);
		} catch (JsonMappingException e) {
			CYLog.getInstance().e(e);
		} catch (IOException e) {
			CYLog.getInstance().e(e);
		}
		return null;
	}

	/**
	 * 解析json使用泛型转换为对应List
	 * 
	 * @param collectionClass
	 * @param elementClasses
	 * @return
	 */
	public static <T> List<T> json2List(String json, Class<T> mclass) {
		// 忽略没有set方法的字段
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		if (TextUtils.isEmpty(json)) {
			return null;
		}
		JavaType javaType = getCollectionType(ArrayList.class, mclass);
		List<T> lst = null;
		try {
			lst = mapper.readValue(json, javaType);
		} catch (JsonParseException e) {
			CYLog.getInstance().e(e);
		} catch (JsonMappingException e) {
			CYLog.getInstance().e(e);
		} catch (IOException e) {
			CYLog.getInstance().e(e);
		}
		return lst;
	}

	public static JavaType getCollectionType(Class<?> collectionClass,
			Class<?>... elementClasses) {
		// 忽略没有set方法的字段
		mapper.configure(
				DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		return mapper.getTypeFactory().constructParametricType(collectionClass,
				elementClasses);
	}

	/**
	 * xumengyang
	 * @param obj
	 * @param json
	 * @return
	 */
	public static <T> T data2Obj(Class<T> t,String json){
		try {
			if(TextUtils.isEmpty(json)){
				return null;
			}
			JSONObject obj = new JSONObject(json);
			if(obj.has("data")){
				T o =  new ObjectMapper()
				.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false)
				.readValue(obj.get("data").toString(),t);
				return o;
			}
		} catch (Exception e) {
		}
		return null;
	}
	
	public static boolean isSuccessful(String content){
		try {
			JSONObject obj = new JSONObject(content);
			if(obj.has("successful")){
				return obj.getInt("successful") == 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
