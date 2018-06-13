package com.tuotiansudai.spring.security;

import com.tuotiansudai.client.SignInClient;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.LoginUserInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@Component
public class MyPreAuthenticatedProcessingFilter extends GenericFilterBean {

    private final SignInClient signInClient = SignInClient.getInstance();

    private final MyAuthenticationUtil myAuthenticationUtil = MyAuthenticationUtil.getInstance();

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (logger.isDebugEnabled()) {
            logger.info("Checking secure context token: " + SecurityContextHolder.getContext().getAuthentication());
        }

        SignInResult signInResult = signInClient.verifyToken(LoginUserInfo.getToken(), Source.WEB);
        if (signInResult == null || !signInResult.isResult()) {
            myAuthenticationUtil.removeAuthentication();
        } else {
            myAuthenticationUtil.refreshGrantedAuthority(signInResult);
        }

        chain.doFilter(request, response);
    }
}
