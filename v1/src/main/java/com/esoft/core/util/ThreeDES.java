package com.esoft.core.util;

/**  
 * Description:   
 * Copyright:Copyright (c)2013 
 * Company:jdp2p 
 * @author:yinjunlu  
 * @version:1.0  
 * Create at:2014-3-13 下午2:10:53  
 *  
 * Modification History:  
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-3-13    yinjunlu             1.0        1.0 Version  
 */

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

/**
 * 加密方法DESede表示是3des加密方式
 * <p>
 * 运算模式CBC,ECB。在CBC模式下使用key,向量iv;在ECB模式下仅使用key。
 * <p>
 * 填充模式NoPadding、PKCS5Padding、SSL3Padding。
 * <p>
 * 语言之间的兼容：<br>
 * 一个是C#采用CBC Mode，PKCS7 Padding,Java采用CBC Mode，PKCS5Padding Padding,<br>
 * 另一个是C#采用ECB Mode，PKCS7 Padding,Java采用ECB Mode，PKCS5Padding Padding,
 * <p>
 * 此段代码使用的CBC模式NoPadding填充方式、用字节零填充，目的是匹配C#语言中CBC模式，zeros填充方式。
 */
public class ThreeDES {

	/*
	 * 加密方法DESede表示是3des加密方式 运算模式CBC,ECB。在CBC模式下使用key,向量iv;在ECB模式下仅使用key。
	 * 填充模式NoPadding、PKCS5Padding、SSL3Padding。 语言之间的兼容： 一个是C#采用CBC Mode，PKCS7
	 * Padding,Java采用CBC Mode，PKCS5Padding Padding, 另一个是C#采用ECB Mode，PKCS7
	 * Padding,Java采用ECB Mode，PKCS5Padding Padding,
	 */

	/**
	 * 3des加密
	 * 
	 * @param str
	 *            被加密的字符串
	 * @param base64Key
	 * @param iv 向量
	 * @param algorithm 加密方法／运算模式／填充模式
	 * @return 加密后密文
	 */
	public static String encrypt(String str, String base64Key, String iv,
			String algorithm) {

		try {
			SecureRandom sr = new SecureRandom();
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			DESedeKeySpec dks = new DESedeKeySpec(base64Key.getBytes("UTF-8"));
			java.security.Key securekey = keyFactory.generateSecret(dks);
			IvParameterSpec ips = new IvParameterSpec(iv.getBytes("UTF-8"));

			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(1, securekey, ips, sr);
			byte[] arry = cipher.doFinal(str.getBytes("UTF-8"));
			return new String(Base64.encodeBase64(arry), "UTF-8");
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 3des解密
	 * 
	 * @param password
	 *            密文
	 *            	 * @param base64Key
	 * @param iv 向量
	 * @param algorithm 加密方法／运算模式／填充模式
	 * @return 解密后密码
	 */
	public static String decrypt(String password, String base64Key, String iv,
			String algorithm) {

		try {
			SecureRandom sr = new SecureRandom();
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance("DESede");
			DESedeKeySpec dks = new DESedeKeySpec(base64Key.getBytes("UTF-8"));
			java.security.Key securekey = keyFactory.generateSecret(dks);
			IvParameterSpec ips = new IvParameterSpec(iv.getBytes("UTF-8"));

			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, securekey, ips, sr);
			byte[] aa = password.getBytes("UTF-8");
			byte[] base = Base64.decodeBase64(aa);
			byte[] arry = cipher.doFinal(base);
			return new String(FormateByte(arry), "UTF-8");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 密码加密时，填充字符串为8的倍数。
	 * <p>
	 * （此方法在模式是CBC模式，填充方式为NoPadding方式的情况下，用字节零填充.）
	 * 
	 * @param str
	 *            密码
	 * @return 加密后的密文
	 */
	public static byte[] FormateData(String str)
			throws UnsupportedEncodingException {

		int yu = str.getBytes("UTF-8").length % 8;
		if (yu != 0) {
			int size = 8 - yu;
			byte[] arr = new byte[str.getBytes("UTF-8").length + size];
			byte[] data = str.getBytes("UTF-8");
			int i = 0;
			for (; i < data.length; i++) {
				arr[i] = data[i];
			}
			for (int j = 0; j < size; j++, i++) {
				arr[i] = new byte[] { 0 }[0];
			}
			return arr;
		}
		return str.getBytes("UTF-8");
	}

	/**
	 * 密码解密时，将填充的字节零去掉！
	 * <p>
	 * (此方法只在模式是CBC模式，填充方式为NoPadding方式，用字节零填充 的情况下使用。)
	 * 
	 * @param arr
	 *            密文字节组
	 * 
	 * @return 密码字节组
	 */
	public static byte[] FormateByte(byte[] arr) {

		int i = 0;
		for (; i < arr.length; i++) {
			if (arr[i] == new Byte("0")) {
				break;
			}
		}
		byte[] result = new byte[i];
		for (int j = 0; j < i; j++) {
			result[j] = arr[j];
		}
		return result;
	}

}
