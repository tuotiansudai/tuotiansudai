package com.esoft.archer.theme.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.esoft.core.annotations.Logger;
import com.esoft.core.annotations.ScopeType;
import com.esoft.core.jsf.util.FacesUtil;

@Component("tplVars")
@Scope(ScopeType.SESSION)
public class TplVars implements java.io.Serializable{
	private static final long serialVersionUID = -7089517277500127277L;
	
	@Logger static Log log ;
	
	public final static String FRONT_PATTERN = "(.*?)(/index.htm)(.*?)";
	public final static String LOGIN_PATTERN = 
			"(.*?)((/memberLogin.htm)|(/register.htm)|(/regsuccess.htm)|(/regActiveuser.htm)|(/findPwd.htm)|(/findPwdbyMail.htm)|(/findPwdbyMailCode.htm))(.*?)";
	
	private String currentTheme;
	
	
	/**
	 * 判断当前页面路径是否是首页，判断方式，当前路径是否包含“/index.htm” 字符
	 * @return
	 */
	public boolean isFront(){
		
		final String uri = FacesUtil.getHttpServletRequest().getRequestURI();
//		log.debug("uri = " + uri +",matches = " + uri.matches("(.*?)(/index.htm)(.*?)"));
		return uri.matches(FRONT_PATTERN) ;
		
	}
	public boolean isLogin(){
		
		final String uri = FacesUtil.getHttpServletRequest().getRequestURI();
		return uri.matches(LOGIN_PATTERN);
	}
	// -----------  
	public final static String LOGO_PATH = "/logo.png";
	public final static String IMAGE_PATH = "images";
	public final static String STYLE_PATH = "style";
	public final static String JS_PATH = "js";
	public final static String COMPONENTS_PATH = "/site/components";
	
	public String getComponentsPath(){
		return COMPONENTS_PATH;
	}
	
	private String logoPath ;
	/**
	 * 当前主题logo路径“#{appInfo.contextPath}/site/themes/"+{currentTheme}+"/images/logo.jpg” 
	 * {@link currentTheme} 
	 * @return
	 */
	public String getLogoPath(){
		//if(logoPath == null){
			logoPath = FacesUtil.getExpressionValue("#{appInfo.contextPath}")
			+ getCurrentThemePath() + LOGO_PATH;
		//}
		return logoPath ;
	}
	
	/**
	 * 获取当前用户使用主题的路径  例如：/site/themes/blog/
	 * @return
	 */
	public String getCurrentThemePath() {
		final String uri = FacesUtil.getHttpServletRequest().getRequestURI();

		Pattern pa = Pattern.compile("/site/themes/.*?/");// 源码中标题正则表达式
		Matcher ma = pa.matcher(uri);
		if (ma.find()){// 寻找符合el的字串
			return ma.group();
		}else{
			return "none";
		}
		
	}
	
	/**
	 * 获取当前用户使用主题存放样式表的路径
	 * @return
	 */
	public String getThemeStylePath(){
		return FacesUtil.getExpressionValue("#{appInfo.contextPath}")
			+getCurrentThemePath()+ STYLE_PATH;
	}
	
	public String getThemeImagePath(){
		return FacesUtil.getExpressionValue("#{appInfo.contextPath}")
			+getCurrentThemePath() + IMAGE_PATH;
	}
	
	public String getThemeJSPath(){
		return FacesUtil.getExpressionValue("#{appInfo.contextPath}")
			+getCurrentThemePath() + JS_PATH;
	}
	
	/**
	 * 获取当前用户使用的主题的名称
	 * @return
	 */
	public String getCurrentThemeName(){
		return getCurrentThemePath().replaceAll("/site/themes/", "").replaceAll("/templates/", "");
	}
}
