package com.tuotiansudai.spring;


import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationManager {

    @Autowired
    private UserDetailsService userDetailsService;

    public void createAuthentication(String loginName) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginName);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        token.setDetails(token.getDetails());

        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public void removeAuthentication() {
        AnonymousAuthenticationToken token = new AnonymousAuthenticationToken("anonymousUser",
                "anonymousUser",
                Lists.newArrayList(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));

        SecurityContextHolder.getContext().setAuthentication(token);
    }
}
