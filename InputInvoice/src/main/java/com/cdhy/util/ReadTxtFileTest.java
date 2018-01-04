package com.cdhy.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cdhy.exception.BizException;

/**
 * 读取烟草公司txt文本内容，拆分放入map
 * 
 * @author
 *
 */
public class ReadTxtFileTest {

    // 电话号码
    private static final String reg_1 = "(\\(\\d{3,4}\\)|\\d{3,4}-|\\s)?\\d{7,11}$";
    // 银行账号
    private static final String reg_2 = "\\d+$";

    public List<Map<String, Object>> readTxtFile(String filePath) {
	InputStreamReader reader = null;
	BufferedReader bufferedReader = null;
	String lineTxt = null;
	int line = 0;//txt文件行数
	List<Map<String, Object>> list =new ArrayList<Map<String,Object>>();
	try {
	    String encoding = getCharset(filePath);// 获取文件的编码格式
	    File file = new File(filePath);
	    reader = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
	    bufferedReader = new BufferedReader(reader);
	    String[] strArry = null;// 公共数据
	    while ((lineTxt = bufferedReader.readLine()) != null) {
		line++;
		if(line<=3) continue;//跳过第一行
		if(!"".equals(lineTxt) && !lineTxt.startsWith("//")){
			strArry = lineTxt.split("[~]+");
			if(strArry.length<=2){
			    continue;
			}else if(strArry.length>=5){
			    Map<String, Object> mp = new HashMap<String,Object>();
			    mp.put("ghdwdm", strArry[0]);
			    mp.put("ghdwmc", strArry[1]);
			    mp.put("ghdwnsrsbh", strArry[2]);
			    mp.put("ghdwdzdh", strArry[3].replaceAll("\"", ""));
			    mp.put("ghdwyhzh", strArry[4].replaceAll("\"", ""));
			    list.add(mp);
			}
		}
		
	    }
	}catch(BizException bz){
	    throw new BizException(bz.getMessage());
	}
	catch (FileNotFoundException e) {
	    e.printStackTrace();
	    System.out.println("找不到指定的文件");
	} catch (Exception e) {
	    e.printStackTrace();
	    System.out.println("读取文件内容出错");
	} finally {
	    if (reader != null) {
		try {
		    reader.close();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	    if (bufferedReader != null) {
		try {
		    bufferedReader.close();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    }
	}
	return list;
    }

    /**
     * 获取txt文件编码格式
     * 
     * @param fileName
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unused")
    private String getCharset(String fileName) throws IOException {

	@SuppressWarnings("resource")
	BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
	int p = (bin.read() << 8) + bin.read();

	String code = null;

	switch (p) {
	case 0xefbb:
	    code = "UTF-8";
	    break;
	case 0xfffe:
	    code = "Unicode";
	    break;
	case 0xfeff:
	    code = "UTF-16BE";
	    break;
	default:
	    code = "GBK";
	}
	return code;
    }

    // 截取地址与电话、开户行银行与帐号在一起的方法
    private Map<String, Object> splitString(String origin) {
	Pattern pattern_1 = Pattern.compile(reg_1);
	Pattern pattern_2 = Pattern.compile(reg_2);
	Map<String, Object> map = new HashMap<String, Object>();
	Matcher matcher = null;
	if (!"".equals(origin)) {
	    matcher = pattern_2.matcher(origin);
	    int num = 10;// 银行卡号长度16-20
	    if (matcher.find()) {
		if (num <= matcher.group().length()) {

		    map.put("khh", origin.substring(0, matcher.start()).trim());
		    map.put("yhzh", matcher.group().toString());

		} else {
		    matcher = pattern_1.matcher(origin);
		    if (matcher.find()) {
			map.put("dz", origin.substring(0, matcher.start()).trim());
			map.put("dh", matcher.group());
		    }

		}
	    }

	}
	return map;
    }

    public static void main(String[] args) {
	ReadTxtFileTest rtf = new ReadTxtFileTest();
	List<Map<String, Object>> s = rtf.readTxtFile("C:\\Users\\i5win10\\Desktop\\20161029(3).txt");
	System.out.println(s);
    }

}
