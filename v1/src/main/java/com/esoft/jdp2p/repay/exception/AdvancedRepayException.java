package com.esoft.jdp2p.repay.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 提前还款异常
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-4-10 下午4:22:20
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-4-10 wangzhi 1.0
 */
public class AdvancedRepayException extends RepayException {
	public AdvancedRepayException(String msg) {
		super(msg);
	}

	public AdvancedRepayException(String msg, Throwable e) {
		super(msg, e);
	}
}
