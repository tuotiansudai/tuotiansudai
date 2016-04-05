package com.tuotiansudai.web.interceptors;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChannelInterceptor extends HandlerInterceptorAdapter {

    private final static String APP_SOURCE_FLAG = "app";
    @Value("${common.environment}")
    private Environment environment;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);

        if (modelAndView == null) {
            return;
        }

        String channel = request.getParameter("channel");
        if (!Strings.isNullOrEmpty(channel)) {
            modelAndView.addObject("channel", channel);
        }
        modelAndView.addObject("isProduction", Environment.isProduction(environment));
        modelAndView.addObject("isAppSource", APP_SOURCE_FLAG.equalsIgnoreCase(request.getParameter("source")));
    }
}
