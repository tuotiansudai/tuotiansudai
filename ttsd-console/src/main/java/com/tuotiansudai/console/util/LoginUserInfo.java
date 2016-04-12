package com.tuotiansudai.console.util;

import com.tuotiansudai.security.MyUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginUserInfo {

    public static String getLoginName() {
        Object principal = LoginUserInfo.getPrincipal();

        if (principal instanceof MyUser) {
            return ((MyUser) principal).getUsername();
        }

        return null;
    }

    public static String getMobile() {
        Object principal = LoginUserInfo.getPrincipal();

        if (principal instanceof MyUser) {
            return ((MyUser) principal).getMobile();
        }

        return null;
    }

    private static Object getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal();
    }
}