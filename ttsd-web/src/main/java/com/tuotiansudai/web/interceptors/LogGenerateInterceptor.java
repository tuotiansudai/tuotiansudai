package com.tuotiansudai.web.interceptors;


import com.tuotiansudai.util.IdGenerator;
import com.tuotiansudai.web.util.LoginUserInfo;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(REQUEST_ID, idGenerator.generate());
        if (StringUtils.isNotEmpty(LoginUserInfo.getLoginName())) {
            MDC.put(USER_ID, LoginUserInfo.getLoginName());
        }
        return true;
    }
}
