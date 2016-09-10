package com.tuotiansudai.spring.security;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.MyUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Component
public class MyAuthenticationUtil {

    static Logger logger = Logger.getLogger(MyAuthenticationUtil.class);

    @Autowired
    private SignInClient signInClient;

    @Autowired
    private HttpServletRequest httpServletRequest;

    public String createAuthentication(String username, Source source) {
        SignInResult signInResult = this.signInClient.loginNoPassword(username, source);

        if (signInResult != null && signInResult.isResult()) {
            List<GrantedAuthority> grantedAuthorities = Lists.transform(signInResult.getUserInfo().getRoles(), new Function<String, GrantedAuthority>() {
                @Override
                public GrantedAuthority apply(String role) {
                    return new SimpleGrantedAuthority(role);
                }
            });

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    new MyUser(signInResult.getToken(), signInResult.getUserInfo().getLoginName(), "", true, true, true, true, grantedAuthorities, signInResult.getUserInfo().getMobile()),
                    "",
                    grantedAuthorities);
            authenticationToken.setDetails(authenticationToken.getDetails());

            if (Source.WEB == source) {
                httpServletRequest.setAttribute("newSessionId", signInResult.getToken());
                httpServletRequest.changeSessionId();
            }

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            return signInResult.getToken();
        }

        return "";
    }

    public void removeAuthentication() {
        SecurityContextHolder.clearContext();

        HttpSession session = httpServletRequest.getSession(false);

        if (session != null) {
            logger.debug("Invalidating existing session");
            session.invalidate();
            httpServletRequest.getSession();
        }
    }
}
