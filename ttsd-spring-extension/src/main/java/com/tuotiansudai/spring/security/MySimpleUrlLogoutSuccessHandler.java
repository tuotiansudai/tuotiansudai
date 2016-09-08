package com.tuotiansudai.spring.security;

import com.tuotiansudai.spring.MyUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MySimpleUrlLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    static Logger logger = Logger.getLogger(MySimpleUrlLogoutSuccessHandler.class);

    @Autowired
    private SignInClient signInClient;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MyUser myUser = (MyUser) authentication.getPrincipal();

        setDefaultTargetUrl("/");
        super.onLogoutSuccess(request, response, authentication);

        signInClient.logout(myUser.getToken());
    }
}
