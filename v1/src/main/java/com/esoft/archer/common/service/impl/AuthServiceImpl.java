package com.esoft.archer.common.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.common.CommonConstants;
import com.esoft.archer.common.exception.AuthInfoAlreadyActivedException;
import com.esoft.archer.common.exception.AuthInfoOutOfDateException;
import com.esoft.archer.common.exception.NoMatchingObjectsException;
import com.esoft.archer.common.model.AuthInfo;
import com.esoft.archer.common.service.AuthService;
import com.esoft.core.annotations.Logger;
import com.esoft.core.util.IdGenerator;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-9 下午5:12:32
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-9 wangzhi 1.0
 */
@Service
public class AuthServiceImpl implements AuthService {

	@Resource
	HibernateTemplate ht;

	@Logger
	static Log log;

	@Resource
	AuthInfoBO authInfoBO;

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public AuthInfo createAuthInfo(String source, String target, Date deadline,
			String authType) {
		AuthInfo ai = new AuthInfo();
		ai.setId(IdGenerator.randomUUID());
		ai.setAuthTarget(target);
		ai.setAuthSource(source);
		ai.setAuthType(authType);
		ai.setAuthCode(RandomStringUtils.random(6, false, true));
		ai.setStatus(CommonConstants.AuthInfoStatus.INACTIVE);
		ai.setDeadline(deadline);
		ai.setGenerationTime(new Date());
		authInfoBO.save(ai);
		return ai;
	}
	
	/**
	 * 验证认证码
	 * @param source 可以使userid，也可以自己约定
	 * @param target 邮箱或者手机号
	 * @param authCode 认证码
	 * @param authType 认证码类型
	 */
	@Override
	public void verifyAuthInfo(String source, String target, String authCode,
			String authType) throws NoMatchingObjectsException,
			AuthInfoOutOfDateException, AuthInfoAlreadyActivedException {
		if (log.isDebugEnabled()) {
			log.debug("source:" + source + " target:" + target + " authCode:"
					+ authCode + " authType:" + authType);
		}
		AuthInfo ai = authInfoBO.get(source, target, authType);
		if (ai == null || !StringUtils.equals(ai.getAuthCode(), authCode)) {
			// 没找到或认证码不正确，抛异常，目前已有异常不合适
			throw new NoMatchingObjectsException(AuthInfo.class, "source:"
					+ source + " target:" + target + " authCode:" + authCode
					+ " authType:" + authType);
		}
		if (!StringUtils.equals(ai.getStatus(),
				CommonConstants.AuthInfoStatus.INACTIVE)) {
			// FIXME:不是未激活状态，抛异常
			throw new AuthInfoAlreadyActivedException("source:"
					+ source + " target:" + target + " authCode:" + authCode
					+ " authType:" + authType);
		}
		if (ai.getDeadline() != null && ai.getDeadline().before(new Date())) {
			// FIXME:已经过期，抛异常
			throw new AuthInfoOutOfDateException("source:" + source
					+ " target:" + target + " authCode:" + authCode
					+ " authType:" + authType);
		}
	}
}
