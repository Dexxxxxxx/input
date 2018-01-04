package com.cdhy.service.tobacco.impl;

import java.util.Map;

import com.cdhy.controller.security.LoginController;
import com.cdhy.exception.BizException;
import com.cdhy.util.ContextHolderUtils;
import com.cdhy.util.MapUtil;

public class GetSuffix {
    @SuppressWarnings("unchecked")
    public static String suffixCheck(){
	Map<String, Object> loginMap = (Map<String, Object>) ContextHolderUtils.getSession()
		.getAttribute(LoginController.LOGIN_USER);
	Map<String, Object> dept = (Map<String, Object>) loginMap.get("dept");
	String pidcode=MapUtil.getString(dept, "CODE");
	if(pidcode.length()>=4){
	    pidcode=pidcode.substring(0,4);
	}
	if(pidcode.equals("")){
	    throw new BizException("");
	}
	String suffix="";
	if(pidcode.equals("6103")){
	    //宝鸡
	    suffix="_BJ";
	}else if(pidcode.equals("6104")){
	    //咸阳
	    suffix="_XY";
	}else if(pidcode.equals("6106")){
	    //延安
	    suffix="_YA";
	}else if(pidcode.equals("6107")){
	    //汉中
	    suffix="_HZ";
	}else if(pidcode.equals("6124")){
	    //安康
	    suffix="_AK";
	}else if(pidcode.equals("6125")){
	    //商洛
	    suffix="_SL";
	}
	return suffix;
    }
}
