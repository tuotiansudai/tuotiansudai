package com.esoft.archer.theme.controller;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.archer.config.ConfigConstants;
import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;

/**
 * SiteVars，通过JSF EL 表达式调用方法#{siteVars.siteName}，
 * 该类主要是包含了一些网站的信息，比如网站标题、名称、口号等信息。
 * 这些信息是通过数据库存储的详细请参见 config模块。
 * @author wanghm
 *
 */
@Component
@Scope(ScopeType.SESSION)
public class SiteVars implements java.io.Serializable{
	private static final long serialVersionUID = -7089517277500127277L;
	
	@Logger static Log log ;
	
	private final static String SITE_NAME_EL = 
		"#{configHome.getConfigValue('"+ConfigConstants.Website.SITE_NAME+"')}";
	private final static String SITE_SLOGAN_EL = 
		"#{configHome.getConfigValue('"+ConfigConstants.Website.SITE_SLOGAN+"')}";
	private final static String SITE_DNS_EL = 
		"#{configHome.getConfigValue('"+ConfigConstants.Website.SITE_DNS+"')}";
	
	private String siteDns,siteName ,siteSlogan;
	
	
	/**
	 * 站点域名
	 * @return
	 */
	public String getSiteDns(){
		if(this.siteDns == null){
			this.siteDns = (String)FacesUtil.getExpressionValue(SITE_DNS_EL);
		}
		return this.siteDns;
	}
	
	/**
	 * 网站名称
	 */
	public String getSiteName(){
		if(siteName == null){
			siteName = (String) FacesUtil.getExpressionValue(SITE_NAME_EL);
		}
		return siteName ;
	}
	/**
	 * 网站标语
	 * @return
	 */
	public String getSiteSlogan(){
		if(siteSlogan == null){
			this.siteSlogan = (String) FacesUtil.getExpressionValue(SITE_SLOGAN_EL);
		}
		return this.siteSlogan;
	}
	
	
}
