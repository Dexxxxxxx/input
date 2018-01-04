package wxfp;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.cdhy.util.Coder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 */
public class CoderTest {

    @Test
    public void test() throws Exception {
	String inputStr = "简单加密";
	System.err.println("原文:\n" + inputStr);

	byte[] inputData = inputStr.getBytes();
	String code = Coder.encryptBASE64(inputData);

	System.err.println("BASE64加密后:\n" + code);

	byte[] output = Coder.decryptBASE64(code);

	String outputStr = new String(output);

	System.err.println("BASE64解密后:\n" + outputStr);

	// 验证BASE64加密解密一致性
	assertEquals(inputStr, outputStr);

	// 验证MD5对于同一内容加密是否一致
	assertArrayEquals(Coder.encryptMD5(inputData), Coder.encryptMD5(inputData));

	// 验证SHA对于同一内容加密是否一致
	assertArrayEquals(Coder.encryptSHA(inputData), Coder.encryptSHA(inputData));

	String key = Coder.initMacKey();
	System.err.println("Mac密钥:\n" + key);

	// 验证HMAC对于同一内容，同一密钥加密是否一致
	assertArrayEquals(Coder.encryptHMAC(inputData, key), Coder.encryptHMAC(inputData, key));

	BigInteger md5 = new BigInteger(Coder.encryptMD5(inputData));
	System.err.println("MD5:\n" + md5.toString(16));

	BigInteger sha = new BigInteger(Coder.encryptSHA(inputData));
	System.err.println("SHA:\n" + sha.toString(32));

	BigInteger mac = new BigInteger(Coder.encryptHMAC(inputData, inputStr));
	System.err.println("HMAC:\n" + mac.toString(16));
    }

    @Test
    public void test2() {
	String ent_uid = "57955c38-88d2-4c68-bd7f-c5c0cdcd6d58";
	String random_str = "123456";
	String enc_str = "test01";
	byte[] inputData = (ent_uid + random_str + enc_str).getBytes();
	String sha_str = "";
	try {
	    sha_str = new BigInteger(Coder.encryptSHA(inputData)).toString(32);
	} catch (Exception e1) {
	}
	System.out.println("啊"+sha_str);
    }

    @Test
    public void test3() {
	String ent_uid = "eyJmaHIiOiLlpI3moLjkuroiLCJza3IiOiLmlLbmrL7kuroiLCJ0c3B6IjoiMDAiLCJoanNlIjoiMjYyLjI2IiwidHpkYmgiOiI0NjU0NjQ2Iiwia3ByIjoi5byA56Wo5Lq6IiwicWRieiI6IjAiLCJieiI6IuWkh+azqCIsInhteHgiOlt7InNsIjoiMC4xNyIsImZwaHh6IjoiMCIsImplIjoiMTU0Mi43NCIsImR3Ijoi5Y+wIiwic3BtYyI6Iua1i+ivleWQjeensCIsImRqIjoiMTU0Mi43NCIsImhzYnoiOiIwIiwiZ2d4aCI6Inh4Iiwic2UiOiIyNjIuMjYiLCJzcHNsIjoiMSIsInNwc20iOiIifV0sImtwbHgiOiIwIiwiaGpqZSI6IjE1NDIuNzQiLCJrcHpkYnMiOiI1MTAxMDIyMiIsImpzaGoiOiIxODA1LjAwIiwienlzcG1jIjoi6aSQ6aWu5pyN5YqhIiwib3JkZXIiOiLorqLljZXlj7cyMjIiLCJmcHFxbHNoIjoieGhkazEyM2hka2oiLCJlbnRfdWlkIjoiNTc5NTVjMzgtODhkMi00YzY4LWJkN2YtYzVjMGNkY2Q2ZDU4IiwicmFuZG9tX3N0ciI6IjEyMzQ1NiIsInJlcV9lbmNfc3RyIjoiLWNmNm00OWRqNnVlbjgxbGJ0MWdpOXNtZGczYmxxYWJkIn0=";
	byte[] base64 = null;
	try {
	    base64 = Coder.decryptBASE64(ent_uid);
	} catch (Exception e) {
	}
	System.out.println(new String(base64));
    }
    
    @Test
    public void test_checkfpParam() {
	String str = "{\"fhr\":\"\",\"skr\":\"\",\"hjse\":\"262.26\",\"fpqqlsh\":\"\",\"kpr\":\"开票人\",\"qdbz\":\"0\",\"kplx\":\"0\",\"hjje\":\"1542.74\",\"kpzdbs\":\"51010222\",\"jshj\":\"1805.00\",\"zyspmc\":\"餐饮服务\",\"order\":\"订单号222\",\"ent_uid\":\"57955c38-88d2-4c68-bd7f-c5c0cdcd6d58\",\"random_str\":\"123456\",\"req_enc_str\":\"-cf6m49dj6uen81lbt1gi9smdg3blqabd\",\"xmxx\":[{\"sl\":\"0.17\",\"fphxz\":\"0\",\"je\":\"1542.74\",\"dw\":\"台\",\"spmc\":\"测试名称\",\"dj\":\"1542.74\",\"hsbz\":\"0\",\"ggxh\":\"xx\",\"se\":\"262.26\",\"spsl\":\"1\",\"spsm\":\"\"}]}";
	JSONObject js = JSONObject.fromObject(str);
	JSONArray jsonArray = js.getJSONArray("xmxx");
	Set keySet = js.keySet();
	Collection values = js.values();
	System.out.println("原始数据："+js);
	js.remove("xmxx");
	List<Object> xmxx_values = new ArrayList<>();
	 Set xmxx_keySet = null;
	for (Object object : jsonArray) {
	    JSONObject jo = (JSONObject) object;
	    xmxx_keySet = jo.keySet();
	    xmxx_values.add(jo.values());
	}
	System.out.println("keySet:"+keySet);
	System.out.println("values:"+values);
	System.out.println("xmxx_keySet:"+xmxx_keySet);
	System.out.println("xmxx_values:"+xmxx_values);
	
	System.out.println("keySet.size:"+keySet.size());
	System.out.println("xmxx_keySet.size:"+xmxx_keySet.size());
    }
}