package com.esoft.archer.user.exception;

/**
 * 该异常出现意味着在当前系统中找到该配置项，
 *
 */
public class UserFoundException extends Exception{
	public UserFoundException(String message) {
		super(message);
	}
}
