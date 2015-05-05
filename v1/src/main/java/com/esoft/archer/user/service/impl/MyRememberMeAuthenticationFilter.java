package com.esoft.archer.user.service.impl;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.stereotype.Service;

import com.esoft.archer.common.service.BaseService;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserLoginLog;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.SpringBeanUtil;

@Service("rememberMeAuthenticationFilter")
public class MyRememberMeAuthenticationFilter extends
		RememberMeAuthenticationFilter {

	private HibernateTemplate getHt() {
		return (HibernateTemplate) SpringBeanUtil.getBeanByName("ht");
	}

	@Override
	protected void onSuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult) {
		String username = authResult.getName();
		List<User> users = getHt().findByNamedQuery("User.findUserByUsername",
				username);
		if (users.size() != 0) {
			User user = users.get(0);
			// 放入session
//			request.getSession(true).setAttribute(
//					UserConstants.SESSION_KEY_LOGIN_USER, user);
			// 记录user登录信息
			UserLoginLog ull = new UserLoginLog();
			ull.setId(IdGenerator.randomUUID());
			ull.setIsSuccess(UserConstants.UserLoginLog.SUCCESS);
			ull.setLoginIp(FacesUtil.getRequestIp(request));
			ull.setLoginTime(new Timestamp(System.currentTimeMillis()));
			ull.setUsername(user.getUsername());
			BaseService<UserLoginLog> loginLogService = (BaseService<UserLoginLog>) SpringBeanUtil
					.getBeanByName("baseService");
			loginLogService.save(ull);
		}
	}
}