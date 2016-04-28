package com.esoft.archer.openauth.service.abs;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.openauth.OpenAuthConstants;
import com.esoft.archer.openauth.model.OpenAuth;
import com.esoft.archer.openauth.model.OpenAuthType;
import com.esoft.archer.openauth.service.OpenAuthService;
import com.esoft.archer.user.model.User;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-8 上午10:01:46
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-8 wangzhi 1.0
 */
public abstract class OpenAuthServiceAbstract implements OpenAuthService {

	@Resource
	HibernateTemplate ht;

	@Resource
	UserDetailsService userDetailsService;
	
	@Autowired
	SessionRegistry sessionRegistry;

	public void login(String openId, String openAuthTypeId, HttpSession session) {
		List<OpenAuth> oas = ht.find(
				"from OpenAuth oa where oa.openId=? and oa.type.id=?",
				new String[] { openId, openAuthTypeId });
		if (oas.size() > 0) {
			User user = oas.get(0).getUser();
			// FIXME：用户不是激活状态，需要抛异常。
			if (user.getStatus().equals("1")) {
				UserDetails userDetails = userDetailsService
						.loadUserByUsername(user.getUsername());
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
						user.getUsername(), userDetails.getPassword(),
						userDetails.getAuthorities());

				// Need to set this as thread locale as available throughout
				SecurityContextHolder.getContext().setAuthentication(token);

				// Set SPRING_SECURITY_CONTEXT attribute in session as Spring
				// identifies
				// context through this attribute
				session.setAttribute(
						HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
						SecurityContextHolder.getContext());
				sessionRegistry.registerNewSession(session.getId(), userDetails);

			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void binding(String userId, String accessToken, String openId,
			String openAuthTypeId) {
		OpenAuth oa = ht.get(OpenAuth.class, userId + "_" + openAuthTypeId);
		if (oa == null) {
			oa = new OpenAuth();
			oa.setId(userId + "_" + openAuthTypeId);
			oa.setType(new OpenAuthType(openAuthTypeId));
			oa.setUser(new User(userId));
		}
		oa.setAccessToken(accessToken);
		oa.setOpenId(openId);
		ht.saveOrUpdate(oa);
	}

	public boolean isBinded(String openId, String openAuthTypeId) {
		List<OpenAuth> oas = ht.find(
				"from OpenAuth oa where oa.openId=? and oa.type.id=?",
				new String[] { openId, openAuthTypeId });
		if (oas.size() > 0) {
			return true;
		}
		return false;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void refreshAccessToken(String openId, String accessToken,
			String openAuthTypeId) {
		List<OpenAuth> oas = ht.find(
				"from OpenAuth oa where oa.openId=? and oa.type.id=?",
				new String[] { openId, openAuthTypeId });
		if (oas.size() > 0) {
			OpenAuth oa = oas.get(0);
			oa.setAccessToken(accessToken);
			ht.update(oa);
		}
	}

	public void unbinding(String userId, String openAuthTypeId) {
		String hql = "delete from OpenAuth oa where oa.id = ?";
		ht.bulkUpdate(hql, userId + "_" + openAuthTypeId);
	}

}
