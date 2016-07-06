package com.tuotiansudai.activity.interceptors;


import com.tuotiansudai.activity.util.AppTokenParser;
import com.tuotiansudai.util.IdGenerator;
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

    @Autowired
    private AppTokenParser appTokenParser;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(REQUEST_ID, idGenerator.generate());
        String loginName = appTokenParser.getLoginName(request);
        if (StringUtils.isNotEmpty(loginName)) {
            MDC.put(USER_ID, loginName);
        }
        return true;
    }
}
