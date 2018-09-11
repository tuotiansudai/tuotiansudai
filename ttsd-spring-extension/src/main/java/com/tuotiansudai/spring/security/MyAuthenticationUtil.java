package com.tuotiansudai.spring.security;


import com.google.common.collect.Sets;
import com.tuotiansudai.client.SignInClient;
import com.tuotiansudai.dto.SignInResult;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.spring.MyUser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class MyAuthenticationUtil {

    private static Logger logger = Logger.getLogger(MyAuthenticationUtil.class);

    private final static MyAuthenticationUtil self = new MyAuthenticationUtil();

    private final SignInClient signInClient = SignInClient.getInstance();

    private MyAuthenticationUtil() {
    }

    public static MyAuthenticationUtil getInstance() {
        return self;
    }

    public String createAuthentication(String username, Source source, String xForwardedForHeader) {
        SignInResult signInResult = signInClient.loginNoPassword(username, source, xForwardedForHeader);

        if (signInResult == null || !signInResult.isResult()) {
            return "";
        }

        List<GrantedAuthority> grantedAuthorities = signInResult.getUserInfo().getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

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

    public void refreshGrantedAuthority(SignInResult signInResult) {
        if (signInResult == null || !signInResult.isResult()) {
            return;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        HashSet<String> authorities = authentication != null ? Sets.newHashSet(authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())) : Sets.newHashSet();
        HashSet<String> roles = Sets.newHashSet(signInResult.getUserInfo().getRoles());
        boolean isSame = CollectionUtils.isNotEmpty(authorities) && Sets.difference(authorities, roles).isEmpty() && Sets.difference(roles, authorities).isEmpty();

        if (isSame) {
            return;
        }

        List<GrantedAuthority> grantedAuthorities = signInResult.getUserInfo().getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                new MyUser(signInResult.getToken(), signInResult.getUserInfo().getLoginName(), "", true, true, true, true, grantedAuthorities, signInResult.getUserInfo().getMobile()),
                "",
                grantedAuthorities);
        authenticationToken.setDetails(authenticationToken.getDetails());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
