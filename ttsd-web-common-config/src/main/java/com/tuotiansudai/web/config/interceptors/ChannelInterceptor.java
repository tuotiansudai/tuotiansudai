package com.tuotiansudai.web.config.interceptors;

import com.google.common.base.Strings;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.repository.model.Source;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;

public class ChannelInterceptor extends HandlerInterceptorAdapter {

    private final static String APP_SOURCE_FLAG = "app";

    @Value("${common.environment}")
    private Environment environment;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        String channel = request.getParameter("channel");
        if (!Strings.isNullOrEmpty(channel) && request.getSession().getAttribute("channel") == null) {
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

        String viewName = modelAndView.getViewName();

        if (viewName != null && viewName.startsWith("redirect:")) {
            try {
                URL url = new URL(viewName.substring(9));
                if (!request.getRemoteHost().equalsIgnoreCase(url.getHost())) {
                    return;
                }
            } catch (MalformedURLException ignored) {
            }
        }

        if (Environment.PRODUCTION == environment) {
            modelAndView.addObject("isProduction", true);
        }

        modelAndView.addObject("isAppSource", APP_SOURCE_FLAG.equalsIgnoreCase(request.getParameter("source")));
    }
}
