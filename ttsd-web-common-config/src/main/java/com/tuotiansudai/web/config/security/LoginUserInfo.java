package com.tuotiansudai.web.config.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class LoginUserInfo {

    public static String getLoginName() {
        Object principal = LoginUserInfo.getPrincipal();

        if (principal instanceof User) {
            return ((User) principal).getUsername();
        }

        return null;
    }

    private static Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
