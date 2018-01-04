package com.cdhy.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {
    private static PropertiesUtil p = null;
    Properties prop;
    private Map<String, String> chacheData = new HashMap<>();

    private PropertiesUtil() {
	prop = new Properties();
	try {
	    InputStream in = this.getClass().getClassLoader().getResourceAsStream("conf/cfg.properties");
	    // InputStream in = PropertiesUtil.class.getClass().getClassLoader()
	    // .getResourceAsStream("src/main/resources/conf.properties");
	    prop.load(in);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static synchronized PropertiesUtil getInstance() {
	if (p == null) {
	    p = new PropertiesUtil();
	}
	return p;
    }

    public String getProperites(String key) {
	if(chacheData.containsKey(key)){
	    return chacheData.get(key);
	}
	String value = prop.getProperty(key);
	if (null == value) {
	    return "";
	}
	chacheData.put(key, value);
	return prop.getProperty(key).trim();
    }

    @SuppressWarnings("unused")
    public void updateProperites(String key, String value) {
	prop.put(key, value);
	OutputStream fos;
	try {
	    // fos = new FileOutputStream("conf/cfg.properties");
	    // prop.store(fos, "Update '" + key + "' value");
	} catch (Exception e) {

	    e.printStackTrace();

	}
    }

    public static void main(String[] args) {
	System.out.println(PropertiesUtil.getInstance().getProperites("remote_fpt_url"));

    }
}
