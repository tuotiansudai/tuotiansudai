package com.tuotiansudai.spring.security;

import com.tuotiansudai.client.SignInClient;
import com.tuotiansudai.spring.MyUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

@Component
public class MySimpleUrlLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    private final static Logger logger = Logger.getLogger(MySimpleUrlLogoutSuccessHandler.class);

    private final SignInClient signInClient = SignInClient.getInstance();

    @Value("${web.server}")
    private String webServer;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MyUser myUser = (MyUser) authentication.getPrincipal();

        setDefaultTargetUrl("/");
        if (request.getParameter("impersonateSecurityCode") != null) {
            setDefaultTargetUrl(MessageFormat.format("{0}/impersonate/security-code/{1}", webServer, request.getParameter("impersonateSecurityCode")));
        }
        super.onLogoutSuccess(request, response, authentication);

        signInClient.logout(myUser.getToken());
    }
}
