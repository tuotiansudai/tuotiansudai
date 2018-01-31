package com.tuotiansudai.web.config.handler;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HandlerExceptionLoggingResolver implements HandlerExceptionResolver, Ordered {
    private static Logger log = Logger.getLogger(HandlerExceptionLoggingResolver.class);

    private static List<String> WARNING_EXCEPTION = Lists.newArrayList(
            "org.springframework.core.convert.ConversionFailedException",
            "org.springframework.web.HttpRequestMethodNotSupportedException",
            "org.springframework.web.method.annotation.MethodArgumentTypeMismatchException",
            "org.apache.catalina.connector.ClientAbortException");

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (WARNING_EXCEPTION.contains(ex.getClass().getName())) {
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
