package com.cdhy.comms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cdhy.util.JSONUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 发票缓存信息 date: 2016年5月12日 上午10:54:46 <br/>
 *
 * @author pjf
 * @version
 */

public class FPCacheData {
    
    private static final Logger log = Logger.getLogger(FPCacheData.class);

    private static Map<String, Object> fpdata = new HashMap<>();
    private static int maxSize = 10 * 10000;
    private static int size = 0;
    private static List<String> keyList = new ArrayList<>();

    /**
     * 存发发票数据到缓存
     * 
     * @author pjf
     * @param
     * @return
     */
    public static synchronized void putFp(JSONArray jsa) {
	if (jsa == null) {
	    return;
	}
	if (size >= maxSize) {
	    int _max = maxSize / 2;
	    for (int i = 0; i < _max / 2; i++) {
		fpdata.remove(keyList.get(i));
	    }
	}
	if (jsa != null && jsa.size() > 0) {
	    if (jsa.get(0) instanceof JSONObject) {
		JSONObject js = (JSONObject) jsa.get(0);
		String fphm = JSONUtil.getString(js, "FPHM");
		String fpdm = JSONUtil.getString(js, "FPDM");
		if (!fpdata.containsKey(fpdm + fphm)) {
		    FPCacheData.fpdata.put(fpdm + fphm, jsa);
		    keyList.add(fpdm + fphm);
		    size++;
		}
	    }
	}
	log.debug("==============>FpCacheData size: " + size);
    }

    public static Object getFp(String fpdm,String fphm) {
	log.debug("==============>FpCacheData size: " + size);
	return fpdata.get(fpdm+fphm);
    }

}
