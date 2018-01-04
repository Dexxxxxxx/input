package com.cdhy.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期类工具类
 * 
 * @Copyright (C),沪友科技
 * @author zlf
 * @Date:2015年6月15日
 */
public class DateUtil {

	/**
	 * 获取当前的时间：字符串类型
	 * 
	 * @author zlf
	 * @Date 2015年6月15日
	 * @return
	 */
	public static String getCurrentTimeString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	/**
	 * <p>
	 * 获取当前的时间：long类型
	 * </p>
	 * 
	 * @author kxq
	 * @Date 2015年3月5日
	 * @return
	 */
	public static long getCurrentTimeLong() {
		Date date = new Date();
		long currentTime = date.getTime();
		return currentTime;
	}

	/**
	 * 获取当前的日期：long类型
	 * 
	 * @author zlf
	 * @Date 2015年6月15日
	 * @return
	 */
	public static Long getCurrentDateLong() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String cDate = sdf.format(new Date());
		Date dateCurrent = null;
		try {
			dateCurrent = new SimpleDateFormat("yyyy-MM-dd").parse(cDate);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dateCurrent.getTime();
	}

	/**
	 * 获取当前日期：字符串类型
	 * 
	 * @author zlf
	 * @Date 2015年6月15日
	 * @return
	 */
	public static String getCurrentDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date());
	}

	/**
	 * 把yyyy-MM-dd解析为long类型时间
	 * 
	 * @author zlf
	 * @Date 2015年6月15日
	 * @param dateStr
	 * @return
	 */
	public static long parseDateMyYYMMDDToLong(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (Exception ex) {

		}
		return date.getTime();
	}

	/**
	 * 把yyyy-MM-dd HH:mm:ss解析为long类型时间
	 * 
	 * @author zlf
	 * @Date 2015年6月15日
	 * @param dateStr
	 * @return
	 */
	public static long parseDateMyYYMMDDHHMMSSToLong(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (Exception ex) {

		}
		return date.getTime();
	}

	/**
	 * 把yyyyMMddHHmmss解析为long类型时间
	 * 
	 * @author zlf
	 * @Date 2015年6月15日
	 * @param dateStr
	 * @return
	 */
	public static long parseYYYYMMDDHHMMSSToLong(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyyMMddHHmmss");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return date.getTime();
	}

	public static String formatLongToNormal(Long timespan){
	    try{
		return formatDateToNormal(new Date(timespan));
	    }catch(Exception e){
		return formatDateToNormal(new Date());
	    }
	}
	
	public static String formatDateToNormal(Date date){
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    date = date == null ? new Date() : date;
	    return sdf.format(date);
	}
	/**
	 * 把yyyyMMddHHmmss格式化为yyyy-MM-dd HH:mm:ss
	 * 
	 * @author zlf
	 * @Date 2015年6月15日
	 * @param dateStr
	 * @return
	 */
	public static String formatDateToNormal(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern("yyyyMMddHHmmss");
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

}
