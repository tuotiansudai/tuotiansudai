package com.tuotiansudai.security;


import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyAuthenticationManager {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    public void createAuthentication(String username) {
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        token.setDetails(token.getDetails());

        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public void removeAuthentication() {
        List<GrantedAuthority> grantedAuthorities = Lists.newArrayList();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
        UserDetails userDetails = new MyUser("anonymousUser", "anonymousUser", true, true, true, true, grantedAuthorities, "", "");
        AnonymousAuthenticationToken authentication = new AnonymousAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword(), grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
