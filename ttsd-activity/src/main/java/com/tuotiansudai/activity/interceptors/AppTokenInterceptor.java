package com.tuotiansudai.activity.interceptors;

import com.google.common.base.Strings;
import com.tuotiansudai.client.AppTokenRedisWrapperClient;
import com.tuotiansudai.security.MyAuthenticationManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;

public class AppTokenInterceptor extends HandlerInterceptorAdapter {

    static Logger logger = Logger.getLogger(AppTokenInterceptor.class);

    @Autowired
    private MyAuthenticationManager myAuthenticationManager;

    @Autowired
    private AppTokenRedisWrapperClient AppTokenRedisWrapperClient;

    private final static String APP_SOURCE_FLAG = "app";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!APP_SOURCE_FLAG.equalsIgnoreCase(request.getParameter("source"))) {
            return true;
        }
        String token = Strings.isNullOrEmpty(request.getHeader("token")) ? request.getParameter("token") : request.getHeader("token");
        if (Strings.isNullOrEmpty(token)) {
            logger.debug(MessageFormat.format("url:{0},uri:{1} , token is null", request.getRequestURL(), request.getRequestURI()));
            myAuthenticationManager.removeAuthentication();
            return true;
        }

        String loginName = AppTokenRedisWrapperClient.get(token);

        if (Strings.isNullOrEmpty(loginName)) {
            myAuthenticationManager.removeAuthentication();
        } else {
            myAuthenticationManager.createAuthentication(loginName);
        }

        return true;
    }

}
