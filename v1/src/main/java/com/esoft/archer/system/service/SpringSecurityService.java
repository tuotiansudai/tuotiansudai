package com.esoft.archer.system.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

//FIXME:这里应该变成SpringSecurityBO
public interface SpringSecurityService {
	/**
	 * 需要刷新的用户名HashSet<String>在application中的标识
	 */
//	public static final String USERS_NEED_REFREASH_GRANTED_AUTHORITIES = "usersNeedRefreashGrantedAuthorities";
	
	/**
	 * 通过用户名，为已登录的用户重新赋予权限，并在通过RefreshGrantedAuthoritiesFilterImpl来刷新已登录用户的权限。
	 * @param username
	 * @param authorities
	 */
//	public void addRefreshAuthoritiesByLoginedUsername(String username,Collection<? extends GrantedAuthority> authorities);
	
	public void refreshLoginUserAuthorities(String userId);
	
	/**
	 * 获取需要刷新权限的用户名HashSet
	 * @return
	 */
	public HashSet<String> getUsersNeedRefreshGrantedAuthorities();
}
