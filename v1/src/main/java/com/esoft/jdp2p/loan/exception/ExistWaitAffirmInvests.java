package com.esoft.jdp2p.loan.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 存在等待第三方资金托管确认的投资
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-21 下午3:45:50
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-21 wangzhi 1.0
 */
public class ExistWaitAffirmInvests extends Exception {
	public ExistWaitAffirmInvests(String msg) {
		super(msg);
	}

	public ExistWaitAffirmInvests(String msg, Throwable e) {
		super(msg, e);
	}
}
