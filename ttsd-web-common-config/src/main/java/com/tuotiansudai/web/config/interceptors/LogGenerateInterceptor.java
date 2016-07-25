package com.tuotiansudai.web.config.interceptors;


import com.google.common.base.Strings;
import com.tuotiansudai.web.config.security.LoginUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class LogGenerateInterceptor extends HandlerInterceptorAdapter {


    private static final String REQUEST_ID = "requestId";

    private static final String USER_ID = "userId";

    private static final String ANONYMOUS = "anonymous";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(REQUEST_ID, UUID.randomUUID().toString().replace("-", ""));
        MDC.put(USER_ID, Strings.isNullOrEmpty(LoginUserInfo.getLoginName()) ? ANONYMOUS : LoginUserInfo.getLoginName());
        return true;
    }
}
