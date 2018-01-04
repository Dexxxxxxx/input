package com.cdhy.controller.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cdhy.entity.Result;
import com.cdhy.exception.BizException;
import com.cdhy.service.security.ILoginService;
import com.cdhy.util.ContextHolderUtils;
import com.cdhy.util.CookieUtil;
import com.cdhy.util.MapUtil;
import com.framework.util.ResourceUtil;



/**
 * 后台登录
 * 
 * @Copyright (C),沪友科技
 * @author dd
 * @Date:2015年10月26日
 */
@Controller
@Scope("prototype")
@RequestMapping("/security/login")
public class LoginController {

    // 要统一常量
    private static final Logger log = Logger.getLogger(LoginController.class);
    public static final String SESSION_KEY_OF_RAND_CODE = "randCode"; // todo
    public static final String LOGIN_USER = "login_user";
    public static final String LOGIN_DEPT="login_dept";
    public static final String SESSION_LOGIN_USER_COUNT = "SESSION_LOGIN_USER_COUNT"; // todo

    public static final int SESSION_LOGIN_USER_COUNT_CFG = Integer
	    .parseInt(ResourceUtil.getConfigByName("SESSION_LOGIN_USER_COUNT_CFG")); // todo
    public static final int SESSION_LOGIN_USER_MINUTE_CFG = Integer
	    .parseInt(ResourceUtil.getConfigByName("SESSION_LOGIN_USER_MINUTE_CFG")); // todo

    @Autowired
    private ILoginService service;

    /**
     * 登录
     * 
     * @author dd
     * @Date 2015年10月28日
     * @param parm
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public Result login(@RequestParam Map<String, Object> parm,HttpServletRequest request,HttpServletResponse response) {
	Result obj = new Result();
	try {
	    valiadataLoginCount();
//	    Object randCode = parm.get("randCode");
//	    if (null == randCode || "".equals(randCode)) {
//		obj.setResult(Result.RESULT_ERROR);
//		obj.setMsg("请输入图片验证码");
//	    } 
//	    else if (!randCode.toString().equalsIgnoreCase(String
//		    .valueOf(ContextHolderUtils.getSession().getAttribute(LoginController.SESSION_KEY_OF_RAND_CODE)))) {
//		obj.setResult(Result.RESULT_ERROR);
//		obj.setMsg("图片验证码错误");
//	    } 
//	    else {
		Map<String, Object> result = service.login(parm,request,response);
		// put到session
		ContextHolderUtils.getSession().setAttribute(LoginController.LOGIN_USER, result);
		obj.setRows(result);
		loginCount(true);
//	    }
	} catch (BizException e) {
	    log.debug("登录失败", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(e.getMessage());
	    loginCount(false);
	} catch (Exception e) {
	    log.error("登录异常", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(Result.RESULT_ERROR_MSG);
	    loginCount(false);
	}
	return obj;
    }

    @SuppressWarnings("unchecked")
    private void valiadataLoginCount() {
	Object obj = ContextHolderUtils.getSession().getAttribute(LoginController.SESSION_LOGIN_USER_COUNT);
	if (null != obj && obj instanceof Map) {
	    Map<String, Object> map = (Map<String, Object>) obj;
	    int len = MapUtil.getInt(map, "len");
	    // Date date = (Date) map.get("date");

	    if (len < SESSION_LOGIN_USER_COUNT_CFG) {
	    } else if (len >= SESSION_LOGIN_USER_COUNT_CFG) {
		Object dateObj = map.get("date");
		if (null == dateObj) {
		    map.put("date", new Date());
		    throw new BizException("连续登录失败"+SESSION_LOGIN_USER_COUNT_CFG+"次，账户锁定"+SESSION_LOGIN_USER_MINUTE_CFG+"分钟。");
		} else {
		    Date date = (Date) dateObj;
		    Date dateNow = new Date();
		    long diff = dateNow.getTime() - date.getTime();
		    long days = diff / (1000 * 60);
		    if (days < SESSION_LOGIN_USER_MINUTE_CFG) {// 时间差
			// 还在锁定中
			throw new BizException("账户锁定中,剩余时间" + (SESSION_LOGIN_USER_MINUTE_CFG - days) + "分钟。");
		    } else {
			// 10分钟已经过了--解锁
			loginCount(true);
		    }
		}
	    }
	}

    }

    @SuppressWarnings("unchecked")
    private void loginCount(boolean flag) {
	if (flag == true) {
	    ContextHolderUtils.getSession().removeAttribute(LoginController.SESSION_LOGIN_USER_COUNT);
	} else {
	    Object obj = ContextHolderUtils.getSession().getAttribute(LoginController.SESSION_LOGIN_USER_COUNT);
	    if (null != obj && obj instanceof Map) {
		Map<String, Object> map = (Map<String, Object>) obj;
		int len = MapUtil.getInt(map, "len");
		map.put("len", len + 1);
		ContextHolderUtils.getSession().setAttribute(LoginController.SESSION_LOGIN_USER_COUNT, map);
	    } else {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("len", 1);
		// map.put("date", new Date());
		ContextHolderUtils.getSession().setAttribute(LoginController.SESSION_LOGIN_USER_COUNT, map);
	    }
	}
    }

    @RequestMapping("/getLoginUserInfo")
    @ResponseBody
    public Result getLoginUserInfo(@RequestParam Map<String, Object> parm,HttpServletRequest request,HttpServletResponse response) {
	Result obj = new Result();
	try {
//	    setSession(request, response);
	    // put到session
	    Object result = ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
	    if(result==null){
		setSession(request, response);
		result = ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
	    }
	    obj.setRows(result);
	} catch (BizException e) {
	    log.debug("获取失败", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(e.getMessage());
	} catch (Exception e) {
	    log.error("获取异常", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(Result.RESULT_ERROR_MSG);
	}
	return obj;
    }
    
  //使用cookie登录
    public void setSession(HttpServletRequest request,HttpServletResponse response){
	String userMsg=CookieUtil.readCookie(request);
	Map<String, Object> param=new HashMap<String,Object>();
	if(userMsg!=null&&!userMsg.equals("")){
	    String[] str=userMsg.split(",");
	    param.put("username", str[0]);
	    param.put("password", str[1]);
	    Map<String, Object> result=service.login(param, request, response);
	    ContextHolderUtils.getSession().setAttribute(LoginController.LOGIN_USER, result);
	}
    }

    @RequestMapping("/logout")
    @ResponseBody
    public Result logout(@RequestParam Map<String, Object> parm,HttpServletRequest request,HttpServletResponse response) {
	Result obj = new Result();
	CookieUtil.removeCookie(response);
	try {
	    ContextHolderUtils.getSession().removeAttribute(LoginController.LOGIN_USER);
	} catch (BizException e) {
	    log.debug("登出失败", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(e.getMessage());
	} catch (Exception e) {
	    log.error("登出异常", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(Result.RESULT_ERROR_MSG);
	}
	return obj;
    }

    @RequestMapping("/updatePassword")
    @ResponseBody
    public Result updatePassword(@RequestParam Map<String, Object> parm) {
	Result obj = new Result();
	try {
	    // put到session
	    Object result = ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);
	    parm.put("loginUser", result);
	    service.updatePassword(parm);
	} catch (BizException e) {
	    log.debug("修改失败", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(e.getMessage());
	} catch (Exception e) {
	    log.error("修改异常", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(Result.RESULT_ERROR_MSG);
	}
	return obj;
    }

    @RequestMapping("/resetPassword")
    @ResponseBody
    public Result resetPassword(@RequestParam Map<String, Object> parm) {
	Result obj = new Result();
	try {
	    service.resetPassword(parm);
	} catch (BizException e) {
	    log.debug("重置失败", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(e.getMessage());
	} catch (Exception e) {
	    log.error("重置异常", e);
	    obj.setResult(Result.RESULT_ERROR);
	    obj.setMsg(Result.RESULT_ERROR_MSG);
	}
	return obj;
    }
}
