package com.esoft.jdp2p.repay.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 逾期还款异常
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-4-10 下午4:22:20
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-4-10 wangzhi 1.0
 */
public class OverdueRepayException extends RepayException {
	public OverdueRepayException(String msg) {
		super(msg);
	}

	public OverdueRepayException(String msg, Throwable e) {
		super(msg, e);
	}
}
