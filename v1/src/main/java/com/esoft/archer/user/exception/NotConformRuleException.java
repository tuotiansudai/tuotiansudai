package com.esoft.archer.user.exception;

/**
 * 该异常表示规则验证过程中出现不匹配情况
 * @author wanghm
 *
 */
public class NotConformRuleException extends Exception{
	
	public NotConformRuleException(String message) {
		super(message);
	}
}
