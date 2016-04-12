package com.esoft.archer.user.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 登录ip不在绑定ip中
 * @author Administrator
 *
 */
public class IllegalLoginIPException extends AuthenticationException{

	public IllegalLoginIPException(String msg) {
		super(msg);
	}

}
