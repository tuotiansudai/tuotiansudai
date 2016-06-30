package com.tuotiansudai.web.interceptors;

import com.google.common.base.Strings;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.security.MyAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppTokenInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private RedisWrapperClient redisWrapperClient;
    @Autowired
    private MyAuthenticationManager myAuthenticationManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String token = Strings.isNullOrEmpty(request.getHeader("token"))?request.getParameter("token"):request.getHeader("token");

        String loginName = Strings.isNullOrEmpty(token)?"":redisWrapperClient.get(token);

        if (Strings.isNullOrEmpty(loginName)) {
            myAuthenticationManager.removeAuthentication();
        } else {
            myAuthenticationManager.createAuthentication(loginName);
        }

        return true;
    }

}
