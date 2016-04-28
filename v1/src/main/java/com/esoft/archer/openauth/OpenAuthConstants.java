package com.esoft.archer.openauth;

public class OpenAuthConstants {

	public static String Package = "com.esoft.archer.openauth";
	
	/**
	 * 获取授权后，放在session中的access_token
	 */
	public static final String ACCESS_TOKEN_SESSION_KEY = "open_auth_access_token";
	/**
	 * 获取授权后，放在session中的open_id
	 */
	public static final String OPEN_ID_SESSION_KEY = "open_auth_open_id";	
	/**
	 * 获取授权后，放在session中的auth_type
	 */
	public static final String OPEN_AUTH_TYPE_SESSION_KEY = "open_auth_type";
	/**
	 * qq昵称
	 */
	public static final String QQNICKNAME = "qq_nickname";

	public static final class Type {
		/**
		 * qq登录
		 */
		public static final String QQ = "qq";
		/**
		 * 新浪微博登录
		 */
		public static final String SINA_WEIBO = "sina_weibo";
	}
}
