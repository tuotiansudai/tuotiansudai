package com.tuotiansudai.signin.interceptors;


import com.tuotiansudai.util.UUIDGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogGenerateInterceptor extends HandlerInterceptorAdapter {


    private static final String USER_ID = "userId";
    private static final String REQUEST_ID = "requestId";
    private static final String ANONYMOUS = "anonymous";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestIdHeader = request.getHeader(REQUEST_ID);
        String userIdHeader = request.getHeader(USER_ID);
        if (StringUtils.isNotEmpty(requestIdHeader) && StringUtils.isNotEmpty(userIdHeader)) {
            MDC.put(REQUEST_ID, requestIdHeader);
            MDC.put(USER_ID, userIdHeader);
        } else {
            MDC.put(REQUEST_ID, UUIDGenerator.generate());
            MDC.put(USER_ID, ANONYMOUS);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        MDC.clear();
    }
}
