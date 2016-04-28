package com.esoft.core.dbcp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.esoft.core.util.ThreeDES;

/**
 * dbcp连接池配置信息，加密数据库连接密码
 * 
 * @author Administrator
 * 
 */
public class MyBasicDataSource extends BasicDataSource {

	static Log log = LogFactory.getLog(MyBasicDataSource.class);

	private static Properties props;
	/**
	 * 3des base64 key
	 */
	public static String THREE_DES_BASE64_KEY;
	/**
	 * 3des iv(向量)
	 */
	public static String THREE_DES_IV;
	/**
	 * 3des 加密方法／运算模式／填充模式
	 */
	public static String THREE_DES_ALGORITHM;
	/** 数据库配置信息是否加密 */
	private static boolean isEncrypt = false;

	static {
		props = new Properties();
		try {
			props.load(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("dbprop"));
			THREE_DES_BASE64_KEY = props.getProperty("three_des_base64_key");
			THREE_DES_IV = props.getProperty("three_des_iv");
			THREE_DES_ALGORITHM = props.getProperty("three_des_algorithm");

			String tmpStr = props.getProperty("is_encrypt");
			if (StringUtils.isNotEmpty(tmpStr)) {
				isEncrypt = Boolean.parseBoolean(tmpStr);
			}
		} catch (FileNotFoundException e) {
			log.info("找不到dbprop文件", e);
		} catch (IOException e) {
			log.info("读取dbprop文件出错", e);
		} catch (NullPointerException e) {
			log.info("读取dbprop文件出错", e);
		}
	}

	@Override
	public synchronized void setUrl(String url) {
		if (isEncrypt) {
			url = ThreeDES.decrypt(url, THREE_DES_BASE64_KEY, THREE_DES_IV,
					THREE_DES_ALGORITHM);
		}
		super.setUrl(url);
	}

	@Override
	public synchronized void setUsername(String username) {
		if (isEncrypt) {
			username = ThreeDES.decrypt(username, THREE_DES_BASE64_KEY,
					THREE_DES_IV, THREE_DES_ALGORITHM);
		}
		super.setUsername(username);
	}

	@Override
	public synchronized void setPassword(String password) {
		if (isEncrypt) {
			password = ThreeDES.decrypt(password, THREE_DES_BASE64_KEY,
					THREE_DES_IV, THREE_DES_ALGORITHM);
		}
		super.setPassword(password);
	}

	public boolean isEncrypt() {
		return isEncrypt;
	}

	public void setEncrypt(boolean isEncrypt) {
		this.isEncrypt = isEncrypt;
	}

}
