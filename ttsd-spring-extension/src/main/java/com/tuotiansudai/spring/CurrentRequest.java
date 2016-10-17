package com.tuotiansudai.spring;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class CurrentRequest {

    private final static Logger logger = Logger.getLogger(CurrentRequest.class);


    public static HttpServletRequest getCurrentRequest() throws IllegalStateException {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            logger.error("[CurrentRequest:] request not exist context");
            throw new IllegalStateException("当前线程中不存在 Request 上下文");
        }
        return attrs.getRequest();
    }

}
