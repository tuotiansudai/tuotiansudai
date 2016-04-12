package com.esoft.jdp2p.risk.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 区间不连续异常
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-6-21 下午3:53:45
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-6-21 wangzhi 1.0
 */
public class NotContinuityIntervalException extends Exception {
	public NotContinuityIntervalException(String msg) {
		super(msg);
	}

	public NotContinuityIntervalException() {
	}

	public NotContinuityIntervalException(String msg, Throwable e) {
		super(msg, e);
	}
}
