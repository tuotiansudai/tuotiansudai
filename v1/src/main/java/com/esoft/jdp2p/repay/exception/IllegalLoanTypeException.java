package com.esoft.jdp2p.repay.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 借款类型异常（计息方式、计息节点之间冲突之类的。。。）
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-4-10 下午5:22:31
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-4-10 wangzhi 1.0
 */
public class IllegalLoanTypeException extends RuntimeException {
	public IllegalLoanTypeException(String msg) {
		super(msg);
	}

	public IllegalLoanTypeException(String msg, Throwable e) {
		super(msg, e);
	}
}