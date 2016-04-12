package com.esoft.archer.user.service.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

import com.esoft.archer.theme.ThemeConstants;
import com.esoft.archer.user.model.Permission;
import com.esoft.core.util.SpringBeanUtil;
import com.ocpsoft.pretty.MyPrettyFilter;
import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.faces.config.PrettyConfig;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;
import com.ocpsoft.pretty.faces.config.reload.PrettyConfigReloader;
import com.ocpsoft.pretty.faces.url.URL;

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
@Service("urlMappingPermissionVoter")
public class UrlMappingPermissionVoter implements AccessDecisionVoter {

	@Resource
	private HibernateTemplate ht;

	private PrettyConfigReloader reloader = new PrettyConfigReloader();

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	public PrettyConfig getConfig(HttpServletRequest hreq) {
		//FIXME:目前放在application中，无法做到用户个性化主题
		PrettyConfig pc = (PrettyConfig) hreq.getSession(true).getServletContext().getAttribute(
				MyPrettyFilter.CONFIG_KEY);
//		PrettyConfig pc = (PrettyConfig) hreq.getSession(true).getAttribute(
//				PrettyContext.CONFIG_KEY);
		if (pc == null) {
			if (!PrettyContext.isInstantiated(hreq)) {
				reloader.onNewRequest(hreq.getSession().getServletContext());
			}
			pc = (PrettyConfig) hreq.getSession().getServletContext()
					.getAttribute(PrettyContext.CONFIG_KEY);
			pc = handleUserConfig(pc, hreq);
			hreq.getSession(true).getServletContext().setAttribute(MyPrettyFilter.CONFIG_KEY, pc);
//			hreq.getSession(true).setAttribute(PrettyContext.CONFIG_KEY, pc);
		}
		return pc;
	}

	/**
	 * 获取用户独有的prettyConfig
	 * 
	 * @return
	 */
	private PrettyConfig handleUserConfig(PrettyConfig pc,
			HttpServletRequest hreq) {
		String userTheme = (String) hreq.getSession(true).getAttribute(
				MyPrettyFilter.USER_THEME);
		if (userTheme == null) {
			// 如果用户的主题不存在，取默认主题
			userTheme = ThemeConstants.DEFAULT_USER_THEME;
			hreq.getSession()
					.setAttribute(MyPrettyFilter.USER_THEME, userTheme);
		}
		// 从数据库中查询所有的urlMapping，然后产生PerttyConfig对象。
		List<UrlMapping> mappings = new LinkedList<UrlMapping>();
		ht = (HibernateTemplate) SpringBeanUtil.getBeanByName("ht");
		List<com.esoft.archer.urlmapping.model.UrlMapping> dbMappings = ht
				.loadAll(com.esoft.archer.urlmapping.model.UrlMapping.class);
		for (com.esoft.archer.urlmapping.model.UrlMapping urlMapping : dbMappings) {
			UrlMapping um = new UrlMapping();
			String viewId = urlMapping.getViewId();
			// 替换当前主题应有的字符串
			if (viewId.startsWith("themepath:")) {
				viewId = viewId.replaceFirst("themepath:", "/site/themes/"
						+ userTheme + "/templates/");
			}
			um.setId(urlMapping.getId());
			um.setPattern(urlMapping.getPattern());
			um.setViewId(viewId);
			mappings.add(um);
		}
		LinkedList<UrlMapping> ums = new LinkedList<UrlMapping>();
		for (UrlMapping urlMapping : pc.getMappings()) {
			String viewId = urlMapping.getViewId();
			if (viewId.startsWith("themepath:")) {
				viewId = viewId.replaceFirst("themepath:", "/site/themes/"
						+ userTheme + "/templates/");
				urlMapping.setViewId(viewId);
			}
		}
		ums.addAll(pc.getMappings());
		ums.addAll(mappings);

		PrettyConfig newPc = new PrettyConfig();
		newPc.setGlobalRewriteRules(pc.getGlobalRewriteRules());
		newPc.setDynaviewId(pc.getDynaviewId());
		newPc.setUseEncodeUrlForRedirects(pc.isUseEncodeUrlForRedirects());
		newPc.setMappings(ums);
		return newPc;
	}

	@Override
	public int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
		FilterInvocation fi = (FilterInvocation) object;
		UrlMapping mapping = getConfig(fi.getRequest()).getMappingForUrl(
				new URL(fi.getRequest().getServletPath()));
		if (mapping != null) {
			com.esoft.archer.urlmapping.model.UrlMapping um = ht.get(
					com.esoft.archer.urlmapping.model.UrlMapping.class,
					mapping.getId());
			if (um != null) {
				// List<Permission> permissions = um.getPermissions();
				List<Permission> permissions = ht
						.findByNamedQueryAndNamedParam(
								"Permission.findPermissionsByUrlMappingId",
								"urlMappingId", um.getId());
				if (permissions.size() == 0) {
					return ACCESS_GRANTED;
				}
				Collection<GrantedAuthority> grantedAs = authentication
						.getAuthorities();
				for (Permission pms : permissions) {
					for (GrantedAuthority ga : grantedAs) {
						if (ga.getAuthority().equals(pms.getId())) {
							return ACCESS_GRANTED;
						}
					}
				}
				return ACCESS_DENIED;
			} else {
				return ACCESS_GRANTED;
			}
		}
		return ACCESS_ABSTAIN;
	}

}
