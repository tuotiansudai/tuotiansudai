package com.tuotiansudai.paywrapper.interceptors;


import com.google.common.base.Strings;
import org.apache.log4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogGenerateInterceptor extends HandlerInterceptorAdapter {


    private static final String USER_ID = "userId";
    private static final String REQUEST_ID = "requestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestIdHeader = request.getHeader(REQUEST_ID);
        String userIdHeader = request.getHeader(USER_ID);
        if (!Strings.isNullOrEmpty(requestIdHeader)) {
            MDC.put(REQUEST_ID, requestIdHeader);
        }

        if (!Strings.isNullOrEmpty(userIdHeader)) {
            MDC.put(USER_ID, userIdHeader);
        }

        return true;
    }


}
