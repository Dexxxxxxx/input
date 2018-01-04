package com.framework.util;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * 项目参数工具类
 * 
 */
public class ResourceUtil {

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("conf/sysConfig");

    /**
     * 获取session定义名称
     * 
     * @return
     */
    public static final String getSessionattachmenttitle(String sessionName) {
	return bundle.getString(sessionName);
    }

    /**
     * 获得请求路径
     * 
     * @param request
     * @return
     */
    public static String getRequestPath(HttpServletRequest request) {
	return getRequestPath(request, false);
    }

    public static String getRequestPath(HttpServletRequest request, boolean full) {
	String requestPath = request.getRequestURI();

	if (requestPath.indexOf("&") > -1 && !full) {// 去掉其他参数
	    requestPath = requestPath.substring(0, requestPath.indexOf("&"));
	}

	// requestPath = requestPath
	// .substring(request.getContextPath().length() + 1);// 去掉项目路径
	requestPath = requestPath.substring(request.getContextPath().length());// 去掉项目路径
	return requestPath;
    }

    /**
     * 没有登录，跳转到登陆界面，获得登录前的url
     * 
     * @param request
     * @return
     */
    public static String getRedirUrl(HttpServletRequest request) {
	String requestPath = request.getRequestURI() + "?" + request.getQueryString();
	requestPath = requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
	return requestPath;
    }

    public static String getDomain(HttpServletRequest request) {
	StringBuffer url = request.getRequestURL();
	String domain = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
	return domain;
    }

    public static boolean isAdminDomain(HttpServletRequest request) {
	String domain = getDomain(request);
	String admin = request.getParameter("admin");
	return StringUtils.trimToEmpty(domain).equals(ResourceUtil.getConfigByName("admin_domain")) || admin != null;
    }

    /**
     * 获取配置文件参数
     * 
     * @param name
     * @return
     */
    public static final String getConfigByName(String name) {
	return bundle.getString(name);
    }

    /**
     * 获取配置文件参数
     * 
     * @param path
     * @return
     */
    public static final Map<Object, Object> getConfigMap(String path) {
	ResourceBundle bundle = ResourceBundle.getBundle(path);
	Set set = bundle.keySet();
	return oConvertUtils.SetToMap(set);
    }	

    public static String getSysPath() {
	String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
	String temp = path.replaceFirst("file:/", "").replaceFirst("WEB-INF/classes/", "");
	String separator = System.getProperty("file.separator");
	String resultPath = temp.replaceAll("/", separator + separator).replaceAll("%20", " ");
	return resultPath;
    }

    /**
     * 获取项目根目录
     * 
     * @return
     */
    public static String getPorjectPath() {
	String nowpath; // 当前tomcat的bin目录的路径 如
			// D:\java\software\apache-tomcat-6.0.14\bin
	String tempdir;
	nowpath = System.getProperty("user.dir");
	tempdir = nowpath.replace("bin", "webapps"); // 把bin 文件夹变到 webapps文件里面
	tempdir += "\\"; // 拼成D:\java\software\apache-tomcat-6.0.14\webapps\sz_pro
	return tempdir;
    }

    public static String getClassPath() {
	String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
	String temp = path.replaceFirst("file:/", "");
	String separator = System.getProperty("file.separator");
	String resultPath = temp.replaceAll("/", separator + separator);
	return resultPath;
    }

    public static String getSystempPath() {
	return System.getProperty("java.io.tmpdir");
    }

    public static String getSeparator() {
	return System.getProperty("file.separator");
    }

    public static String getParameter(String field) {
	HttpServletRequest request = ContextHolderUtils.getRequest();
	return request.getParameter(field);
    }

    /**
     * 获取随机码的长度
     *
     * @return 随机码的长度
     */
    public static String getRandCodeLength() {
	return bundle.getString("randCodeLength");
    }

    /**
     * 获取随机码的类型
     *
     * @return 随机码的类型
     */
    public static String getRandCodeType() {
	return bundle.getString("randCodeType");
    }

    public static String getDefaultPassword() {
	return bundle.getString("DEFAULT_PASSWORD");
    }
    
    
    
   /**
    * 获取开票服务器地址和密码
    * @return
    */
    public static String getDefaultServletIp() {
	return bundle.getString("SERVLETIP");
    }
    public static String getDefaultKeyPwd() {
	return bundle.getString("KEYPWD");
    }
    
    public static String getDefaultServletPort() {
	return bundle.getString("SERVLETPORT");
    }
    
    /**
     * 获取发票开具的最大金额
     * @return
     */
    public static String getLimitCount(){
	return bundle.getString("LIMIT_COUNT");
    }
    /**
     * 税控盘信息
     * @return
     */
    //注册导入信息
    public static String getZCMXX(){
	return bundle.getString("ZCMXX");
    }
    //税控盘编号
    public static String getSKPBH(){
	return bundle.getString("SKPBH");
    }
    //税控盘口令
    public static String getSKPKL(){
	return bundle.getString("SKPKL");
    }
    //数字证书密码
    public static String getKEYPWD(){
	return bundle.getString("KEYPWD");
    }
    public static String getNSRSBH(){
	return bundle.getString("NSRSBH");
    }
}
