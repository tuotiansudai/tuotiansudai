package com.esoft.jdp2p.message.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 短信发送出错
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-2-10 下午4:11:37
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-2-10 wangzhi 1.0
 */
public class SmsSendErrorException extends RuntimeException {
	public SmsSendErrorException(String msg, Throwable e) {
		super(msg, e);
	}

	public SmsSendErrorException(String msg) {
		super(msg);
	}
}
