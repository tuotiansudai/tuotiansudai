package com.tuotiansudai.web.interceptors;


import com.tuotiansudai.util.UUIDGenerator;
import com.tuotiansudai.web.config.security.LoginUserInfo;
import org.apache.commons.lang3.StringUtils;
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
        String loginName = StringUtils.isNotEmpty(LoginUserInfo.getLoginName()) ? LoginUserInfo.getLoginName() : ANONYMOUS;
        MDC.put(USER_ID, loginName);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        MDC.clear();
    }
}
