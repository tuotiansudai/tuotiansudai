package com.esoft.archer.user.controller;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.exception.AuthInfoAlreadyActivedException;
import com.esoft.archer.common.exception.AuthInfoOutOfDateException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.common.service.AuthService;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserInfoService;
import com.esoft.archer.user.service.UserService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

/**
 * Description: 用户找回密码 Copyright: Copyright (c)2013 Company: jdp2p
 * 
 * @author: yinjunlu
 * @Deprecated
 * @see UserInfoHome
 * @version: 1.0 Create at: 2014-1-10 下午2:32:26
 * 
 *           Modification History: Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-10 yinjunlu 1.0 1.0 Version
 */
@Component
@Scope(ScopeType.VIEW)
@Deprecated
public class UserFindPasswordHome {

	@Logger
	static Log log;

	// 认证码
	private String authCode;
	// 新密码
	private String newPassword;
	//新提现密码
	private String newCashPwd;
	// 用于找回密码的邮箱
	private String email;
	private boolean showResetPassword = false;

	private static StringManager sm = StringManager
			.getManager(UserConstants.Package);

	@Resource
	private UserInfoService userInfoService;
	@Resource
	private UserService userService;
	@Resource
	private AuthService authService;
	@Resource
	private LoginUserInfo loginUserInfo;
	/**
	 * 通过邮箱找回密码
	 * 
	 * @return
	 */
	public String findPwdByEmail() {
		if (!userInfoService.isEmailExist(email)) {
			String message = "对不起，邮箱验证失败！" + getEmail() + "尚未注册";
			FacesUtil.addErrorMessage(message);
			return null;
		}
		// 发送找回密码的邮件
		try {
			userService.sendFindLoginPasswordEmail(email);
			FacesUtil.addInfoMessage("验证码已发送到你邮箱。");
		} catch (UserNotFoundException e) {
			FacesUtil.addInfoMessage("未找到该邮箱。");
		}
		FacesUtil.setSessionAttribute("confirmEmail", email);
		return FacesUtil.getThemePath() + "findPwdbyMailCode";
	}

	/**
	 * 检验认证码
	 * 
	 * @return
	 */
	public void verifyAuthInfo() {
		try {
			User user = userService.getUserByEmail(email);
			authService.verifyAuthInfo(user.getId(), email, authCode,
				CommonConstants.AuthInfoType.FIND_LOGIN_PASSWORD_BY_EMAIL);
			showResetPassword = true;
		} catch (AuthInfoOutOfDateException e) {
			FacesUtil.addErrorMessage("认证码已过期！");
		} catch (NoMatchingObjectsException e) {
			FacesUtil
					.addErrorMessage(UserConstants.Errormsg.USERVALIDATECODEERRORMSG);
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户不存在！");
		} catch (AuthInfoAlreadyActivedException e) {
			FacesUtil.addErrorMessage("认证码已激活！");
		}
	}

	/**
	 * 根据邮箱修改密码
	 * 
	 * @return
	 */
	public String modifyPasswordByEmail() {
		try {
			User user = userService.getUserByEmail(email);
			userService.modifyPassword(user.getId(), newPassword);
			FacesUtil.addInfoMessage("修改密码成功！");
			return "pretty:memberLogin";
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return "pretty:findPwdFail";
		}
	}

	/**
	 * 重新发送邮件
	 * 
	 * @return
	 */
	public String resendEmail() {
		try {
			userService.sendFindLoginPasswordEmail(email);
		} catch (UserNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isShowResetPassword() {
		return showResetPassword;
	}

	public void setShowResetPassword(boolean showResetPassword) {
		this.showResetPassword = showResetPassword;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getNewCashPwd() {
		return newCashPwd;
	}
	public void setNewCashPwd(String newCashPwd) {
		this.newCashPwd = newCashPwd;
	}
	
}
