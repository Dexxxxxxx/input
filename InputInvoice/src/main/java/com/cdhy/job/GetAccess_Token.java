package com.cdhy.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cdhy.dao.security.CommInfoMapper;
import com.cdhy.util.EInvoiceUtil;

/**
 * 定时任务，获取ACCESS_TOKEN
 * 
 * @Copyright (C),沪友科技
 * @author pjf
 * @Date:2016年2月16日
 */
public class GetAccess_Token {
    private static final Logger log = Logger.getLogger(GetAccess_Token.class);
    @Autowired
    private CommInfoMapper mapper;
    /**
     * 批量获取更新
     */
    public void getAccess_token() {
	// 获取数据中所有发票的接口信息
	Map<String, Object> get_map = new HashMap<String,Object>();
	get_map.put("status", "1");
	List<Map<String, Object>> list = mapper.list(get_map);
	log.debug("############【获取ACCESS_TOKEN开始】##################");
	for (Map<String, Object> mm : list) {
	    Map<String, Object> map=new HashMap<String,Object>();
	    map.put("OPENID", mm.get("OPENID"));
	    map.put("APP_SECRET", mm.get("APP_SECRET"));
	    String access_token = EInvoiceUtil.getAccess_token(map);
	    map.put("access_token", access_token);
	    map.put("nsrsbh", mm.get("NSRSBH"));
	    mapper.update(map);
	}
	log.debug("############【获取ACCESS_TOKEN结束】##################");
	log.debug("############【ACCESS_TOKEN更新数据库结束】##################");
    }
    /**
     * 单个获取更新
     * @param parm
     */
    public void getAccess_tokenByOne(Map<String, Object> parm){
	log.debug("############【获取ACCESS_TOKEN开始】##################");
	Map<String, Object> mm=mapper.getInfo(parm);
	Map<String, Object> map = new HashMap<String, Object>();
	    map.put("openid", mm.get("OPENID"));
	    map.put("app_secret", mm.get("APP_SECRET"));
	    String access_token = EInvoiceUtil.getAccess_token(map);
	    map.put("access_token", access_token);
	    mapper.update(map);
    }

}
