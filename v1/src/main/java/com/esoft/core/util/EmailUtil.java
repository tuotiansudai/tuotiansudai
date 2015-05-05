package com.esoft.core.util;

import javax.faces.bean.ManagedBean;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-6-13 下午8:41:45
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-6-13 wangzhi 1.0
 */
@ManagedBean
public class EmailUtil {
	/**
	 * 通过email，获取邮箱登录地址
	 * 
	 * @param email
	 * @return
	 */
	public static String getUrlByEmail(String email) {
		int offect = email.indexOf("@");
		String suffix = email.substring(offect + 1, email.length());
		String emailLoginUrl;
		if (suffix.indexOf("gmail") > -1) {
			emailLoginUrl = "http://mail.google.com";

		} else if (suffix.indexOf("hotmail") > -1) {
			emailLoginUrl = "http://login.live.com";
		} else {
			emailLoginUrl = "http://mail." + suffix;
		}
		return emailLoginUrl;
	}
}
