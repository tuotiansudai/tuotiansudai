package com.tuotiansudai.web.config.handler;

import org.apache.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerExceptionLoggingResolver implements HandlerExceptionResolver, Ordered {
    private static Logger log = Logger.getLogger(HandlerExceptionLoggingResolver.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex.getClass().getSimpleName().equals("ClientAbortException")) {
            log.warn(ex.getLocalizedMessage(), ex);
        } else {
            log.error(ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
