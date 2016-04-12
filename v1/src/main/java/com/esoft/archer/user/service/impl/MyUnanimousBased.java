package com.esoft.archer.user.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.esoft.archer.system.service.SpringSecurityService;
import com.esoft.archer.user.exception.MyAccessDeniedException;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.SpringBeanUtil;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-2-25 下午2:43:25
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-2-25 wangzhi 1.0
 */
public class MyUnanimousBased extends UnanimousBased {

	@Resource
	SpringSecurityService springSecurityService;

	@Logger
	static Log log;

	@Override
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException {
		FilterInvocation fi = (FilterInvocation) object;
		authentication = fresh(authentication, fi.getRequest());
		try {
			super.decide(authentication, object, configAttributes);
		} catch (AccessDeniedException ade) {
			throw new MyAccessDeniedException(ade.getMessage(), authentication,
					object, configAttributes);
		}
	}

	private Authentication fresh(Authentication authentication,
			ServletRequest req) {
		HttpServletRequest request = (HttpServletRequest) req;

		HttpSession session = request.getSession(false);

		if (session != null) {
			SessionRegistry sessionRegistry = (SessionRegistry) SpringBeanUtil
					.getBeanByName("sessionRegistry");
			SessionInformation info = sessionRegistry
					.getSessionInformation(session.getId());

			if (info != null) {
				// Non-expired - update last request date/time
				Object principal = info.getPrincipal();
				if (principal instanceof org.springframework.security.core.userdetails.User) {
					org.springframework.security.core.userdetails.User userRefresh = (org.springframework.security.core.userdetails.User) principal;
					ServletContext sc = session.getServletContext();
					HashSet<String> unrgas = springSecurityService
							.getUsersNeedRefreshGrantedAuthorities();
					if (unrgas.size() > 0) {
						if (log.isDebugEnabled()) {
							log.debug("UsersNeedRefreshGrantedAuthorities:");
							for (String u : unrgas) {
								log.debug(u+"  ");
							}
						}
						HashSet<String> loginedUsernames = new HashSet<String>();

						List<Object> loggedUsers = sessionRegistry
								.getAllPrincipals();
						if (log.isDebugEnabled()) {
							log.debug("loggedUsers size:"+loggedUsers.size());
						}
						for (Object lUser : loggedUsers) {
							if (lUser instanceof org.springframework.security.core.userdetails.User) {
								org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) lUser;
								loginedUsernames.add(u.getUsername());
							}
						}
						if (log.isDebugEnabled()) {
							log.debug("loggedUsers:");
							for (String lu : loginedUsernames) {
								log.debug(lu);
							}
						}
						// 清除已经下线的但需要刷新的username
						for (Iterator iterator = unrgas.iterator(); iterator
								.hasNext();) {
							String unrgs = (String) iterator.next();
							if (!loginedUsernames.contains(unrgs)) {
								iterator.remove();
							}
						}
						if (unrgas.contains(userRefresh.getUsername())) {
							// 如果需要刷新权限的列表中有当前的用户，刷新登录用户权限
							// FIXME：与springSecurityServiceImpl中的功能，相重复，需重构此方法和springSecurityServiceImpl
							MyJdbcUserDetailsManager mdudm = (MyJdbcUserDetailsManager) SpringBeanUtil
									.getBeanByType(MyJdbcUserDetailsManager.class);
							SecurityContextHolder
									.getContext()
									.setAuthentication(
											new UsernamePasswordAuthenticationToken(
													userRefresh,
													userRefresh.getPassword(),
													mdudm.getUserAuthorities(userRefresh
															.getUsername())));
							session.setAttribute(
									HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
									SecurityContextHolder.getContext());
							unrgas.remove(userRefresh.getUsername());
							return SecurityContextHolder.getContext()
									.getAuthentication();
						}
					}
				}
			}
		}
		return authentication;
	}
}
