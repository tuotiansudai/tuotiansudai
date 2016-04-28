package com.esoft.archer.user.exception;

/**
 * 该异常出现意味着在当前系统中找不到该角色
 * @author wanghm
 *
 */
public class RoleNotFoundException extends Exception{
	public RoleNotFoundException(String message) {
		super(message);
	}
}
