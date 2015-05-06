package com.esoft.archer.openauth.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import weibo4j.util.WeiboConfig;

import com.esoft.archer.openauth.OpenAuthConstants;
import com.esoft.archer.openauth.model.OpenAuth;
import com.esoft.archer.openauth.model.OpenAuthType;
import com.esoft.archer.openauth.service.OpenAuthService;
import com.esoft.archer.openauth.service.abs.OpenAuthServiceAbstract;
import com.esoft.archer.user.model.User;

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
@Service(value = "sinaWeiboOpenAuthService")
public class SinaWeiboOpenAuthServiceImpl extends OpenAuthServiceAbstract {

	@Resource
	private HibernateTemplate ht;

	@Override
	public void login(String openId, HttpSession session) {
		super.login(openId, OpenAuthConstants.Type.SINA_WEIBO, session);
	}

	@Override
	@Transactional(readOnly = false)
	public void binding(String userId, String openId, String accessToken) {
		super.binding(userId, accessToken, openId,
				OpenAuthConstants.Type.SINA_WEIBO);
	}

	@Override
	public String getAuthUrl() {
		return WeiboConfig.getValue("authorizeURL").trim() + "?client_id="
				+ WeiboConfig.getValue("client_ID").trim()
				+ "&response_type=code&redirect_uri="
				+ WeiboConfig.getValue("redirect_URI").trim();
	}

	@Override
	public boolean isBinded(String openId) {
		return super.isBinded(openId, OpenAuthConstants.Type.SINA_WEIBO);
	}

	@Override
	@Transactional(readOnly = false)
	public void refreshAccessToken(String openId, String accessToken) {
		super.refreshAccessToken(openId, accessToken,
				OpenAuthConstants.Type.SINA_WEIBO);
	}

	@Override
	public void unbinding(String userId) {
		super.unbinding(userId, OpenAuthConstants.Type.SINA_WEIBO);
	}
}
