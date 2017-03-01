package com.tuotiansudai.rest.interceptors;

import com.tuotiansudai.rest.authenticate.RestUserInfo;
import com.tuotiansudai.rest.authenticate.UserInfoRequired;
import com.tuotiansudai.rest.exceptions.BadRequestException;
import com.tuotiansudai.rest.exceptions.RestErrorCode;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestHeaderValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (isLoginNameRequired(handler)) {
            String loginName = RestUserInfo.getCurrentLoginName();
            if (StringUtils.isEmpty(loginName) || "anonymous".equals(loginName)) {
                throw new BadRequestException(RestErrorCode.LoginNameNotPresent, "can not retrieve login name from request header [userId]");
            }
        }
        return true;
    }

    private boolean isLoginNameRequired(Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            return handlerMethod.hasMethodAnnotation(UserInfoRequired.class);
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
