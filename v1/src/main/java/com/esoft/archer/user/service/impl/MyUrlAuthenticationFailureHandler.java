package com.esoft.archer.user.service.impl;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.util.StringUtils;

public class MyUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private boolean useModalBox;

	private Map<String, String> appHeaders = Maps.newHashMap();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		if (!isContainsAppHeader(request)) {
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
		} else {
			this.saveException(request, exception);
		}
	}

	private boolean isContainsAppHeader(final HttpServletRequest httpServletRequest) {
		Optional<Map.Entry<String, String>> optional = Iterators.tryFind(appHeaders.entrySet().iterator(), new Predicate<Map.Entry<String, String>>() {
			@Override
			public boolean apply(Map.Entry<String, String> entry) {
				return entry.getValue().equals(httpServletRequest.getHeader(entry.getKey()));
			}
		});

		return optional.isPresent();
	}

	public void setAppHeaders(Map<String, String> appHeaders) {
		this.appHeaders = appHeaders;
	}

	public boolean isUseModalBox() {
		return useModalBox;
	}

	public void setUseModalBox(boolean useModalBox) {
		this.useModalBox = useModalBox;
	}

}