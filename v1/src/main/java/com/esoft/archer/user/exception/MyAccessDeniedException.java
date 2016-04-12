package com.esoft.archer.user.exception;

import java.util.Collection;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

/**  
 * Company:     jdp2p <br/> 
 * Copyright:   Copyright (c)2013 <br/>
 * Description:  
 * @author:     wangzhi  
 * @version:    1.0
 * Create at:   2014-2-25 下午2:45:42  
 *  
 * Modification History: <br/>
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-2-25      wangzhi      1.0          
 */
public class MyAccessDeniedException extends AccessDeniedException{
	private Authentication authentication;
	private Object object;
	private Collection<ConfigAttribute> configAttributes;

	public MyAccessDeniedException(String msg, Throwable t, Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) {
		super(msg, t);
		this.authentication = authentication;
		this.object =object;
		this.configAttributes= configAttributes;
	}

	public MyAccessDeniedException(String msg, Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) {
		super(msg);
		this.authentication = authentication;
		this.object =object;
		this.configAttributes= configAttributes;
	}

	public Authentication getAuthentication() {
		return authentication;
	}

	public Object getObject() {
		return object;
	}

	public Collection<ConfigAttribute> getConfigAttributes() {
		return configAttributes;
	}
	

}
