package com.tuotiansudai.web.ask.interceptors;

import com.tuotiansudai.ask.service.ExchangeLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LinkExchangeInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ExchangeLinkService exchangeLinkService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);

        if (modelAndView == null) {
            return;
        }

        modelAndView.addObject("linkExchangeList", exchangeLinkService.getLinkExchangeListByAsc());
    }
}
