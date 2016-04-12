package com.esoft.jdp2p.trusteeship.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 第三方资金托管时候,返回的错误信息
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-4-4 上午10:57:48
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-4-4 wangzhi 1.0
 */
public class TrusteeshipReturnException extends Exception {
	public TrusteeshipReturnException(Throwable e, String msg) {
		super(msg, e);
	}

	public TrusteeshipReturnException(String msg) {
		super(msg);
	}
}
