package com.ttsd.api.filter;

import com.esoft.core.annotations.Logger;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttsd.api.dto.BaseParam;
import com.ttsd.api.dto.BaseParamDto;
import com.ttsd.api.security.BufferedRequestWrapper;
import com.ttsd.api.service.MobileAppClientStatistics;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component("appClientStatisticsFilter")
public class MobileAppClientStatisticsFilter implements Filter {
    @Logger
    private Log log;

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
            statistics.statClientParams(param);
        } catch (Exception e) {
            log.error(e);
        }
    }

    private BaseParam extractBaseParam(HttpServletRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            BaseParamDto dto = objectMapper.readValue(request.getInputStream(), BaseParamDto.class);
            return dto.getBaseParam();
        } catch (IOException e) {
            log.error(e);
        }
        return null;
    }
}
