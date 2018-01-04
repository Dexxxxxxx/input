package com.cdhy.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cdhy.exception.BizException;
import com.framework.util.ResourceUtil;

public class BuildInvoiceData {
    /**
     * 拆分发票
     * @param list
     * @return
     */
    public static List<Map<String, Object>> splitInvoice(List<Map<String, Object>> list){
	    double limit_count=Double.parseDouble(ResourceUtil.getLimitCount());
	    List<Map<String, Object>> splitlist=new ArrayList<>();
	    // DJ 含税单价 SL 税率   JE 金额  SE 税额   JSHJ 价税合计
	    for(Map<String, Object> mm:list){
		if(Double.parseDouble(MapUtil.getString(mm, "JSHJ"))>limit_count){
		    throw new BizException("单件商品金额超出发票最大金额");
		}
		Map<String, Object> item_map = new HashMap<String, Object>();
		item_map.putAll(mm);
		double jshj = Double.parseDouble(MapUtil.getString(mm, "JSHJ"));//价税合计
		double je = Double.parseDouble(MapUtil.getString(mm, "JE"));// 不含税金额
		double se = jshj-je;
		String str_dj = MapUtil.getString(mm, "DJ");
		int dj_l =  (str_dj+"").length()-(str_dj+"").indexOf(".")-1;
		if(dj_l>6){
		    item_map.put("DJ", String.format("%.6f", MapUtil.getDouble(mm, "DJ")));//含税单价
		}else{
		    item_map.put("DJ", MapUtil.getDouble(mm, "DJ"));//含税单价
		}
    	    	item_map.put("SPSL", MapUtil.getDouble(mm, "SPSL"));//商品数量
    	    	item_map.put("HSDJ", String.format("%.2f", MapUtil.getDouble(mm, "DJ")));//含税单价
    	    	item_map.put("JE", String.format("%.2f", je));
    	    	item_map.put("SL", String.format("%.2f", MapUtil.getDouble(mm, "SL")));
    	    	item_map.put("SE", String.format("%.2f", se));
    	    	item_map.put("JSHJ", String.format("%.2f", jshj));
    	    	splitlist.add(item_map);
	    }
	    return splitlist;
    }
    /**
     * 组建发票数据
     * @param splitlist
     * @param comm_map
     * @return
     */
    public static List<Map<String, Object>> buildInvoice(List<Map<String, Object>> splitlist,Map<String, Object> comm_map){
	    double limit_count = Double.parseDouble(ResourceUtil.getLimitCount());
	    List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		double jshj_count=0.0;
		double je_count=0.0;
		double se_count=0.0;
		Map<String, Object> mp=new HashMap<String,Object>();
		List<Map<String, Object>> sp_list= new ArrayList<Map<String,Object>>();
		for(Map<String, Object> map:splitlist){
		    double jshj=MapUtil.getDouble(map, "JSHJ");
		    double je=MapUtil.getDouble(map, "JE");
		    double se=MapUtil.getDouble(map, "SE");
		    if(jshj_count+jshj>=limit_count){
			mp.putAll(comm_map);
			mp.put("data", sp_list);
			list.add(mp);
			sp_list= new ArrayList<Map<String,Object>>();
			mp=new HashMap<String,Object>();
			jshj_count=0.0;
			je_count=0.0;
			se_count=0.0;
			
			jshj_count+=jshj;
			je_count+=je;
			se_count+=se;
			sp_list.add(map);
			mp.put("SE_COUNT", String.format("%.2f", se_count));
			mp.put("JE_COUNT", String.format("%.2f", je_count));
			mp.put("JSHJ_COUNT", String.format("%.2f", jshj_count));
		    }else{
			jshj_count+=jshj;
			je_count+=je;
			se_count+=se;
			sp_list.add(map);
			mp.put("SE_COUNT", String.format("%.2f", se_count));
			mp.put("JE_COUNT", String.format("%.2f", je_count));
			mp.put("JSHJ_COUNT", String.format("%.2f", jshj_count));
		    }
		}
		if(!mp.isEmpty()){
		    mp.putAll(comm_map);
		    mp.put("data", sp_list);
		    list.add(mp);
		}
		return list;
	}
    
}
