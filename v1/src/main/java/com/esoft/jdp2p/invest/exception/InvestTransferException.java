package com.esoft.jdp2p.invest.exception;
/**
 * 债权转让异常，主要用户传递错误信息
 * @author wangxiao
 *
 */
public class InvestTransferException extends Exception{
	private static final long serialVersionUID = 2153842067808461722L;

	public InvestTransferException() {
	}

	public InvestTransferException(String msg) {
		super(msg);
	}
	
	public InvestTransferException(String msg,Throwable e) {
		super(msg,e);
	}
}
