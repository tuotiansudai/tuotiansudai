/**
 * 
 */
package com.esoft.core.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author yinjunlu
 * 参数编码和解码
 */
public class ParamHelper {

	private static final String TEXT_ENCODING = "utf-8";

	/**
	 * 对参数内容进行编码
	 * @param source 原始字符串
	 * @return 编码结果
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeParameter(final String source) throws UnsupportedEncodingException{
		if(source == null || source.equals("")){
			return "";
		}
		
		return URLEncoder.encode((new sun.misc.BASE64Encoder()).encode(source.getBytes(TEXT_ENCODING)), TEXT_ENCODING);
	}
	
	/**
	 * 对参数内容捷星解码
	 * @param source 编码后的字符串
	 * @return 解码内容
	 * @throws IOException
	 */
	public static String decodeParameter(final String source) throws IOException{
		if(source == null || source.equals("")){
			return "";
		}
		
		return new String((new sun.misc.BASE64Decoder()).decodeBuffer(URLDecoder.decode(source, TEXT_ENCODING)), TEXT_ENCODING);
	}
}
