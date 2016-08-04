package com.tuotiansudai.web.filter;


import com.tuotiansudai.util.UUIDGenerator;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;

import javax.servlet.*;
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
        MDC.put(REQUEST_ID, UUIDGenerator.generate());
        String loginName = StringUtils.isNotEmpty(LoginUserInfo.getLoginName()) ? LoginUserInfo.getLoginName() : ANONYMOUS;
        MDC.put(USER_ID, loginName);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
