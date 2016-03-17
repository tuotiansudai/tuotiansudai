package com.tuotiansudai.console.util;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component("pageFilter")
public class PageFilter implements Filter {

    @Value("${console.pagination.defaultPageIndex}")
    private String defaultPageIndex;

    @Value("${console.pagination.defaultPageSize}")
    private String defaultPageSize;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getMethod().equalsIgnoreCase("GET")) {
            if (request.getParameterMap().containsKey("index") && Integer.parseInt(request.getParameterMap().get("index")[0]) < 0) {
                Map<String, String[]> hashMap = Maps.newHashMap(request.getParameterMap());
                hashMap.remove("index");
                hashMap.put("index", new String[]{defaultPageIndex});
                request = new ParameterRequestWrapper(request, hashMap);
            }
            if (request.getParameterMap().containsKey("pageSize") && Integer.parseInt(request.getParameterMap().get("pageSize")[0]) <= 0) {
                Map<String, String[]> hashMap = Maps.newHashMap(request.getParameterMap());
                hashMap.remove("pageSize");
                hashMap.put("pageSize", new String[]{defaultPageSize});
                request = new ParameterRequestWrapper(request, hashMap);
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
