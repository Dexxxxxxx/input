package com.cdhy.service.security;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public interface ILoginService {
	 public Map<String, Object> login(Map<String, Object> parm,HttpServletRequest request,HttpServletResponse response);

	    public void updatePassword(Map<String, Object> parm);

	    public void resetPassword(Map<String, Object> parm);
}
