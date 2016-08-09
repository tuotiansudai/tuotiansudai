package com.tuotiansudai.api.filter;


import com.tuotiansudai.util.UUIDGenerator;
import org.apache.log4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            MDC.put(REQUEST_ID, UUIDGenerator.generate());
            Object currentLoginName = request.getAttribute("currentLoginName");
            String loginName = (currentLoginName != null && currentLoginName instanceof String) ? currentLoginName.toString() : ANONYMOUS;
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
