package com.tuotiansudai.web.config.interceptors;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MobileAccessDecisionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String requestURI = request.getRequestURI();
        MobileAccessDecision.initDecision(requestURI.equals("/m") || requestURI.startsWith("/m/"));
        return true;
    }
}
