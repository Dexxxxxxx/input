package com.cdhy.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cdhy.exception.BizException;

public class CookieUtil {
    //保存cookie
    public static void saveCookie(String username,String password,HttpServletResponse response){
	String ecode="";
	try {
	    ecode=URLEncoder.encode(username+","+password, "utf-8");
	} catch (Exception e) {
	    throw new BizException(e.getMessage());
	}
	Cookie cookie = new Cookie("user",ecode);
	cookie.setPath("/");
	cookie.setMaxAge(60 * 60 * 24);
	response.addCookie(cookie);
    }
    //读出cookie
    public static String readCookie(HttpServletRequest request){
	Cookie cookie[]=request.getCookies();
	String user="";
	if(cookie!=null){
	    for(int i=0;i<cookie.length;i++){
		if(cookie[i].getName().equals("user")){
		    try {
			user=URLDecoder.decode(cookie[i].getValue(),"utf-8");
		    } catch (Exception e) {
			throw new BizException(e.getMessage());
		    }
		}
	    }
	}
	return user;
    }
    //清空cookie
    public static void removeCookie(HttpServletResponse response){
	Cookie cookie = new Cookie("user", null);
	cookie.setMaxAge(0);
	cookie.setPath("/");
	response.addCookie(cookie);

    }
}
