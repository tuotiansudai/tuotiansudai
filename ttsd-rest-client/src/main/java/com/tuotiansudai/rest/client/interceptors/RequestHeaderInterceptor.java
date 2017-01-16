package com.tuotiansudai.rest.client.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.log4j.MDC;

public class RequestHeaderInterceptor implements RequestInterceptor {
    private static final String REQUEST_ID = "requestId";
    private static final String USER_ID = "userId";

    public void apply(RequestTemplate template) {
        template.header(REQUEST_ID, (String) MDC.get(REQUEST_ID));
        template.header(USER_ID, (String) MDC.get(USER_ID));
        template.header("Content-Type","application/json;charset=UTF-8");
    }
}
