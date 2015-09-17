package com.ttsd.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtils {
    public static String getLoginUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            return authentication.getName();
        } else {
            return null;
        }
    }

    public static boolean isAnonymousUser(String userId){
        return "anonymousUser".equalsIgnoreCase(userId);
    }
}
