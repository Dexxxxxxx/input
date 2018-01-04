package com.framework.interceprots;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.cdhy.controller.security.LoginController;
import com.framework.util.ContextHolderUtils;
import com.framework.util.ResourceUtil;

/**
 * 权限拦截器
 * 
 * @author 张代浩
 * 
 */
public class AuthInterceptor implements HandlerInterceptor {

    private static final Logger logger = Logger.getLogger(AuthInterceptor.class);
    private List<String> excludeUrls;
    private List<String> excludeNamespace;

    public List<String> getExcludeNamespace() {
	return excludeNamespace;
    }

    public void setExcludeNamespace(List<String> excludeNamespace) {
	this.excludeNamespace = excludeNamespace;
    }

    public List<String> getExcludeUrls() {
	return excludeUrls;
    }

    public void setExcludeUrls(List<String> excludeUrls) {
	this.excludeUrls = excludeUrls;
    }

    /**
     * 在controller后拦截
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object,
	    Exception exception) throws Exception {
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object,
	    ModelAndView modelAndView) throws Exception {

    }

    public void setRequestUrl(HttpServletRequest request) {
	String url = request.getRequestURL().toString();
	String queryString = request.getQueryString();
	if (!StringUtils.isBlank(queryString)) {
	    url += "?" + queryString;
	}
	request.setAttribute("TAG_ACTION_URL", url);
    }

    /**
     * 在controller前拦截
     */
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {

	
	
	
	String requestPath = ResourceUtil.getRequestPath(request);// 用户访问的资源地址

	if (excludeUrls.contains(requestPath)) {
	    return true;
	}
	for (String string : excludeNamespace) {
	    if (requestPath.startsWith(string)) {
		return true;
	    }
	}
	
	Object loginObj = ContextHolderUtils.getSession().getAttribute(LoginController.LOGIN_USER);

	if (null == loginObj) {
	    response600(request, response);
	    return false;
	}
	
	Map<String, Object> loginMap = (Map<String, Object>) loginObj;
	Map<String, Object> user = (Map<String, Object>) loginMap.get("user");
	if (null == user.get("ID") || "".equals(user.get("ID"))) {
	    response401(request, response);
	    return false;
	}

	// if ("admin".equals(user.get("USERNAME"))) {
	// return true;
	// }

	Set<String> rolePrivilegeSet = (Set<String>) loginMap.get("rolePrivilegeSet");
	if (rolePrivilegeSet.contains(requestPath)||requestPath.indexOf("getTree")!=-1) {
	    return true;
	} else {
	    response401(request, response);
	    return false;
	}
    }

    private static final void response600(HttpServletRequest request, HttpServletResponse response) {
   	response.setStatus(600);
       }
    private static final void response401(HttpServletRequest request, HttpServletResponse response) {
	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    /**
     * 转发
     * 
     * @param request
     * @return
     */
    @RequestMapping(params = "forword")
    public ModelAndView forword(HttpServletRequest request) {
	return new ModelAndView(new RedirectView("loginController.do?login"));
    }

    private void forward(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	request.getRequestDispatcher("login.html").forward(request, response);
    }

}
