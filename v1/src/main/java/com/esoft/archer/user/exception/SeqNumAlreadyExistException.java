package com.esoft.archer.user.exception;


/**
 * 等级序号已存在 Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-2-25 下午2:45:42
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-2-25 wangzhi 1.0
 */
public class SeqNumAlreadyExistException extends Exception {

	public SeqNumAlreadyExistException(String msg, Throwable t) {
		super(msg, t);
	}

	public SeqNumAlreadyExistException(String msg) {
		super(msg);
	}

	public SeqNumAlreadyExistException() {
	}

}
