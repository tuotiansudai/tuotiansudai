package com.tuotiansudai.security;

import com.google.common.collect.Maps;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class MyAccessDeniedHandler extends AccessDeniedHandlerImpl {

    private Map<String, String> errorPageMapping = Maps.newHashMap();

    private String onlyUserAccessDeniedRedirect;

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
            super.handle(request, response, accessDeniedException);
            return;
        }

        if (!response.isCommitted()) {
            response.sendRedirect(errorPageMapping.get(requestURI));
        }
    }

    public void setErrorPageMapping(Map<String, String> errorPageMapping) {
        this.errorPageMapping = errorPageMapping;
    }

    public void setOnlyUserAccessDeniedRedirect(String onlyUserAccessDeniedRedirect) {
        this.onlyUserAccessDeniedRedirect = onlyUserAccessDeniedRedirect;
    }
}
