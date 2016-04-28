package com.esoft.archer.user.service.impl;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import com.esoft.archer.menu.model.Menu;
import com.esoft.archer.user.model.Permission;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 菜单权限投票器，查看当前用户是否有访问该菜单的权限
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-3-13 下午3:09:16
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-3-13 wangzhi 1.0
 */
@Service("menuPermissionVoter")
public class MenuPermissionVoter implements AccessDecisionVoter {

	@Resource
	private HibernateTemplate ht;

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
		FilterInvocation fi = (FilterInvocation) object;
		Collection<GrantedAuthority> grantedAs = authentication
				.getAuthorities();
		List<Menu> menus = ht.findByNamedQueryAndNamedParam(
				"Menu.getMenuByUrl", "url", fi.getRequestUrl());
		if (menus.size() == 0) {
			menus = ht.findByNamedQueryAndNamedParam("Menu.getMenuByUrl", "url", fi.getRequest().getServletPath());
		}
		// 查询当前用户是否拥有访问该菜单的权限。
		if (menus.size() > 0) {
			List<Permission> permissions = menus.get(0).getPermissions();
			if (permissions.size()==0) {
				return ACCESS_ABSTAIN;
			}
			for (Permission pms : permissions) {
				for (GrantedAuthority ga : grantedAs) {
					if (ga.getAuthority().equals(pms.getId())) {
						return ACCESS_GRANTED;
					}
				}
			}
			return ACCESS_DENIED;
		}
		return ACCESS_ABSTAIN;
	}

}
