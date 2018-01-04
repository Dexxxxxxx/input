package wxfp;
import java.util.HashMap;
import java.util.Map;

import com.cdhy.util.HttpClientUtils;
import com.cdhy.util.HttpsClientUtils;

import net.sf.json.JSONObject;
//对接口进行测试
public class TestMain {
	private String url = "https://api.weixin.qq.com/cgi-bin/token";
	private String charset = "utf-8";
	private HttpsClientUtils httpsClientUtils = null;
	
	public TestMain(){
		httpsClientUtils = new HttpsClientUtils();
	}
	
	public void test(){
	    String httpOrgCreateTest = url;
	    Map<String,String> createMap = new HashMap<String,String>();
	    createMap.put("grant_type","client_credential");
	    createMap.put("appid","wxcd5be5c1e71ce0cc");
	    createMap.put("secret","6ca8b005ac7475e52653344d17d22286");
	    String httpOrgCreateTestRtn = httpsClientUtils.doPostMap(httpOrgCreateTest,createMap,charset);
	    System.out.println("result:"+httpOrgCreateTestRtn);
	}
	
	public void test2(){
	    String url = "http://192.168.0.123:8080/wxfp/page/fpkj.do";
	    String string="{\"fhr \":\"复核人\",\"skr \":\"收款人\",\"tspz\":\"00\",\"hjse\":\"262.26\",\"ghdwmc\":\"购货单位名称\",\"fpqqlsh\":\"20170518162451\",\"yfpdm\":\"\",\"xhdwyhzh\":\"88888888\",\"tzdbh\":\"4654646\",\"ghdwyhzh\":\"35456534654654646\",\"kpr\":\"开票人\",\"ghdwdzdh\":\"456465465456\",\"qdbz\":\"0\",\"ghdwsbh\":\"510198066961390\",\"xhdwsbh\":\"51019806696139X\",\"bz\":\"备注\",\"xhdwmc\":\"四川百望金赋科技有限公司\",\"yfphm\":\"\",\"xmxx\":[{\"sl\":\"0.17\",\"fphxz\":\"0\",\"je\":\"1542.74\",\"dw\":\"台\",\"spmc\":\"测试名称\",\"dj\":\"1542.74\",\"hsbz\":\"0\",\"ggxh\":\"xx\",\"se\":\"262.26\",\"spsl\":\"1\",\"spsm\":\"\"}],\"kplx\":\"0\",\"hjje\":\"1542.74\",\"kpzdbs\":\"51010222\",\"xhdwdzdh\":\"四川成都\",\"fplxdm\":\"007\",\"jshj\":\"1805.00\"}";
	    JSONObject json=JSONObject.fromObject(string);
	    JSONObject responseBody = HttpClientUtils.postMethodByJson(json, url);
	    System.out.println(responseBody);
	}
	
	public void test_getcode(){
	    String url = "http://192.168.0.123:8080/wxfp/app/service.do";
//	    String url = "http://118.123.249.141:9100/wxfp/app/service.do";
	    JSONObject json= new JSONObject();
	    json.put("invoke", "getSmsCode");
	    
	    JSONObject js= new JSONObject();
	    js.put("phone", "15828675378");
	    
	    json.put("p", js);
	    JSONObject responseBody = HttpClientUtils.postMethodByJson(json, url);
	    System.out.println(responseBody);
	}
	public void test_regist(){
	    String url = "http://192.168.0.123:8080/wxfp/app/service.do";
	    JSONObject json= new JSONObject();
	    json.put("invoke", "regist");
	    
	    JSONObject js= new JSONObject();
	    js.put("phone", "15828675378");
	    js.put("password", "123456");
	    js.put("nicename", "帆");
	    js.put("code", "827180");
	    
	    json.put("p", js);
	    JSONObject responseBody = HttpClientUtils.postMethodByJson(json, url);
	    System.out.println(responseBody);
	}
	
	public void test_login(){
	    String url = "http://192.168.0.30/piaoju/cdhy/service.do";
	    JSONObject json= new JSONObject();
	    json.put("invoke", "loginByPwd");
	    
	    JSONObject js= new JSONObject();
	    js.put("username", "18349280204");
	    js.put("password", "123456");
	    js.put("type", "Android");
	    js.put("client", "122333");
	    json.put("p", js);
	    JSONObject responseBody = HttpClientUtils.postMethodByJson(json, url);
	    System.out.println(responseBody);
	}
	public void test_smslogin(){
		String url = "http://192.168.0.123:8080/wxfp/app/service.do";
		JSONObject json= new JSONObject();
		json.put("invoke", "smsLogin");

		JSONObject js= new JSONObject();
		js.put("phone", "15828675378");
		js.put("code", "652853");
		
		json.put("p", js);
		JSONObject responseBody = HttpClientUtils.postMethodByJson(json, url);
		System.out.println(responseBody);
	}
	
	public static void main(String[] args){
		TestMain main = new TestMain();
//		main.test_getcode();
//		main.test_smslogin();
		main.test_login();
	    System.out.println(System.currentTimeMillis());
	}
}