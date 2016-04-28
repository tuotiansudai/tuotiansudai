package com.esoft.archer.user.service.impl;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.service.BaseService;
import com.esoft.archer.config.ConfigConstants;
import com.esoft.archer.config.model.Config;
import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.openauth.OpenAuthConstants;
import com.esoft.archer.openauth.service.OpenAuthService;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.exception.IllegalLoginIPException;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.model.UserLoginLog;
import com.esoft.archer.user.schedule.EnableUserJob;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.IdGenerator;
import com.esoft.core.util.SpringBeanUtil;
import com.esoft.core.util.StringManager;
import org.apache.commons.lang.StringUtils;
import org.hibernate.ObjectNotFoundException;
import org.quartz.*;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.Date;

@Service("myAuthenticationManager")
public class MyAuthenticationManager extends DaoAuthenticationProvider {

	private BaseService<UserLoginLog> loginLogService;

	private static StringManager sm = StringManager.getManager(UserConstants.Package);

	@Override
	@Resource(name = "jdbcUserDetailsManager")
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		super.setUserDetailsService(userDetailsService);
	}

	@Autowired
	private HttpServletRequest request;

	@Resource
	StdScheduler scheduler;

	@Resource
	ConfigService configService;

	// 直接用@resource注入ht会有问题，在初始化ht之前，就会加载此filter，所以注不进来。
	private HibernateTemplate getHt() {
		return (HibernateTemplate) SpringBeanUtil.getBeanByName("ht");

	}

	/**
	 * 验证用户名
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// 是否需要验证码
		Boolean needValidateCode = (Boolean) request.getSession(true).getAttribute(UserConstants.AuthenticationManager.NEED_VALIDATE_CODE);

		// check validate code
		if (needValidateCode != null && needValidateCode) {
			checkValidateCode(request);
		}
		try {
			return super.authenticate(authentication);
		} catch (AuthenticationException ae) {
			// 方法additionalAuthenticationChecks中会捕获此异常并进行异常处理，因此无需再次对此异常进行处理
			if (ae instanceof DisabledException) {
				String lock_time = configService.getConfigValue("user_safe.user_disable_time");
				int seconds = Integer.parseInt(lock_time);
				String messageTemplate = "由于登录失败过多，您的账户将禁用{0}分钟，或与客服联系！";
				request.setAttribute(UserConstants.AuthenticationManager.USER_LOCK, MessageFormat.format(messageTemplate, seconds / 60));
			}
			throw ae;
		}
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		this.setPasswordEncoder(new ShaPasswordEncoder());
		User user = (User) getHt().findByNamedQuery("User.findUserByUsername",
				userDetails.getUsername()).get(0);
		// 判断用户是否在绑定的ip下登录，为空则不限制登录ip
		String bindIPs = user.getBindIP();
		if (StringUtils.isNotEmpty(bindIPs)) {
			String loginIp = FacesUtil.getRequestIp(request);
			if (!bindIPs.contains(loginIp)) {
				throw new IllegalLoginIPException("当前登录IP未绑定！");
			}
		}
		try {
			super.additionalAuthenticationChecks(userDetails, authentication);
			handleLoginSuccess(user, request);
		} catch (AuthenticationException ae) {
			handleLoginFail(user, request);
			throw ae;
		}
	}

	/**
	 * 处理登录成功
	 * 
	 * @param user
	 * @param request
	 */
	private void handleLoginSuccess(User user, HttpServletRequest request) {
		UserService userService = (UserService) SpringBeanUtil.getBeanByName("userService");

		try {
			request.getSession(true).setAttribute(UserConstants.AuthenticationManager.NEED_VALIDATE_CODE, false);
			userService.changeUserStatus(user.getId(), UserConstants.UserStatus.ENABLE);

		} catch (UserNotFoundException e) {
			logger.error(e);
			return;
		}

		String openAuthBidding = request.getParameter("open_auth_bidding_login");

		if (StringUtils.isNotEmpty(openAuthBidding) && openAuthBidding.trim().equals("true")) {
			// 第三方首次登录，绑定已有账号
			Object openId = request.getSession().getAttribute(OpenAuthConstants.OPEN_ID_SESSION_KEY);
			Object openAuthType = request.getSession().getAttribute(OpenAuthConstants.OPEN_AUTH_TYPE_SESSION_KEY);
			Object accessToken = request.getSession().getAttribute(OpenAuthConstants.ACCESS_TOKEN_SESSION_KEY);

			if (openId != null && openAuthType != null && accessToken != null) {
				OpenAuthService oas = null;
				if (OpenAuthConstants.Type.QQ.equals((String) openAuthType)) {
					oas = (OpenAuthService) SpringBeanUtil.getBeanByName("qqOpenAuthService");
				} else if (OpenAuthConstants.Type.SINA_WEIBO.equals((String) openAuthType)) {
					oas = (OpenAuthService) SpringBeanUtil.getBeanByName("sinaWeiboOpenAuthService");
				}
				// 找不到应该抛异常
				if (oas != null) {
					oas.binding(user.getId(), (String) openId, (String) accessToken);
				}
			}
		}

		addUserLoginLog(user, request, UserConstants.UserLoginLog.SUCCESS);
	}

	/**
	 * 处理登录失败
	 * 
	 * @param user
	 * @param request
	 */
	private void handleLoginFail(User user, HttpServletRequest request) {
		// 连续登录失败，达到一定次数，就出验证码
		int loginFailLimit = Integer.parseInt(getHt().get(Config.class, ConfigConstants.UserSafe.LOGIN_FAIL_MAX_TIMES).getValue());
		Integer loginFailTime = user.getLoginFailedTimes();
		loginFailTime = loginFailTime == null ? 1 : loginFailTime + 1;
		user.setLoginFailedTimes(loginFailTime);
		BaseService<User> userService = (BaseService<User>) SpringBeanUtil.getBeanByName("baseService");
		userService.save(user);

		if (loginFailLimit <= loginFailTime && !isContainsAppHeader(request)) {
			request.getSession(true).setAttribute(UserConstants.AuthenticationManager.NEED_VALIDATE_CODE, true);
		}

		handleUserState(user);
		addUserLoginLog(user, request, UserConstants.UserLoginLog.FAIL);
	}

	private boolean isContainsAppHeader(final HttpServletRequest httpServletRequest) {
		return "YES".equalsIgnoreCase(httpServletRequest.getHeader("MobileApp"));
	}

	/**
	 * 处理用户状态，是否锁定用户之类的
	 * 
	 * @param user
	 */
	private void handleUserState(User user) {
		// 同一个用户名，密码一直输入错误，达到一定次数，就锁定账号。
		int passwordFailLimit = Integer.parseInt(getHt().get(Config.class, ConfigConstants.UserSafe.PASSWORD_FAIL_MAX_TIMES).getValue());

		if (user.getLoginFailedTimes() >= passwordFailLimit) {
			// 锁定账号
			user.setStatus(UserConstants.UserStatus.DISABLE);

			BaseService<User> userService = (BaseService<User>) SpringBeanUtil.getBeanByName("baseService");

			userService.save(user);
			// 解锁时间可配(默认30分钟)
			int disableTime = 1800;
			try {
				String disableTimeStr = configService.getConfigValue("user_safe.user_disable_time");

				disableTime = Integer.parseInt(disableTimeStr);
			} catch (ObjectNotFoundException | NumberFormatException e) {
				logger.error(e);
			}

			Date enableDate = DateUtil.addSecond(new Date(), disableTime);
			// 到期自动解锁
			SimpleTrigger trigger;
			try {
				trigger = (SimpleTrigger) scheduler.getTrigger(TriggerKey
						.triggerKey("enable" + user.getId(), "enable_user"));
				if (trigger != null) {
					// 修改之前trigger的时间，重新触发
					trigger.getTriggerBuilder().startAt(enableDate);
					scheduler.resumeTrigger(trigger.getKey());
				} else {
					// 生成新的job和trigger
					JobDetail jobDetail = JobBuilder
							.newJob(EnableUserJob.class)
							.withIdentity("enable " + user.getId(), "enable_user").build();
					jobDetail.getJobDataMap().put(EnableUserJob.USER_ID, user.getId());
					trigger = TriggerBuilder
							.newTrigger()
							.withIdentity(user.getId(), EnableUserJob.USER_ID)
							.forJob(jobDetail)
							.withSchedule(
									SimpleScheduleBuilder.simpleSchedule())
							.startAt(enableDate).build();

					scheduler.scheduleJob(jobDetail, trigger);
				}

			} catch (SchedulerException e) {
				logger.error("用户 " + user.getId() + " 解锁任务创建失败！");
				logger.error(e);
			}
		}
	}

	/**
	 * 添加用户登录记录
	 * 
	 * @param user
	 * @param request
	 * @param isSuccess
	 *            登录是否成功
	 */
	private void addUserLoginLog(User user, HttpServletRequest request,
			String isSuccess) {
		// 记录user登录信息
		UserLoginLog ull = new UserLoginLog();
		ull.setId(IdGenerator.randomUUID());
		ull.setIsSuccess(isSuccess);
		ull.setLoginIp(FacesUtil.getRequestIp(request));
		ull.setLoginTime(new Timestamp(System.currentTimeMillis()));
		ull.setUsername(user.getUsername());
		loginLogService = (BaseService<UserLoginLog>) SpringBeanUtil
				.getBeanByName("baseService");
		loginLogService.save(ull);
	}

	/**
	 * 
	 * <li>比较session中的验证码和用户输入的验证码是否相等</li>
	 * 
	 */
	protected void checkValidateCode(HttpServletRequest request) {
		String sessionValidateCode = obtainSessionValidateCode(request);
		String validateCodeParameter = obtainValidateCodeParameter(request);
		if (StringUtils.isEmpty(validateCodeParameter)
				|| !sessionValidateCode.equalsIgnoreCase(validateCodeParameter)) {
			throw new AuthenticationServiceException(
					sm.getString("verificationCodeError"));
		}
	}

	private String obtainValidateCodeParameter(HttpServletRequest request) {
		return request.getParameter(CommonConstants.CaptchaFlag.CAPTCHA_INPUT);
	}

	protected String obtainSessionValidateCode(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(
				CommonConstants.CaptchaFlag.CAPTCHA_SESSION);
		return null == obj ? "" : obj.toString();
	}

}