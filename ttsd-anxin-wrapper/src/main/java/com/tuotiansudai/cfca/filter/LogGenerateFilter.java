package com.tuotiansudai.cfca.filter;


import com.tuotiansudai.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class LogGenerateFilter implements Filter {


    private static final String REQUEST_ID = "requestId";

    private static final String USER_ID = "userId";

    private static final String ANONYMOUS = "anxinAnonymous";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String requestIdHeader = StringUtils.isNotEmpty(httpServletRequest.getHeader(REQUEST_ID)) ? httpServletRequest.getHeader(REQUEST_ID) : UUIDGenerator.generate();
            String userIdHeader = StringUtils.isNotEmpty(httpServletRequest.getHeader(USER_ID)) ? httpServletRequest.getHeader(USER_ID) : ANONYMOUS;
            MDC.put(REQUEST_ID, requestIdHeader);
            MDC.put(USER_ID, userIdHeader);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_ID);
            MDC.remove(USER_ID);
        }
    }

    @Override
    public void destroy() {
    }
}
