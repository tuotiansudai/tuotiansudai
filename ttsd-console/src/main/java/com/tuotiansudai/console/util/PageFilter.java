package com.tuotiansudai.console.util;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class PageFilter extends OncePerRequestFilter{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equalsIgnoreCase("GET")) {
            if (request.getParameterMap().containsKey("index") && Integer.parseInt(request.getParameterMap().get("index")[0]) < 0) {
                HashMap hashMap = new HashMap(request.getParameterMap());
                hashMap.remove("index");
                hashMap.put("index", new String[]{"1"});
                ParameterRequestWrapper parameterRequestWrapper = new ParameterRequestWrapper(request, hashMap);
                request = parameterRequestWrapper;
            }
            if (request.getParameterMap().containsKey("pageSize") && Integer.parseInt(request.getParameterMap().get("pageSize")[0]) < 0) {
                HashMap hashMap = new HashMap(request.getParameterMap());
                hashMap.remove("pageSize");
                hashMap.put("pageSize", new String[]{"10"});
                ParameterRequestWrapper parameterRequestWrapper = new ParameterRequestWrapper(request, hashMap);
                request = parameterRequestWrapper;
            }
        }
        filterChain.doFilter(request, response);
    }
}
