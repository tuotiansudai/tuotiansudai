package com.esoft.archer.user.controller;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.common.exception.AuthInfoAlreadyActivedException;
import com.esoft.archer.common.exception.AuthInfoOutOfDateException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.common.service.AuthService;
import com.esoft.archer.common.service.impl.AuthInfoBO;
import com.esoft.archer.config.model.Config;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.archer.user.service.impl.UserBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.DateStyle;
import com.esoft.core.util.DateUtil;
import com.esoft.jdp2p.message.MessageConstants;
import com.esoft.jdp2p.message.exception.MailSendErrorException;
import com.esoft.jdp2p.message.model.UserMessageTemplate;
import com.esoft.jdp2p.message.service.impl.MessageBO;
import com.ttsd.redis.RedisClient;
import com.ttsd.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Filename: UserHome.java Description: Copyright: Copyright (c)2013
 * Company:jdp2p
 * 
 * @author: yinjunlu
 * @version: 1.0 Create at: 2014-1-9 上午10:16:53
 * 
 *           Modification History: Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-9 yinjunlu 1.0 1.0 Version
 */
@Component
@Scope(ScopeType.VIEW)
public class UserInfoHome extends EntityHome<User> implements Serializable {

	@Logger
	static Log log;

	@Value("${redis.registerVerifyCode.expireTime}")
	private int registerVerifyCodeExpireTime;

	@Resource
	RedisClient redisClient;

	/**
	 * 步骤
	 */
	private int step = 1;

	// 新密码
	private String newPassword;
	// 用于找回密码的邮箱
	private String findPwdEmail;

	//用户基本信息接口
	@Resource
	private AuthInfoBO authInfoBO;

	@Resource
	private UserService userService;
	//信息认证service，生成和验证认证信息。例如：手机短信认证，邮箱认证等
	@Resource
	private AuthService authService;

	//用户登录信息
	@Resource
	private LoginUserInfo loginUserInfo;
	
	@Resource
	private MessageBO messageBO;
	
	@Resource
	private UserBO userBO;
	
	//用于绑定的新邮箱
	private String newEmail;
	// 新手机号
	private String newMobileNumber;
	// 认证码
	private String authCode;

	private String imageCaptcha;
	
	
	public String getFindPwdEmail() {
		return findPwdEmail;
	}
	public void setFindPwdEmail(String findPwdEmail) {
		this.findPwdEmail = findPwdEmail;
	}
	public String getNewMobileNumber() {
		return newMobileNumber;
	}
	public void setNewMobileNumber(String newMobileNumber) {
		this.newMobileNumber = newMobileNumber;
	}
	public String getNewEmail() {
		return newEmail;
	}
	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getImageCaptcha() {
		return imageCaptcha;
	}

	public void setImageCaptcha(String imageCaptcha) {
		this.imageCaptcha = imageCaptcha;
	}
	// /////////////////////////////////////////通过邮箱找回密码--开始////////////////////////////////////

	/**
	 * 通过邮箱找回密码，第一步：发送认证码到用户邮箱
	 * 
	 * @return
	 */
	public String findPwdByEmail() {
		// 发送找回密码的邮件
		try {
			userService.sendFindLoginPasswordLinkEmail(findPwdEmail);
		} catch (UserNotFoundException e) {
			String message = "对不起，邮箱验证失败！" + findPwdEmail + "尚未注册";
			FacesUtil.addErrorMessage(message);
			FacesUtil.getCurrentInstance().validationFailed();
			return null;
		}
		FacesUtil.setRequestAttribute("email", findPwdEmail);
		// 跳转到第二步，验证邮箱验证码
		return "pretty:findPwdByEmail2";
	}

	/**
	 * 通过邮箱找回密码，第二步：检验认证码
	 * 
	 * @return
	 */
	public boolean findPwdByEmail2(String findPwdEmailActiveCode) {
		try {
			User user = userService
					.verifyFindLoginPasswordActiveCode(findPwdEmailActiveCode);
			this.setFindPwdEmail(user.getEmail());
			return true;
		} catch (UserNotFoundException e) {
		} catch (NoMatchingObjectsException e) {
		} catch (AuthInfoOutOfDateException e) {
		} catch (AuthInfoAlreadyActivedException e) {
			
		}
		return false;
	}

	/**
	 * 根据邮箱找回密码，第三步：修改密码，指定新密码
	 * 
	 * @return
	 */
	public String findPwdByEmail3() {
		try {
			User user = userService.getUserByEmail(findPwdEmail);
			userService.modifyPassword(user.getId(), newPassword);
			authInfoBO.activate(user.getId(), findPwdEmail,
					CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_EMAIL);
			FacesUtil.addInfoMessage("修改密码成功！");
			return "pretty:memberLogin";
		} catch (UserNotFoundException e) {
			String message = "对不起，邮箱验证失败！" + findPwdEmail + "尚未注册";
			FacesUtil.addErrorMessage(message);
			return null;
		}
	}

	/**
	 * 重新发送通过邮箱找回密码的验证码邮件
	 * 
	 * @return
	 */
	public void resendFindPwdEmail() {
		try {
			userService.sendFindLoginPasswordEmail(findPwdEmail);
			FacesUtil.addInfoMessage("邮件已发送");
		} catch (UserNotFoundException e) {
			String message = "对不起，邮箱验证失败！" + findPwdEmail + "尚未注册";
			FacesUtil.getCurrentInstance().validationFailed();
			FacesUtil.addErrorMessage(message);
		}
	}
///////////////////////////////////////////通过邮箱找回密码--结束////////////////////////////////////

	///////////////////////////////////////////通过手机找回密码--结束////////////////////////////////////
	/**
	 * 
	 * @param mobileNumber
	 */
	@Deprecated
	public void sentVerifyAuthCodeToMobile(String mobileNumber){
		User l = userBO.getUserByMobileNumber(mobileNumber);
		if(l == null ){
			FacesUtil.getCurrentInstance().validationFailed();
			FacesUtil.addErrorMessage("该手机号未注册");
			return;
		}
		this.setInstance(l);
		// 发送手机验证码
		Map<String, String> params = new HashMap<String, String>();
		params.put("time", DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_CN));
		params.put("authCode", authService.createAuthInfo(l.getId(), mobileNumber, null, CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_MOBILE).getAuthCode());
		messageBO.sendSMS(getBaseService().get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.FIND_LOGIN_PASSWORD_BY_MOBILE + "_sms"), params, mobileNumber);
	}
	
	/**
	 * 发送手机认证码
	 * @param mobileNumber
	 * @param jsCode 方法执行完以后要执行的js
	 */
	public void sentVerifyAuthCodeToMobile(String mobileNumber, String jsCode){
		Date nowTime = new Date();
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String sessionId = request.getSession().getId()+"_image_captcha_status";
		String remoteIp = CommonUtils.getRemoteHost(request);
		User l = userBO.getUserByMobileNumber(mobileNumber);
		if(l == null ){
			FacesUtil.getCurrentInstance().validationFailed();
			FacesUtil.addErrorMessage("该手机号未注册");
			return;
		}
		this.setInstance(l);
		if (redisClient.exists(remoteIp)) {
			FacesUtil.addInfoMessage("您的操作过于频繁，请稍后再试！");
		} else {
			Map<String, String> params = new HashMap<String, String>();
			Config config = getBaseService().get(Config.class, "site_phone");
			String site_phone = "";
			if (config != null) {
				site_phone = config.getValue();
			}
			params.put("authCode", authService.createAuthInfo(l.getId(), mobileNumber, null, CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_MOBILE).getAuthCode());
			params.put("site_phone",site_phone);
			if (redisClient.exists(sessionId)) {
				if (!redisClient.get(sessionId).equals("success")) {
					FacesUtil.addErrorMessage("图形码输入错误！");
					try {
						FacesContext.getCurrentInstance().getExternalContext().redirect("/find_pwd_by_mobile");
					} catch (IOException e) {
						log.error(e.getLocalizedMessage(),e);
					}
					return;
				}
			} else {
				FacesUtil.addErrorMessage("图形码未记录！");
				try {
					FacesContext.getCurrentInstance().getExternalContext().redirect("/find_pwd_by_mobile");
				} catch (IOException e) {
					log.error(e.getLocalizedMessage(),e);
				}
				return;
			}
			// 发送手机验证码
			messageBO.sendSMS(getBaseService().get(UserMessageTemplate.class, MessageConstants.UserMessageNodeId.FIND_LOGIN_PASSWORD_BY_MOBILE + "_sms"), params, mobileNumber);
			redisClient.setex(remoteIp, DateUtil.DateToString(nowTime, "yyyy-MM-dd HH:mm:ss"), registerVerifyCodeExpireTime);
			FacesUtil.addInfoMessage("验证码已经发送至手机！");
			RequestContext.getCurrentInstance().execute(jsCode);
		}
	}

	public void findPwdByMobile1(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String sessionId = request.getSession().getId()+"_image_captcha_status";
		try {
			authService.verifyAuthInfo(getInstance().getId(), getInstance().getMobileNumber(),
					authCode,
					CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_MOBILE);
			if (redisClient.exists(sessionId)) {
				if (!redisClient.get(sessionId).equals("success")) {
					FacesUtil.addErrorMessage("图形码输入错误！");
					FacesContext.getCurrentInstance().getExternalContext().redirect("/find_pwd_by_mobile");
					return;
				}
			} else {
				FacesUtil.addErrorMessage("图形码已经过期！");
				FacesContext.getCurrentInstance().getExternalContext().redirect("/find_pwd_by_mobile");
				return;
			}
			//imageCaptcha
			this.step = 2;
		} catch (NoMatchingObjectsException |AuthInfoOutOfDateException |AuthInfoAlreadyActivedException e) {
			FacesUtil.addErrorMessage("验证码输入有误！");
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("/find_pwd_by_mobile");
			} catch (IOException e1) {
				log.error(e1.getLocalizedMessage(),e1);
			}
		} catch (IOException e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	public String findPwdByMobile2() {
		try {
			User user = this.getInstance();
			userService.modifyPassword(user.getId(), newPassword);
			FacesUtil.addInfoMessage("修改密码成功！");
			if(StringUtils.isNotEmpty(super.getUpdateView())){
				return super.getUpdateView();
			}
			return "pretty:memberLogin";
		} catch (UserNotFoundException e) {
			String message = "对不起，手机验证失败！";
			FacesUtil.addErrorMessage(message);
			return null;
		}
	}
	
	
/////////////////////////////////修改绑定手机--开始////////////////////////////////////
	/**
	 * 更改绑定手机号第一步 给用户当前手机发送认证码
	 */
	@Deprecated
	public void sendCurrentBindingMobileNumberSMS() {
		User user;
		try {
			user = userService.getUserById(loginUserInfo.getLoginUserId());
			userService.sendChangeBindingMobileNumberSMS(user.getId(),
					user.getMobileNumber());
			FacesUtil.addInfoMessage("验证码已经发送至手机！");
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		}
	}
	
	/**
	 * 更改绑定手机号第一步 给用户当前手机发送认证码
	 * @param jsCode 发送成功后，执行的js代码
	 */
	public void sendCurrentBindingMobileNumberSMS(String jsCode) {
		User user;
		try {
			user = userService.getUserById(loginUserInfo.getLoginUserId());
			userService.sendChangeBindingMobileNumberSMS(user.getId(),
					user.getMobileNumber());
			FacesUtil.addInfoMessage("验证码已经发送至手机！");
			RequestContext.getCurrentInstance().execute(jsCode);
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		}
	}
	
	/**
	 * 更改绑定手机号第一步 通过收到手机认证码验证用户当前手机
	 * 
	 * @return
	 */
	public void checkCurrentMobileNumber() {
		User user;
		try {
			user = userService.getUserById(loginUserInfo.getLoginUserId());
			authService.verifyAuthInfo(user.getId(), user.getMobileNumber(),
					authCode,
					CommonConstants.AuthInfoType.CHANGE_BINDING_MOBILE_NUMBER);
			this.authCode = null;
			step = 2;
		} catch (AuthInfoOutOfDateException e) {
			FacesUtil.addErrorMessage("认证码已过期！");
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		} catch (NoMatchingObjectsException e) {
			FacesUtil.addErrorMessage("输入验证码错误，请重新输入！");
		} catch (AuthInfoAlreadyActivedException e) {
			
		}
	}

	/**
	 * 更改绑定手机号第3步 给新手机发送验证码
	 */
	public void sendNewBindingMobileNumber() {
		User user;
		try {
			user = userService.getUserById(loginUserInfo.getLoginUserId());
			userService.sendBindingMobileNumberSMS(user.getId(),
					newMobileNumber);
			FacesUtil.addInfoMessage("验证码已经发送至手机！");
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		}
	}

	/**
	 * 更改绑定手机号第3步 给新手机发送验证码
	 * @param jsCode 发送成功后，执行的js代码
	 */
	public void sendNewBindingMobileNumber(String jsCode) {
		User user;
		try {
			user = userService.getUserById(loginUserInfo.getLoginUserId());
			userService.sendBindingMobileNumberSMS(user.getId(),
					newMobileNumber);
			FacesUtil.addInfoMessage("验证码已经发送至手机！");
			RequestContext.getCurrentInstance().execute(jsCode);
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		}
	}

	/**
	 * 更改绑定手机第4步 验证认证码并更改绑定手机
	 * 
	 * @return
	 */
	public String changeBindingMobileNumber() {
		User user;
		try {
			user = userService.getUserById(loginUserInfo.getLoginUserId());
			userService.bindingMobileNumber(user.getId(), newMobileNumber,
					authCode);
			// 给提示:修改绑定手机成功
			FacesUtil.addInfoMessage("绑定手机修改成功!");
			// 跳转到个人中心
			return "pretty:userCenter";
		} catch (AuthInfoOutOfDateException e) {
			FacesUtil.addErrorMessage("认证码已过期！");
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		} catch (NoMatchingObjectsException e) {
			FacesUtil.addErrorMessage("输入验证码错误，请重新输入！");
		} catch (AuthInfoAlreadyActivedException e) {
			FacesUtil.addErrorMessage("认证码已经使用！");
		}
		return null;
	}


///////////////////////////////////////////修改绑定手机--结束////////////////////////////////////
	
///////////////////////////////////////////通过手机找回交易密码--开始////////////////////////////////////
	/**
	 * 通过手机找回交易密码第一步 给用户当前手机发送认证码
	 * @param jsCode 发送成功后，执行的js代码
	 */
	public void sendFindCashPwdSMS(String jsCode) {
		User user;
		try {
			user = userService.getUserById(loginUserInfo.getLoginUserId());
			userService.sendFindCashPwdSMS(user.getId(), user.getMobileNumber());
			step = 2;
			FacesUtil.addInfoMessage("验证码已经发送至手机！");
			RequestContext.getCurrentInstance().execute(jsCode);
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过手机找回交易密码第一步 通过收到手机认证码验证用户当前手机
	 * 
	 * @return
	 */
	public void checkFindCashPwdCode() {
		User user;
		try {
			user = userService.getUserById(loginUserInfo.getLoginUserId());
			authService.verifyAuthInfo(user.getId(), user.getMobileNumber(), authCode, CommonConstants.AuthInfoType.FIND_CASH_PASSWORD_BY_MOBILE);
			this.authCode = null;
			step = 2;
		} catch (AuthInfoOutOfDateException e) {
			FacesUtil.addErrorMessage("认证码已过期！");
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		} catch (NoMatchingObjectsException e) {
			FacesUtil.addErrorMessage("输入验证码错误，请重新输入！");
		} catch (AuthInfoAlreadyActivedException e) {
			
		}
	}
///////////////////////////////////////////通过手机找回交易密码--结束////////////////////////////////////
	
	
///////////////////////////////////////////修改绑定邮箱--开始////////////////////////////////////
	/**
	 *更改绑定邮箱第一步 给用户当前邮箱发送认证码
	 */
	@Deprecated
	public void sendCurrentBindingEmail(){
		try {
			User user=userService.getUserById(loginUserInfo.getLoginUserId());
			//发邮件(认证码)给原邮箱
			userService.sendChangeBindingEmail(user.getId(), user.getEmail());
			FacesUtil.addInfoMessage("验证码已经发送至邮箱！");
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		}
	}
	/**
	 *更改绑定邮箱第一步 给用户当前邮箱发送认证码
	 *@param jsCode 发送成功后执行的js代码
	 */
	public void sendCurrentBindingEmail(String jsCode){
		try {
			User user=userService.getUserById(loginUserInfo.getLoginUserId());
			//发邮件(认证码)给原邮箱
			userService.sendChangeBindingEmail(user.getId(), user.getEmail());
			FacesUtil.addInfoMessage("验证码已经发送至邮箱！");
			RequestContext.getCurrentInstance().execute(jsCode);
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		}
	}
	/**
	 * 更改绑定邮箱第二步 通过收到邮件认证码验证用户当前邮箱
	 */
	public void checkCurrentEmail(){
			try {
				User user=userService.getUserById(loginUserInfo.getLoginUserId());

				authService.verifyAuthInfo(user.getId(), user.getEmail(), authCode, CommonConstants.AuthInfoType.BINDING_EMAIL);
				this.step = 2;
				this.authCode = null;
			} catch (UserNotFoundException e) {
				FacesUtil.addErrorMessage("用户未登录");
				e.printStackTrace();
			} catch (NoMatchingObjectsException e) {
				FacesUtil.addErrorMessage("输入验证码错误，请重新输入");
			} catch (AuthInfoOutOfDateException e) {
				FacesUtil.addErrorMessage("验证码已过期");
			} catch (AuthInfoAlreadyActivedException e) {
				FacesUtil.addErrorMessage("认证码已经使用！");
			}
	}
	
	
	/**
	 * 更改绑定邮箱第三步 给新邮箱发送验证码
	 */
	@Deprecated
	public void sendNewBindingEmail() {
		try {
			User user=userService.getUserById(loginUserInfo.getLoginUserId());
			//发送绑定新邮箱接口 、 新邮箱需要验证唯一性()    发邮件(认证码)给新邮箱
			userService.sendBindingEmail(user.getId(), newEmail);
			FacesUtil.addInfoMessage("验证码已经发送至新邮箱！");
			
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		}
	}
	
	/**
	 * 更改绑定邮箱第三步 给新邮箱发送验证码
	 * @param jsCode 成功发送后执行的js代码
	 */
	public void sendNewBindingEmail(String jsCode) {
		try {
			//根据当前登录用户的id,获得当前用户
			User user=userService.getUserById(loginUserInfo.getLoginUserId());
			//发送绑定新邮箱接口 、 新邮箱需要验证唯一性()    发邮件(认证码)给新邮箱
			String email = newEmail;
			if (email == null){
				email = getInstance().getEmail();
			}
			userService.sendBindingEmail(user.getId(), email);
			FacesUtil.addInfoMessage("验证码已经发送至新邮箱！");
			RequestContext.getCurrentInstance().execute(jsCode);
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			log.error(e.getLocalizedMessage(), e);
		} catch (MailSendErrorException e) {
			FacesUtil.addErrorMessage("验证码发送失败，请检查邮箱合法性！");
			log.error(e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * 更改绑定邮箱第四步 验证认证码并更改绑定邮箱
	 * 
	 * @return
	 */
	public String changeBindingEmail() {
		try {
			User user=userService.getUserById(loginUserInfo.getLoginUserId());
			String email = newEmail;
			if (email == null){
				email = getInstance().getEmail();
			}

			userService.bindingEmail(user.getId(), email, authCode);
			FacesUtil.addInfoMessage("绑定新邮箱成功！");
			return "pretty:userCenter";
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		} catch (NoMatchingObjectsException e) {
			FacesUtil.addErrorMessage("输入验证码错误，请重新输入！");
		} catch (AuthInfoOutOfDateException e) {
			FacesUtil.addErrorMessage("认证码已过期");
		} catch (AuthInfoAlreadyActivedException e) {
			FacesUtil.addErrorMessage("认证码已经使用！");
		}
		return null;
	}
///////////////////////////////////////////修改绑定邮箱--结束////////////////////////////////////
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	
}
	