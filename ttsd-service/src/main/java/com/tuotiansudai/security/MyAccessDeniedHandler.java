package com.tuotiansudai.security;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class MyAccessDeniedHandler extends AccessDeniedHandlerImpl {

    private Map<String, String> errorPageMapping = Maps.newHashMap();

    private String onlyUserAccessDeniedRedirect;

    private String loginUrl;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        boolean isOnlyUserAccessDenied = accessDeniedException instanceof UserRoleAccessDeniedException;
        if (isOnlyUserAccessDenied) {
            if (!response.isCommitted()) {
                response.sendRedirect(onlyUserAccessDeniedRedirect);
                return;
            }
        }

        String requestURI = request.getRequestURI();
        if (!errorPageMapping.containsKey(requestURI)) {
            if (isAjaxRequest(request)) {
                String referer = request.getHeader(HttpHeaders.REFERER);
                PrintWriter writer = response.getWriter();
                if (!Strings.isNullOrEmpty(referer)) {
                    writer.print(referer);
                }
                response.setStatus(HttpStatus.FORBIDDEN.value());
                return;
            }
            if(isAnonymous()){
                response.sendRedirect(loginUrl);
                return;
            }
            super.handle(request, response, accessDeniedException);
            return;
        }

        if (!response.isCommitted()) {
            response.sendRedirect(errorPageMapping.get(requestURI));
        }
    }

    private boolean isAnonymous() {
        return SecurityContextHolder.getContext().getAuthentication() == null;
    }

    public void setErrorPageMapping(Map<String, String> errorPageMapping) {
        this.errorPageMapping = errorPageMapping;
    }

    public void setOnlyUserAccessDeniedRedirect(String onlyUserAccessDeniedRedirect) {
        this.onlyUserAccessDeniedRedirect = onlyUserAccessDeniedRedirect;
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
