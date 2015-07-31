package com.esoft.archer.user.service.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.StringUtils;

public class MyUrlAuthenticationFailureHandler extends
		SimpleUrlAuthenticationFailureHandler {
	private boolean useModalBox;

	private String source = "web";

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		if (source.equalsIgnoreCase(request.getParameter("source"))) {
			String targetUrl = request.getHeader("Referer");
			String failTargetUrl = request
					.getParameter("spring-security-fail-redirect");
			if (StringUtils.hasText(failTargetUrl)) {
				saveException(request, exception);
				if (super.isUseForward()) {
					logger.debug("Forwarding to " + failTargetUrl);
					request.getRequestDispatcher(failTargetUrl).forward(request,
							response);
				} else {
					logger.debug("Redirecting to " + failTargetUrl);
					getRedirectStrategy().sendRedirect(request, response,
							failTargetUrl);
				}
			} else if (StringUtils.hasText(targetUrl) && useModalBox
					&& targetUrl.endsWith("?needLogin=true")) {
				saveException(request, exception);
				getRedirectStrategy().sendRedirect(request, response, targetUrl);
			} else {
				super.onAuthenticationFailure(request, response, exception);
			}
		}
	}

	public boolean isUseModalBox() {
		return useModalBox;
	}

	public void setUseModalBox(boolean useModalBox) {
		this.useModalBox = useModalBox;
	}

}