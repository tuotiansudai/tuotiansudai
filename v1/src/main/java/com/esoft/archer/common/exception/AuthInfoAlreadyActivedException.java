package com.esoft.archer.common.exception;

/**
 * 认证信息已经被激活
 * @author Administrator
 *
 */
public class AuthInfoAlreadyActivedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2817975701999806021L;

	public AuthInfoAlreadyActivedException(String msg, Throwable e) {
		super(msg, e);
	}

	public AuthInfoAlreadyActivedException(String msg) {
		super(msg);
	}
}
