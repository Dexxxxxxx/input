package com.cdhy.util;

import java.math.BigDecimal;

public class ValidateUtil {

    public static boolean validateDecimalString(String... decimalStrArr) {
	boolean flag = true;
	try {
	    for (String decimalStr : decimalStrArr) {
		new BigDecimal(decimalStr);
	    }
	} catch (Exception e) {
	    flag = false;
	}
	return flag;
    }
}
