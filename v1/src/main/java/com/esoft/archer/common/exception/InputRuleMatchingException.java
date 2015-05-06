package com.esoft.archer.common.exception;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:输入规则匹配验证，如果验证出错，则抛此异常。
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-10 上午11:49:35
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-10 wangzhi 1.0
 */
public class InputRuleMatchingException extends Exception {
	
	public InputRuleMatchingException(String msg, Throwable e) {
		super(msg, e);
	}
	
	public InputRuleMatchingException(String msg) {
		super(msg);
	}
}
