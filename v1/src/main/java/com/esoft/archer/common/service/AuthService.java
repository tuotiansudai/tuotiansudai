package com.esoft.archer.common.service;

import java.util.Date;

import org.hibernate.ObjectNotFoundException;

import com.esoft.archer.common.exception.AuthInfoAlreadyActivedException;
import com.esoft.archer.common.exception.AuthInfoOutOfDateException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.common.model.AuthInfo;
import com.esoft.archer.user.exception.ConfigNotFoundException;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: 信息认证service，生成和验证认证信息。例如：手机短信认证，邮箱认证等
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-7 下午3:49:56
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-7 wangzhi 1.0
 */
public interface AuthService {

	/**
	 * 生成认证码
	 * 
	 * @param source 来源
	 * @param target 目标
	 * @param deadline 到期时间
	 * @param authType 认证码类型
	 * @return
	 */
	public AuthInfo createAuthInfo(String source, String target, Date deadline,
			String authType) throws ConfigNotFoundException;

	/**
	 * 验证认证码（source，target，authType 为联合主键）
	 * 
	 * @param authCode
	 * @throws NoMatchingObjectsException
	 * @throws AuthInfoOutOfDateException
	 *             认证码过期异常
	 * @throws AuthInfoAlreadyActivedException
	 *             认证信息已经被激活
	 */
	public void verifyAuthInfo(String source, String target, String authCode,
			String authType) throws NoMatchingObjectsException,
			AuthInfoOutOfDateException, AuthInfoAlreadyActivedException;
}
