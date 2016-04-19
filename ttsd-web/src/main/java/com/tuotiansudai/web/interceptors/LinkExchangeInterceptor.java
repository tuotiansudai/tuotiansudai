package com.tuotiansudai.web.interceptors;

import com.tuotiansudai.repository.model.Environment;
import com.tuotiansudai.service.LinkExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LinkExchangeInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private LinkExchangeService linkExchangeService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);

        if (modelAndView == null) {
            return;
        }

        modelAndView.addObject("linkExchangeList", linkExchangeService.getLinkExchangeListByAsc());
    }
}
