/**
 * 
 */
package com.esoft.core.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * @author yinjunlu
 * 
 * 用于处理数字格式化
 *
 */
public class DigitFormatUtil {
	
	private static NumberFormat format1 = new DecimalFormat("###,###,###.##");
	private static NumberFormat format2 = new DecimalFormat("###,###,###");
	private static NumberFormat format3 = new DecimalFormat("###,###,#00");
	
	/**
	 * 保留两位小数
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Number parseToDecimal2(String str) throws ParseException{
		Number parse = format1.parse(str);
		return parse;
	}
	
	/**
	 * 保留整数
	 * @param str
	 * @return
	 * @throws ParseException
	 */
	public static Number parseToInteger(String str) throws ParseException{
		Number parse = format2.parse(str);
		return parse;
	}
	
	/**
	 * 保留整数，不足补零
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String formatToDecimal2(long value) throws Exception{
		String format = format3.format(value);
		return format;
	}
}
