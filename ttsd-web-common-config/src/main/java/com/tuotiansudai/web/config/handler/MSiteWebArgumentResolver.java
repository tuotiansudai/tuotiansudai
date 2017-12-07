package com.tuotiansudai.web.config.handler;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;

public class MSiteWebArgumentResolver implements WebArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
        if (methodParameter.getParameterName().equals("isMSite") &&
                (boolean.class.isAssignableFrom(methodParameter.getParameterType()) || Boolean.class.isAssignableFrom(methodParameter.getParameterType()))) {
            HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

            String requestURI = httpServletRequest.getRequestURI();
            return  requestURI.equals("/m") || requestURI.startsWith("/m/");
        }
        return false;
    }
}
