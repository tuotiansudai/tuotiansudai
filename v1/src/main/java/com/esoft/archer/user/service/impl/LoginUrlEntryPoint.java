package com.esoft.archer.user.service.impl;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

public class LoginUrlEntryPoint implements AuthenticationEntryPoint {
	/**
	 * 如果来访url中包含key，则跳转到对应的value url, eg：key:admin, value:/user/loginPage
	 */
	private LinkedHashMap<String, String> dispatcherRules;
	private String defaultTargetUrl;
	private boolean useModalBox;

	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		// 先判断有误referer，如果有，则说明是在页面上点击链接触发了登录页面，那么就直接返回该页面，然后弹出登录框。
		String targetUrl = request.getHeader("Referer");
		if (StringUtils.hasText(targetUrl) && useModalBox) {
			// request.setAttribute("needLogin", true);
			response.sendRedirect(targetUrl + "?needLogin=true");
		} else {
			targetUrl = defaultTargetUrl;
			String url = request.getRequestURI();
			if (dispatcherRules != null) {
				Set<String> keySet = dispatcherRules.keySet();
				for (String key : keySet) {
					if (url.indexOf("/"+key+"/") != -1 || url.endsWith("/"+key)) {
						targetUrl = dispatcherRules.get(key);
						break;
					}
				}
			}
			try {
				if (targetUrl == null) {
					throw new Exception(
							"please set a value to defaultTargetUrl");
				}

				//如果是jsf ajax 请求，直接给跳转了。
				boolean ajaxRedirect = request.getHeader("faces-request") != null
						&& request.getHeader("faces-request").toLowerCase()
								.indexOf("ajax") > -1;

				if (ajaxRedirect) {
					String ajaxRedirectXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
							+ "<partial-response><redirect url=\""
							+  request.getContextPath()+targetUrl + "?spring-security-redirect="+request.getServletPath()+"\"></redirect></partial-response>";
					response.setContentType("text/xml");
					response.getWriter().write(ajaxRedirectXml);
				} else {
					RequestDispatcher dispatcher = request
							.getRequestDispatcher(targetUrl);
					dispatcher.forward(request, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public LinkedHashMap<String, String> getDispatcherRules() {
		return dispatcherRules;
	}

	public void setDispatcherRules(LinkedHashMap<String, String> dispatcherRules) {
		this.dispatcherRules = dispatcherRules;
	}

	public String getDefaultTargetUrl() {
		return defaultTargetUrl;
	}

	public void setDefaultTargetUrl(String defaultTargetUrl) {
		this.defaultTargetUrl = defaultTargetUrl;
	}

	public boolean isUseModalBox() {
		return useModalBox;
	}

	public void setUseModalBox(boolean useModalBox) {
		this.useModalBox = useModalBox;
	}

}