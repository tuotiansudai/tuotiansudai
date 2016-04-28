package com.esoft.jdp2p.coupon.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 优惠券超过有效期
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-7-19 下午2:47:07
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-7-19 wangzhi 1.0
 */
public class ExceedDeadlineException extends Exception {
	public ExceedDeadlineException(String msg) {
		super(msg);
	}

	public ExceedDeadlineException(String msg, Throwable e) {
		super(msg, e);
	}

	public ExceedDeadlineException() {
	}
}
