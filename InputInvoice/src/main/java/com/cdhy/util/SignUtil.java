package com.cdhy.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SignUtil {

    /**
     * 
     * 1）将token、timestamp、nonce三个参数进行字典序排序 2）将三个参数字符串拼接成一个字符串进行sha1加密
     * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     * 
     * @author pjf
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */

    @SuppressWarnings("unchecked")
    public static boolean checkSignature(String signature, String timestamp, String nonce) {

	String token = PropertiesUtil.getInstance().getProperites("token");
	// 1）将token、timestamp、nonce三个参数进行字典序排序
	List<String> list = new ArrayList<String>();
	list.add(token);
	list.add(timestamp);
	list.add(nonce);
	Collections.sort(list, new SpellComparator());
	StringBuilder sb = new StringBuilder();
	sb.append(list.get(0));
	sb.append(list.get(1));
	sb.append(list.get(2));

	// 2）将三个参数字符串拼接成一个字符串进行sha1加密
	String digest = new SHA1Utils().getDigestOfString(sb.toString());

	// 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
	return digest.equals(signature);

    }

    /**
     *  获取签名
     * @author pjf
     * @param jsapi_ticket
     * @param timestamp
     * @param nonce
     * @param url
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String getSignature(String jsapi_ticket, String timestamp, String nonce, String url) {

	String token = PropertiesUtil.getInstance().getProperites("token");
	if (url.indexOf("#") != -1) {
	    url = url.split("#")[0];
	}

	// 1）将token、timestamp、nonce,url进行字典序排序
	List<String> list = new ArrayList<String>();
	list.add(token);
	list.add(timestamp);
	list.add(nonce);
	list.add(url);
	Collections.sort(list, new SpellComparator());
	StringBuilder sb = new StringBuilder();
	sb.append(list.get(0));
	sb.append("&").append(list.get(1));
	sb.append("&").append(list.get(2));
	sb.append("&").append(list.get(3));

	// 2）对string1进行sha1签名，得到signature
	String signature = new SHA1Utils().getDigestOfString(sb.toString());
	return signature;
    }

    @SuppressWarnings("rawtypes")
    static class SpellComparator implements Comparator {

	@Override
	public int compare(Object o1, Object o2) {

	    try {
		// 取得比较对象的汉字编码，并将其转换成字符串
		String s1 = new String(o1.toString().getBytes("GB2312"), "ISO-8859-1");
		String s2 = new String(o2.toString().getBytes("GB2312"), "ISO-8859-1");
		// 运用String类的 compareTo（）方法对两对象进行比较
		return s1.compareTo(s2);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    return 0;

	}

    }

}
