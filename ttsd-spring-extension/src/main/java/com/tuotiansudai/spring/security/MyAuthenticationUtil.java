package com.tuotiansudai.spring.security;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.MyUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyAuthenticationUtil {

    static Logger logger = Logger.getLogger(MyAuthenticationUtil.class);

    @Autowired
    private SignInClient signInClient;

    public String createAuthentication(String username, Source source) {
        SignInResult signInResult = this.signInClient.loginNoPassword(username, source);

        if (signInResult == null || !signInResult.isResult()) {
            return "";
        }

        List<GrantedAuthority> grantedAuthorities = Lists.transform(signInResult.getUserInfo().getRoles(), (Function<String, GrantedAuthority>) SimpleGrantedAuthority::new);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                new MyUser(signInResult.getToken(), signInResult.getUserInfo().getLoginName(), "", true, true, true, true, grantedAuthorities, signInResult.getUserInfo().getMobile()),
                "",
                grantedAuthorities);
        authenticationToken.setDetails(authenticationToken.getDetails());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        return signInResult.getToken();
    }

    public void removeAuthentication() {
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        AnonymousAuthenticationToken authenticationToken = new AnonymousAuthenticationToken("anonymousUser", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
