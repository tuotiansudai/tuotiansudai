package com.esoft.jdp2p.invest.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 投资竞标利率大于借款可接受的最大利率
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-22 下午3:53:05
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-22 wangzhi 1.0
 */
public class ExceedMaxAcceptableRate extends Exception {
	public ExceedMaxAcceptableRate() {
	}

	public ExceedMaxAcceptableRate(String msg) {
		super(msg);
	}
	
	public ExceedMaxAcceptableRate(String msg,Throwable e) {
		super(msg,e);
	}

}
