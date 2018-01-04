package com.cdhy.util;


import org.apache.commons.codec.binary.Base64;


public class Base64Util {
    
    private static final Base64 base64 = new Base64();
    
    public static final String Base64Encode(String origin) {
	
	try {
	    if (origin == null || "".equals(origin.trim())) {
		return "";
	    }
	    return Base64Encode(origin.getBytes("utf-8"));
	} catch (Exception e) {
	    return null;
	}
    }
    
    public static final String Base64Encode(byte[] bytes) {
	if (bytes == null || bytes.length <= 0) {
	    return "";
	}
	try {
	    return new String(base64.encode(bytes), "utf-8");
	} catch (Exception e) {
	    return "";
	}
    }
    
    public static final String Base64Decode(String origin) {
	try {
	    if (origin == null || "".equals(origin.trim())) {
		return "";
	    }
	    return Base64Decode(origin.getBytes("utf-8"));
	} catch (Exception e) {
	    return null;
	}
    }
    
    public static final String Base64Decode(byte[] bytes) {
	try {
	    byte[] decodeBytes = base64.decode(bytes);
	    return new String(decodeBytes, "utf-8");
	} catch (Exception e) {
	    return "";
	}
    }
    
    public static final String Base64Decode(byte[] bytes, String charset) {
	try {
	    byte[] decodeBytes = base64.decode(bytes);
	    return new String(decodeBytes, charset);
	} catch (Exception e) {
	    return "";
	}
    }
}
