package com.esoft.archer.system.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import com.esoft.archer.system.service.SpringSecurityService;
import com.esoft.archer.user.service.impl.MyJdbcUserDetailsManager;
import com.esoft.core.annotations.Logger;

@Service(value = "springSecurityService")
public class SpringSecurityServiceImpl implements SpringSecurityService {

	@Autowired
	SessionRegistry sessionRegistry;

	/**
	 * 需要刷新权限的用户名HashSet
	 */
	private static HashSet<String> unrgas = new HashSet<String>();

	@Resource
	HibernateTemplate ht;
	
	@Logger
	Log log;

	@Resource
	MyJdbcUserDetailsManager myJdbcUserDetailsManager;

	private void addRefreshAuthoritiesByLoginedUsername(String username,
			Collection<? extends GrantedAuthority> authorities) {
		if (log.isDebugEnabled()) {
			log.debug("refresh authorities, username:"+username);			
		}
		List<Object> loggedUsers = sessionRegistry.getAllPrincipals();
		if (log.isDebugEnabled()) {
			log.debug("loggedUsers size:"+loggedUsers.size());			
		}
		for (Object principal : loggedUsers) {
			if (principal instanceof org.springframework.security.core.userdetails.User) {
				org.springframework.security.core.userdetails.User u = (org.springframework.security.core.userdetails.User) principal;
				if (log.isDebugEnabled()) {
					log.debug("loggend username:"+u.getUsername());			
				}
				if (username.equals(u.getUsername())) {
					// List<GrantedAuthority> authorities = new
					// ArrayList<GrantedAuthority>();
					// authorities.addAll(u.getAuthorities());
					// authorities.add(new GrantedAuthorityImpl("INVESTOR"));
					org.springframework.security.core.userdetails.User userN = new org.springframework.security.core.userdetails.User(
							u.getUsername(), u.getPassword(), u.isEnabled(),
							u.isAccountNonExpired(),
							u.isCredentialsNonExpired(),
							u.isAccountNonLocked(), authorities);
					if (!unrgas.contains(username)) {
						unrgas.add(username);
					}
					List<SessionInformation> sessionsInfo = sessionRegistry
							.getAllSessions(principal, false);
					if (log.isDebugEnabled()) {
						log.debug("SessionInformation size:"+sessionsInfo.size());			
					}
					if (null != sessionsInfo && sessionsInfo.size() > 0) {
						for (SessionInformation sessionInformation : sessionsInfo) {
							sessionRegistry
									.removeSessionInformation(sessionInformation
											.getSessionId());
							sessionRegistry.registerNewSession(
									sessionInformation.getSessionId(), userN);
						}
					}
				}
			}
		}
	}

	@Override
	public void refreshLoginUserAuthorities(String userId) {
		List<GrantedAuthority> authorities = myJdbcUserDetailsManager
				.getUserAuthorities(userId);
		// 刷新登录用户权限
		addRefreshAuthoritiesByLoginedUsername(userId, authorities);

	}

	@Override
	public HashSet<String> getUsersNeedRefreshGrantedAuthorities() {
		return unrgas;
	}
}
