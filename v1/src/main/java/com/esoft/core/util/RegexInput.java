package com.esoft.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 输入验证
 * 
 * @author yinjunlu
 * 
 */
public class RegexInput {

	/**
	 * 验证email
	 * 
	 * @param email
	 * @return
	 */
	public static boolean emailFormat(String email) {
		boolean tag = true;
		final String pattern1 = "^([a-z0-9A-Z_-]+[-|\\.]?)+[a-z0-9A-Z_-]@([a-z0-9A-Z_-]+(-[a-z0-9A-Z_-]+)?\\.)+[a-zA-Z]{2,}$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find()) {
			tag = false;
		}
		return tag;
	}

	/**
	 * 验证数字
	 * 
	 * @param numberStr
	 * @return
	 */
	public static boolean isNumber(String numberStr) {
		boolean tag = true;
		final String number = "123456789";
		for (int i = 0; i < numberStr.length(); i++) {
			if (number.indexOf(numberStr.charAt(i)) == -1) {
				tag = false;
				break;
			}
		}
		return tag;
	}

	/**
	 * 验证邮政编码
	 * 
	 * @param postcode
	 * @return
	 */
	public static boolean checkPostcode(String postcode) {
		if (postcode.matches("[0-9]\\d{5}(?!\\d)")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证手机
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean checkMobilePhone(String phone) {
		if (phone.matches("^[1][3,5,8]+\\d{9}")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 验证固话
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean checkPhone(String phone) {
		if (phone.matches("\\d{4}-\\d{8}|\\d{4}-\\d{7}|\\d{3}-\\d{8}")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 验证是否含有中文字符
	 * @param inputStr
	 * @return
	 */
	public static boolean isContainChinese(String inputStr) {
		if (StringUtils.isEmpty(inputStr)) {
			return false;
		}
		
		char[] cc = inputStr.toCharArray();
		for (char c : cc) {
			if (Character.toString(c).matches("[\\u4E00-\\u9FBF]+")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 验证是否还有特殊符号
	 * @param inputStr
	 * @return
	 */
	public static boolean isContainPunctuation(String inputStr){
		String regEx = "[`~!@%#$^&*()=|{}':;',　\\[\\]<>/? \\.；：%……+￥（）【】‘”“'。，、？]";
		if (StringUtils.isEmpty(inputStr)) {
			return false;
		}
		
		char[] cc = inputStr.toCharArray();
		for (char c : cc) {
			if (Character.toString(c).matches(regEx)) {
				return true;
			}
		}
		return false;
	}
}
