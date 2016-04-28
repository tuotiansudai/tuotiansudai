package com.esoft.core.base;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**  
 * Company:     jdp2p <br/> 
 * Copyright:   Copyright (c)2013 <br/>
 * Description:  
 * @author:     wangzhi  
 * @version:    1.0
 * Create at:   2014-1-13 下午12:41:42  
 *  
 * Modification History: <br/>
 * Date         Author      Version     Description  
 * ------------------------------------------------------------------  
 * 2014-1-13      wangzhi      1.0          
 */
public class DelegatingServletProxy extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String targetServletBeanName;
	private String targetServletBeanClassName;
	private Servlet proxy;
	
	@Override
	public void init() throws ServletException {
		this.targetServletBeanName = this.getInitParameter("targetServletBeanName");
		this.targetServletBeanClassName = this.getInitParameter("targetServletBeanClassName");
		if (StringUtils.isEmpty(targetServletBeanClassName) && StringUtils.isEmpty(targetServletBeanName)) {
			//两个参数不能都为空
			throw new IllegalArgumentException("targetServletBean and targetServletBeanClassName can not both be empty!");
		}
		this.getServletBean();
		this.proxy.init(this.getServletConfig());
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		proxy.service(request,response); 
	}

	private void getServletBean(){
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext wac = null; 
		wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
		if (StringUtils.isNotEmpty(targetServletBeanName)) {
			this.proxy = (Servlet) wac.getBean(targetServletBeanName);
			if (StringUtils.isNotEmpty(targetServletBeanClassName) && !StringUtils.equals(this.proxy.getClass().getName(), targetServletBeanClassName)) {
				throw new IllegalArgumentException("beanName:"+targetServletBeanName+" and beanClassName:"+targetServletBeanClassName+" not point to the same class");
			}
				
		} else {
			Class clazz;
			try {
				clazz = Class.forName(targetServletBeanClassName);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("class name of "+targetServletBeanClassName+" not found");
			}
			this.proxy = (Servlet) wac.getBean(clazz);						
		}
	}
}