package com.esoft.archer.openauth.controller;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.common.controller.EntityHome;
import com.esoft.archer.openauth.model.OpenAuth;
import com.esoft.archer.openauth.service.OpenAuthService;
import com.esoft.core.annotations.ScopeType;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-8 上午10:59:08
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-8 wangzhi 1.0
 */
@Component
@Scope(ScopeType.REQUEST)
public class OpenAuthHome extends EntityHome<OpenAuth> {

	@Resource(name = "qqOpenAuthService")
	private OpenAuthService qqOAS;

	@Resource(name = "sinaWeiboOpenAuthService")
	private OpenAuthService sinaWeiboOAS;

	public String getQQAuthUrl() {
		return qqOAS.getAuthUrl();
	}

	public String getSinaWeiboAuthUrl() {
		return sinaWeiboOAS.getAuthUrl();
	}
	
	/**
	 * 第三方授权后，绑定已有账号
	 * @return
	 */
//	public String bindingExistUser(){
		
//	}
}
