package com.tuotiansudai.paywrapper.interceptors;


import com.tuotiansudai.util.UUIDGenerator;
import org.apache.log4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogGenerateInterceptor extends HandlerInterceptorAdapter {

    private static final String WEB_REQUEST_ID = "webRequestId";

    private static final String REQUEST_ID = "requestId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(WEB_REQUEST_ID, request.getHeader(WEB_REQUEST_ID));
        MDC.put(REQUEST_ID, UUIDGenerator.generate());
        return true;
    }


}
