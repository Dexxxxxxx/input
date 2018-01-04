package com.cdhy.comms;

import com.cdhy.util.PropertiesUtil;
import com.cdhy.util.SignUtil;
import com.cdhy.util.WeChatUtils;

/**
 * 获取JSAPI的ticket date: 2016年5月27日 下午3:07:12 <br/>
 *
 * @author pjf
 * @version
 */
public class JSAPIutils {
    private String ticket = "";

    public String getTicket() {
	if ("".equals(ticket)) {
	    ticket = WeChatUtils.getJsapi_ticket();
	}
	return ticket;
    }

    public void setTicket(String access_token) {
	ticket = access_token;
    }

    private static JSAPIutils _this;

    public static JSAPIutils getInstance() {
	if (_this == null) {
	    _this = new JSAPIutils();
	}
	return _this;
    }

    /**
     * 签名生成规则如下：参与签名的字段包括noncestr（随机字符串）, 有效的jsapi_ticket, timestamp（时间戳）,
     * url（当前网页的URL，不包含#及其后面部分） 。对所有待签名参数按照字段名的ASCII
     * 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1。
     * 这里需要注意的是所有参数名均为小写字符。对string1作sha1加密，字段名和字段值都采用原始值，不进行URL 转义。
     * 
     * 注意事项 
     * 1.签名用的noncestr和timestamp必须与wx.config中的nonceStr和timestamp相同。
     * 2.签名用的url必须是调用JS接口页面的完整URL。 
     * 3.出于安全考虑，开发者必须在服务器端实现签名的逻辑。
     * 
     * @author pjf
     * @return signature=signature+"@@"+noncestr+"@@"+timestamp;
     */
    public String getSignature(String url) {
	// 步骤1. 对所有待签名参数按照字段名的ASCII
	// 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1：
	String noncestr = PropertiesUtil.getInstance().getProperites("noncestr");
	String jsapi_ticket = getTicket();
	String timestamp = System.currentTimeMillis()+"";
	
	String signature = SignUtil.getSignature(jsapi_ticket, timestamp, noncestr,url);
	signature=signature+"@@"+noncestr+"@@"+timestamp;
	return signature;
    }
}
