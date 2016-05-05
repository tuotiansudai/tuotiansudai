package com.esoft.core.base;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Company: jdp2p <br/>
 * Copyright: Copyright (c)2013 <br/>
 * Description: Filter 的代理类。系统所有的 Filter 共用此一个
 * 
 * @author: wangzhi
 * @version: 1.0 Create at: 2014-1-14 上午9:41:59
 * 
 *           Modification History: <br/>
 *           Date Author Version Description
 *           ------------------------------------------------------------------
 *           2014-1-14 wangzhi 1.0
 */
public class DelegatingFilterProxy implements Filter {
	private String targetFilterBeanName;
	private String targetFilterBeanClassName;
	private Filter proxy;

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		proxy.doFilter(servletRequest, servletResponse, filterChain);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.targetFilterBeanName = config.getInitParameter("targetFilterBeanName");
		this.targetFilterBeanClassName = config.getInitParameter("targetFilterBeanClassName");
		if (StringUtils.isEmpty(targetFilterBeanClassName) && StringUtils.isEmpty(targetFilterBeanName)) {
			//两个参数不能都为空
			throw new IllegalArgumentException("targetFilterBeanName and targetFilterBeanClassName can not both be empty!");
		}
		ServletContext servletContext = null;
		servletContext = config.getServletContext();
		WebApplicationContext wac = null;
		wac = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);
		if (StringUtils.isNotEmpty(targetFilterBeanName)) {
			this.proxy = (Filter) wac.getBean(targetFilterBeanName);
			if (StringUtils.isNotEmpty(targetFilterBeanClassName) && !StringUtils.equals(this.proxy.getClass().getName(), targetFilterBeanClassName)) {
				throw new IllegalArgumentException("beanName:"+targetFilterBeanName+" and beanClassName:"+targetFilterBeanClassName+" not point to the same class");
			}
				
		} else {
			Class clazz;
			try {
				clazz = Class.forName(targetFilterBeanClassName);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("class name of "+targetFilterBeanClassName+" not found");
			}
			this.proxy = (Filter) wac.getBean(clazz);						
		}
		
		this.proxy.init(config);
	}

	@Override
	public void destroy() {
	}
}
