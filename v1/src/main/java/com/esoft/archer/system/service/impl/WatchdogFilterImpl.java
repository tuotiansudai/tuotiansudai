package com.esoft.archer.system.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.esoft.archer.config.ConfigConstants;
import com.esoft.archer.config.model.Config;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.system.model.Watchdog;
import com.esoft.archer.system.service.AppLocalFilter;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.model.User;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.SpringBeanUtil;
import com.esoft.core.util.StringManager;

@Service
public class WatchdogFilterImpl implements AppLocalFilter {

	@Logger
	static Log log;
	
	LoginUserInfo loginUserInfo;

	//这里注入会有问题，在初始化ht之前，就会加载此filter，所以注不进来。
	// @Resource
	HibernateTemplate ht;
	private final static StringManager sm = StringManager
			.getManager(ConfigConstants.Package);

	private static int BATCH_INSERT_SIZE = 20;
	private List<Watchdog> list = new ArrayList<Watchdog>();

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		ht = (HibernateTemplate) SpringBeanUtil.getBeanByName("ht");
		loginUserInfo = (LoginUserInfo) SpringBeanUtil.getBeanByName("loginUserInfo");
		Config config = null;
		try {
			config = ht.get(Config.class,
					ConfigConstants.Watchdog.IF_OPEN_WATCHDOG);
		} catch (Exception e) {
			log.error(sm.getString("log.error.configWatchdogNotFound",
					ConfigConstants.Watchdog.IF_OPEN_WATCHDOG, e.toString()));
			e.printStackTrace();
			return;
		}
		String isOpen = ConfigConstants.Watchdog.UN_OPEN_WATCHDOG;
		if (config == null) {
			log.warn(sm.getString("log.notFoundConfig",
					ConfigConstants.Watchdog.IF_OPEN_WATCHDOG));
		} else {
			isOpen = config.getValue();
		}
		if (isOpen.equals(ConfigConstants.Watchdog.UN_OPEN_WATCHDOG)) {
			return;
		}
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String requestURL = httpRequest.getRequestURL().toString();
		if (requestURL.indexOf("/site/themes/") > 0
				|| requestURL.indexOf("/include/") > 0
				|| requestURL.indexOf("javax.faces.resource") > 0
				|| requestURL.indexOf("/upload/") > 0) {
			return;
		}

		Watchdog watchdog = new Watchdog();

		
		User user = null;
		if (loginUserInfo != null) {
			user = ht.get(User.class, loginUserInfo.getLoginUserId());
		}
//		= (User) httpRequest.getSession().getAttribute(
//				UserConstants.SESSION_KEY_LOGIN_USER);
		if (user != null && user.getId() != null) {
			watchdog.setUserId(user.getId());
		} else {
			watchdog.setUserId("");
		}
		watchdog.setUrl(requestURL);
		watchdog.setReallyUrl(requestURL);
		watchdog.setIp(request.getRemoteAddr());
		watchdog.setTime(new Date());
		list.add(watchdog);

		if (list.size() >= BATCH_INSERT_SIZE) {
			ht = (HibernateTemplate) SpringBeanUtil.getBeanByName("ht");
			ht.execute(new HibernateCallback<Boolean>() {
				public Boolean doInHibernate(Session session) {
					for (Watchdog t : list) {
						session.save(t);
					}
					session.flush();
					session.clear();

					return null;
				}
			});
			list.clear();
		}

	}

}
