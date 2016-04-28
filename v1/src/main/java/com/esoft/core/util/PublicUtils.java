package com.esoft.core.util;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.math.RandomUtils;



/**
 * 项目的共用方法
 * 
 * @author yinjunlu
 * 
 */
public class PublicUtils {

	/***************随机数***********************/
	/**
	 * 给定字符和长度生成随机数
	 * 
	 * @param source
	 * @param length
	 * @return
	 */
	public static String getRandom(String source, int length) {
		char[] sourceChar = source.toCharArray();
		if (sourceChar == null || sourceChar.length == 0 || length < 0) {
			return null;
		}

		StringBuilder str = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			str.append(sourceChar[RandomUtils.nextInt(sourceChar.length)]);
		}
		return str.toString();
	}

	/**
	 * 数字随机数
	 * 
	 * @param length
	 * @return
	 */
	public static String getrandomInt(int length) {
		return getRandom("0123456789", length);
	}

	/**
	 * 得到固定长度的随机字符串，字符串由数字和大小写字母混合组成
	 * 
	 * @param length
	 * @return
	 */
	public static String getrandomString(int length) {
		return getRandom(
				"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ",
				length);
	}

	
	/***********************时间处理**************************/
	
	/**
	 * 根据给定的日期，返回给定的字符串， 返回 字符串的形式是：yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 *            要格式化的日期
	 * @return 将日期格式化后返回的字符串，以这中格式返回：yyyy-MM-dd HH:mm:ss
	 * @throws Exception
	 */
	public final static String getStrByDate(java.util.Date date, String format)
			throws Exception {
		try {
			if (date == null || isEmpty(format)) {
				return null;
			} else {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
				return simpleDateFormat.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 时间格式化 格式：yyyyMMddHHmmss yyyy-MM-dd yyyy-MM-dd HH:mm:ss等
	 * 
	 * @param dateFormat
	 * @return
	 */
	public static String formatDate(String dateFormat) {
		String rdate;
		SimpleDateFormat date = new SimpleDateFormat(dateFormat);
		rdate = date.format(new java.util.Date());
		return rdate;
	}

	/**
	 * 根据固定的时间格式的字符串返回时间
	 * 
	 * @param dateString
	 * @return
	 */
	public static Date getDatebyString(String dateString){
		SimpleDateFormat dateStr = new SimpleDateFormat();
		try {
			Date date = dateStr.parse(dateString);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}
	
	/**
	 * 当前时间，精确到秒
	 * 
	 * @return
	 */
	public static String getCurrentDatetimeByString() {
		String datetime = new Timestamp(System.currentTimeMillis()).toString();
		return datetime.substring(0, 19);
	}

	/**
	 * 为给定的日历字段添加或减去指定的时间
	 * @param amount  正数为添加|负数为减少
	 * @return
	 */
	public static Date setDatelessormore(int dateType, int amount){
		Calendar calendar = Calendar.getInstance();
		if(amount == 0){
			return new Date();
		}
		calendar.add(dateType, amount);
		return calendar.getTime();
	}
	
	/**
	 * 为给定的日历字段添加或减去指定的小时
	 * @param amount
	 * @return
	 */
	public static Date setDatelessormoreHour(int amount){
		return setDatelessormore(Calendar.HOUR_OF_DAY,amount);
	}
	/**
	 * 把"<"、">"转义
	 * 
	 * @param input
	 * @return
	 */
	public static final String escapeHTMLTags(String input) {
		if (input == null || input.length() == 0) {
			return input;
		}
		StringBuffer buf = new StringBuffer(input.length());
		char ch = ' ';
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			if (ch == '<') {
				buf.append("&lt;");
			} else if (ch == '>') {
				buf.append("&gt;");
			} else {
				buf.append(ch);
			}
		}
		return buf.toString();
	}


    /***********************空判断*****************************/
	
	/**
	 * @param 判断字Object是否为空，空true，否则false
	 * @param str
	 * @return
	 */
	public static boolean isObjectEmpty(Object obj){
		try{
			if(obj == null){
				return true;
			}else if("".equals(obj.toString())){
				return true;
			}else if("null".equals(obj.toString())){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return true;
		}
		
	}
	/**
	 * @param 判断字符串是否为空
	 *            ，空true，否则false
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		try {
			if (str == null) {
				return true;
			} else if ("".equals(str.trim())) {
				return true;
			} else if ("null".equals(str.trim())) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}

	}
	
	public static void main(String[] args) {
		try {
			Class<?> test = Class.forName("com.esoft.core.util.PublicUtils");
			Method[] methods = test.getMethods();
			for(Method method : methods){
				System.out.println(method);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
