package com.cdhy.comms;

import com.cdhy.util.WeChatUtils;

public class Access_Token {
    private String ACCSEE_TOKEN = "";

    public String getAccess_token() {
	if ("".equals(ACCSEE_TOKEN)) {
	    ACCSEE_TOKEN = WeChatUtils.getAccess_token();
	}
	return ACCSEE_TOKEN;
    }

    public void setAccess_token(String access_token) {
	ACCSEE_TOKEN = access_token;
    }
    public void refreshAccess_token() {
	ACCSEE_TOKEN = WeChatUtils.getAccess_token();
    }

    private static Access_Token _this;

    public static Access_Token getInstance() {
	if (null == _this) {
	    _this = new Access_Token();
	}
	return _this;
    }

}
