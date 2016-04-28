package com.esoft.archer.user.service.impl;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.exception.AuthInfoAlreadyActivedException;
import com.esoft.archer.common.exception.AuthInfoOutOfDateException;
import com.esoft.archer.common.exception.InputRuleMatchingException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.common.model.AuthInfo;
import com.esoft.archer.common.service.AuthService;
import com.esoft.archer.common.service.ValidationService;
import com.esoft.archer.common.service.impl.AuthInfoBO;
import com.esoft.archer.openauth.OpenAuthConstants;
import com.esoft.archer.openauth.service.OpenAuthService;
import com.esoft.archer.system.service.SpringSecurityService;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.exception.*;
import com.esoft.archer.user.model.ReferrerRelation;
import com.esoft.archer.user.model.Role;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserInfoService;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.HashCrypt;
import com.esoft.jdp2p.message.MessageConstants;
import com.esoft.jdp2p.message.exception.MailSendErrorException;
import com.esoft.jdp2p.message.model.UserMessageTemplate;
import com.esoft.jdp2p.message.service.MessageService;
import com.esoft.jdp2p.message.service.impl.MessageBO;
import com.google.common.base.Strings;
import com.ttsd.redis.RedisClient;
import com.ttsd.util.CommonUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.quartz.impl.StdScheduler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.*;

/**
 * <p>
 * Title: UserServiceImpl.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * <p>
 * Company: jdp2p
 * </p>
 * 
 * @author wangzhi
 * @date 2014-1-3
 * @version 1.0
 */
@Service("userService")
public class UserServiceImpl implements UserService {
	@Logger
	static Log log;
	@Resource
	private HibernateTemplate ht;

	@Resource
	private AuthService authService;

	@Resource
	private UserInfoService userInfoService;

	@Resource
	private UserBO userBO;

	@Resource(name = "qqOpenAuthService")
	private OpenAuthService qqOAS;

	@Resource(name = "sinaWeiboOpenAuthService")
	private OpenAuthService sinaWeiboOAS;

	@Resource
	private MessageBO messageBO;

	@Resource
	private AuthInfoBO authInfoBO;

	@Resource
	private SpringSecurityService springSecurityService;

	@Resource
	private MessageService messageService;

	@Resource
	StdScheduler scheduler;

	@Resource
	private ValidationService validationService;

	@Resource
	RedisClient redisClient;

	@Value("${redis.registerVerifyCode.expireTime}")
	private int registerVerifyCodeExpireTime;

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public User getCrruntUserInfo(String username) throws UserNotFoundException {
		List<User> users = ht.find("from User user where user.username=?",
				username);
		if (users.size() == 0) {
			return null;
		} else if (users.size() > 1) {
			throw new DuplicateKeyException("duplicate user.username:"
					+ username);
		}
		return users.get(0);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void registerByMobileNumber(User user, String authCode)
			throws NoMatchingObjectsException, AuthInfoOutOfDateException,
			AuthInfoAlreadyActivedException {
		// 验证手机认证码是否正确
		authService.verifyAuthInfo(null, user.getMobileNumber(), authCode,
				CommonConstants.AuthInfoType.REGISTER_BY_MOBILE_NUMBER);
		user.setRegisterTime(new Date());
		// 用户密码通过sha加密
		user.setPassword(HashCrypt.getDigestHash(user.getPassword()));
		user.setCashPassword(HashCrypt.getDigestHash(user.getCashPassword()));
		user.setStatus(UserConstants.UserStatus.ENABLE);
		userBO.save(user);
		// 添加普通用户权限
		Role role = new Role("MEMBER");
		userBO.addRole(user, role);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void register(User user) {
		// FIXME:缺验证
		user.setRegisterTime(new Date());
		user.setPassword(HashCrypt.getDigestHash(user.getPassword()));
		user.setCashPassword(HashCrypt.getDigestHash(user.getCashPassword()));
		user.setStatus(UserConstants.UserStatus.ENABLE);
		// /////////
		// user.setStatus(UserConstants.UserStatus.NOACTIVE);
		userBO.save(user);
		// 添加普通用户权限
		Role role = new Role("MEMBER");
		// 添加inactive角色（inactive role has inactive permission）
		Role role2 = new Role("INACTIVE");
		userBO.addRole(user, role2);
		// //////////
		userBO.addRole(user, role);
		sendActiveEmail(
				user,
				authService.createAuthInfo(user.getId(), user.getEmail(), null,
						CommonConstants.AuthInfoType.ACTIVATE_USER_BY_EMAIL)
						.getAuthCode(), null);
	}

	private void sendActiveEmail(User user, String authCode, String url) {
		final String email = user.getEmail();
		// 发送账号激活邮件
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", user.getUsername());
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		String activeCode = email + "&" + authCode;
		// base64编码
		activeCode = Base64.encodeBase64URLSafeString(activeCode.getBytes());
		String currentAppUrl = Strings.isNullOrEmpty(url) ? FacesUtil.getCurrentAppUrl() : url;
		String activeLink = currentAppUrl
				+ "/activateAccount?activeCode=" + activeCode;
		params.put("active_url", activeLink);

		messageBO.sendEmailBySendCloud(ht.get(UserMessageTemplate.class,
						MessageConstants.UserMessageNodeId.REGISTER_ACTIVE + "_email"),
				params, email);
	}

	@Override
	public void sendActiveEmail(String userId, String authCode, String url)
			throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("userId:" + userId);
		}
		sendActiveEmail(user, authCode, url);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public User verifyFindLoginPasswordActiveCode(String activeCode)
			throws UserNotFoundException, NoMatchingObjectsException,
			AuthInfoOutOfDateException, AuthInfoAlreadyActivedException {
		// base64解码
		activeCode = new String(Base64.decodeBase64(activeCode));
		String[] aCodes = activeCode.split("&");
		if (aCodes.length != 2) {
			throw new UserNotFoundException("activeCode has error" + activeCode);
		}
		String email = aCodes[0];
		String code = aCodes[1];

		User user = getUserByEmail(email);
		authService.verifyAuthInfo(user.getId(), email, code,
				CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_EMAIL);
		return user;
	}

	// ///////////////////
	/**
	 * 再次发送激活邮件
	 * 
	 * @author wangxiao 5-6
	 * @param userId
	 * @param email
	 */
	@Override
	public void sendActiveEmailAgain(User user) {
		sendActiveEmail(
				user,
				authService.createAuthInfo(user.getId(), user.getEmail(), null,
						CommonConstants.AuthInfoType.ACTIVATE_USER_BY_EMAIL)
						.getAuthCode(), null);
	}

	// ////////////////////

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void activateUserByEmailActiveCode(String activeCode)
			throws UserNotFoundException, NoMatchingObjectsException,
			AuthInfoOutOfDateException, AuthInfoAlreadyActivedException {
		// base64解码
		activeCode = new String(Base64.decodeBase64(activeCode));
		String[] aCodes = activeCode.split("&");
		if (aCodes.length != 2) {
			throw new UserNotFoundException("activeCode has error" + activeCode);
		}
		String email = aCodes[0];
		String code = aCodes[1];

		User user = userBO.getUserByEmail(email);
		if (user == null) {
			throw new UserNotFoundException("user.email:" + email);
		}
		authService.verifyAuthInfo(user.getId(), email, code,
				CommonConstants.AuthInfoType.ACTIVATE_USER_BY_EMAIL);
		userBO.enableUser(user);
		// 去掉inactive角色（inactive role has inactive permission）
		Role role = new Role("INACTIVE");
		userBO.removeRole(user, role);
		// /////////////////
		authInfoBO.activate(user.getId(), email,
				CommonConstants.AuthInfoType.ACTIVATE_USER_BY_EMAIL);
		// 刷新用户权限
		springSecurityService.refreshLoginUserAuthorities(user.getId());
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void disableUser(String userId) throws UserNotFoundException {
		try {
			changeUserStatus(userId, CommonConstants.DISABLE);
		} catch (ConfigNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void enableUser(String userId) throws UserNotFoundException {
		try {
			changeUserStatus(userId, CommonConstants.ENABLE);
		} catch (ConfigNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void changeUserStatus(String userId, String status)
			throws UserNotFoundException, ConfigNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("userId:" + userId);
		}
		user.setStatus(status);
		if (status.equalsIgnoreCase(UserConstants.UserStatus.ENABLE)) {
			user.setLoginFailedTimes(0);
		}
		userBO.update(user);
	}

	@Override
	public boolean verifyPasswordRule(String password)
			throws NotConformRuleException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean verifyUsernameRule(String username)
			throws NotConformRuleException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void addRole(String userId, String roleId)
			throws UserNotFoundException, RoleNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("userId:" + userId);
		}
		Role role = ht.get(Role.class, roleId);
		if (role == null) {
			throw new RoleNotFoundException("roleId:" + roleId);
		}
		userBO.addRole(user, role);
	}

	/**
	 * 修改登录密码 密码通过sha加密
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void modifyPassword(String userId, String newPassword)
			throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("id:" + userId);
		}
		// 用户密码通过sha加密保存
		user.setPassword(HashCrypt.getDigestHash(newPassword));
		userBO.update(user);
	}

	/**
	 * 修改交易密码
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void modifyCashPassword(String userId, String newCashPassword)
			throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("id:" + userId);
		}
		// 密码用sha加密
		user.setCashPassword(HashCrypt.getDigestHash(newCashPassword));
		userBO.update(user);
	}

	@Override
	public void resetPassword(String userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean verifyOldPassword(String userId, String oldPassword)
			throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("id:" + userId);
		}
		// 用户密码是通过加密保存的
		if (user.getPassword().equals(HashCrypt.getDigestHash(oldPassword))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean verifyOldCashPassword(String userId, String oldCashPassword)
			throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("id:" + userId);
		}
		if (user.getCashPassword() != null
				&& user.getCashPassword().equals(
						HashCrypt.getDigestHash(oldCashPassword))) {
			return true;
		}
		return false;
	}

	@Override
	public void sendFindLoginPasswordEmail(String email)
			throws UserNotFoundException {
		User user = userBO.getUserByEmail(email);
		if (user == null) {
			throw new UserNotFoundException("user.email:" + email);
		}
		// 发送登录密码找回邮件
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", user.getUsername());
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		params.put(
				"authCode",
				authService
						.createAuthInfo(
								user.getId(),
								email,
								null,
								CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_EMAIL)
						.getAuthCode());
		messageBO.sendEmailBySendCloud(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.FIND_LOGIN_PASSWORD_BY_EMAIL
						+ "_email"), params, email);
	}

	@Override
	public void sendFindLoginPasswordLinkEmail(String email)
			throws UserNotFoundException {
		User user = userBO.getUserByEmail(email);
		if (user == null) {
			throw new UserNotFoundException("user.email:" + email);
		}
		// 发送登录密码找回邮件
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", user.getUsername());
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		// 生成认证码
		String authCode = authService.createAuthInfo(user.getId(), email, null,
				CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_EMAIL)
				.getAuthCode();
		String activeCode = email + "&" + authCode;
		// base64编码
		activeCode = Base64.encodeBase64URLSafeString(activeCode.getBytes());
		String resetPasswrodUrl = FacesUtil.getCurrentAppUrl()
				+ "/find_pwd_by_email3/" + activeCode;
		params.put("reset_password_url", resetPasswrodUrl);
		// 发送邮件
		messageBO.sendEmailBySendCloud(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.FIND_LOGIN_PASSWORD_BY_EMAIL
						+ "_email"), params, email);
	}

	@Override
	public void sendBindingEmail(String userId, String email)
			throws UserNotFoundException,MailSendErrorException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("user.id:" + userId);
		}
		// 发送绑定邮箱确认邮件
		Map<String, String> params = new HashMap<String, String>();
		// TODO:实现模板
		params.put("username", user.getUsername());
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		params.put(
				"authCode",
				authService.createAuthInfo(userId, email, null,
						CommonConstants.AuthInfoType.BINDING_EMAIL)
						.getAuthCode());
		messageBO.sendEmailBySendCloud(ht.get(UserMessageTemplate.class,
						MessageConstants.UserMessageNodeId.BINDING_EMAIL + "_email"),
				params, email);

	}

	/**
	 * 发送认证码邮件指定邮箱
	 */
	@Override
	public void sendChangeBindingEmail(String userId, String oriEmail)
			throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("user.id:" + userId);
		}
		Map<String, String> params = new HashMap<String, String>();
		// TODO:实现模板
		params.put("username", user.getUsername());
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		params.put(
				"authCode",
				authService.createAuthInfo(userId, oriEmail, null,
						CommonConstants.AuthInfoType.BINDING_EMAIL)
						.getAuthCode());
		messageBO.sendEmailBySendCloud(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.BINDING_EMAIL
						+ "_email"), params, oriEmail);
	}

	/**
	 * 绑定邮箱
	 * 
	 * @param userId
	 *            用户id
	 * @param email
	 *            邮箱
	 * @param authCode
	 *            认证码
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void bindingEmail(String userId, String email, String authCode)
			throws UserNotFoundException, NoMatchingObjectsException,
			AuthInfoOutOfDateException, AuthInfoAlreadyActivedException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("user.id:" + userId);
		}
		// 根据用户id，邮箱，认证码，认证码类型,验证认证码
		authService.verifyAuthInfo(userId, email, authCode,
				CommonConstants.AuthInfoType.BINDING_EMAIL);
		String oldEmail = user.getEmail();
		AuthInfo activateUserByEmailAi = authInfoBO.get(userId, oldEmail, CommonConstants.AuthInfoType.ACTIVATE_USER_BY_EMAIL);
		AuthInfo bingEmailAi = authInfoBO.get(userId,oldEmail,CommonConstants.AuthInfoType.BINDING_EMAIL);
		if (activateUserByEmailAi != null){
			ht.delete(activateUserByEmailAi);
		}
		if (oldEmail != null && !oldEmail.equals(email) && bingEmailAi != null){
				ht.delete(bingEmailAi);
		}
		// 如果认证码输入正确，更改此认证码的状态为已激活
		authInfoBO.activate(userId, email,
				CommonConstants.AuthInfoType.BINDING_EMAIL);
		user.setEmail(email);
		userBO.update(user);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void bindingMobileNumber(String userId, String mobileNumber,
			String authCode) throws UserNotFoundException,
			NoMatchingObjectsException, AuthInfoOutOfDateException,
			AuthInfoAlreadyActivedException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("user.id:" + userId);
		}
		authService.verifyAuthInfo(userId, mobileNumber, authCode,
				CommonConstants.AuthInfoType.BINDING_MOBILE_NUMBER);
		authInfoBO.activate(userId, mobileNumber,
				CommonConstants.AuthInfoType.BINDING_MOBILE_NUMBER);
		user.setMobileNumber(mobileNumber);
		userBO.update(user);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void realNameCertification(User user, String authCode)
			throws NoMatchingObjectsException, AuthInfoOutOfDateException,
			AuthInfoAlreadyActivedException {
		// FIXME:缺验证

		authService.verifyAuthInfo(user.getId(), user.getMobileNumber(),
				authCode, CommonConstants.AuthInfoType.BINDING_MOBILE_NUMBER);
		user.setCashPassword(HashCrypt.getDigestHash(user.getCashPassword()));
		userBO.update(user);
		userBO.addRole(user, new Role("INVESTOR"));

		// 刷新登录用户权限
		springSecurityService.refreshLoginUserAuthorities(user.getId());
	}

	/**
	 * 通过邮箱进行实名认证
	 * 
	 * @throws AuthInfoAlreadyActivedException
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void realNameCertificationByEmail(User user, String authCode)
			throws NoMatchingObjectsException, AuthInfoOutOfDateException,
			AuthInfoAlreadyActivedException {
		// FIXME:缺验证

		authService.verifyAuthInfo(user.getId(), user.getEmail(), authCode,
				CommonConstants.AuthInfoType.BINDING_EMAIL);
		user.setCashPassword(HashCrypt.getDigestHash(user.getCashPassword()));
		userBO.update(user);
		userBO.addRole(user, new Role("INVESTOR"));

		// 刷新登录用户权限
		springSecurityService.refreshLoginUserAuthorities(user.getId());
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void realNameCertification(User user) {
		String trimRN = user.getRealname().trim();
		user.setRealname(trimRN);
		user.setCashPassword(HashCrypt.getDigestHash(user.getCashPassword()));
		String birthday = user.getIdCard().substring(6,10)+"-"+user.getIdCard().substring(10,12)+"-"+user.getIdCard().substring(12,14);
		user.setBirthday(DateUtil.StringToDate(birthday));
		userBO.update(user);
		userBO.addRole(user, new Role("INVESTOR"));

		// 刷新登录用户权限
		springSecurityService.refreshLoginUserAuthorities(user.getId());
	}

	@Override
	public void sendBindingMobileNumberSMS(String userId, String mobileNumber)
			throws UserNotFoundException {
		// FIXME:验证手机号码的合法性
		// 发送手机验证码
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("user.id:" + userId);
		}
		Map<String, String> params = new HashMap<String, String>();
		// TODO:实现模板
		params.put("username", user.getUsername());
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		params.put(
				"authCode",
				authService.createAuthInfo(userId, mobileNumber, null,
						CommonConstants.AuthInfoType.BINDING_MOBILE_NUMBER)
						.getAuthCode());
		messageBO.sendSMS(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.BINDING_MOBILE_NUMBER
						+ "_sms"), params, mobileNumber);
	}

	@Override
	public boolean sendSmsMobileNumber(String mobileNumber, String remoteIp, String authType) {
		if (Strings.isNullOrEmpty(authType)){
			return false;
		}
		String template = "ip={0}|mobileNumber={1}|registerTime={2}";
		// FIXME:验证手机号码的合法性
		// 发送手机验证码
		Map<String, String> params = new HashMap<String, String>();
		// TODO:实现模板
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		params.put(
				"authCode",
				authService.createAuthInfo(null, mobileNumber, null,authType)
						.getAuthCode());
		if(!CommonUtils.isDevEnvironment("environment")){
			if (Strings.isNullOrEmpty(remoteIp)){
				HttpServletRequest request =(HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
				remoteIp = CommonUtils.getRemoteHost(request);
			}
			Date nowTime = new Date();
			redisClient.lpush("userRegisterList", MessageFormat.format(template, remoteIp, mobileNumber, DateUtil.DateToString(nowTime, "yyyy-MM-dd HH:mm:ss")));
			if (redisClient.exists(remoteIp)) {
				return false;
			} else {
				redisClient.setex(remoteIp, DateUtil.DateToString(nowTime, "yyyy-MM-dd HH:mm:ss"), registerVerifyCodeExpireTime);
			}
			messageBO.sendSMS(ht.get(UserMessageTemplate.class,authType + "_sms"), params, mobileNumber);
		}
		return true;
	}
	@Override
	public void sendChangeBindingMobileNumberSMS(String userId,
			String oriMobileNumber) throws UserNotFoundException {
		// FIXME:验证手机号码的合法性
		// 发送手机验证码
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("user.id:" + userId);
		}
		Map<String, String> params = new HashMap<String, String>();
		// TODO:实现模板
		params.put("username", user.getUsername());
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		params.put(
				"authCode",
				authService
						.createAuthInfo(
								userId,
								oriMobileNumber,
								null,
								CommonConstants.AuthInfoType.CHANGE_BINDING_MOBILE_NUMBER)
						.getAuthCode());
		if(!CommonUtils.isDevEnvironment("environment")){

			messageBO.sendSMS(ht.get(UserMessageTemplate.class,
					MessageConstants.UserMessageNodeId.CHANGE_BINDING_MOBILE_NUMBER
							+ "_sms"), params, oriMobileNumber);
		}
	}

	/**
	 * 根据邮箱查找用户
	 */
	@Override
	public User getUserByEmail(String email) throws UserNotFoundException {
		User user = userBO.getUserByEmail(email);
		if (user == null) {
			throw new UserNotFoundException("user.email:" + email);
		}
		return user;
	}

	/**
	 * 根据手机号查找用户
	 */
	@Override
	public User getUserByMobileNumber(String mobileNumber)
			throws UserNotFoundException {
		User user = userBO.getUserByMobileNumber(mobileNumber);
		if (user == null) {
			throw new UserNotFoundException("user.mobileNumber:" + mobileNumber);
		}
		return user;
	}

	/**
	 * 根据用户id查找用户
	 */
	@Override
	public User getUserById(String userId) throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("user.id:" + userId);
		}
		return user;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void registerByOpenAuth(User user) {
		register(user);

		// 第三方首次登录，绑定已有账号
		Object openId = FacesUtil
				.getSessionAttribute(OpenAuthConstants.OPEN_ID_SESSION_KEY);
		Object openAutyType = FacesUtil
				.getSessionAttribute(OpenAuthConstants.OPEN_AUTH_TYPE_SESSION_KEY);
		Object accessToken = FacesUtil
				.getSessionAttribute(OpenAuthConstants.ACCESS_TOKEN_SESSION_KEY);
		if (openId != null && openAutyType != null && accessToken != null) {
			if (OpenAuthConstants.Type.QQ.equals((String) openAutyType)) {
				qqOAS.binding(user.getId(), (String) openId,
						(String) accessToken);
			} else if (OpenAuthConstants.Type.SINA_WEIBO
					.equals((String) openAutyType)) {
				sinaWeiboOAS.binding(user.getId(), (String) openId,
						(String) accessToken);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void createBorrowerByAdmin(User user) {
		// FIXME:缺验证，这里应该同时创建borrowerInfo，或者该方法应该放在borrowerInfoService中。
		user.setRegisterTime(new Date());
		user.setPassword(HashCrypt.getDigestHash(user.getPassword()));
		user.setStatus(UserConstants.UserStatus.ENABLE);
		userBO.save(user);
		// 添加普通用户权限
		userBO.addRole(user, new Role("MEMBER"));
		// 添加借款者权限
		userBO.addRole(user, new Role("LONAER"));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void register(User user, String referrer) {
		register(user);
		saveReferrerInfo(user.getId(), referrer);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void registerByMobileNumber(User user, String authCode,
			String referrer) throws NoMatchingObjectsException,
			AuthInfoOutOfDateException, AuthInfoAlreadyActivedException {
		registerByMobileNumber(user, authCode);
		saveReferrerRelations(user);
		saveReferrerInfo(user.getId(), referrer);
	}

	private void saveReferrerRelations(User user) {
		String referrerId = user.getReferrer();
		if (Strings.isNullOrEmpty(referrerId)) return;
		String userId = user.getId();
		saveReferrerRelations(referrerId, userId);
	}

	private void saveReferrerRelations(String referrerId, String userId) {
		ReferrerRelation referrerRelation = new ReferrerRelation();
		referrerRelation.setUserId(userId);
		referrerRelation.setReferrerId(referrerId.trim());
		referrerRelation.setLevel(1);
		ht.save(referrerRelation);

		String hqlTemplate = "from ReferrerRelation where userId=''{0}''";
		List<ReferrerRelation> list = ht.find(MessageFormat.format(hqlTemplate, referrerId));
		for (ReferrerRelation relation : list) {
			ReferrerRelation upperRelation = new ReferrerRelation();
			upperRelation.setUserId(userId);
			upperRelation.setReferrerId(relation.getReferrerId());
			upperRelation.setLevel(relation.getLevel() + 1);
			ht.save(upperRelation);
		}
	}

	/**
	 * 保存注册时候的推荐人信息
	 *
	 * @param user
	 * @param referrer
	 */
	private void saveReferrerInfo(String userId, String referrer) {
		// if (StringUtils.isNotEmpty(referrer)) {
		// // 保存推荐人信息
		// MotionTracking mt = new MotionTracking();
		// mt.setId(IdGenerator.randomUUID());
		// mt.setFromWhere(referrer.trim());
		// mt.setFromTime(new Date());
		// mt.setFromType(SystemConstants.MotionTrackingConstants.FromType.REFERRER);
		// mt.setWho(userId);
		// mt.setWhoType(SystemConstants.MotionTrackingConstants.WhoType.USER);
		// mt.setActionType(SystemConstants.MotionTrackingConstants.ActionType.REGISTER);
		// ht.save(mt);
		// }
	}

	@Override
	public boolean hasRole(String userId, String roleId) {
		String hql = "select user from User user left join user.roles r where user.id=? and r.id = ?";
		return ht.find(hql, new String[] { userId, roleId }).size() > 0;
	}

	@Override
	public void sendFindCashPwdSMS(String userId, String mobileNumber)
			throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("user.id:" + userId);
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		params.put(
				"authCode",
				authService
						.createAuthInfo(
								userId,
								mobileNumber,
								null,
								CommonConstants.AuthInfoType.FIND_CASH_PASSWORD_BY_MOBILE)
						.getAuthCode());
		messageBO.sendSMS(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.FIND_CASH_PASSWORD_BY_MOBILE
						+ "_sms"), params, mobileNumber);
	}

	@Override
	public boolean validateRegisterUser(User instance) throws UserRegisterException, InputRuleMatchingException {
		try {
			validationService.inputRuleValidation("input.username", instance.getUsername());

		} catch (NoMatchingObjectsException e) {
			log.error(e.getLocalizedMessage(), e);
			return false;
		} catch (InputRuleMatchingException e) {
			throw new InputRuleMatchingException(MessageFormat.format(e.getMessage(), "用户名"));
		}

		try {
			validationService.inputRuleValidation("input.mobile", instance.getMobileNumber());
		} catch (NoMatchingObjectsException e) {
			log.error(e.getLocalizedMessage(), e);
			return false;
		} catch (InputRuleMatchingException e) {
			throw new InputRuleMatchingException(MessageFormat.format(e.getMessage(), "手机号"));
		}

		try {
			validationService.inputRuleValidation("input.email", instance.getEmail());
		} catch (NoMatchingObjectsException e) {
			log.error(e.getLocalizedMessage(), e);
			return false;
		} catch (InputRuleMatchingException e) {
			throw new InputRuleMatchingException(MessageFormat.format(e.getMessage(), "邮箱"));
		}

		try {
			String className = User.class.getName();
			boolean usernameIsExist = validationService.isAlreadExist(className, "username", instance.getUsername());
			if (usernameIsExist) {
				throw new UserRegisterException("用户名已存在！");
			}

			boolean emailIsExist = validationService.isAlreadExist(className, "email", instance.getEmail());
			if (emailIsExist) {
				throw new UserRegisterException("邮箱已存在！");
			}

			boolean mobileIsExist = validationService.isAlreadExist(className, "mobileNumber", instance.getMobileNumber());
			if (mobileIsExist) {
				throw new UserRegisterException("手机号已存在!");
			}

			if (!Strings.isNullOrEmpty(instance.getReferrer()) ) {
				boolean referrerIsNotExist = !validationService.isAlreadExist(className, "username", instance.getReferrer());
				if (referrerIsNotExist) {
					throw new UserRegisterException("推荐人不存在！");
				}
			}
		} catch (ClassNotFoundException | NoSuchMethodException e) {
			log.error(e.getLocalizedMessage(), e);
			return false;
		}
		return true;
	}

	public boolean idCardIsExists(String idCard){
		int count = DataAccessUtils.intResult(ht.find("select count(user) FROM User user WHERE user.idCard = ?", new String[]{idCard}));
		return  count > 0;
	}

	@Override
	public List<User> searchUserByUserName(String userName) {
		User example = new User();
		example.setUsername("%" + userName + "%");
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.like("username", "%" + userName + "%"));
		return ht.findByCriteria(criteria, 0, 50);
	}


	public String getFirstLevelReferrer(String userId) {
		String hql = "from ReferrerRelation rr where rr.userId=:userId and rr.level=1";
		Query query = ht.getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameter("userId", userId);
		List<ReferrerRelation> referrerRelations = query.list();
		if (referrerRelations != null && referrerRelations.size() > 0) {
			return referrerRelations.get(0).getReferrerId();
		} else {
			return null;
		}
	}

	/**
	 * 获取用户作为推荐人的所有推荐关系
	 *
	 * @param referrerId
	 * @return
	 */
	private List<ReferrerRelation> findReferrerRelationsByReferrer(String referrerId) {
		String hql = "from ReferrerRelation rr where rr.referrerId=:referrerId order by rr.level asc";
		Query query = ht.getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameter("referrerId", referrerId);
		List<ReferrerRelation> referrerRelations = query.list();
		return referrerRelations;
	}

	/**
	 * 查找推荐人变更的受影响的用户及其对应的推荐人
	 * <pre>
	 *     Key of map : 受影响的用户
	 *     Value of map : 该用户的推荐人
	 * </pre>
	 *
	 * @param currentUserId
	 * @return
	 */
	private Map<String, String> buildEffectiveUserReferrerMap(String currentUserId, String currentUserReferrer) {
		// 由于包含当前用户自身这一层级，因此查找受影响的用户时，需要将 effectiveLevel 减去 1 。
		List<ReferrerRelation> referrerRelations = findReferrerRelationsByReferrer(currentUserId);
		// 默认情况下受影响的用户只有自己.
		int effectiveUserCount = 1;
		if (referrerRelations != null) {
			effectiveUserCount += referrerRelations.size();
		}
		Map<String, String> effectiveUserIdList = new LinkedHashMap<>(effectiveUserCount);
		effectiveUserIdList.put(currentUserId, currentUserReferrer);
		for (ReferrerRelation rr : referrerRelations) {
			effectiveUserIdList.put(rr.getUserId(), getFirstLevelReferrer(rr.getUserId()));
		}
		return effectiveUserIdList;
	}


	/**
	 * 判断用户是否存在非直接的推荐关系
	 * @param userId
	 * @param referrerId
	 * @return
	 */
	public boolean hasDiffReferrerRelation(String userId, String referrerId){
		Session session = ht.getSessionFactory().getCurrentSession();

		// 检查是否有间接推荐关系
		/* 不用检查正向推荐关系
		String hql1 = "select count(*) from ReferrerRelation rr where rr.userId=:userId and rr.referrerId=:referrerId and rr.level>1";
		Query query = session.createQuery(hql1);
		query.setParameter("userId",userId);
		query.setParameter("referrerId", referrerId);
		int n1 = ((Number) query.uniqueResult()).intValue();
		if(n1>0){
			return true;
		}
		*/

		// 检查是否有反向推荐关系，避免出现推荐环
		String hql2 = "select count(*) from ReferrerRelation rr where rr.userId=:userId and rr.referrerId=:referrerId";
		Query query2 = session.createQuery(hql2);
		query2.setParameter("userId",referrerId);
		query2.setParameter("referrerId", userId);
		int n2 = ((Number) query2.uniqueResult()).intValue();
		if(n2>0){
			return true;
		}

		// 当用户不存在推荐关系时才返回 false
		return false;
	}

	/**
	 * 删除用户作为被推荐人的推荐链（所有推荐该用户的记录）
	 *
	 * @param userId
	 */
	private void removeUserReferrerChain(String userId) {
		String sql = "from ReferrerRelation rr where rr.userId = :user_id";
		Session session = ht.getSessionFactory().getCurrentSession();
		Query query = session.createQuery(sql);
		query.setParameter("user_id", userId);
		List<ReferrerRelation> rrList = query.list();
		for(ReferrerRelation rr : rrList){
			session.delete(rr);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void updateUserReferrerRelation(String userId, String oldReferrer, String newReferrer) {
		// 由于后续重新构建用户的推荐关系，因此这个map的顺序十分重要，
		// 所以查询数据库时根据level进行排序，并在实现上使用了 LinkedHashMap 以确保顺序不乱
		Map<String, String> effectiveUserReferrerMap = buildEffectiveUserReferrerMap(userId, oldReferrer);

		// 清除这些用户的推荐关系
		for (String uId : effectiveUserReferrerMap.keySet()) {
			removeUserReferrerChain(uId);
		}

		// 将当前用户的推荐人设置为新推荐人
		effectiveUserReferrerMap.put(userId, newReferrer);

		// 重新构建这些用户的的推荐关系
		for (String uId : effectiveUserReferrerMap.keySet()) {
			String referrerId= effectiveUserReferrerMap.get(uId);
			if(StringUtils.isNotBlank(referrerId)) {
				saveReferrerRelations(referrerId, uId);
			}
		}
	}

	@Override
	public List<String> getAllChannelName() {
		String sql = "select distinct channel from user where channel is not NULL";
		Session session = ht.getSessionFactory().getCurrentSession();
		Query query = session.createSQLQuery(sql);
		return query.list();
	}
}
