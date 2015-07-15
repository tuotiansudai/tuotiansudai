package com.esoft.archer.user.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.esoft.archer.common.exception.InputRuleMatchingException;
import com.esoft.archer.common.service.impl.AuthInfoBO;
import com.esoft.archer.user.exception.UserRegisterException;
import com.ttsd.aliyun.AliyunUtils;
import com.ttsd.aliyun.PropertiesUtils;
import com.ttsd.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.common.exception.AuthInfoAlreadyActivedException;
import com.esoft.archer.common.exception.AuthInfoOutOfDateException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.common.model.AuthInfo;
import com.esoft.archer.common.service.AuthService;
import com.esoft.archer.openauth.OpenAuthConstants;
import com.esoft.archer.openauth.service.OpenAuthService;
import com.esoft.archer.system.controller.LoginUserInfo;
import com.esoft.archer.user.UserConstants;
import com.esoft.archer.user.exception.ConfigNotFoundException;
import com.esoft.archer.user.exception.UserNotFoundException;
import com.esoft.archer.user.model.User;
import com.esoft.archer.user.service.UserService;
import com.esoft.archer.user.service.impl.UserBO;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.HashCrypt;
import com.esoft.core.util.ImageUploadUtil;
import com.esoft.core.util.SpringBeanUtil;
import com.esoft.core.util.StringManager;

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
public class UserHome extends EntityHome<User> implements java.io.Serializable {

	@Resource
	private UserService userService;

	@Resource
	private AuthService authService;

	@Resource
	private LoginUserInfo loginUser;

	@Resource
	private UserBO userBO;
	@Resource
	private AuthInfoBO authInfoBO;

	/**
	 * 推荐人
	 */
	@Deprecated
	private String referrer;

	// 验证认证码是否正确
	private boolean correctAuthCode = false;
	@Logger
	static Log log;
	private static StringManager sm = StringManager
			.getManager(UserConstants.Package);
	// 认证码
	private String authCode;
	// 实名认证绑定所要手机号
	private String mobileNumber;
	// 旧密码
	private String oldPassword;
	// 旧交易密码
	private String oldCashPassword;
	// 新邮箱
	private String newEmail;
	// 新手机号
	private String newMobileNumber;
	/** 注册完成后是否默认登录 */
	private boolean isLoginAfterRegister = false;
	@Resource
	UserDetailsService userDetailsService;
	@Autowired
	SessionRegistry sessionRegistry;

	/**
	 * 更改绑定邮箱第二步 验证认证码并更改绑定邮箱
	 * 
	 * @return
	 */
	public String changeBindingEmail() {
		User user;
		try {
			user = userService.getUserById(loginUser.getLoginUserId());
			userService.bindingEmail(user.getId(), newEmail, authCode);
			correctAuthCode = true;
		} catch (AuthInfoOutOfDateException e) {
			FacesUtil.addErrorMessage("认证码已过期！");
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		} catch (NoMatchingObjectsException e) {
			FacesUtil.addErrorMessage("输入验证码错误，请重新输入！");
		} catch (AuthInfoAlreadyActivedException e) {
			FacesUtil.addErrorMessage("认证码已激活！");
		}
		return null;
	}

	/**
	 * 更改绑定手机第二步 验证认证码并更改绑定手机
	 * 
	 * @return
	 */
	public String changeBindingMobileNumber() {
		User user;
		try {
			user = userService.getUserById(loginUser.getLoginUserId());
			userService.bindingMobileNumber(user.getId(), newMobileNumber,
					authCode);
			correctAuthCode = true;
		} catch (AuthInfoOutOfDateException e) {
			FacesUtil.addErrorMessage("认证码已过期！");
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		} catch (NoMatchingObjectsException e) {
			FacesUtil.addErrorMessage("输入认证码错误，请重新输入！");
		} catch (AuthInfoAlreadyActivedException e) {
			FacesUtil.addErrorMessage("认证码已激活！");
		}
		return null;
	}

	/**
	 * 更改绑定邮箱第一步 通过收到邮件认证码验证用户当前邮箱
	 * 
	 * @return
	 */
	public String checkCurrentEmail() {
		User user;
		try {
			user = userService.getUserById(loginUser.getLoginUserId());
			authService.verifyAuthInfo(user.getId(), user.getEmail(), authCode,
					CommonConstants.AuthInfoType.CHANGE_BINDING_EMAIL);
			correctAuthCode = true;
		} catch (AuthInfoOutOfDateException e) {
			FacesUtil.addErrorMessage("认证码已过期！");
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		} catch (NoMatchingObjectsException e) {
			FacesUtil.addErrorMessage("输入验证码错误，请重新输入！");
		} catch (AuthInfoAlreadyActivedException e) {
			FacesUtil.addErrorMessage("输入认证码错误，认证码已经使用！");
		}
		return null;
	}

	/**
	 * 更改绑定手机号第一步 通过收到手机认证码验证用户当前手机
	 * 
	 * @return
	 * @throws AuthInfoAlreadyActivedException
	 */
	public String checkCurrentMobileNumber()
			throws AuthInfoAlreadyActivedException {
		User user;
		try {
			user = userService.getUserById(loginUser.getLoginUserId());
			authService.verifyAuthInfo(user.getId(), user.getMobileNumber(),
					authCode,
					CommonConstants.AuthInfoType.CHANGE_BINDING_MOBILE_NUMBER);
			correctAuthCode = true;
		} catch (AuthInfoOutOfDateException e) {
			FacesUtil.addErrorMessage("认证码已过期！");
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		} catch (NoMatchingObjectsException e) {
			FacesUtil.addErrorMessage("输入验证码错误，请重新输入！");
		}
		return null;
	}

	/**
	 * 禁止用户
	 * 
	 * @return
	 */
	public String forbid(String userId) {
		// if (log.isInfoEnabled()) {
		// log.info(sm.getString("log.info.forbidUser",
		// userId, new Date(), userId));
		// }
		try {
			userService.changeUserStatus(userId,
					UserConstants.UserStatus.DISABLE);
		} catch (ConfigNotFoundException e) {
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("该用户不存在");
		}
		return getSaveView();
	}

	public String getAuthCode() {
		return authCode;
	}

	/**
	 * 获取投资权限,即实名认证
	 * 
	 * @return
	 */
	public String getInvestorPermission() {

		if (StringUtils.equals(
				HashCrypt.getDigestHash(getInstance().getCashPassword()),
				getInstance().getPassword())) {
			FacesUtil.addErrorMessage("交易密码不能与登录密码相同");
			// 修复 输入交易密码和登录密码相同时，隐藏交易密码输入框的问题，在这里设置交易密码为空
			getInstance().setCashPassword(null);
			return null;
		}
		userService.realNameCertification(getInstance());
		FacesUtil.addInfoMessage("保存成功，你已通过了实名认证！");
		if (FacesUtil.isMobileRequest()) {
			return "pretty:mobile_user_center";
		}
		return "pretty:userCenter";
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public String getNewMobileNumber() {
		return newMobileNumber;
	}

	public String getOldCashPassword() {
		return oldCashPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public String getReferrer() {
		return referrer;
	}

	public boolean isCorrectAuthCode() {
		return correctAuthCode;
	}

	/**
	 * 修改交易密码
	 * 
	 * @return
	 */
	public String modifycashPassword() {
		String userId = loginUser.getLoginUserId();
		try {
			if (oldCashPassword != null
					&& !"".equals(oldCashPassword)
					&& !userService.verifyOldCashPassword(userId,
							oldCashPassword)) {
				FacesUtil.addErrorMessage("输入旧交易密码错误，请重新输入！");
				return null;
			}
			if (HashCrypt.getDigestHash(getInstance().getCashPassword())
					.equals(getInstance().getPassword())) {
				FacesUtil.addErrorMessage("交易密码不能与登录密码相同");
				return null;
			}
			userService.modifyCashPassword(userId, getInstance()
					.getCashPassword());
			if (oldCashPassword == null || "".equals(oldCashPassword)) {
				FacesUtil.addInfoMessage("设置交易密码成功。");
			} else {
				FacesUtil.addInfoMessage("修改交易密码成功。");
			}
			return "pretty:userCenter";
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录！");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 修改密码
	 * 
	 * @return
	 */
	public String modifyPassword() {
		String userId = loginUser.getLoginUserId();
		try {
			if (!userService.verifyOldPassword(userId, oldPassword)) {
				FacesUtil.addErrorMessage("输入旧密码错误，请重新输入！");
				return null;
			}
			userService.modifyPassword(loginUser.getLoginUserId(),
					getInstance().getPassword());
			FacesUtil.addInfoMessage("密码修改成功！");
			return "pretty:userCenter";
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录！");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 用户注册
	 * 
	 * @deprecated
	 * @see UserHome.registerByEmail()
	 * @return
	 */
	@Deprecated
	public String register() {
		// 保存用户
		userService.register(getInstance(), referrer);
		// regSuccess = true;
		FacesUtil.setSessionAttribute("userEmail", getInstance().getEmail());
		return "pretty:userRegActiveuser";
	}

	/**
	 * 通过邮箱注册用户
	 * 
	 * @since 2.0
	 * @return
	 */
	public String registerByEmail() {
		// 保存用户
		userService.register(getInstance(), referrer);
		// 跳转到“提示通过邮箱激活页面”
		FacesUtil.setRequestAttribute("email", getInstance().getEmail());
		return "pretty:emailActiveNotice";
	}

	/**
	 * 通过手机注册
	 * 
	 * @return
	 */
	public String registerByMobileNumber() {
		try {
			boolean validationSuccess = userService.validateRegisterUser(getInstance());
			if (!validationSuccess) {
				FacesUtil.addErrorMessage("注册失败！");
				return null;
			}
			userService.registerByMobileNumber(getInstance(), authCode,
					referrer);
			if (isLoginAfterRegister) {
				login(getInstance().getId(), FacesUtil.getHttpSession());
			}
			if (FacesUtil.isMobileRequest()) {
				return "pretty:mobile_user_center";
			}
			if (StringUtils.isEmpty(super.getSaveView())) {
				return "pretty:userRegSuccess";
			} else {
				FacesUtil.addInfoMessage("注册成功");
				return super.getSaveView();
			}
		} catch (NoMatchingObjectsException e) {
			FacesUtil.addErrorMessage("输入的验证码错误，验证失败！");
		} catch (AuthInfoOutOfDateException e) {
			FacesUtil.addErrorMessage("验证码已过期！");
		} catch (AuthInfoAlreadyActivedException e) {
			FacesUtil.addErrorMessage("验证码已被使用！");
		} catch (InputRuleMatchingException | UserRegisterException e) {
			FacesUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	/**
	 * 第三方登录 绑定账号注册 QQ、新浪微博
	 * 
	 * @return
	 */
	public String registerByOpenAuth() {
		try {
			userService.registerByMobileNumber(getInstance(), authCode,
					referrer);
		} catch (NoMatchingObjectsException e) {
			e.printStackTrace();
		} catch (AuthInfoOutOfDateException e) {
			e.printStackTrace();
		} catch (AuthInfoAlreadyActivedException e) {
			e.printStackTrace();
		}
		;
		Object openId = FacesUtil
				.getSessionAttribute(OpenAuthConstants.OPEN_ID_SESSION_KEY);
		Object openAutyType = FacesUtil
				.getSessionAttribute(OpenAuthConstants.OPEN_AUTH_TYPE_SESSION_KEY);
		Object accessToken = FacesUtil
				.getSessionAttribute(OpenAuthConstants.ACCESS_TOKEN_SESSION_KEY);
		if (openId != null && openAutyType != null && accessToken != null) {
			OpenAuthService oas = null;
			// QQ
			if (OpenAuthConstants.Type.QQ.equals((String) openAutyType)) {
				oas = (OpenAuthService) SpringBeanUtil
						.getBeanByName("qqOpenAuthService");
				// weibo
			} else if (OpenAuthConstants.Type.SINA_WEIBO
					.equals((String) openAutyType)) {
				oas = (OpenAuthService) SpringBeanUtil
						.getBeanByName("sinaWeiboOpenAuthService");
			}
			// 找不到应该抛异常
			if (oas != null) {
				oas.binding(getInstance().getId(), (String) openId,
						(String) accessToken);
			}
		}
		FacesUtil.addInfoMessage("注册成功！");
		return "pretty:home";
	}

	/**
	 * 解禁用户
	 * 
	 * @return
	 */
	public String release(String userId) {
		if (log.isInfoEnabled()) {
			log.info(sm.getString("log.info.releaseUser", FacesUtil
					.getExpressionValue("#{loginUserInfo.loginUserId}"),
					new Date(), userId));
		}
		try {
			userService.changeUserStatus(userId,
					UserConstants.UserStatus.ENABLE);
			// FIXME:下面异常不合理
		} catch (ConfigNotFoundException e) {
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("该用户不存在");
		}
		return getSaveView();
	}

	/**
	 * 后台保存用户
	 */
	@Override
	@Transactional(readOnly = false)
	public String save() {
		// FIXME:放在service中
		if (StringUtils.isEmpty(getInstance().getId())) {
			getInstance().setId(getInstance().getUsername());
			getInstance().setPassword(HashCrypt.getDigestHash("123abc"));
			FacesUtil.addInfoMessage("用户创建成功，初始密码为123abc，请及时通知用户修改密码！");
			getInstance().setRegisterTime(new Date());
		}

		setUpdateView(FacesUtil.redirect("/admin/user/userList"));
		return super.save();
	}

	/**
	 * 管理员修改用户，修改普通信息和邮箱、手机，不可修改密码等
	 * 
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public String modifyByAdmin() {
		if (StringUtils.isNotEmpty(getInstance().getEmail())) {
			User user = userBO.getUserByEmail(getInstance().getEmail());
			if (user != null && !user.getId().equals(getInstance().getId())) {
				FacesUtil.addErrorMessage("该邮箱已存在！");
				return null;
			}
		}
		if (StringUtils.isNotEmpty(getInstance().getMobileNumber())) {
			User user = userBO.getUserByMobileNumber(getInstance()
					.getMobileNumber());
			if (user != null && !user.getId().equals(getInstance().getId())) {
				FacesUtil.addErrorMessage("该手机号已存在！");
				return null;
			}
		}
		getBaseService().merge(getInstance());
		FacesUtil.addInfoMessage("用户信息修改成功！");
		return FacesUtil.redirect("/admin/user/userList");
	}

	/**
	 * 后台创建或修改借款者
	 * 
	 * @return
	 */
	public String saveBorrower() {
		// TODO:用户风险等级
		this.getInstance().setPassword("123456");
		userService.createBorrowerByAdmin(this.getInstance());
		FacesUtil.addInfoMessage("借款者创建成功，初始密码为123456");
		return FacesUtil.redirect("/admin/user/userList");
	}

	/**
	 * 再次发送激活邮件
	 * 
	 * @author wangxiao 5-6
	 * @return
	 */
	public void sendActiveEmailAgain() {
		User user;
		try {
			user = userService.getUserById(loginUser.getLoginUserId());
			userService.sendActiveEmailAgain(user);
			FacesUtil
					.setSessionAttribute("userEmail", getInstance().getEmail());

		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		}
		FacesUtil.addInfoMessage("邮件已发送请登录邮箱激活");
	}

	/**
	 * 给绑定手机发送认证码
	 */
	public void sendBdMobileNumber() {
		try {
			userService.sendBindingMobileNumberSMS(loginUser.getLoginUserId(),
					getInstance().getMobileNumber());
			FacesUtil.addInfoMessage("验证码短信已经发送！");
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户尚未登录！");
			e.printStackTrace();
		}
	}

	/**
	 * 更改绑定邮箱第一步 给用户当前邮箱发送认证码
	 */
	public void sendCurrentBindingEmail() {
		User user;
		try {
			user = userService.getUserById(loginUser.getLoginUserId());
			userService.sendChangeBindingEmail(user.getId(), user.getEmail());
			FacesUtil.addInfoMessage("验证码已经发送至邮箱！");
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		}

	}

	/**
	 * 更改绑定手机号第一步 给用户当前手机发送认证码
	 */
	public void sendCurrentBindingMobileNumberSMS() {
		User user;
		try {
			user = userService.getUserById(loginUser.getLoginUserId());
			userService.sendChangeBindingMobileNumberSMS(user.getId(),
					user.getMobileNumber());
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		}
	}

	/**
	 * 更改绑定邮箱第二步 给新邮箱发送验证码
	 */
	public void sendNewBindingEmail() {
		User user;
		try {
			user = userService.getUserById(loginUser.getLoginUserId());
			if (user.getEmail().equals(newEmail)) {
				FacesUtil.addErrorMessage("新邮箱不能与当前邮箱相同！");
				return;
			}
			// FIXME 缺发送绑定新邮箱接口 、 新邮箱需要验证唯一性
			userService.sendBindingEmail(user.getId(), newEmail);
			FacesUtil.addInfoMessage("验证码已经发送至新邮箱！");
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		}
	}

	/**
	 * 更改绑定手机号第二步 给新手机发送验证码（修改绑定手机）
	 */
	public void sendNewBindingMobileNumber() {
		User user;
		try {
			user = userService.getUserById(loginUser.getLoginUserId());
			if (user.getMobileNumber().equals(newMobileNumber)) {
				FacesUtil.addErrorMessage("新手机号不能与当前手机相同！");
				return;
			}
			// FIXME 缺发送绑定新手机接口 、 新手机需要验证唯一性
			userService.sendBindingMobileNumberSMS(user.getId(),
					newMobileNumber);
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户未登录");
			e.printStackTrace();
		}
	}

	/**
	 * 用户注册操作，发送手机验证码验证（用户注册时）
	 */
	@Deprecated
	public void sendRegisterAuthCodeToMobile(String mobileNumber) {
		userService.sendRegisterByMobileNumberSMS(mobileNumber);
		FacesUtil.addInfoMessage("短信已发送，请注意查收！");
	}

	/**
	 * 用户注册操作，发送手机验证码验证（用户注册时）
	 * 
	 * @param mobileNumber
	 * @param jsCode
	 *            成功后执行的js代码
	 */
	public void sendRegisterAuthCodeToMobile(String mobileNumber, String jsCode) {
		boolean isSend = userService.sendRegisterByMobileNumberSMS(mobileNumber);
		if (isSend) {
			FacesUtil.addInfoMessage("短信已发送，请注意查收！");
			RequestContext.getCurrentInstance().execute(jsCode);
		} else {
			FacesUtil.addInfoMessage("您的操作过于频繁，请稍后再试！");
		}
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public void setCorrectAuthCode(boolean correctAuthCode) {
		this.correctAuthCode = correctAuthCode;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	public void setNewMobileNumber(String newMobileNumber) {
		this.newMobileNumber = newMobileNumber;
	}

	public void setOldCashPassword(String oldCashPassword) {
		this.oldCashPassword = oldCashPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	/**
	 * 上传图片
	 * 
	 * @return
	 */
	@Transactional(readOnly = false)
	public void uploadPhoto(FileUploadEvent event) {
		UploadedFile file = event.getFile();
		try {
			boolean isUpload = false;
			if (!CommonUtils.isDevEnvironment("environment")) {
				isUpload = true;
			}
			InputStream is = file.getInputstream();
			String fileName = file.getFileName();
			String uploadPath = isUpload ? AliyunUtils.uploadFile(fileName, is) : ImageUploadUtil.upload(is, fileName);
			this.getInstance().setPhoto(uploadPath);
			getBaseService().merge(getInstance());
			FacesUtil.addInfoMessage("上传成功！");
		} catch (IOException e) {
			FacesUtil.addErrorMessage("上传失败！");
			e.printStackTrace();
		}
	}

	/* =======================通过手机修改密码=============================== */

	/**
	 * 第一步： 给手机发送验证码
	 * 
	 * @author liuchun
	 * @param mobileNumber
	 *            注册时候用的手机号码
	 * 
	 */
	public String sendAuthCodeToMobile(String mobileNumber) {
		String hql = "from User u where u.mobileNumber = ?";
		if (0 != getBaseService().find(hql, mobileNumber).size()) {
			userService.sendRegisterByMobileNumberSMS(mobileNumber);
			RequestContext.getCurrentInstance().addCallbackParam("sendSuccess",
					true);
			FacesUtil.setSessionAttribute("mobileNumber", mobileNumber);
			FacesUtil.addInfoMessage("短信已发送，请注意查收！");
			// FIXME:专属页面没有做，直接在原来页面上修改的
			return "pretty:findPwdByEmail2";
		} else {
			FacesUtil.addErrorMessage("此用户没有注册!!");
			return null;
		}
	}

	/**
	 * 第二部： 检查发送的修改密码验证码是否和数据库收到的验证码一致
	 * 
	 * @author liuchun
	 * @param authCode
	 *            验证码
	 * @param mobileNumber
	 *            手机号
	 * @return
	 */
	@Transactional
	public String checkAuthCode(String authCode, String mobileNumber) {
		String hql = "from AuthInfo ai where ai.authCode =? and ai.authTarget=?";
		ArrayList<AuthInfo> list = (ArrayList<AuthInfo>) getBaseService().find(
				hql, new String[] { authCode, mobileNumber });

		// FIXME:验证不够严谨，有可能出现重复数据
		if (list.size() > 0) {
			FacesUtil.setRequestAttribute("mobileNumber", mobileNumber);
			return "pretty:findPwdByEmail3";
		} else {
			FacesUtil.addErrorMessage("验证码输入错误！！");
			return null;
		}
	}

	/**
	 * 第三部：通过手机修改密码
	 * 
	 * @param mobileNumber
	 * @param newPwd
	 * @return
	 */
	@Transactional
	public String ModificationPwdByMobileNum(String mobileNumber, String newPwd) {
		try {
			User user = userService.getUserByMobileNumber(mobileNumber);
			userService.modifyPassword(user.getId(), newPwd);
			FacesUtil.addInfoMessage("修改密码成功");
			return "pretty:memberLogin";
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("此号码未注册");
			e.printStackTrace();
		}
		return authCode;
	}

	/*
	 * =================================通过邮箱进行实名认证================================
	 */

	/**
	 * 第一步： 给邮箱发送验证码(实名认证的时候用)
	 * 
	 * @author liuchun
	 */
	public void sendAuthCodeToEmail() {
		try {
			userService.sendBindingEmail(loginUser.getLoginUserId(),
					getInstance().getEmail());
			FacesUtil.addInfoMessage("验证码已经发送，请注意查收！");
		} catch (UserNotFoundException e) {
			FacesUtil.addErrorMessage("用户尚未登录！");
			e.printStackTrace();
		}
	}

	/**
	 * 第二部： 获取投资权限,即实名认证(通过邮箱进行实名认证 )
	 * 
	 * @return
	 */

	public String getInvestorPermissionByEmail() {
		try {
			userService.realNameCertificationByEmail(getInstance(), authCode);
			FacesUtil.addInfoMessage("保存成功，你已通过了实名认证！");
			return "pretty:userCenter";
		} catch (AuthInfoOutOfDateException e) {
			FacesUtil.addErrorMessage("认证码已过期！");
		} catch (NoMatchingObjectsException e) {
			FacesUtil.addErrorMessage("输入认证码错误，实名认证失败！");
		} catch (AuthInfoAlreadyActivedException e) {
			FacesUtil.addErrorMessage("输入认证码错误，实名认证失败！");
		}
		return null;
	}

	/**
	 * 注册成功后登录
	 * 
	 * @param openId
	 * @param openAuthTypeId
	 * @param session
	 */
	public void login(String userId, HttpSession session) {
		User user = getBaseService().get(User.class, userId);
		if (user.getStatus().equals("1")) {
			UserDetails userDetails = userDetailsService
					.loadUserByUsername(user.getUsername());
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
					user.getUsername(), userDetails.getPassword(),
					userDetails.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(token);
			session.setAttribute(
					HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
					SecurityContextHolder.getContext());
			sessionRegistry.registerNewSession(session.getId(), userDetails);
		}
	}
	/**
	 * 获取邮箱验证状态
	 *
	 * @param source 用户名
	 * @param target 邮箱
	 */
	public boolean isActivatedByEmail(String source, String target){
		String activateUseByEmailAt = CommonConstants.AuthInfoType.ACTIVATE_USER_BY_EMAIL;
		boolean isActivatedFlag = false;
		AuthInfo  activateUseByEmailAi = authInfoBO.get(source, target, activateUseByEmailAt);
		if (activateUseByEmailAi != null){
			isActivatedFlag = CommonConstants.AuthInfoStatus.ACTIVATED.equals(activateUseByEmailAi.getStatus());
		}else{
			String bingEmailAt = CommonConstants.AuthInfoType.BINDING_EMAIL;
			AuthInfo bingEmailAi = authInfoBO.get(source, target, bingEmailAt);
			if (bingEmailAi != null){
				isActivatedFlag = CommonConstants.AuthInfoStatus.ACTIVATED.equals(bingEmailAi.getStatus());
			}
		}
		return isActivatedFlag;
	}

	public boolean getIsLoginAfterRegister() {
		return isLoginAfterRegister;
	}

	public void setIsLoginAfterRegister(boolean isLoginAfterRegister) {
		this.isLoginAfterRegister = isLoginAfterRegister;
	}

}
