package com.tuotiansudai.web.ask.filter;


import com.tuotiansudai.spring.LoginUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

public class LogGenerateFilter implements Filter {


    private static final String USER_ID = "userId";
    private static final String REQUEST_ID = "requestId";
    private static final String ANONYMOUS = "anonymous";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            MDC.put(REQUEST_ID, UUID.randomUUID().toString().replace("-", ""));
            String loginName = StringUtils.isNotEmpty(LoginUserInfo.getLoginName()) ? LoginUserInfo.getLoginName() : ANONYMOUS;
            MDC.put(USER_ID, loginName);
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
