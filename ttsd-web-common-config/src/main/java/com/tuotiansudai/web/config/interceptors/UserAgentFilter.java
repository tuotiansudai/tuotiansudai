package com.tuotiansudai.web.config.interceptors;

import com.google.common.base.Strings;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

public class UserAgentFilter implements Filter {
    private static final String webRriRegex = "^/(register/account|loan-list|transfer-list|loan/\\d+|transfer/\\d+)?$";
    private static final String mSiteRriRegex = "^(/m|((/m/)(register/account|loan-list|transfer-list|loan/\\d+|transfer/\\d+)?))$";

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String requestUri = httpServletRequest.getRequestURI();
        String queryString = httpServletRequest.getQueryString();
        String userAgent = httpServletRequest.getHeader("User-Agent");
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (isMobile(userAgent) && isWebUrl(requestUri)) {
            httpServletResponse.sendRedirect(String.format("/m%s%s", requestUri, Strings.isNullOrEmpty(queryString) ? "" : "?" + queryString));
            return;
        }

        if (!isMobile(userAgent) && isMSiteUrl(requestUri)) {
            httpServletResponse.sendRedirect(String.format("%s%s", requestUri.substring(requestUri.indexOf("/m") + 2),
                    Strings.isNullOrEmpty(queryString) ? "" : "?" + queryString));
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isWebUrl(String requestUri) {
        Pattern pattern = Pattern.compile(webRriRegex);
        return pattern.matcher(requestUri).matches();

    }

    private boolean isMSiteUrl(String requestUri) {
        Pattern pattern = Pattern.compile(mSiteRriRegex);
        return pattern.matcher(requestUri).matches();
    }

    private boolean isMobile(String userAgent) {
        return !Strings.isNullOrEmpty(userAgent) && (userAgent.toLowerCase().contains("iphone") || userAgent.toLowerCase().contains("android"));
    }
}
