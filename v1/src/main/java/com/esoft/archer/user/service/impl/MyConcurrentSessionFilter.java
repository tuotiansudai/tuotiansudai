package com.esoft.archer.user.service.impl;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import com.esoft.archer.common.service.BaseService;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserLoginLog;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.SpringBeanUtil;

public class MyConcurrentSessionFilter extends GenericFilterBean {
	// ~ Instance fields
	// ================================================================================================

	private SessionRegistry sessionRegistry;
	private String expiredUrl;
	private LogoutHandler[] handlers = new LogoutHandler[] { new SecurityContextLogoutHandler() };
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	// ~ Methods
	// ========================================================================================================

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(sessionRegistry, "SessionRegistry required");
		Assert.isTrue(
				expiredUrl == null || UrlUtils.isValidRedirectUrl(expiredUrl),
				expiredUrl + " isn't a valid redirect URL");
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		HttpSession session = request.getSession(false);

		if (session != null) {
			SessionInformation info = sessionRegistry
					.getSessionInformation(session.getId());

			if (info != null) {
				if (info.isExpired()) {
					// Expired - abort processing
					doLogout(request, response);

					String targetUrl = determineExpiredUrl(request, info);

					if (targetUrl != null) {
						redirectStrategy.sendRedirect(request, response,
								targetUrl);

						return;
					} else {
						response.getWriter()
								.print("This session has been expired (possibly due to multiple concurrent "
										+ "logins being attempted as the same user).");
						response.flushBuffer();
					}

					return;
				} else {
					// Non-expired - update last request date/time
					Object principal = info.getPrincipal();
					if (principal instanceof org.springframework.security.core.userdetails.User) {
						org.springframework.security.core.userdetails.User userRefresh = (org.springframework.security.core.userdetails.User) principal;
						ServletContext sc = session.getServletContext();
						Object o = sc
								.getAttribute("usersNeedRefreashGrantedAuthorities");
						if (o != null) {
							SessionRegistry sessionRegistry = (SessionRegistry) SpringBeanUtil
									.getBeanByName("sessionRegistry");
							HashSet<String> unrgas = (HashSet<String>) o;
							if (unrgas.size() > 0) {
								HashSet<String> loginedUsernames = new HashSet<String>();

								List<Object> loggedUsers = sessionRegistry
										.getAllPrincipals();
								for (Object lUser : loggedUsers) {
									if (lUser instanceof org.springframework.security.core.userdetails.User) {
										org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) lUser;
										loginedUsernames.add(u.getUsername());
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
									SecurityContextHolder
											.getContext()
											.setAuthentication(
													new UsernamePasswordAuthenticationToken(
															userRefresh,
															userRefresh
																	.getPassword(),
															userRefresh
																	.getAuthorities()));
									unrgas.remove(userRefresh.getUsername());
								}
								sc.setAttribute(
										"usersNeedRefreashGrantedAuthorities",
										unrgas);
							}
						}
					}
					info.refreshLastRequest();
				}
			}
		}

		chain.doFilter(request, response);
	}

	protected String determineExpiredUrl(HttpServletRequest request,
			SessionInformation info) {
		return expiredUrl;
	}

	private void doLogout(HttpServletRequest request,
			HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		for (int i = 0; i < handlers.length; i++) {
			handlers[i].logout(request, response, auth);
		}
	}

	public void setExpiredUrl(String expiredUrl) {
		this.expiredUrl = expiredUrl;
	}

	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	public void setLogoutHandlers(LogoutHandler[] handlers) {
		Assert.notNull(handlers);
		this.handlers = handlers;
	}

	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}
}