package com.esoft.jdp2p.coupon.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 金额未达到优惠券使用条件
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-7-19 下午3:00:00
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-7-19 wangzhi 1.0
 */
public class UnreachedMoneyLimitException extends Exception {
	public UnreachedMoneyLimitException(String msg) {
		super(msg);
	}

	public UnreachedMoneyLimitException(String msg, Throwable e) {
		super(msg, e);
	}

	public UnreachedMoneyLimitException() {
	}
}
