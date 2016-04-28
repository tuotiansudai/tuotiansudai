package com.esoft.archer;

import javax.annotation.Resource;

import javassist.bytecode.annotation.StringMemberValue;

import org.apache.commons.logging.Log;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.esoft.archer.common.CommonConstants;
import com.esoft.core.annotations.Logger;
import com.esoft.core.jsf.util.FacesUtil;
import com.esoft.core.util.StringManager;

/**
 * 整个项目中所用到的一些App常量的获取。<br/>
 * <li>在xhtml中可以使用 #{appInfo.contextPath}获取当前项目的上下文路径</li>
 * <li>在xhtml中可以使用#{appInfo.appUrl}获取当前站点地址，包含端口号，格式如下：
 * 	   http://|https://+serverName+port+contextPath</li>
 * @author wanghm
 * 
 */
@Component
@Scope("application")
public class AppInfo {
	
	private static String contextPath ;
	
	private static String appUrl ;
	
	private static AppInfo instance ;
	
	private static StringManager sm = StringManager.getManager(CommonConstants.Package);
	
	@Logger
	private static Log log ;
	
	private AppInfo(){
		// 
	}
	
	/**
	 * 刷新系统Bean
	 */
	public void refresh(){
		long startTime = System.currentTimeMillis();
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(FacesUtil.getHttpSession().getServletContext());  
		
		AbstractRefreshableApplicationContext absContext = 
			((AbstractRefreshableApplicationContext) context); 
		
		
		absContext.refresh();
		
		long endTime = System.currentTimeMillis();
		
		final String message = sm.getString("refreshSuccess", (endTime - startTime));
		FacesUtil.addInfoMessage(message);
		log.debug(message);
		
	}
	
	public static AppInfo getInstance(){
		if(instance == null){
			instance = new AppInfo();
		}
		
		return instance ;
	}
	
	public String getContextPath(){
		if(contextPath == null){
			contextPath = FacesUtil.getContextPath();
			if(contextPath.equals("/")){
				contextPath = "";
			}
		}
	
		return contextPath ;

	}
	
	public String getAppUrl(){
		if(appUrl == null){
			appUrl = FacesUtil.getCurrentAppUrl();
			if(appUrl.endsWith("/")){
				appUrl = appUrl.substring(0, appUrl.length()-1);
			}
		}
		
		return appUrl ;
	}
	
	public String getAppRealPath(){
		return FacesUtil.getAppRealPath();
	}
	
	
}
