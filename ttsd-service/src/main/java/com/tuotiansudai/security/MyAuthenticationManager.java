package com.tuotiansudai.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

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
}
