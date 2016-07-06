package com.tuotiansudai.paywrapper.interceptors;


import com.tuotiansudai.util.IdGenerator;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogGenerateInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private IdGenerator idGenerator;

    private static final String REQUEST_ID = "requestId";

    private static final String USER_ID = "userId";

    private static final String LOGIN_NAME = "loginName";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(REQUEST_ID, idGenerator.generate());
        String loginName = request.getParameter(LOGIN_NAME);
        MDC.put(USER_ID, loginName);
        return true;
    }
}
