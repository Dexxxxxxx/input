package com.cdhy.comms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * 扫码开票参数缓存信息 date: 2016年5月12日 上午10:54:46 <br/>
 *
 * @author pjf
 * @version
 */

public class ParamCacheData {
    private static final Logger log = Logger.getLogger(ParamCacheData.class);

    private static Map<String, Object> fpParamData = new HashMap<>();
    private static int maxSize = 10 * 10000;
    private static int size = 0;
    private static List<String> keyList = new ArrayList<>();

    /**
     * 存扫码开票参数缓存信息
     * 
     * @author pjf
     * @param
     * @return
     */
    public static synchronized void putParam(String key, Map<String, Object> value) {
	if (key == null || "".equals(key) || value == null) {
	    return;
	}
	if (size >= maxSize) {
	    int _max = maxSize / 2;
	    for (int i = 0; i < _max / 2; i++) {
		fpParamData.remove(keyList.get(i));
	    }
	}
	if (!fpParamData.containsKey(key)) {
	    ParamCacheData.fpParamData.put(key, value);
	    keyList.add(key);
	    size++;
	}
	log.debug("==============>ParamCacheData size: " + size);
    }

    @SuppressWarnings("unchecked")
    public static Object getParam(String key) {
	log.debug("==============>ParamCacheData size: " + size);
	Object o = fpParamData.get(key);
	if (null != o) {
	    Map<String, Object> rmMap = new HashMap<>();
	    copyMap((Map<String, Object>) fpParamData.get(key), rmMap);
	    return rmMap;
	}
	return null;
    }

    @SuppressWarnings({ "unchecked" })
    public static void copyMap(Map<String, Object> source, Map<String, Object> target) {
	if (null == source) {
	    return;
	}
	Set<String> keySet = source.keySet();
	Object value;
	Map<String, Object> tmp;
	for (String key : keySet) {
	    value = source.get(key);
	    if (value instanceof Map) {
		Map<String, Object> _tmap = (Map<String, Object>) value;
		tmp = new HashMap<>();
		copyMap(_tmap, tmp);
		target.put(key, tmp);
	    } else if (value instanceof List) {
		List<Map<String, Object>> lmp = (List<Map<String, Object>>) value;
		List<Map<String, Object>> t_lmp = new ArrayList<>();
		for (Map<String, Object> map : lmp) {
		    tmp = new HashMap<>();
		    copyMap(map, tmp);
		    t_lmp.add(tmp);
		}
		target.put(key, t_lmp);
	    } else {
		target.put(key, source.get(key));
	    }
	}
    }

}
