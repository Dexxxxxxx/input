package com.cdhy.util.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cdhy.util.PropertiesUtil;

/**
 * link sms client
 */
public class LinkSMSClient {
    private static final Logger logger = LoggerFactory
	    .getLogger(LinkSMSClient.class);

    private static final String account;
    private static final String password;
    private static final String signature;
    private static final String debug;
    private static final String url;

    static {
	account = PropertiesUtil.getInstance().getProperites("link.sms.account");
	password = PropertiesUtil.getInstance().getProperites("link.sms.password");
	signature = PropertiesUtil.getInstance().getProperites("link.sms.signature");
	debug = PropertiesUtil.getInstance().getProperites("link.sms.debug");
	url = PropertiesUtil.getInstance().getProperites("link.sms.url");
    }

    public static String buildUrl(String mobiles, String content, Date sendtime)
	    throws UnsupportedEncodingException {

	String sendtimestr = StringUtils.EMPTY;
	if (sendtime != null) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	    sendtimestr = sdf.format(sendtime);
	}

	content = URLEncoder.encode(StringUtils
		.trimToEmpty(content + signature).replaceAll("<br/>", " "),
		"GBK");

	StringBuilder str = new StringBuilder(url);
	// CorpID=*&Pwd=*&Mobile=*&Content=*&Cell=*&SendTime=*
	str.append("?CorpID=").append(account);
	str.append("&Pwd=").append(password);
	str.append("&Mobile=").append(mobiles);
	str.append("&Content=").append(content);
	str.append("&Cell=");
	str.append("&SendTime=").append(sendtimestr);

	return str.toString();

    }

    public static String send(String mobiles, String content) {
	return send(mobiles, content, null);
    }

    public static String send(String mobiles, String content, Date sendtime) {
	if (!"true".equals(debug)) {
	    BufferedReader in = null;
	    int inputLine = 0;
	    try {
		URL url = new URL(buildUrl(mobiles, content, sendtime));
		in = new BufferedReader(new InputStreamReader(url.openStream()));
		inputLine = new Integer(in.readLine()).intValue();
	    } catch (Exception e) {
		logger.error("网络异常,发送短信失败！");
		System.out.println("网络异常,发送短信失败！");
		inputLine = -2;
	    }
	    logger.error("结束发送短信返回值：  " + inputLine);
	    System.out.println("结束发送短信返回值：  " + inputLine);
	    return inputLine == 1 ? "success" : "fail";
	} else {
	    return "success";
	}

    }

    public static void main(String[] args) {
	LinkSMSClient
		.send("15828675378",
			"快乐圣诞，什么是圣诞快乐？不是那快乐的阳光，也不是鸟儿的啁啾，那是愉快的念头和幸福的笑容，是温馨慈爱的问候测试。【车税通】");
    }
}
