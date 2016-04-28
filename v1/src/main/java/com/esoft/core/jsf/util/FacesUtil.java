package com.esoft.core.jsf.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;

import com.esoft.archer.config.service.ConfigService;
import com.esoft.archer.theme.ThemeConstants;
import com.esoft.core.jsf.MultiPageMessagesSupport;
import com.esoft.core.util.SpringBeanUtil;

@ManagedBean
public class FacesUtil {

	public static String getThemePath() {
		return "/site/themes/" + getUserTheme() + "/templates/";
	}

	/**
	 * 判断是否为手机浏览器请求
	 * 
	 * @return
	 */
	public static boolean isMobileRequest() {
		return isMobileRequest(getHttpServletRequest());
	}

	/**
	 * 判断是否为手机浏览器请求
	 * 
	 * @return
	 */
	public static boolean isMobileRequest(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		ConfigService cs=  (ConfigService) SpringBeanUtil.getBeanByName("configService");
		return userAgent.contains("Mobile") && !userAgent.contains("iPad") && "1".equals(cs.getConfigValue("enable_mobile_site"));
	}


	public static boolean isMobileRequestForMobile(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent");
		if (null == userAgent){
			return false;
		}
		return userAgent.contains("Mobile");
	}

	public static void evictSecondLevelCache() {
		SessionFactory sf = (SessionFactory) SpringBeanUtil
				.getBeanByName("sessionFactory");
		Map<String, ClassMetadata> roleMap = sf.getAllCollectionMetadata();
		for (String roleName : roleMap.keySet()) {
			sf.evictCollection(roleName);
		}

		Map<String, ClassMetadata> entityMap = sf.getAllClassMetadata();
		for (String entityName : entityMap.keySet()) {
			sf.evictEntity(entityName);
		}

		sf.evictQueries();
	}

	public static String getUserTheme() {
		String userTheme = (String) getHttpServletRequest().getSession(true)
				.getAttribute(ThemeConstants.SESSION_KEY_USER_THEME);
		if (userTheme == null) {
			// 如果用户的主题不存在，取默认主题
			userTheme = ThemeConstants.DEFAULT_USER_THEME;
			setSessionAttribute(ThemeConstants.SESSION_KEY_USER_THEME,
					userTheme);
		}
		return userTheme;
	}

	// ~ Base ..
	public static FacesContext getCurrentInstance() {
		return FacesContext.getCurrentInstance();
	}

	public static ExternalContext getExternalContext() {
		return getCurrentInstance().getExternalContext();
	}

	public static Application getApplication() {
		return getCurrentInstance().getApplication();
	}

	public static UIViewRoot getViewRoot() {
		return FacesUtil.getCurrentInstance().getViewRoot();
	}

	public static Map<String, Object> getViewMap() {
		return getViewRoot().getViewMap();
	}

	public static Object getViewMapValueByKey(String key) {
		return getViewMap().get(key);
	}

	public static void putViewMap(String key, Object value) {
		getViewMap().put(key, value);
	}

	// ~ JSF EL
	/**
	 * Method for taking a reference to a JSF binding expression and returning
	 * the matching object (or creating it).
	 * 
	 * @param expression
	 *            EL expression
	 * @return Managed object
	 */
	public static Object getExpressionValue(String expression) {

		Application app = getApplication();
		ExpressionFactory elFactory = app.getExpressionFactory();
		ELContext elContext = getCurrentInstance().getELContext();

		ValueExpression valueExp = elFactory.createValueExpression(elContext,
				expression, Object.class);
		return valueExp.getValue(elContext);
	}

	/**
	 * 如果消息产生不在jsf生命周期中，且需要展示，则调用如下方法。例如：通过pretty-faces：action中添加的message。
	 * 
	 * @param context
	 */
	public static void addMessagesOutOfJSFLifecycle(FacesContext context) {
		List<FacesMessage> messages = new ArrayList<FacesMessage>();
		for (Iterator<FacesMessage> iter = context.getMessages(null); iter
				.hasNext();) {
			messages.add(iter.next());
			iter.remove();
		}
		Map<String, Object> sessionMap = FacesUtil.getCurrentInstance()
				.getExternalContext().getSessionMap();
		List<FacesMessage> existingMessages = (List<FacesMessage>) sessionMap
				.get(MultiPageMessagesSupport.sessionToken);
		if (existingMessages != null) {
			existingMessages.addAll(messages);
		} else {
			sessionMap.put(MultiPageMessagesSupport.sessionToken, messages);
		}
	}

	// ~ Servlet ..
	public static HttpServletRequest getHttpServletRequest() {
		return (HttpServletRequest) getExternalContext().getRequest();
	}

	public static HttpServletResponse getHttpServletResponse() {
		return (HttpServletResponse) getExternalContext().getResponse();
	}

	public static void sendRedirect(String url) {
		try {
			getExternalContext().redirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HttpSession getHttpSession() {
		if (getCurrentInstance() == null) {
			return null;
		} else {
			return (HttpSession) getExternalContext().getSession(true);
		}
	}

	public static String getParameter(String name) {
		return getHttpServletRequest().getParameter(name);
	}

	public static Object getRequestAttribute(String name) {
		return getHttpServletRequest().getAttribute(name);
	}

	public static void setRequestAttribute(String name, Object value) {
		getHttpServletRequest().setAttribute(name, value);
	}

	public static Object getSessionAttribute(String name) {
		return getHttpSession().getAttribute(name);
	}

	public static void setSessionAttribute(String name, Object value) {
		getHttpSession().setAttribute(name, value);
	}

	// ~ Message ..
	public static void addMessage(String clientId, FacesMessage message) {
		getCurrentInstance().addMessage(clientId, message);
	}

	public static void addInfoMessage(String message) {
		addInfoMessage(message, null);

	}

	public static void addInfoMessage(String summary, String message) {
		addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, summary,
				message));
	}

	public static void addWarnMessage(String message) {
		addWarnMessage(message, null);

	}

	public static void addWarnMessage(String summary, String message) {
		addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, summary,
				message));
	}

	public static void addErrorMessage(String message) {
		addErrorMessage(message, null);

	}

	public static void addErrorMessage(String summary, String message) {
		addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary,
				message));
	}

	public static void addErrorMessage(String clientId, String summary,
			String message) {
		addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR,
				summary, message));
	}

	public static void addFatalMessage(String message) {
		addFatalMessage(message, null);

	}

	public static void addFatalMessage(String summary, String message) {
		addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, summary,
				message));
	}

	public static String redirect(String url) {
		if (url == null) {
			return url;
		}
		return (url + "?faces-redirect=true");
	}

	// ~~ Path
	public static String getRealPath(String path) {
		//modify by lance  fix getRealPath
		return getExternalContext().getRealPath("")+path;
	}


	/**
	 * 获取app硬盘绝对路径
	 * 
	 * @return
	 */
	public static String getAppRealPath() {
		return getRealPath("/");
	}

	public static String getCurrentAppUrl() {
		ExternalContext ec = getExternalContext();

		return ec.getRequestScheme()
				+ "://"
				+ ec.getRequestServerName()
				+ (ec.getRequestServerPort() == 80 ? "" : (":" + ec
						.getRequestServerPort())) + ec.getRequestContextPath();
		// return getExternalContext().getRequestServerPort()+"";
	}

	public static String getContextPath() {
		return getExternalContext().getRequestContextPath();
	}

	public static String getRequestIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if("127.0.0.1".equals(ip)||"0:0:0:0:0:0:0:1".equals(ip)){
				InetAddress inet=null;
				try{
					inet=InetAddress.getLocalHost();
				}catch(Exception e){
					e.printStackTrace();
				}
				ip=inet.getHostAddress();
			}
		}
		// 如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串ip值,X-Forwarded-For中第一个非unknown的有效IP字符串是真实ip。
		return ip.split(",")[0];
	}

}
