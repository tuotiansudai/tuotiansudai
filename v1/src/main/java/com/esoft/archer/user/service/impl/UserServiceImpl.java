package com.esoft.archer.user.service.impl;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

import javax.annotation.Resource;

import com.esoft.archer.common.model.AuthInfo;
import com.esoft.archer.user.model.ReferrerRelation;
import com.esoft.core.annotations.Logger;
import com.esoft.jdp2p.coupon.exception.ExceedDeadlineException;
import com.google.common.base.Strings;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.exception.AuthInfoAlreadyActivedException;
import com.esoft.archer.common.exception.AuthInfoOutOfDateException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.common.service.AuthService;
import com.esoft.archer.common.service.impl.AuthInfoBO;
import com.esoft.archer.openauth.OpenAuthConstants;
import com.esoft.archer.openauth.service.OpenAuthService;
import com.esoft.archer.system.service.SpringSecurityService;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.exception.ConfigNotFoundException;
import com.esoft.archer.user.exception.NotConformRuleException;
import com.esoft.archer.user.exception.RoleNotFoundException;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.Role;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserInfoService;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.core.util.HashCrypt;
import com.esoft.jdp2p.message.MessageConstants;
import com.esoft.jdp2p.message.model.UserMessageTemplate;
import com.esoft.jdp2p.message.service.MessageService;
import com.esoft.jdp2p.message.service.impl.MessageBO;
import com.esoft.archer.config.controller.ConfigHome;

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
		Calendar cal = Calendar.getInstance();
		int emailValidDay = 7;
		Date today = new Date();
		cal.setTime(today);
		cal.add(Calendar.DATE, emailValidDay);
		Date deadline = cal.getTime();
		user.setRegisterTime(today);
		// 用户密码通过sha加密
		user.setPassword(HashCrypt.getDigestHash(user.getPassword()));
		user.setCashPassword(HashCrypt.getDigestHash(user.getCashPassword()));
		user.setStatus(UserConstants.UserStatus.ENABLE);
		userBO.save(user);
		// 添加普通用户权限
		Role role = new Role("MEMBER");
		userBO.addRole(user, role);
		try {
			sendActiveEmail(
					user,
					authService.createAuthInfo(user.getId(), user.getEmail(), deadline,
							CommonConstants.AuthInfoType.ACTIVATE_USER_BY_EMAIL)
							.getAuthCode());
		}catch (Exception e){
			log.error(e.getStackTrace());
		}

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
						.getAuthCode());
	}

	private void sendActiveEmail(User user, String authCode) {
		final String email = user.getEmail();
		// 发送账号激活邮件
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", user.getUsername());
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		String activeCode = email + "&" + authCode;
		// base64编码
		activeCode = Base64.encodeBase64URLSafeString(activeCode.getBytes());
		String activeLink = FacesUtil.getCurrentAppUrl()
				+ "/activateAccount?activeCode=" + activeCode;
		params.put("active_url", activeLink);
		messageBO.sendEmail(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.REGISTER_ACTIVE + "_email"),
				params, email);
	}

	@Override
	public void sendActiveEmail(String userId, String authCode)
			throws UserNotFoundException {
		User user = ht.get(User.class, userId);
		if (user == null) {
			throw new UserNotFoundException("userId:" + userId);
		}
		sendActiveEmail(user, authCode);
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
						.getAuthCode());
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
		messageBO.sendEmail(ht.get(UserMessageTemplate.class,
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
		messageBO.sendEmail(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.FIND_LOGIN_PASSWORD_BY_EMAIL
						+ "_email"), params, email);
	}

	@Override
	public void sendBindingEmail(String userId, String email)
			throws UserNotFoundException {
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
		messageBO.sendEmail(ht.get(UserMessageTemplate.class,
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
		messageBO.sendEmail(ht.get(UserMessageTemplate.class,
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
		if (!oldEmail.equals(email) && bingEmailAi != null){
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
	public void sendRegisterByMobileNumberSMS(String mobileNumber) {
		// FIXME:验证手机号码的合法性
		// 发送手机验证码
		Map<String, String> params = new HashMap<String, String>();
		// TODO:实现模板
		params.put("time", DateUtil.DateToString(new Date(),
				DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		params.put(
				"authCode",
				authService.createAuthInfo(null, mobileNumber, null,
						CommonConstants.AuthInfoType.REGISTER_BY_MOBILE_NUMBER)
						.getAuthCode());
		messageBO.sendSMS(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.REGISTER_BY_MOBILE_NUMBER
						+ "_sms"), params, mobileNumber);
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
		messageBO.sendSMS(ht.get(UserMessageTemplate.class,
				MessageConstants.UserMessageNodeId.CHANGE_BINDING_MOBILE_NUMBER
						+ "_sms"), params, oriMobileNumber);
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
		ReferrerRelation referrerRelation = new ReferrerRelation();
		referrerRelation.setUserId(userId);
		referrerRelation.setReferrerId(referrerId);
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

}
