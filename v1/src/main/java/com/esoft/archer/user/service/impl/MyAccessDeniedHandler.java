package com.esoft.archer.user.service.impl;

/**  
 * Company:     jdp2p <br/> 
 * Copyright:   Copyright (c)2013 <br/>
 * Description:  
 * @author:     wangzhi  
 * @version:    1.0
 * Create at:   2014-2-25 下午2:10:36  
 *  
 * Modification History: <br/>
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-2-25      wangzhi      1.0          
 */
import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.esoft.archer.user.exception.MyAccessDeniedException;

public class MyAccessDeniedHandler implements AccessDeniedHandler {
	private String accessDeniedUrl;

	public MyAccessDeniedHandler() {
	}

	public MyAccessDeniedHandler(String accessDeniedUrl) {
		this.accessDeniedUrl = accessDeniedUrl;
	}

	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException,
			ServletException {

		MyAccessDeniedException made = (MyAccessDeniedException) accessDeniedException;
		// TODO:可以自定义如下跳转。
		// admin
		// 没有投资权限
		// 未激活
		// 没有借款权限
		// 等等。。。需要能配置
		// 如果都找不到，返回默认403页面

		// 登录用户已有权限
		Authentication authentication = made.getAuthentication();
		// 请求url
//		made.getObject();
		// spring el 权限表达式
//		Collection<ConfigAttribute> configAttributes = made
//				.getConfigAttributes();

//		Object userObj = authentication.getPrincipal();
//		if (userObj instanceof org.springframework.security.core.userdetails.User) {
//			org.springframework.security.core.userdetails.User user = (User) userObj;
//			Collection<GrantedAuthority> authorities = user.getAuthorities();
			Collection<GrantedAuthority> authorities = authentication.getAuthorities();
			if (authorities.contains(new GrantedAuthorityImpl("INACTIVE"))) {
				// 用户未激活
				// FIXME:用户激活url  页面内加上注销链接     
//				response.sendRedirect(request.getContextPath()+"/用户激活url");
				response.sendRedirect(request.getContextPath()+"/re_active");
			} else {
				response.sendRedirect(accessDeniedUrl);
			}
//		}

		// for (ConfigAttribute ca : configAttributes) {
		// ca.getAttribute();
		// }

	}

	public String getAccessDeniedUrl() {
		return accessDeniedUrl;
	}

	public void setAccessDeniedUrl(String accessDeniedUrl) {
		this.accessDeniedUrl = accessDeniedUrl;
	}
}