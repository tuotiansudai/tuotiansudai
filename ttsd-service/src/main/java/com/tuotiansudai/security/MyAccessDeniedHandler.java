package com.tuotiansudai.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.tuotiansudai.dto.AjaxAccessDeniedDto;
import com.tuotiansudai.dto.BaseDto;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class MyAccessDeniedHandler extends AccessDeniedHandlerImpl {

    static Logger logger = Logger.getLogger(MyAccessDeniedHandler.class);

    private Map<String, String> errorPageMapping = Maps.newHashMap();

    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, String> accessDeniedExceptionRedirect = Maps.newHashMap();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String accessDeniedRedirectUrl = this.getAccessDeniedRedirectUrl(accessDeniedException);
        if (!Strings.isNullOrEmpty(accessDeniedRedirectUrl)) {
            if (isAjaxRequest(request)) {
                this.generateAjaxAccessDeniedResponse(request, response, accessDeniedRedirectUrl);
                return;
            }
            response.sendRedirect(accessDeniedRedirectUrl);
            return;
        }

        String requestURI = request.getRequestURI();
        if (!errorPageMapping.containsKey(requestURI)) {
            if (isAjaxRequest(request)) {
                this.generateAjaxAccessDeniedResponse(request, response, "/");
                return;
            }
            super.handle(request, response, accessDeniedException);
            return;
        }

        if (!response.isCommitted()) {
            response.sendRedirect(errorPageMapping.get(requestURI));
        }
    }

    private String getAccessDeniedRedirectUrl(AccessDeniedException accessDeniedException) {
        for (String exception : accessDeniedExceptionRedirect.keySet()) {
            try {
                if (Class.forName(exception).isInstance(accessDeniedException)) {
                    return accessDeniedExceptionRedirect.get(exception);
                }
            } catch (ClassNotFoundException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        return null;
    }

    private void generateAjaxAccessDeniedResponse(HttpServletRequest request, HttpServletResponse response, String accessDeniedRedirectUrl) {
        if (response.isCommitted()) {
            return;
        }

        BaseDto<AjaxAccessDeniedDto> baseDto = new BaseDto<>();
        AjaxAccessDeniedDto dataDto = new AjaxAccessDeniedDto();
        dataDto.setStatus(true);
        dataDto.setDirectUrl(accessDeniedRedirectUrl);
        baseDto.setData(dataDto);

        String referer = request.getHeader(HttpHeaders.REFERER);

        if (!Strings.isNullOrEmpty(referer)) {
            dataDto.setRefererUrl(referer);
        }
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.print(objectMapper.writeValueAsString(baseDto));
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public void setErrorPageMapping(Map<String, String> errorPageMapping) {
        this.errorPageMapping = errorPageMapping;
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    public void setAccessDeniedExceptionRedirect(Map<String, String> accessDeniedExceptionRedirect) {
        this.accessDeniedExceptionRedirect = accessDeniedExceptionRedirect;
    }
}
