package com.cdhy.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.cdhy.dao.security.CommInfoMapper;
import com.cdhy.util.EInvoiceUtil;
public class InitAccess_Token implements ApplicationListener<ContextRefreshedEvent>{
    private static final Logger log = Logger.getLogger(InitAccess_Token.class);
    @Autowired
    private CommInfoMapper mapper;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
	//获取数据中所有发票的接口信息
	Map<String, Object> get_map = new HashMap<String,Object>();
	get_map.put("status", "1");
	List<Map<String, Object>> list = mapper.list(get_map);
	log.debug("############【获取ACCESS_TOKEN开始】##################");
	for(Map<String, Object> mm:list){
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

}
