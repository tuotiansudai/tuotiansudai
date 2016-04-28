package com.esoft.archer.user.exception;

import org.springframework.security.access.AccessDeniedException;

/**
 * 等级积分下限的顺序，与等级序号的顺序，不相匹配 Company: jdp2p <br/>
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
public class MinPointLimitCannotMattchSeqNumException extends Exception {

	public MinPointLimitCannotMattchSeqNumException(String msg, Throwable t) {
		super(msg, t);
	}

	public MinPointLimitCannotMattchSeqNumException(String msg) {
		super(msg);
	}

	public MinPointLimitCannotMattchSeqNumException() {
	}

}
