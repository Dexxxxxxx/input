package com.cdhy.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cdhy.comms.Access_Token;
import com.cdhy.comms.FPCacheData;
import com.cdhy.entity.Result;
import com.cdhy.exception.BizException;
import com.cdhy.util.HttpClientUtils;
import com.cdhy.util.HttpsClientUtils;
import com.cdhy.util.JSONUtil;
import com.cdhy.util.MapUtil;
import com.cdhy.util.PropertiesUtil;
import com.cdhy.util.WeChatUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WeChatUtils {

    private static final Logger log = Logger.getLogger(WeChatUtils.class);

    /**
     * 获取access_token
     * 
     * access_token是公众号的全局唯一接口调用凭据，公众号调用各接口时都需使用access_token。开发者需要进行妥善保存。
     * access_token的存储至少要保留512个字符空间。access_token的有效期目前为2个小时，需定时刷新，
     * 重复获取将导致上次获取的access_token失效。
     * 
     * @author pjf
     * @return
     */
    public static String getAccess_token() {
    log.debug("【获取access_token】开始");
    HttpsClientUtils httpsClientUtils = new HttpsClientUtils();
    String url = PropertiesUtil.getInstance().getProperites("getAccess_token_url");
    String charset = "utf-8";
    String appid = PropertiesUtil.getInstance().getProperites("AppID");
    String secret = PropertiesUtil.getInstance().getProperites("AppSecret");

    Map<String, String> createMap = new HashMap<String, String>();
    createMap.put("grant_type", "client_credential");
    createMap.put("appid", appid);
    createMap.put("secret", secret);

    // access_token 获取到的凭证
    // expires_in 凭证有效时间，单位：秒
    String httpOrgCreateTestRtn = httpsClientUtils.doPostMap(url, createMap, charset);
    log.debug("【获取access_token】返回结果：" + httpOrgCreateTestRtn);
    JSONObject jsonObject = JSONObject.fromObject(httpOrgCreateTestRtn);
    String access_token = jsonObject.getString("access_token");

    log.debug("【获取access_token】结束");
    return access_token;
    }

    /**
     * 获取IP列表
     * 
     * @author pjf
     * @return
     */

    public static List<String> getIPList() {
    log.debug("【获取IP列表】开始");
    HttpsClientUtils httpsClientUtils = new HttpsClientUtils();
    String url = PropertiesUtil.getInstance().getProperites("getIpList_url");
    String charset = "utf-8";

    List<String> list = new ArrayList<>();

    // String access_token =
    // PropertiesUtil.getInstance().getProperites("access_token");
    String access_token = Access_Token.getInstance().getAccess_token();

    Map<String, String> createMap = new HashMap<String, String>();
    createMap.put("access_token", access_token);

    String httpOrgCreateTestRtn = httpsClientUtils.doPostMap(url, createMap, charset);
    System.out.println(httpOrgCreateTestRtn);
    JSONObject jsonObject = JSONObject.fromObject(httpOrgCreateTestRtn);
    Object ip_list = jsonObject.get("ip_list");
    if (null == ip_list) {
	log.debug("【获取IP列表】返回结果：" + httpOrgCreateTestRtn);
    } else {
	JSONArray jArray = (JSONArray) ip_list;
	for (Object ip : jArray) {
	    list.add((String) ip);
	}
    }
    log.debug("【获取IP列表】结束");
    return list;
    }

    /**
     * 创建菜单
     * 
     * @author pjf
     * @return
     */

    public static boolean createMenu() {
    log.debug("【创建自定义菜单】开始");
    HttpsClientUtils httpsClientUtils = new HttpsClientUtils();
    // String access_token =
    // PropertiesUtil.getInstance().getProperites("access_token");
    String access_token = Access_Token.getInstance().getAccess_token();
    String url = PropertiesUtil.getInstance().getProperites("createMenu_url") + "?access_token=" + access_token;
    String charset = "utf-8";

    Map<String, String> createMap = new HashMap<String, String>();
    createMap.put("access_token", access_token);

    JSONObject menu = new JSONObject();
    JSONArray menus = new JSONArray();

    // 开票名片
    // menu_fp.put("name", "开票");
    String wx_QRCom_url = PropertiesUtil.getInstance().getProperites("authIn_url") + "?invoke=qRCard&v="
	    + System.currentTimeMillis();
    // 手动查验
    JSONObject qr_menu = new JSONObject();
    qr_menu.put("type", "view");
    qr_menu.put("name", "企业名片");
    qr_menu.put("url", wx_QRCom_url);

    /*
     * JSONArray fp_menus = new JSONArray(); // 扫码开票 JSONObject btn_smkp = new
     * JSONObject(); btn_smkp.put("type", "scancode_push"); btn_smkp.put("name",
     * "扫码开票"); btn_smkp.put("key", "smkp"); fp_menus.add(btn_smkp);
     */

    /*
     * // 秒开 JSONObject btn_kk = new JSONObject(); btn_kk.put("type",
     * "scancode_push"); btn_kk.put("name", "秒开发票"); btn_kk.put("key", "smkp");
     * //fp_menus.add(btn_kk);
     */
    menus.add(qr_menu);

    // 查验
    JSONObject menu_bx = new JSONObject();
    menu_bx.put("name", "发票管理");

    JSONArray bx_menus = new JSONArray();
    // 我的发票
    String wx_myfplist_url = PropertiesUtil.getInstance().getProperites("authIn_url") + "?invoke=wdfp&v="
	    + System.currentTimeMillis();
    // 我的发票
    JSONObject btn_card = new JSONObject();
    btn_card.put("type", "view");
    btn_card.put("name", "我的发票");
    btn_card.put("url", wx_myfplist_url);
    bx_menus.add(btn_card);

    // 扫码查验
    JSONObject btn_scan = new JSONObject();
    btn_scan.put("type", "scancode_waitmsg");
    btn_scan.put("name", "扫码查验");
    btn_scan.put("key", "smcy");
    bx_menus.add(btn_scan);

    String wx_sdcy_url = PropertiesUtil.getInstance().getProperites("authIn_url") + "?invoke=sdcy&v="
	    + System.currentTimeMillis();
    // 手动查验
    JSONObject btn_mybx = new JSONObject();
    btn_mybx.put("type", "view");
    btn_mybx.put("name", "手动查验");
    btn_mybx.put("url", wx_sdcy_url);
    bx_menus.add(btn_mybx);

    String wx_fpgf_url = PropertiesUtil.getInstance().getProperites("wx_fpgf_url") + "?v=" + System.currentTimeMillis();
    // 发票规范
    JSONObject btn_fpgf = new JSONObject();
    btn_fpgf.put("type", "view");
    btn_fpgf.put("name", "发票规范");
    btn_fpgf.put("url", wx_fpgf_url);
    bx_menus.add(btn_fpgf);

    menu_bx.put("sub_button", bx_menus);
    menus.add(menu_bx);

    // 我的信息
    JSONObject menu_me = new JSONObject();
    menu_me.put("name", "我的信息");
    JSONArray me_menus = new JSONArray();

    /*
     * String wx_mycom_url =
     * PropertiesUtil.getInstance().getProperites("authIn_url") +
     * "?invoke=myCard&v=" + System.currentTimeMillis(); // 我的公司 JSONObject
     * btn_info = new JSONObject(); btn_info.put("type", "view");
     * btn_info.put("name", "我的名片"); btn_info.put("url", wx_mycom_url);
     * me_menus.add(btn_info);
     */

    /*
     * String wx_QRCom_url =
     * PropertiesUtil.getInstance().getProperites("authIn_url") +
     * "?invoke=qRCom&v=" + System.currentTimeMillis(); // 企业名片 JSONObject
     * btn_qrcom = new JSONObject(); btn_qrcom.put("type", "view");
     * btn_qrcom.put("name", "企业名片"); btn_qrcom.put("url", wx_QRCom_url);
     * me_menus.add(btn_qrcom);
     */

    /*
     * String wx_cjgs_url =
     * PropertiesUtil.getInstance().getProperites("authIn_url") +
     * "?invoke=createCard&v=" + System.currentTimeMillis(); // 创建公司 JSONObject
     * btn_cjgs = new JSONObject(); btn_cjgs.put("type", "view");
     * btn_cjgs.put("name", "创建名片"); btn_cjgs.put("url", wx_cjgs_url);
     * me_menus.add(btn_cjgs);
     */

    String wx_binduser_url = PropertiesUtil.getInstance().getProperites("authIn_url") + "?invoke=zhbd&v="
	    + System.currentTimeMillis();
    // 绑定账号
    JSONObject btn_regis = new JSONObject();
    btn_regis.put("type", "view");
    btn_regis.put("name", "绑定账号");
    btn_regis.put("url", wx_binduser_url);
    me_menus.add(btn_regis);

    // APP下载
    String app_down_url = PropertiesUtil.getInstance().getProperites("app_down_url") + "?v="
	    + System.currentTimeMillis();
    // 绑定账号
    JSONObject btn_app = new JSONObject();
    btn_app.put("type", "view");
    btn_app.put("name", "APP下载");
    btn_app.put("url", app_down_url);
    me_menus.add(btn_app);

    menu_me.put("sub_button", me_menus);
    menus.add(menu_me);

    menu.put("button", menus);

    log.debug("自定义菜单字符串：" + menu.toString());
    String httpOrgCreateTestRtn = httpsClientUtils.doPostJson(url, menu, charset);
    log.debug("【创建自定义菜单】返回结果：" + httpOrgCreateTestRtn);

    JSONObject js_result = JSONObject.fromObject(httpOrgCreateTestRtn);
    String errcode = js_result.getString("errcode");
    String errmsg = js_result.getString("errmsg");
    if ("0".equals(errcode) && "ok".equals(errmsg)) {
	log.debug("【创建自定义菜单】成功");
	return true;
    }
    log.debug("【创建自定义菜单】失败:" + errmsg);
    return false;
    }

    /**
     * 查询菜单
     * 
     * @author pjf
     * @return
     */

    public static String getMenuList() {
    log.debug("【查询菜单】");
    HttpsClientUtils httpsClientUtils = new HttpsClientUtils();
    String url = PropertiesUtil.getInstance().getProperites("searchMenu_url");
    String charset = "utf-8";
    // String access_token =
    // PropertiesUtil.getInstance().getProperites("access_token");
    String access_token = Access_Token.getInstance().getAccess_token();

    Map<String, String> createMap = new HashMap<String, String>();
    createMap.put("access_token", access_token);

    String httpOrgCreateTestRtn = httpsClientUtils.doPostMap(url, createMap, charset);
    log.debug("【查询菜单】返回结果：" + httpOrgCreateTestRtn);
    return httpOrgCreateTestRtn;

    }

    /**
     * 获取web_access_token
     * 
     * @author pjf
     * @return
     */

    public static JSONObject getWebAccessToken(String code) {
    HttpsClientUtils httpsClientUtils = new HttpsClientUtils();
    String url = PropertiesUtil.getInstance().getProperites("get_web_access_token_url");
    Map<String, String> map = new HashMap<>();
    map.put("appid", PropertiesUtil.getInstance().getProperites("AppID"));
    map.put("secret", PropertiesUtil.getInstance().getProperites("AppSecret"));
    map.put("code", code);
    map.put("grant_type", "authorization_code");
    String doPostMap = httpsClientUtils.doPostMap(url, map, "utf-8");
    log.debug("获取web_access_token返回结果：" + doPostMap);

    JSONObject result = JSONObject.fromObject(doPostMap);
    return result;

    }

    /**
     * 删除菜单
     * 
     * @author pjf
     * @return
     */

    public static String deleteMenuList() {

    log.debug("【删除菜单】开始：");
    HttpsClientUtils httpsClientUtils = new HttpsClientUtils();
    String url = PropertiesUtil.getInstance().getProperites("deleteMenu_url");
    String charset = "utf-8";
    // String access_token =
    // PropertiesUtil.getInstance().getProperites("access_token");
    String access_token = Access_Token.getInstance().getAccess_token();

    Map<String, String> createMap = new HashMap<String, String>();
    createMap.put("access_token", access_token);

    String httpOrgCreateTestRtn = httpsClientUtils.doPostMap(url, createMap, charset);
    log.debug("【删除菜单】返回结果：" + httpOrgCreateTestRtn);

    return httpOrgCreateTestRtn;

    }

    /**
     * 获取用户详细信息
     * 
     * @author pjf
     * @return
     */

    public static String getUserInfo(String openid) {
    // getUserInfo_url
    log.debug("开始【获取用户详细信息】");
    HttpsClientUtils httpsClientUtils = new HttpsClientUtils();
    String url = PropertiesUtil.getInstance().getProperites("getUserInfo_url");
    String charset = "utf-8";
    // String access_token =
    // PropertiesUtil.getInstance().getProperites("access_token");
    String access_token = Access_Token.getInstance().getAccess_token();

    Map<String, String> createMap = new HashMap<String, String>();
    createMap.put("access_token", access_token);
    createMap.put("openid", openid);
    createMap.put("lang", "zh_CN");

    String httpOrgCreateTestRtn = httpsClientUtils.doPostMap(url, createMap, charset);
    log.debug("【获取用户详细信息】结果：" + httpOrgCreateTestRtn);

    return httpOrgCreateTestRtn;

    }

    /**
     * 解析扫描查验 结果
     * 
     * @author pjf
     * @param str
     * @return
     */

    public static Map<String, Object> getSMInfo(String str) {

    Map<String, Object> map = new HashMap<>();
    String[] split = str.split(",");
    if (split != null && split.length >= 7) {
	map.put("fpdm", split[2]);
	map.put("fphm", split[3]);
	map.put("jshj", split[4]);
	map.put("kprq", split[5]);
	map.put("jym", split[6]);
    }
    return map;

    }

    /**
     * 调用接口获取发票信息
     * 
     * @author pjf
     * @param {fphm,fpdm,jym}
     * @return
     */

    public static JSONArray getFPFromDB(Map<String, Object> param) {
    Map<String, Object> tparam = new HashMap<>();
    tparam.put("fpdm", MapUtil.getString(param, "fpdm"));
    tparam.put("fphm", MapUtil.getString(param, "fphm"));
    JSONArray jsonArray = new JSONArray();
    String url = PropertiesUtil.getInstance().getProperites("fpcy_Url");
    JSONObject js_result = HttpClientUtils.postMethodByMap(tparam, url);
    String resultType = JSONUtil.getString(js_result, "resultType");

    if (resultType.equals(Result.RESULT_SUCCESS)) {
	// 查询成功
	System.out.println("查询成功");
	jsonArray = js_result.getJSONArray("data");
	FPCacheData.putFp(jsonArray);
    } else {
	log.debug("查询失败:" + JSONUtil.getString(js_result, "msg"));
	throw new BizException(JSONUtil.getString(js_result, "msg"));
    }

    return jsonArray;

    }

    /**
     * 设置所属行业
     * 
     * @author pjf
     * @return
     */
    public static void setIndustry() {
    String url = PropertiesUtil.getInstance().getProperites("setIndustry_url") + "?access_token="
	    + Access_Token.getInstance().getAccess_token();
    JSONObject json = new JSONObject();
    json.put("industry_id1", "1");
    json.put("industry_id2", "41");
    JSONObject js_result = HttpClientUtils.postMethodByJson(json, url);

    log.debug("返回信息：" + js_result.toString());
    System.out.println(js_result);

    }

    /**
     * 获取所属行业
     * 
     * @author pjf
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void getIndustry() {
    String url = PropertiesUtil.getInstance().getProperites("getIndustry_url") + "?access_token="
	    + Access_Token.getInstance().getAccess_token();
    Map map = new HashMap<>();
    JSONObject postMethodByMap = HttpClientUtils.postMethodByMap(map, url);
    log.debug("返回信息：" + postMethodByMap.toString());
    System.out.println(postMethodByMap);

    }

    /**
     * 获得模板ID
     * 
     * @author pjf
     * @return
     */
    public static void getTemplateID() {
    String url = PropertiesUtil.getInstance().getProperites("getTemplateID_url") + "?access_token="
	    + Access_Token.getInstance().getAccess_token();
    JSONObject json = new JSONObject();
    json.put("template_id_short", "TM00015");
    JSONObject postMethodByMap = HttpClientUtils.postMethodByJson(json, url);
    log.debug("返回信息：" + postMethodByMap.toString());
    System.out.println(postMethodByMap);

    }

    /**
     * 获取模板列表
     * 
     * @author pjf
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void getTemplateList() {
    String url = PropertiesUtil.getInstance().getProperites("getTemplateList_url") + "?access_token="
	    + Access_Token.getInstance().getAccess_token();
    Map map = new HashMap<>();
    JSONObject postMethodByMap = HttpClientUtils.postMethodByMap(map, url);
    log.debug("返回信息：" + postMethodByMap.toString());
    System.out.println(postMethodByMap);

    }

    /**
     * 删除模版
     * 
     * @author pjf
     * @return
     */
    public static void delTemplate(JSONObject json) {
    String url = PropertiesUtil.getInstance().getProperites("delTemplate_url") + "?access_token="
	    + Access_Token.getInstance().getAccess_token();
    JSONObject postMethodByMap = HttpClientUtils.postMethodByJson(json, url);
    log.debug("返回信息：" + postMethodByMap.toString());
    System.out.println(postMethodByMap);

    }

    /**
     * 发送模板消息
     * 
     * @author pjf
     * @return
     */
    public static JSONObject sendTemplateMsg(JSONObject json) {
    String url = PropertiesUtil.getInstance().getProperites("sendTemplateMsg_url") + "?access_token="
	    + Access_Token.getInstance().getAccess_token();
    JSONObject postMethodByJson = HttpClientUtils.postMethodByJson(json, url);
    log.debug("发送模版消息返回结果：" + postMethodByJson.toString());
    String errcode = JSONUtil.getString(postMethodByJson, "errcode");
    if ("40001".equals(errcode) || "42001".equals(errcode) || "40014".equals(errcode)) {// access_token
											// 超时或不合法
	Access_Token.getInstance().refreshAccess_token();
	return sendTemplateMsg(json);
    }
    return postMethodByJson;
    }

    /**
     * 获取jsapi_ticket
     * 
     * @author pjf
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String getJsapi_ticket() {
    String url = PropertiesUtil.getInstance().getProperites("getJsapi_ticket_url") + "?type=jsapi&access_token="
	    + Access_Token.getInstance().getAccess_token();
    Map map = new HashMap<>();
    JSONObject json = HttpClientUtils.postMethodByMap(map, url);
    String errcodestr = JSONUtil.getString(json, "errcode");
    if ("40001".equals(errcodestr) || "42001".equals(errcodestr) || "40014".equals(errcodestr)) {// access_token
	// 超时或不合法
	Access_Token.getInstance().refreshAccess_token();
	return getJsapi_ticket();
    }
    log.debug("返回信息：" + json.toString());
    System.out.println(json);
    int errcode = JSONUtil.getInteger(json, "errcode", 0);
    String errmsg = JSONUtil.getString(json, "errmsg");

    if (0 == errcode && "ok".equals(errmsg)) {
	return JSONUtil.getString(json, "ticket");
    }
    return "";
    }

    public static void main(String[] args) {
    // WeChatUtils.getAccess_token();
    System.out.println(WeChatUtils.createMenu());
    System.out.println(WeChatUtils.getMenuList());
    // WeChatUtils.deleteMenuList();
    // WeChatUtils.setIndustry();
    // WeChatUtils.getTemplateList();
    // System.out.println(WeChatUtils.getJsapi_ticket());
    // System.out.println(JSAPIutils.getInstance().getSignature("http://15006q7z78.iask.in/wxfp/fplist.html"));
    }
}
