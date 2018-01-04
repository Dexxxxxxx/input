package com.cdhy.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import net.sf.json.JSONObject;

/**
 * 电子发票接口远程调用 数据参数拼接
 * @author Administrator
 *
 */
public class EInvoiceDataHandle {
    //开蓝票
    @SuppressWarnings("unchecked")
    public static Map<String, Object> newBlueInvoiceBuild(Map<String, Object> map){
	JSONObject jsonobject = new JSONObject();
	jsonobject.put("access_token", MapUtil.getString(map, "access_token"));
	JSONObject jsonobject_data = new JSONObject();
	jsonobject_data.put("data_resources", "API");
	jsonobject_data.put("nsrsbh", MapUtil.getString(map, "XHDWSBH"));
	jsonobject_data.put("order_num", MapUtil.getString(map, "ID"));
	jsonobject_data.put("bmb_bbh", MapUtil.getString(map, ""));
	jsonobject_data.put("zsfs", "0");
	jsonobject_data.put("tspz", MapUtil.getString(map, "00"));
	jsonobject_data.put("xsf_nsrsbh", MapUtil.getString(map, "XHDWSBH"));
	jsonobject_data.put("xsf_mc", MapUtil.getString(map, "XHDWMC"));
	jsonobject_data.put("xsf_dzdh", MapUtil.getString(map, "XHDWDZDH"));
	jsonobject_data.put("xsf_yhzh", MapUtil.getString(map, "XHDWYHZH"));
	jsonobject_data.put("gmf_nsrsbh", MapUtil.getString(map, "GHDWSBH"));
	jsonobject_data.put("gmf_mc", MapUtil.getString(map, "GHDWMC"));
	jsonobject_data.put("gmf_dzdh", MapUtil.getString(map, "GHDWDZDH"));
	jsonobject_data.put("gmf_yhzh", MapUtil.getString(map, "GHDWYHZH"));
	jsonobject_data.put("kpr", MapUtil.getString(map, "KPR"));
	jsonobject_data.put("skr", MapUtil.getString(map, "SKR"));
	jsonobject_data.put("fhr", MapUtil.getString(map, "FHR"));
	jsonobject_data.put("yfp_dm", MapUtil.getString(map, "YFPDM"));
	jsonobject_data.put("yfp_hm", MapUtil.getString(map, "YFPHM"));
	jsonobject_data.put("jshj", MapUtil.getString(map, "JSHJ"));
	jsonobject_data.put("hjje", MapUtil.getString(map, "HJJE"));
	jsonobject_data.put("hjse", MapUtil.getString(map, "HJSE"));
	jsonobject_data.put("kce", MapUtil.getString(map, "KCE"));
	jsonobject_data.put("bz", MapUtil.getString(map, "BZ"));
	
	List<Map<String, Object>> list=(List<Map<String, Object>>) map.get("data");
	List<JSONObject> items=new ArrayList<JSONObject>();
	for(Map<String, Object> mm:list){
	    JSONObject json=new JSONObject();
	    json.put("fphxz", MapUtil.getString(mm, "FPHXZ"));
	    json.put("spbm", MapUtil.getString(mm, "SPBM"));
	    json.put("zxbm", MapUtil.getString(mm, "ZXBM"));
	    json.put("yhzcbs", MapUtil.getString(mm, "YHZCBS"));
	    json.put("lslbs", MapUtil.getString(mm, "LSLBS"));
	    json.put("zzstsgl", MapUtil.getString(mm, "LSLBS"));
	    json.put("xmmc", MapUtil.getString(mm, "SPMC"));
	    json.put("ggxh", MapUtil.getString(mm, "GGXH"));
	    json.put("dw", MapUtil.getString(mm, "DW"));
	    json.put("xmsl", MapUtil.getString(mm, "SPSL"));
	    json.put("xmdj", MapUtil.getDouble(mm, "DJ")+"");
	    json.put("xmje", MapUtil.getDouble(mm, "JSHJ")+"");
	    json.put("sl", MapUtil.getString(mm, "SL"));
	    json.put("se", MapUtil.getString(mm, "SE"));
	    items.add(json);
	}
	jsonobject_data.put("common_fpkj_xmxx", items);
	jsonobject.put("data", jsonobject_data);
	Map<String, Object> json_return = EInvoiceUtil.newBlueInvoice(jsonobject);
	return json_return;
    }
    @SuppressWarnings("unchecked")
    public static Map<String, Object> newRedInvoiceBuild(Map<String, Object> parm){
	JSONObject jsonobject = new JSONObject();
	jsonobject.put("access_token", MapUtil.getString(parm, "access_token"));
	JSONObject jsonobject_data = new JSONObject();
	jsonobject_data.put("data_resources", "API");
	jsonobject_data.put("nsrsbh", MapUtil.getString(parm, "nsrsbh"));
	jsonobject_data.put("order_num", MapUtil.getString(parm, "order_num"));
	jsonobject_data.put("yfp_dm", MapUtil.getString(parm, "fpdm"));
	jsonobject_data.put("yfp_hm", MapUtil.getString(parm, "fphm"));
	jsonobject_data.put("bz", MapUtil.getString(parm, "bz"));
	jsonobject.put("data", jsonobject_data);
	Map<String, Object> json_return = EInvoiceUtil.newRedInvoice(jsonobject);
	return json_return;
    }
    
    public static void main(String[] args) {
    }
}
