package com.cdhy.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.cdhy.exception.BizException;

public class MapUtil {

    /**
     * 获取不为null的 字符串值
     * 
     * @author pjf
     * @Date 2016年1月21日
     * @param js
     * @param key
     * @return
     */
    public static String getString(Map<String, Object> map, String key) {
	String value = "";
	try {
	    value = map.get(key) == null ? "" : map.get(key).toString();
	} catch (Exception e) {
	    throw new BizException("类型错误");
	}
	return value;
    }
    public static String getValue(Map<String, Object> map, String key) {
	String value = "";
	try {
		value = map.get(key) == null ? "" : map.get(key).toString();
		if("".equals(value)||value==null){
			throw new BizException(key+"的值为空");
		}
	} catch (Exception e) {
		throw new BizException(e.getMessage());
	}
	return value;
    }
    
    /**
     * 获取int
     * 
     * @author pjf
     * @Date 2016年1月21日
     * @param js
     * @param key
     * @return
     */
    public static int getInt(Map<String, Object> map, String key) {
	String value = "";
	int result= 0;
	try {
	    value = map.get(key) == null ? "" : map.get(key).toString();
	    result = Integer.parseInt(value);
	} catch (Exception e) {
	    throw new BizException("类型错误");
	}
	return result;
    }
    
    /**
     * 获取int
     * 
     * @author pjf
     * @Date 2016年1月21日
     * @param js
     * @param key
     * @return
     */
    public static double getDouble(Map<String, Object> map, String key) {
	String value = "";
	double result= 0;
	try {
	    value = map.get(key) == null ? "" : map.get(key).toString();
	    result = Double.parseDouble(value);
	} catch (Exception e) {
	    throw new BizException("类型错误");
	}
	return result;
    }

    /**
     * 转换map的KEY为小写
     * 
     * @author pjf
     * @param map
     * @return
     */
    public static Map<String, Object> mapKey2Lower(Map<String, Object> map) {
	Map<String, Object> result = new HashMap<>();
	Set<String> keySet = map.keySet();
	for (String key : keySet) {
	    Object value = map.get(key);
	    result.put(key.toLowerCase(), value);
	}
	return result;
    }
    
    /**
     * 转换map的KEY为大写
     * 
     * @author pjf
     * @param map
     * @return
     */
    public static Map<String, Object> mapKey2Upper(Map<String, Object> map) {
	Map<String, Object> result = new HashMap<>();
	Set<String> keySet = map.keySet();
	for (String key : keySet) {
	    Object value = map.get(key);
	    result.put(key.toUpperCase(), value);
	}
	return result;
    }

}
