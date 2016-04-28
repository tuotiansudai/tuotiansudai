package com.esoft.archer.user.service;

import com.esoft.archer.common.exception.AuthInfoAlreadyActivedException;
import com.esoft.archer.common.exception.AuthInfoOutOfDateException;
import com.esoft.archer.common.exception.InputRuleMatchingException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.exception.*;
import com.esoft.archer.user.model.ReferrerRelation;
import com.esoft.archer.user.model.User;

import java.util.Date;

import java.util.List;


/**
 * Description: 用户service<br/>
 * Copyright: Copyright (c)2013<br/>
 * Company:jdp2p<br/>
 * 
 * @author wanghm
 * @version: 1.0 Create at: 2014-1-4 下午3:36:30
 * 
 *           Modification History: Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-4 wanghm 1.0
 * 
 */
public interface UserService {
	/**
	 * 获取当前登陆用户，如果当前用户不存在则抛出异常
	 * 
	 * @param userId
	 * @param status
	 *            状态编号
	 * @throws UserNotFoundException
	 *             系统中找不到该用户
	 * 
	 */
	public User getCrruntUserInfo(String username) throws UserNotFoundException;

	/**
	 * 用户基本信息注册(通过邮箱激活)
	 * 
	 * @param user
	 *            用户对象
	 */
	public void register(User user);

	/**
	 * 用户基本信息注册（通过邮箱激活）
	 * 
	 * @param user
	 * @param referrer
	 *            推荐人
	 */
	public void register(User user, String referrer);

	/**
	 * 根据用户编号查找用户
	 * 
	 * @param userId
	 *            用户编号
	 * @return
	 * @throws UserNotFoundException
	 */
	public User getUserById(String userId) throws UserNotFoundException;

	/**
	 * 根据邮箱查找用户
	 * 
	 * @param email
	 * @return
	 * @throws UserNotFoundException
	 */
	public User getUserByEmail(String email) throws UserNotFoundException;

	/**
	 * 根据手机号查找用户
	 * 
	 * @param mobileNumber
	 * @return
	 * @throws UserNotFoundException
	 */
	public User getUserByMobileNumber(String mobileNumber)
			throws UserNotFoundException;

	/**
	 * 发送激活邮件
	 * 
	 * @param userId
	 * @param url
	 * @throws UserNotFoundException
	 */
	public void sendActiveEmail(String userId, String authCode, String url)
			throws UserNotFoundException;

	/**
	 * 通过邮件激活用户
	 * 
	 * @param activeCode
	 *            激活邮件里的激活码
	 * @throws UserNotFoundException
	 * @throws NoMatchingObjectsException
	 * @throws AuthInfoOutOfDateException
	 *             认证码过期异常
	 * @throws AuthInfoAlreadyActivedException
	 */
	public void activateUserByEmailActiveCode(String activeCode)
			throws UserNotFoundException, NoMatchingObjectsException,
			AuthInfoOutOfDateException, AuthInfoAlreadyActivedException;

	/**
	 * 锁定当前用户，用户状态参照 {@link UserConstants.UserStatus}， 如果用户不存在抛出异常
	 * 
	 * @param userId
	 * @throws UserNotFoundException
	 *             系统中找不到该用户
	 */
	public void disableUser(String userId) throws UserNotFoundException;

	/**
	 * 锁定当前用户，用户状态参照 {@link UserConstants.UserStatus}， 如果当前用户不存在则抛出异常
	 * 
	 * @param userId
	 * @throws UserNotFoundException
	 *             系统中找不到该用户
	 */
	public void enableUser(String userId) throws UserNotFoundException;

	/**
	 * 改变当前用户状态，用户状态参照 {@link UserConstants.UserStatus}， 如果当前用户不存在则抛出异常
	 * 
	 * @param userId
	 * @param status
	 *            状态编号
	 * @throws UserNotFoundException
	 *             系统中找不到该用户
	 * @throws ConfigNotFoundException
	 *             找不到该状态
	 */
	public void changeUserStatus(String userId, String status)
			throws UserNotFoundException, ConfigNotFoundException;

	/**
	 * 验证密码规则，如果不符合条件则抛出异常，如果符合条件则返回true
	 * 
	 * @param password
	 * @return
	 * @throws NotConformRuleException
	 *             密码不符合格式则抛出此异常
	 */
	public boolean verifyPasswordRule(String password)
			throws NotConformRuleException;

	/**
	 * 用户名规则验证，例如：不允许有中文、特殊符号，长度5-16
	 * 
	 * @param username
	 *            用户名
	 * @return boolean
	 * @throws NotConformRuleException
	 *             用户名不符合规则
	 */
	public boolean verifyUsernameRule(String username)
			throws NotConformRuleException;

	/**
	 * 为该用户添加一个角色，当用户不存在或者角色不存在的时候会抛出异常
	 * 
	 * @param userId
	 *            用户编号
	 * @param roleId
	 *            角色编号
	 * @throws UserNotFoundException
	 *             用户不存在
	 * @throws RoleNotFoundException
	 *             角色不存在
	 */
	public void addRole(String userId, String roleId)
			throws UserNotFoundException, RoleNotFoundException;

	/**
	 * 修改登录密码
	 * 
	 * @param userId
	 *            用户id
	 * @param newPassword
	 *            新密码
	 * @throws UserNotFoundException
	 */
	public void modifyPassword(String userId, String newPassword)
			throws UserNotFoundException;

	/**
	 * 修改现金密码
	 * 
	 * @param userId
	 *            用户id
	 * @param newPassword
	 *            新密码
	 * @throws UserNotFoundException
	 */
	public void modifyCashPassword(String userId, String newCashPassword)
			throws UserNotFoundException;

	/**
	 * 重置登录密码
	 * 
	 * @param userId
	 *            用户id
	 */
	public void resetPassword(String userId);

	/**
	 * 验证旧登录密码 正确返回true，错误返回false
	 * 
	 * @param userId
	 *            用户id
	 * @param oldPassword
	 *            旧密码
	 * @return boolean
	 * @throws UserNotFoundException
	 */
	public boolean verifyOldPassword(String userId, String oldPassword)
			throws UserNotFoundException;

	/**
	 * 验证旧交易密码 正确返回true，错误返回false
	 * 
	 * @param userId
	 *            用户id
	 * @param oldcashPassword
	 *            旧交易密码
	 * @return boolean
	 * @throws UserNotFoundException
	 */
	public boolean verifyOldCashPassword(String userId, String oldcashPassword)
			throws UserNotFoundException;

	/**
	 * 找回密码发送邮件，发送激活码
	 * 
	 * @param email
	 *            注册邮箱
	 * @throws UserNotFoundException
	 */
	@Deprecated
	public void sendFindLoginPasswordEmail(String email)
			throws UserNotFoundException;

	/**
	 * 找回密码发送邮件，通过发送的连接重置密码
	 * 
	 * @param email
	 *            注册邮箱
	 * @throws UserNotFoundException
	 */
	public void sendFindLoginPasswordLinkEmail(String email)
			throws UserNotFoundException;

	/**
	 * 验证找回密码链接中的activeCode
	 * 
	 * @param activeCode
	 * @return 需要修改密码的用户
	 * @throws UserNotFoundException
	 * @throws AuthInfoAlreadyActivedException
	 */
	public User verifyFindLoginPasswordActiveCode(String activeCode)
			throws UserNotFoundException, NoMatchingObjectsException,
			AuthInfoOutOfDateException, AuthInfoAlreadyActivedException;

	/**
	 * 绑定邮箱发送邮件
	 * 
	 * @param userId
	 *            用户编号
	 * @param email
	 *            邮箱（当前邮箱、新邮箱）
	 * @throws UserNotFoundException
	 */
	public void sendBindingEmail(String userId, String email)
			throws UserNotFoundException;

	/**
	 * 实名认证，即授予借款权限
	 * 
	 * @param user
	 *            修改过属性值的持久态对象
	 * @throws NoMatchingObjectsException
	 * @throws AuthInfoOutOfDateException
	 *             认证码过期异常
	 * @throws AuthInfoAlreadyActivedException
	 */
	public void realNameCertification(User user, String authCode)
			throws NoMatchingObjectsException, AuthInfoOutOfDateException,
			AuthInfoAlreadyActivedException;

	/**
	 * 实名认证，即授予借款权限（不绑定手机，无需验证手机认证码）
	 * 
	 * @param user
	 *            修改过属性值的持久态对象
	 */
	public void realNameCertification(User user);

	/**
	 * 给绑定手机发送认证码
	 * 
	 * @param userId
	 *            用户编号
	 * @param mobile
	 *            绑定手机号
	 * @throws UserNotFoundException
	 */
	public void sendBindingMobileNumberSMS(String userId, String mobile)
			throws UserNotFoundException;

	/**
	 * 绑定邮箱
	 * 
	 * @param userId
	 * @param email
	 * @param authCode
	 * @throws UserNotFoundException
	 * @throws NoMatchingObjectsException
	 * @throws AuthInfoOutOfDateException
	 *             认证码过期异常
	 * @throws AuthInfoAlreadyActivedException
	 */
	public void bindingEmail(String userId, String email, String authCode)
			throws UserNotFoundException, NoMatchingObjectsException,
			AuthInfoOutOfDateException, AuthInfoAlreadyActivedException;

	/**
	 * 绑定手机号
	 * 
	 * @param userId
	 * @param mobileNumber
	 * @param authCode
	 * @throws UserNotFoundException
	 * @throws NoMatchingObjectsException
	 * @throws AuthInfoOutOfDateException
	 *             认证码过期异常
	 * @throws AuthInfoAlreadyActivedException
	 */
	public void bindingMobileNumber(String userId, String mobileNumber,
			String authCode) throws UserNotFoundException,
			NoMatchingObjectsException, AuthInfoOutOfDateException,
			AuthInfoAlreadyActivedException;

	/**
	 * 发送更换绑定手机号的短信
	 * 
	 * @param userId
	 * @param oriMobileNumber
	 *            原来的手机号
	 * @throws UserNotFoundException
	 */
	public void sendChangeBindingMobileNumberSMS(String userId,
			String oriMobileNumber) throws UserNotFoundException;

	/**
	 * 发送更换绑定邮箱的邮件
	 * 
	 * @param userId
	 * @param oriEmail
	 *            原来的邮箱
	 * @throws UserNotFoundException
	 */
	public void sendChangeBindingEmail(String userId, String oriEmail)
			throws UserNotFoundException;

	/**
	 * 通过手机号注册
	 * 
	 * @param user
	 * @param authCode
	 * @throws NoMatchingObjectsException
	 * @throws AuthInfoOutOfDateException
	 *             认证码过期异常
	 * @throws AuthInfoAlreadyActivedException
	 */
	public void registerByMobileNumber(User user, String authCode)
			throws NoMatchingObjectsException, AuthInfoOutOfDateException,
			AuthInfoAlreadyActivedException;

	/**
	 * 通过手机号注册
	 * 
	 * @param user
	 * @param authCode
	 *            认证码
	 * @param referrer
	 *            推荐人
	 * @throws NoMatchingObjectsException
	 * @throws AuthInfoOutOfDateException
	 *             认证码过期异常
	 * @throws AuthInfoAlreadyActivedException
	 */
	public void registerByMobileNumber(User user, String authCode,
			String referrer) throws NoMatchingObjectsException,
			AuthInfoOutOfDateException, AuthInfoAlreadyActivedException;

	/**
	 * 发送“通过手机号注册”的认证短信
	 * @param mobileNumber
	 * @param remoteIp
	 * @param authType
	 */
	public boolean sendSmsMobileNumber(String mobileNumber, String remoteIp, String authType);

	/**
	 * 管理员创建借款者
	 * 
	 * @param user
	 */
	public void createBorrowerByAdmin(User user);

	/**
	 * 再次发送激活邮件 wangxiao 5-6
	 */
	public void sendActiveEmailAgain(User user);

	/**
	 * 通过邮箱进行实名认证。
	 * 
	 * @author liuchun
	 * @param user
	 *            需要被实名认证的用户
	 * @param authCode
	 *            认证码
	 * @throws NoMatchingObjectsException
	 * @throws AuthInfoOutOfDateException
	 * @throws AuthInfoAlreadyActivedException
	 */
	public void realNameCertificationByEmail(User user, String authCode)
			throws NoMatchingObjectsException, AuthInfoOutOfDateException,
			AuthInfoAlreadyActivedException;

	/**
	 * 判断某个用户是否拥有某个权限
	 * 
	 * @param userId
	 *            用户id
	 * @param roleId
	 *            权限id
	 * @return
	 */
	public boolean hasRole(String userId, String roleId);

	/**
	 * 
	 */
	public void sendFindCashPwdSMS(String id, String mobileNumber)
			throws UserNotFoundException;
	

	boolean validateRegisterUser(User instance) throws UserRegisterException, NoMatchingObjectsException, InputRuleMatchingException;

	public boolean idCardIsExists(String idCard);

	public List<User> searchUserByUserName(String userName);

	/**
	 * 判断用户是否存在非直接的推荐关系
	 * @param userId
	 * @param referrerId
	 * @return
	 */
	public boolean hasDiffReferrerRelation(String userId, String referrerId);

	/**
	 * 修改用户的推荐人链
	 * @param userId 直接相关的用户Id
	 * @param oldReferrer 修改前的推荐人
	 * @param newReferrer 修改后的推荐人
	 */
	public void updateUserReferrerRelation(String userId, String oldReferrer, String newReferrer);

	List<String> getAllChannelName();
}
