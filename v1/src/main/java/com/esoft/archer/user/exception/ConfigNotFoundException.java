package com.esoft.archer.user.exception;

/**
 * 该异常出现意味着在当前系统中找不到该配置
 * @author wanghm
 *
 */
public class ConfigNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 4219463816789581499L;

	public ConfigNotFoundException(String message) {
		super(message);
	}
}
