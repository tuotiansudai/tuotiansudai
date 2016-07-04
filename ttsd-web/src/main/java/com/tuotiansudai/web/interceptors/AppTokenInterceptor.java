package com.tuotiansudai.web.interceptors;

import com.google.common.base.Strings;
import com.tuotiansudai.client.RedisWrapperClient;
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
    private RedisWrapperClient redisWrapperClient;
    @Autowired
    private MyAuthenticationManager myAuthenticationManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String token = Strings.isNullOrEmpty(request.getHeader("token"))?request.getParameter("token"):request.getHeader("token");
        if (Strings.isNullOrEmpty(token)) {
            logger.debug(MessageFormat.format("url:{0},uri:{1} , token is null",request.getRequestURL(),request.getRequestURI()));
            myAuthenticationManager.removeAuthentication();
            return true;
        }

        String loginName = redisWrapperClient.get(token);

        if (Strings.isNullOrEmpty(loginName)) {
            myAuthenticationManager.removeAuthentication();
        } else {
            myAuthenticationManager.createAuthentication(loginName);
        }

        return true;
    }

}
