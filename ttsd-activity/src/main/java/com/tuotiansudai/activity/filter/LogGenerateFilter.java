package com.tuotiansudai.activity.filter;

import com.tuotiansudai.activity.util.LoginUserInfo;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;

import javax.servlet.*;
import java.io.IOException;

public class LogGenerateFilter implements Filter {

    private static final String REQUEST_ID = "requestId";

    private static final String USER_ID = "userId";

    private static final String ANONYMOUS = "anonymous";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String userId = StringUtils.isNotEmpty(LoginUserInfo.getLoginName()) ? LoginUserInfo.getLoginName() : ANONYMOUS;
            String requestId = UUIDGenerator.generate();
            MDC.put(REQUEST_ID, requestId);
            MDC.put(USER_ID, userId);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(USER_ID);
            MDC.remove(REQUEST_ID);
        }

    }

    @Override
    public void destroy() {

    }


}
