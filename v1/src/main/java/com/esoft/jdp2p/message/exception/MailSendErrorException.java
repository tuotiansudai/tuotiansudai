package com.esoft.jdp2p.message.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 邮件发送出错
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-2-10 下午4:11:37
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-2-10 wangzhi 1.0
 */
public class MailSendErrorException extends RuntimeException {
	public MailSendErrorException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public MailSendErrorException(Throwable e) {
		super(e);
	}

	public MailSendErrorException(String msg) {
		super(msg);
	}
}
