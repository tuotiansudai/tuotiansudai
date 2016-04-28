package com.esoft.core.util;
import java.security.MessageDigest;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
/** 
 * @ClassName: Base64Coder 
 * @Description: TODO Base64加密解密
 * @author 李哲
 * @date 2015-1-21 下午12:00:21 
 */
public class Base64Coder {
	public static final String KEY_SHA="SHA";
	public static final String KEY_MD5="MD5";
	/**
	 * base64解密
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptBase64(String key) throws Exception{
		int len,n=1,m=0;
		len=key.length();
		String strs1=key.substring(0, len/2);
		String strs2=key.substring(len/2);
		key="";
		for(int i=0;i<len;i++){
			if(i==0){
				key+=strs1.charAt(0);
			}else if(i==(len-1)){
				key+=strs2.charAt(len-(len/2+1));
			}else if(i%2==0){
				key+=strs1.charAt(n++);
			}else{
				key+=strs2.charAt(m++);
			}
		}
		//System.out.println(key);
		return (new BASE64Decoder()).decodeBuffer(key);
	}
	/**
	 * base64加密
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptBase64(byte[] key)throws Exception{
		return (new BASE64Encoder()).encodeBuffer(key);
	}
	/**
	 * MD5加密
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptMD5(byte[] key) throws Exception{
		MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
		md5.update(key);
		return md5.digest();
	}
	/**
	 * SHA加密
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptSHA(byte[] key) throws Exception{
		MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
		sha.update(key);
		return sha.digest();
	}
}
