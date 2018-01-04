package com.cdhy.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;
/*
 * 利用HttpClient进行post请求的工具类
 */
@SuppressWarnings("deprecation")
public class HttpsClientUtils {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String doPostMap(String url,Map<String,String> map,String charset){
	HttpClient httpClient = null;
	HttpPost httpPost = null;
	String result = null;
	try{
	    httpClient = new SSLClient();
	    httpPost = new HttpPost(url);
	    //设置参数
	    List<NameValuePair> list = new ArrayList<NameValuePair>();
	    Iterator iterator = map.entrySet().iterator();
	    while(iterator.hasNext()){
		Entry<String,String> elem = (Entry<String, String>) iterator.next();
		list.add(new BasicNameValuePair(elem.getKey(),elem.getValue()));
	    }
	    if(list.size() > 0){
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list,charset);
		httpPost.setEntity(entity);
	    }
	    HttpResponse response = httpClient.execute(httpPost);
	    if(response != null){
		HttpEntity resEntity = response.getEntity();
		if(resEntity != null){
		    result = EntityUtils.toString(resEntity,charset);
		}
	    }
	}catch(Exception ex){
	    ex.printStackTrace();
	}
	return result;
    }
	public String doPostJson(String url,JSONObject json,String charset){
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		String result = null;
		try{
			httpClient = new SSLClient();
			httpPost = new HttpPost(url);
			//设置参数
			StringEntity uefEntity = new StringEntity(json.toString(), "UTF-8");
			    uefEntity.setContentEncoding("UTF-8");
			    uefEntity.setContentType("application/json");
			    httpPost.setEntity(uefEntity);
			HttpResponse response = httpClient.execute(httpPost);
			if(response != null){
				HttpEntity resEntity = response.getEntity();
				if(resEntity != null){
					result = EntityUtils.toString(resEntity,charset);
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}

// 用于进行Https请求的HttpClient
class SSLClient extends DefaultHttpClient{
	public SSLClient() throws Exception{
        super();
        SSLContext ctx = SSLContext.getInstance("TLS");
        X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {
                }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        ClientConnectionManager ccm = this.getConnectionManager();
        SchemeRegistry sr = ccm.getSchemeRegistry();
        sr.register(new Scheme("https", 443, ssf));
    }
}}
