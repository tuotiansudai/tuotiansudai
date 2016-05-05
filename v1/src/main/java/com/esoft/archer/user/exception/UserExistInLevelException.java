package com.esoft.archer.user.exception;


/**
 * 有用户处于该等级，不能删除
 *  Company: jdp2p <br/>
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
public class UserExistInLevelException extends Exception {

	public UserExistInLevelException(String msg, Throwable t) {
		super(msg, t);
	}

	public UserExistInLevelException(String msg) {
		super(msg);
	}

	public UserExistInLevelException() {
	}

}
