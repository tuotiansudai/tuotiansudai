package com.tuotiansudai.api.interceptors;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.security.BufferedRequestWrapper;
import com.tuotiansudai.util.IdGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogGenerateInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private IdGenerator idGenerator;

    private static Logger log = Logger.getLogger(LogGenerateInterceptor.class);

    private static final String REQUEST_ID = "requestId";

    private static final String USER_ID = "userId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(REQUEST_ID, idGenerator.generate());
        HttpServletRequest webRequest = new BufferedRequestWrapper(request);
        String userId = extractBaseParam(webRequest).getUserId();
        if (StringUtils.isNotEmpty(userId)) {
            MDC.put(USER_ID, userId);
        }
        return true;
    }

    private BaseParam extractBaseParam(HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String requestJson = "";
        try {
            requestJson = ((BufferedRequestWrapper) request).getInputStreamString();
            BaseParamDto dto = objectMapper.readValue(requestJson, BaseParamDto.class);
            return dto.getBaseParam();
        } catch (IOException e) {
            log.error("app client json invalid:" + requestJson + e);
        }
        return null;
    }
}
