package com.esoft.archer.openauth.service.impl;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esoft.archer.openauth.OpenAuthConstants;
import com.esoft.archer.openauth.service.abs.OpenAuthServiceAbstract;
import com.esoft.core.jsf.util.FacesUtil;
import com.qq.connect.QQConnectException;
import com.qq.connect.oauth.Oauth;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-8 上午9:46:40
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-8 wangzhi 1.0
 */
@Service(value = "qqOpenAuthService")
public class QQOpenAuthServiceImpl extends OpenAuthServiceAbstract {

	@Resource
	private HibernateTemplate ht;

	@Override
	public void login(String openAuthId, HttpSession session) {
		super.login(openAuthId, OpenAuthConstants.Type.QQ, session);
	}

	@Override
	@Transactional(readOnly = false)
	public void binding(String userId, String openId, String accessToken) {
		super.binding(userId, accessToken, openId, OpenAuthConstants.Type.QQ);
	}

	@Override
	public String getAuthUrl() {
		try {
			return new Oauth().getAuthorizeURL(FacesUtil
					.getHttpServletRequest());
		} catch (QQConnectException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isBinded(String openId) {
		return super.isBinded(openId, OpenAuthConstants.Type.QQ);
	}

	@Override
	@Transactional(readOnly = false)
	public void refreshAccessToken(String openId, String accessToken) {
		super.refreshAccessToken(openId, accessToken, OpenAuthConstants.Type.QQ);
	}

	@Override
	public void unbinding(String userId) {
		super.unbinding(userId, OpenAuthConstants.Type.QQ);
	}
}
