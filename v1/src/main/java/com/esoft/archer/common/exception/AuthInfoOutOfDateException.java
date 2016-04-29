package com.esoft.archer.common.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:认证码过期异常
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-10 上午11:49:35
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-10 wangzhi 1.0
 */
public class AuthInfoOutOfDateException extends Exception {

	public AuthInfoOutOfDateException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public AuthInfoOutOfDateException(String msg) {
		super(msg);
	}

}
