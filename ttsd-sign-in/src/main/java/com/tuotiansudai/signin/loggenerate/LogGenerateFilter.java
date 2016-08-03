package com.tuotiansudai.signin.loggenerate;


import com.tuotiansudai.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LogGenerateFilter implements Filter {

    FilterConfig filterConfig = null;
    private static final String USER_ID = "userId";
    private static final String REQUEST_ID = "requestId";
    private static final String ANONYMOUS = "anonymous";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String userId = StringUtils.isNotEmpty(httpServletRequest.getHeader(USER_ID)) ? httpServletRequest.getHeader(USER_ID) : ANONYMOUS;
        String requestId = StringUtils.isNotEmpty(httpServletRequest.getHeader(REQUEST_ID)) ? httpServletRequest.getHeader(REQUEST_ID) : UUIDGenerator.generate();
        MDC.put(USER_ID, userId);
        MDC.put(REQUEST_ID, requestId);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
