package com.cdhy.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.cdhy.comms.IConstants;
import com.cdhy.exception.BizException;

import net.sf.json.JSONObject;

public class HttpClientUtils {
    private static final Logger log = Logger.getLogger(HttpClientUtils.class);

    private static final String url = IConstants.USERLOGIN_URL;
//    private static final int timeout = IConstants.TIMEOUT;
    private static final int timeout = 20000;
    private static BasicCookieStore cookieStore = null;

    /**
     * http post 请求，参数 map ，超时时间2000ms
     * 
     * @param map
     * @param url
     * @return
     */
    public static JSONObject postMethodByMap(Map<String, Object> map) {
	return postMethodByMap(map, url);
    }

    /**
     * http post 请求，参数 map ，超时时间2000ms
     * 
     * @param map
     * @param url
     * @return
     */
    public static JSONObject postMethodByMap(Map<String, Object> map, String url) {
	JSONObject js = new JSONObject();
	CloseableHttpClient httpClient = getHttpClient();
	try {
	    HttpPost post = new HttpPost(url);
	    List<NameValuePair> list = new ArrayList<NameValuePair>();
	    for (String key : map.keySet()) {
		list.add(new BasicNameValuePair(key, map.get(key).toString()));
	    }
	    // url格式编码
	    UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list, "UTF-8");
	    
	    uefEntity.setContentEncoding("UTF-8");
	    uefEntity.setContentType("application/x-www-form-urlencoded");
	    post.setEntity(uefEntity);
	    
	    // 设置请求和传输超时时间
	    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
		    .build();
	    post.setConfig(requestConfig);
	    
	    // 执行请求
	    log.debug("请求开始，参数为：" + map.toString());
	    CloseableHttpResponse httpResponse = httpClient.execute(post);
	    if (httpResponse.getStatusLine().getStatusCode() == 200) {// 请求成功
		log.debug("请求成功");
		try {
		    HttpEntity entity = httpResponse.getEntity();
		    if (null != entity) {
			String resultStr = EntityUtils.toString(entity,"utf-8");
			try {
			    js = JSONObject.fromObject(resultStr);
			} catch (Exception e) {
			    // e.printStackTrace();
			    log.debug("返回数据转换JSON失败,resultStr:" + resultStr);
			}
		    }
		} finally {
		    httpResponse.close();
		}
	    } else {
		log.debug("请求失败");
	    }
	    
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}catch (SocketTimeoutException e){
	    throw new BizException("连接超时");
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		closeHttpClient(httpClient);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return js;
	
    }
    /**
     * http post 请求，参数 map ，超时时间2000ms
     * 
     * @param map
     * @param url
     * @return
     */

    @SuppressWarnings("unused")
    public static void main(String[] args) {
	String url = "http://118.123.249.176:13080/BSPTSystem/userController/login.do";
	// String url = "http://localhost:80/BSPTSystem/client/client.do";
	Map<String, Object> map = new HashMap<>();
	map.put("username", "admin");
	map.put("password", "123456");
	JSONObject json = HttpClientUtils.postMethodByMap(map);
	System.out.println(json.toString());
    }

    /**
     * http post 请求，参数 json ，超时时间2000ms
     * 
     * @param json
     * @param url
     * @return
     */
    public static JSONObject postMethodByJson(JSONObject json) {
	return postMethodByJson(json, url);
    }

    /**
     * http post 请求，参数 json ，超时时间2000ms
     * 
     * @param json
     * @param url
     * @return
     */
    public static JSONObject postMethodByJson(JSONObject json, String url) {
	JSONObject js = new JSONObject();
	CloseableHttpClient httpClient = getHttpClient();
	try {
	    HttpPost post = new HttpPost(url);
	    // url格式编码
	    StringEntity uefEntity = new StringEntity(json.toString(), "UTF-8");
	    uefEntity.setContentEncoding("UTF-8");
	    uefEntity.setContentType("application/json");
	    post.setEntity(uefEntity);
	    
	    // 设置请求和传输超时时间
	    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
		    .build();
	    post.setConfig(requestConfig);
	    
	    // 执行请求
	    log.debug("请求开始，参数为：" + json.toString());
	    CloseableHttpResponse httpResponse = httpClient.execute(post);
	    System.out.println(httpResponse.getStatusLine().getStatusCode());
	    if (httpResponse.getStatusLine().getStatusCode() == 200) {// 请求成功
		log.debug("请求成功");
		try {
		    HttpEntity entity = httpResponse.getEntity();
		    if (null != entity) {
			String resultStr = EntityUtils.toString(entity,"utf-8");
			try {
			    js = JSONObject.fromObject(resultStr);
			} catch (Exception e) {
			    log.debug("返回数据转换JSON失败,resultStr:" + resultStr);
			}
		    }
		} finally {
		    httpResponse.close();
		}
	    } else {
		js.put("msg", "请求失败");
		js.put("result", "ERROR");
		log.debug("请求失败");
	    }
	    
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}catch (SocketTimeoutException e){
	    throw new BizException("连接超时");
	}  catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		closeHttpClient(httpClient);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return js;
	
    }
    
    /**
     * http post 请求，参数 string ，超时时间2000ms
     * 
     * @param json
     * @param url
     * @return String
     */
    public static String postMethodByXml(String xml, String url) {
	String resultStr="";
	CloseableHttpClient httpClient = getHttpClient();
	try {
	    HttpPost post = new HttpPost(url);
	    // url格式编码
	    StringEntity uefEntity = new StringEntity(xml, "GBK");
	    uefEntity.setContentEncoding("GBK");
	    uefEntity.setContentType("text/xml");
	    post.setEntity(uefEntity);
	    
	    // 设置请求和传输超时时间
	    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
		    .build();
	    post.setConfig(requestConfig);
	    
	    // 执行请求
	    log.debug("请求开始，参数为：" + xml);
	    CloseableHttpResponse httpResponse = httpClient.execute(post);
	    if (httpResponse.getStatusLine().getStatusCode() == 200) {// 请求成功
		log.debug("请求成功");
		try {
		    HttpEntity entity = httpResponse.getEntity();
		    if (null != entity) {
			resultStr = EntityUtils.toString(entity,"utf-8");
		    }
		} finally {
		    httpResponse.close();
		}
	    } else {
		log.debug("请求失败");
	    }
	    
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}catch (SocketTimeoutException e){
	    throw new BizException("连接超时");
	}  catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		closeHttpClient(httpClient);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return resultStr;
	
    }
    /**
     * http post 请求，参数 string ，超时时间2000ms
     * 
     * @param json
     * @param url
     * @return
     */
    public static JSONObject postMethodByString(String parmars, String url) {
	JSONObject js = new JSONObject();
	CloseableHttpClient httpClient = getHttpClient();
	try {
	    HttpPost post = new HttpPost(url);
	    // url格式编码
	    StringEntity uefEntity = new StringEntity(parmars, "UTF-8");
	    uefEntity.setContentEncoding("UTF-8");
	    uefEntity.setContentType("html/text");
	    post.setEntity(uefEntity);

	    // 设置请求和传输超时时间
	    RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
		    .build();
	    post.setConfig(requestConfig);

	    // 执行请求
	    log.debug("请求开始，参数为：" + parmars);
	    CloseableHttpResponse httpResponse = httpClient.execute(post);
	    if (httpResponse.getStatusLine().getStatusCode() == 200) {// 请求成功
		log.debug("请求成功");
		try {
		    HttpEntity entity = httpResponse.getEntity();
		    if (null != entity) {
			String resultStr = EntityUtils.toString(entity,"utf-8");
			try {
			    js = JSONObject.fromObject(resultStr);
			} catch (Exception e) {
			    log.debug("返回数据转换JSON失败,resultStr:" + resultStr);
			}
		    }
		} finally {
		    httpResponse.close();
		}
	    } else {
		js.put("msg", "请求失败");
		js.put("result", "ERROR");
		log.debug("请求失败");
	    }

	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}catch (SocketTimeoutException e){
	    throw new BizException("连接超时");
	}  catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    try {
		closeHttpClient(httpClient);
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	return js;

    }

    public static void setCookieStore() {
	System.out.println("----setCookieStore");
	cookieStore = new BasicCookieStore();
	// JSESSIONID
	String JSESSIONID = PropertiesUtil.getInstance().getProperites("sessionid");
	System.out.println("JSESSIONID:" + JSESSIONID);
	// 新建一个Cookie
	BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", JSESSIONID);
	cookie.setVersion(0);
	cookie.setPath(PropertiesUtil.getInstance().getProperites("server_path"));
	cookie.setDomain(PropertiesUtil.getInstance().getProperites("server_ip"));
	cookie.setAttribute(ClientCookie.VERSION_ATTR, PropertiesUtil.getInstance().getProperites("cookie_version"));
	cookie.setAttribute(ClientCookie.DOMAIN_ATTR, PropertiesUtil.getInstance().getProperites("server_ip"));
	cookie.setAttribute(ClientCookie.PORT_ATTR, PropertiesUtil.getInstance().getProperites("server_port"));
	cookie.setAttribute(ClientCookie.PATH_ATTR, PropertiesUtil.getInstance().getProperites("server_path"));
	cookieStore.addCookie(cookie);
    }

    public static void setCookieStore(HttpResponse httpResponse) {
	System.out.println("----setCookieStore");
	Header ck = httpResponse.getFirstHeader("Set-Cookie");
	if (ck == null) {
	    return;
	}
	cookieStore = new BasicCookieStore();
	// JSESSIONID
	String setCookie = ck.getValue();
	String JSESSIONID = setCookie.substring("JSESSIONID=".length(), setCookie.indexOf(";"));
	System.out.println("JSESSIONID:" + JSESSIONID);
	// 新建一个Cookie
	BasicClientCookie cookie = new BasicClientCookie("JSESSIONID", JSESSIONID);
	cookie.setAttribute(ClientCookie.VERSION_ATTR, PropertiesUtil.getInstance().getProperites("cookie_version"));
	cookie.setAttribute(ClientCookie.DOMAIN_ATTR, PropertiesUtil.getInstance().getProperites("server_ip"));
	cookie.setAttribute(ClientCookie.PORT_ATTR, PropertiesUtil.getInstance().getProperites("server_port"));
	cookie.setAttribute(ClientCookie.PATH_ATTR, PropertiesUtil.getInstance().getProperites("server_path"));
	cookieStore.addCookie(cookie);
    }

    private static CloseableHttpClient getHttpClient() {
//	if (cookieStore == null) {
//	    setCookieStore();
//	}
//	client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
	return HttpClients.createDefault();
    }

    private static void closeHttpClient(CloseableHttpClient client) throws IOException {
	if (client != null) {
	    client.close();
	}
    }

}