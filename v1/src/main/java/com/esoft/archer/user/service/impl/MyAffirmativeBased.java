package com.esoft.archer.user.service.impl;

import java.util.Collection;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.core.Authentication;

import com.esoft.archer.user.exception.MyAccessDeniedException;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description:
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-2-25 下午2:43:25
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-2-25 wangzhi 1.0
 */
public class MyAffirmativeBased extends AffirmativeBased {
	@Override
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException {
		try {
			super.decide(authentication, object, configAttributes);
		} catch (AccessDeniedException ade) {
			throw new MyAccessDeniedException(ade.getMessage(), authentication, object, configAttributes);
		}
	}
}
