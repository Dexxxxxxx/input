package com.cdhy.util;

import java.io.File;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import com.cdhy.comms.Access_Token;

import net.sf.json.JSONObject;

/**
 * 上传媒体文件到微信临时服务器
 * 
 * @author http://blog.csdn.net/u012613903/article/details/49445223
 * 
 */
public class HttpsClientToWeChat {

    public static JSONObject httpsClient(String path) throws Exception {
    //
    JSONObject json = new JSONObject();
    // 获得utf-8编码的mbuilder
    MultipartEntityBuilder mBuilder = get_COMPATIBLE_Builder("UTF-8");
    /**
     * 原生的微信使用的url是https://api.weixin.qq.com/cgi-bin/media/upload?
     * access_token=##ACCESS_TOKEN##&type=##TYPE##
     * 一般都会使用这个把参数直接携带在url中。我个人不喜欢这样，因为既然使用了httpclient，完全可以把参数
     * 设置在我们的body体中。所以我们使用的url是这样的
     * https://api.weixin.qq.com/cgi-bin/media/upload 然后通过在body体中设置参数来设置
     * access_token和type这两个字段
     * 
     */
    // 设置type，我这里用一个缩略图来做实验，所以type是thumb
    // 设置type，
    mBuilder.addTextBody("type", "image");
    // 设置access_token，
    mBuilder.addTextBody("access_token", getAccessToken());
    // 这里就是我要上传到服务器的多媒体图片
    mBuilder.addBinaryBody("media", getFile(path), ContentType.APPLICATION_OCTET_STREAM,
	    getFile(path).getName());
    // 建造我们的http多媒体对象
    HttpEntity he = mBuilder.build();
    // 建立一个sslcontext，这里我们信任任何的证书。
    SSLContext context = getTrustAllSSLContext();
    // 建立socket工厂
    SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(context);
    // 建立连接器
    CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(factory).build();
    try {
	// 得到一个post请求的实体
	HttpPost post = getMultipartPost();
	// 给请求添加参数
	post.setEntity(he);
	// 执行请求并获得结果
	CloseableHttpResponse reponse = client.execute(post);
	try {
	    // 获得返回的内容
	    HttpEntity entity = reponse.getEntity();
	    // 返回并测试输出
	    json = JSONObject.fromObject(EntityUtils.toString(entity));
	    System.out.println(json);
	    // 消耗实体
	    EntityUtils.consume(entity);
	} finally {
	    // 关闭返回的reponse
	    reponse.close();
	}
    } finally {
	// 关闭client
	client.close();
    }
    return json;
    }

    private static String getBoundaryStr(String str) {
    return "------------" + str;
    }

    private static File getFile(String path) {
    return new File(path);
    }

    private static MultipartEntityBuilder get_COMPATIBLE_Builder(String charSet) {
    MultipartEntityBuilder result = MultipartEntityBuilder.create();
    result.setBoundary(getBoundaryStr("cdhy123456com")).setCharset(Charset.forName(charSet))
	    .setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    return result;
    }

    private static String getAccessToken() {
    // 这里返回一个access_token，
    return Access_Token.getInstance().getAccess_token();
    }

    private static String getUrl() {
    return PropertiesUtil.getInstance().getProperites("up_temporary_url");
    }

    private static HttpPost getMultipartPost() {
    /* 这里设置一些post的头部信息，具体求百度吧 */
    HttpPost post = new HttpPost(getUrl());
    post.addHeader("Connection", "keep-alive");
    post.addHeader("Accept", "*/*");
    post.addHeader("Content-Type", "multipart/form-data;boundary=" + getBoundaryStr("cdhy123456com"));
    post.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
    return post;
    }

    private static SSLContext getTrustAllSSLContext() throws Exception {
    SSLContext context = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
	@Override
	public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	// 这一句就是信任任何的证书，当然你也可以去验证微信服务器的真实性
	return true;
	}
    }).build();
    return context;
    }
}
