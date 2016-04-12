package com.esoft.jdp2p.loan.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 募集到的资金太少，为0、或者不足以支付借款保证金；
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-21 下午3:45:50
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-21 wangzhi 1.0
 */
public class BorrowedMoneyTooLittle extends Exception {
	public BorrowedMoneyTooLittle(String msg) {
		super(msg);
	}

	public BorrowedMoneyTooLittle(String msg, Throwable e) {
		super(msg, e);
	}

	public BorrowedMoneyTooLittle() {
	}
}
