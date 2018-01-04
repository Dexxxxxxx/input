package com.cdhy.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONObject;

public class JSONUtil {

    /**
     * 获取不为null(包括"null")的json 字符串值
     * 
     * @author pjf
     * @Date 2016年1月21日
     * @param js
     * @param key
     * @return
     */
    public static String getString(JSONObject js, String key) {
	String value = js.get(key) == null ? "" : js.getString(key);
	if (value.equals("null")) {
	    value = "";
	}
	return value;
    }

    public static double getDouble(JSONObject js, String key, double def) {
	String str = getString(js, key);
	if ("".equals(str)) {
	    return def;
	}
	return Double.parseDouble(str);
    }

    public static int getInteger(JSONObject js, String key, int def) {
	String str = getString(js, key);
	if ("".equals(str)) {
	    return def;
	}
	return Integer.parseInt(str);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Map<String, Object> json2Map(JSONObject json){
	Map<String, Object> map = new HashMap<>();
	Set keySet = json.keySet();
	for (Object key : keySet) {
	    Object value = json.get(key);
	    if (value instanceof Map) {
		map.put((String) key, json2Map(JSONObject.fromObject(value)));
	    }else if (value instanceof List) {
		List list = (List) value;
		List<Object> tList = new ArrayList<>();
		for (Object obj : list) {
		    if (obj instanceof Map) {
			Map<String, Object> tmap = (Map<String, Object>) obj;
			tmap.put((String) key, json2Map(JSONObject.fromObject(obj)));
			tList.add(tmap);
		    }else{
			tList.add(obj);
		    }
		    map.put((String) key, tList);
		}
	    }else {
		map.put((String) key, value);
	    }
	}
	return map;
    }
    
}
