package com.esoft.jdp2p.invest.exception;
/**
 * 债权转让的金额大于or等于or小于债权本身的价值
 * @author wangxiao
 *
 */
public class ExceedInvestTransferMoney extends Exception{
	private static final long serialVersionUID = 2153842067808401722L;

	public ExceedInvestTransferMoney() {
	}

	public ExceedInvestTransferMoney(String msg) {
		super(msg);
	}
	
	public ExceedInvestTransferMoney(String msg,Throwable e) {
		super(msg,e);
	}
}
