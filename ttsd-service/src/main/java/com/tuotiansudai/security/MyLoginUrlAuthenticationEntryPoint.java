package com.tuotiansudai.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        String requestURI = request.getRequestURI();
        String loginFormUrl = super.determineUrlToUseForThisRequest(request, response, exception);
        return MessageFormat.format("{0}?redirect={1}", loginFormUrl, requestURI);
    }
}
