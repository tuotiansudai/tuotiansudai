package com.tuotiansudai.api.interceptors;


import com.tuotiansudai.api.security.BufferedRequestWrapper;
import com.tuotiansudai.util.UUIDGenerator;
import org.apache.log4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogGenerateInterceptor extends HandlerInterceptorAdapter {


    private static final String REQUEST_ID = "requestId";

    private static final String USER_ID = "userId";

    private static final String ANONYMOUS = "anonymous";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(REQUEST_ID, UUIDGenerator.generate());
        HttpServletRequest webRequest = new BufferedRequestWrapper(request);
        Object currentLoginName = webRequest.getAttribute("currentLoginName");
        String loginName = (currentLoginName instanceof String && currentLoginName != null) ? currentLoginName.toString() : ANONYMOUS;
        MDC.put(USER_ID, loginName);
        return true;
    }
}
