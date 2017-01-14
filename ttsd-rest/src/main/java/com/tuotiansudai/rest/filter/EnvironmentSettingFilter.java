package com.tuotiansudai.rest.filter;


import com.tuotiansudai.rest.authenticate.RestUserInfo;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.log4j.MDC;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class EnvironmentSettingFilter implements Filter {


    private static final String REQUEST_ID = "requestId";

    private static final String USER_ID = "userId";

    private static final String ANONYMOUS = "restAnonymous";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String requestIdHeader = StringUtils.isEmpty(httpServletRequest.getHeader(REQUEST_ID)) ? UUIDGenerator.generate() : httpServletRequest.getHeader(REQUEST_ID);
            String userIdHeader = ANONYMOUS;
            if (!StringUtils.isEmpty(httpServletRequest.getHeader(USER_ID))) {
                userIdHeader = httpServletRequest.getHeader(USER_ID);
                RestUserInfo.setCurrentLoginName(userIdHeader);
            }
            MDC.put(REQUEST_ID, requestIdHeader);
            MDC.put(USER_ID, userIdHeader);
            chain.doFilter(request, response);
        } finally {
            RestUserInfo.clearCurrentLoginName();
            MDC.remove(REQUEST_ID);
            MDC.remove(USER_ID);
        }
    }

    @Override
    public void destroy() {
    }
}
