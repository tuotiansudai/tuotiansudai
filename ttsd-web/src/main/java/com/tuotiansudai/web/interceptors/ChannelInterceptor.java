package com.tuotiansudai.web.interceptors;

import com.google.common.base.Strings;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChannelInterceptor extends HandlerInterceptorAdapter {

    private final static String APP_SOURCE_FLAG = "app";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String channel = request.getParameter("channel");
        if (!Strings.isNullOrEmpty(channel)) {
            request.getSession().setAttribute("channel", channel);
        }
        return true;
    }

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

        modelAndView.addObject("isAppSource", APP_SOURCE_FLAG.equalsIgnoreCase(request.getParameter("source")));
    }
}
