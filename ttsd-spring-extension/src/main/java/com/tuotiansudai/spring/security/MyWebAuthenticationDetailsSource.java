package com.tuotiansudai.spring.security;

import org.springframework.security.authentication.AuthenticationDetailsSource;

import javax.servlet.http.HttpServletRequest;

public class MyWebAuthenticationDetailsSource implements
		AuthenticationDetailsSource<HttpServletRequest, MyWebAuthenticationDetails> {

	public MyWebAuthenticationDetails buildDetails(HttpServletRequest context) {
		return new MyWebAuthenticationDetails(context);
	}
}
