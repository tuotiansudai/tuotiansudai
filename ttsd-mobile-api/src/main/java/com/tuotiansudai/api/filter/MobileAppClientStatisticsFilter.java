package com.tuotiansudai.api.filter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.tuotiansudai.api.dto.BaseParam;
import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.security.BufferedRequestWrapper;
import com.tuotiansudai.api.service.MobileAppClientStatistics;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component("appClientStatisticsFilter")
public class MobileAppClientStatisticsFilter implements Filter {

    static Logger log = Logger.getLogger(MobileAppClientStatisticsFilter.class);

    @Autowired
    private MobileAppClientStatistics statistics;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest webRequest = new BufferedRequestWrapper((HttpServletRequest) request);
        statisticsMobileClientParams(webRequest);
        chain.doFilter(webRequest, response);
    }

    @Override
    public void destroy() {

    }

    private void statisticsMobileClientParams(HttpServletRequest request) {
        try {
            BaseParam param = extractBaseParam(request);
            if(param != null ){
                statistics.statClientParams(param);
            }
        } catch (Exception e) {
            log.error(e);
        }
    }

    private BaseParam extractBaseParam(HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String requestJson = "";
        try {
            requestJson = ((BufferedRequestWrapper)request).inputStream2String(request.getInputStream());
            BaseParamDto dto = objectMapper.readValue(requestJson, BaseParamDto.class);
            return dto.getBaseParam();
        } catch (IOException e) {
            log.error("app client json invalid:" + requestJson + e);
        }
        return null;
    }

}
