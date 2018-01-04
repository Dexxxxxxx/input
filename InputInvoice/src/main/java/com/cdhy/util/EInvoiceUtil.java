package com.cdhy.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.cdhy.exception.BizException;

import net.sf.json.JSONObject;

public class EInvoiceUtil {
    private static final Logger log = Logger.getLogger(EInvoiceUtil.class);
    //公共远程http及返回
    public static String token_http(JSONObject createMap){
	HttpsClientUtils httpsClientUtils = new HttpsClientUtils();
	String url = PropertiesUtil.getInstance().getProperites("getAccess_token_url");
	String charset = "utf-8";
	String httpOrgCreateTestRtn = httpsClientUtils.doPostJson(url, createMap, charset);
	return httpOrgCreateTestRtn;
    }
    //获取远程http及返回
    public static String comm_http(JSONObject createMap){
	HttpsClientUtils httpsClientUtils = new HttpsClientUtils();
	String url = PropertiesUtil.getInstance().getProperites("buss_url");
	String charset = "utf-8";
	String httpOrgCreateTestRtn = httpsClientUtils.doPostJson(url, createMap, charset);
	return httpOrgCreateTestRtn;
    }
    
    //获取access_token
    public static String getAccess_token(Map<String, Object> parm) {
	    log.debug("【获取access_token】开始");
	    String appid = MapUtil.getString(parm, "OPENID");
	    String app_secret = MapUtil.getString(parm, "APP_SECRET");
	    JSONObject createMap = new JSONObject();
	    createMap.put("openid", appid);
	    createMap.put("app_secret", app_secret);
	    String httpOrgCreateTestRtn = token_http(createMap);
	    log.debug("【获取access_token】返回结果：" + httpOrgCreateTestRtn);
	    JSONObject jsonObject = JSONObject.fromObject(httpOrgCreateTestRtn);
	    String result=jsonObject.getString("result");
	    String access_token="";
	    if(result.equals("SUCCESS")){
		String rows_object=jsonObject.getString("rows");
		JSONObject rows=JSONObject.fromObject(rows_object);
		access_token = rows.getString("access_token");
		log.debug("【获取access_token】结束");
	    }else{
		log.debug("【获取access_token】失败："+jsonObject.getString("msg"));
		throw new BizException(jsonObject.getString("msg"));
	    }
	    
	    return access_token;
    }
    //电子发票库存查询
    public static int queryStock(Map<String, Object> parm){
	log.debug("【获取发票库存】开始");
	JSONObject createMap = new JSONObject();
	createMap.put("access_token", parm.get("access_token"));
	createMap.put("serviceKey", "ebi_InvoiceHandle_queryStock");
	createMap.put("nsrsbh", parm.get("nsrsbh"));
	String httpOrgCreateTestRtn = comm_http(createMap);
	log.debug("【获取发票库存】返回结果：" + httpOrgCreateTestRtn);
	JSONObject jsonObject = JSONObject.fromObject(httpOrgCreateTestRtn);
	String result=jsonObject.getString("result");
	int stock=0;
	if(result.equals("SUCCESS")){
	    String rows_object=jsonObject.getString("rows");
	    JSONObject rows=JSONObject.fromObject(rows_object);
	    stock = rows.getInt("SYFPFS");
	    log.debug("【获取发票库存】结束");
	}else{
	    log.debug("【获取发票库存】失败："+jsonObject.getString("msg"));
	    throw new BizException(jsonObject.getString("msg"));
	}
	return stock;
    }
    //查询单个发票
    public static JSONObject queryInvoice(Map<String, Object> parm){
	log.debug("【查询单个发票】开始");
	JSONObject createMap = new JSONObject();
	createMap.put("access_token", parm.get("access_token"));
	createMap.put("serviceKey", "ebi_InvoiceHandle_queryInvoice");
	createMap.put("data", JSONObject.fromObject(parm.get("data")));
	String httpOrgCreateTestRtn = comm_http(createMap);
	log.debug("【查询单个发票】返回结果：" + httpOrgCreateTestRtn);
	JSONObject jsonObject = JSONObject.fromObject(httpOrgCreateTestRtn);
	String result=jsonObject.getString("result");
	JSONObject json=new JSONObject();
	if(result.equals("SUCCESS")){
	    String rows_object=jsonObject.getString("rows");
	    JSONObject rows=JSONObject.fromObject(rows_object);
	    json.put("data", rows);
	    log.debug("【查询单个发票】结束");
	}else{
	    log.debug("【查询单个发票】失败："+jsonObject.getString("msg"));
	    throw new BizException(jsonObject.getString("msg"));
	}
	return json;
    }
    //查询单张发票（简易查询 不含具体商品信息）
    public static JSONObject queryInvoice4DMDH(Map<String, Object> parm){
	log.debug("【简易查询单个发票】开始");
	JSONObject createMap = new JSONObject();
	createMap.put("access_token", parm.get("access_token"));
	createMap.put("serviceKey", "ebi_InvoiceHandle_queryInvoice4DMDH");
	createMap.put("data", JSONObject.fromObject(parm.get("data")));
	String httpOrgCreateTestRtn = comm_http(createMap);
	log.debug("【简易查询单个发票】返回结果：" + httpOrgCreateTestRtn);
	JSONObject jsonObject = JSONObject.fromObject(httpOrgCreateTestRtn);
	String result=jsonObject.getString("result");
	JSONObject json=new JSONObject();
	if(result.equals("SUCCESS")){
	    String rows_object=jsonObject.getString("rows");
	    JSONObject rows=JSONObject.fromObject(rows_object);
	    json.put("data", rows);
	    log.debug("【简易查询单个发票】结束");
	}else{
	    log.debug("【简易查询单个发票】失败："+jsonObject.getString("msg"));
	    throw new BizException(jsonObject.getString("msg"));
	}
	return json;
    }
    //开具蓝字发票
    @SuppressWarnings("unchecked")
    public static JSONObject newBlueInvoice(JSONObject _json){
	log.debug("【开具蓝字发票】开始");
	JSONObject createMap = new JSONObject();
	createMap.put("access_token", _json.get("access_token"));
	createMap.put("serviceKey", "ebi_InvoiceHandle_newBlueInvoice");
	createMap.put("data", JSONObject.fromObject(_json.get("data")));
	String httpOrgCreateTestRtn = comm_http(createMap);
	log.debug("【开具蓝字发票】返回结果：" + httpOrgCreateTestRtn);
	JSONObject jsonObject = JSONObject.fromObject(httpOrgCreateTestRtn);
	String result=jsonObject.getString("result");
	JSONObject json=new JSONObject();
	if(result.equals("SUCCESS")){
	   List<JSONObject> rows=(List<JSONObject>) jsonObject.get("rows");
	    json.put("data", rows);
	    log.debug("【开具蓝字发票】结束");
	}else{
	    log.debug("【开具蓝字发票】失败："+jsonObject.getString("msg"));
	    throw new BizException(jsonObject.getString("msg"));
	}
	return json;
    }
    //开具红字发票
    public static JSONObject newRedInvoice(JSONObject _json){
	log.debug("【开具红字发票】开始");
	JSONObject createMap = new JSONObject();
	createMap.put("access_token", _json.get("access_token"));
	createMap.put("serviceKey", "ebi_InvoiceHandle_newRedInvoice");
	createMap.put("data", JSONObject.fromObject(_json.get("data")));
	String httpOrgCreateTestRtn = comm_http(createMap);
	log.debug("【开具红字发票】返回结果：" + httpOrgCreateTestRtn);
	JSONObject jsonObject = JSONObject.fromObject(httpOrgCreateTestRtn);
	String result=jsonObject.getString("result");
	JSONObject json=new JSONObject();
	if(result.equals("SUCCESS")){
	    String rows_object=jsonObject.getString("rows");
	    JSONArray rows=JSONArray.parseArray(rows_object);
	    json.put("data", rows);
	    log.debug("【开具红字发票】结束");
	}else{
	    log.debug("【开具红字发票】失败："+jsonObject.getString("msg"));
	    throw new BizException(jsonObject.getString("msg"));
	}
	return json;
    }
    //发票交付给最终客户  获取result字符串
    public static JSONObject newInvoiceDelay(Map<String, Object> parm){
	log.debug("【获取返回的result字符串】开始");
	JSONObject createMap = new JSONObject();
	createMap.put("access_token", parm.get("access_token"));
	createMap.put("serviceKey", "ebi_InvoiceHandle_newInvoiceDelay");
	createMap.put("data", JSONObject.fromObject(parm.get("data")));
	String httpOrgCreateTestRtn = comm_http(createMap);
	log.debug("【获取返回的result字符串】返回结果：" + httpOrgCreateTestRtn);
	JSONObject jsonObject = JSONObject.fromObject(httpOrgCreateTestRtn);
	String result=jsonObject.getString("result");
	JSONObject json=new JSONObject();
	if(result.equals("SUCCESS")){
	    String rows_object=jsonObject.getString("rows");
	    JSONObject rows=JSONObject.fromObject(rows_object);
	    json.put("data", rows);
	    log.debug("【获取返回的result字符串】结束");
	}else{
	    log.debug("【获取返回的result字符串】失败："+jsonObject.getString("msg"));
	    throw new BizException(jsonObject.getString("msg"));
	}
	return json;
    }
    //获取发票或使用发票抬头开票
    public static JSONObject newOrGetBlueInvoice(Map<String, Object> parm){
	return null;
    }
    //获取电子发票的地址
    public static String getEIURL(Map<String, Object> parm){
	log.debug("【获取电子发票URL】开始");
	String URL=PropertiesUtil.getInstance().getProperites("EInvoice_url")+"?data=";
	String nsrsbh=MapUtil.getString(parm, "nsrsbh");
	String order_num=MapUtil.getString(parm, "order_num");
	String time=new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	String ck=MD5.MD5Encode(nsrsbh+order_num+time+"hydzfp.com");
	JSONObject jo=new JSONObject();
	jo.put("nsrsbh", nsrsbh);
	jo.put("order_num", order_num);
	jo.put("time", time);
	jo.put("ck", ck);
	String data=Base64Util.Base64Encode(jo.toString());
	log.debug("【获取电子发票URL】结束");
	return URL+data;
    }
    
    public static void main(String[] args) {
//	String ss=getAccess_token();
//	System.out.println(ss);
//	Map<String, Object> map=new HashMap<String,Object>();
//	map.put("nsrsbh", "510302744676556");
//	map.put("order_num", "4BC88353DB68B24CE0507B769FF988EDS");
//	String url=getEIURL(map);
//	System.out.println(url);
	String row_object="[{\"fhr\":\"孙雅雯\",\"xsf_mc\":\"自贡市商业银行股份有限公司\","
		+ "\"yfp_hm\":\"26658099\",\"bz\":\"对应正数发票代码:150003521030 号码:26658099\n\","
		+ "\"xsf_yhzh\":\"自贡市商业银行股份有限公司\",\"gmf_mc\":\"个人\",\"hjse\":\"-0.17\","
		+ "\"fp_dm\":\"150003521030\",\"kce\":\"\",\"yfp_dm\":\"150003521030\",\"fp_hm\":"
		+ "\"26658104\",\"common_fpkj_xmxx\":[{\"sl\":\"0.17\",\"dw\":\"件\",\"xmdj\":\"1\","
		+ "\"lslbs\":\"\",\"xmje\":\"-1.0\",\"xmmc\":\"一锭银\",\"zxbm\":\"\",\"se\":\"-0.17\","
		+ "\"yhzcbs\":\"\",\"zzstsgl\":\"\",\"fphxz\":\"0\",\"ggxh\":\"33.3g\",\"xmsl\":\"-1.0\","
		+ "\"spbm\":\"\"}],\"gmf_nsrsbh\":\"\",\"itype\":\"026\",\"jff_phone\":\"\",\"jym\":\"10642094045233932263\","
		+ "\"kplx\":\"1\",\"pdf_item_key\":\"pdf_detail_tN1xm2A1490753306354\",\"order_num\":"
		+ "\"4BD5E859D82EE7F3E0507B769FF99E14RED\",\"zsfs\":\"0\",\"xsf_dzdh\":\"自贡市\",\"jqbh\""
		+ ":\"499111004173\",\"hjje\":\"-1.0\",\"ext_code\":\"dxzpra3hm8\",\"tspz\":\"00\",\"gmf_dzdh\":"
		+ "\"1\",\"fpqqlsh\":\"tN1xm2A1490753306354\",\"skr\":\"蒋瑜\",\"jff_email\":\"\",\"gmf_yhzh\":\"1\","
		+ "\"kpr\":\"admin\",\"xsf_nsrsbh\":\"510302744676556\",\"fp_mw\":\"03662937148*+2384/90*1>>76-8*><458198782553+099/50/4838--5662937148*+2384/8144852/4/813+7/0*+5115174198/91>70*0+\","
		+ "\"jshj\":\"-1.17\",\"pdf_key\":\"pdf_tN1xm2A1490753306354\",\"kprq\":\"20170329100744\"}]";
	JSONArray rows=JSONArray.parseArray(row_object);
	System.out.println(rows);
    }
}
