package com.tuotiansudai.security;

import com.google.common.base.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;


public class MyLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
    /**
     * @param loginFormUrl URL where the login page can be found. Should either be
     *                     relative to the web-app context path (include a leading {@code /}) or an absolute
     *                     URL.
     */
    public MyLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);

    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (isAjaxRequest(request)) {
            String header = request.getHeader(HttpHeaders.REFERER);
            PrintWriter writer = response.getWriter();
            if (!Strings.isNullOrEmpty(header)) {
                writer.print(header);
            }
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return;
        }
        super.commence(request, response, authException);
    }

    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        String requestURI = request.getRequestURI();
        String loginFormUrl = super.determineUrlToUseForThisRequest(request, response, exception);
        return MessageFormat.format("{0}?redirect={1}", loginFormUrl, requestURI);
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }
}