package com.esoft.jdp2p.invest.exception;

/**
 * 借款状态非法
 * 
 * @author Administrator
 * 
 */
public class IllegalLoanStatusException extends Exception {
	
	public IllegalLoanStatusException() {
	}

	public IllegalLoanStatusException(String msg) {
		super(msg);
	}

	public IllegalLoanStatusException(String msg, Throwable t) {
		super(msg, t);
	}
}
