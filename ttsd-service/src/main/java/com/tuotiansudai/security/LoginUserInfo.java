package com.tuotiansudai.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class LoginUserInfo {

    public String getLoginName() {
        Object principal = this.getPrincipal();

        if (principal instanceof MyUser) {
            return ((MyUser) principal).getUsername();
        }

        return null;
    }

    public String getMobile() {
        Object principal = this.getPrincipal();

        if (principal instanceof MyUser) {
            return ((MyUser) principal).getMobile();
        }

        return null;
    }

    private Object getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal();
    }
}
