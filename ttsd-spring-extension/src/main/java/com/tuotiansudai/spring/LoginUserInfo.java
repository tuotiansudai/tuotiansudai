package com.tuotiansudai.spring;

import com.tuotiansudai.enums.Role;
import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public class LoginUserInfo {

    private final static Logger logger = Logger.getLogger(LoginUserInfo.class);

    public static String getLoginName() {
        Object principal = LoginUserInfo.getPrincipal();

        if (principal instanceof User) {
            try {
                Class<?> aClass = principal.getClass();
                Method method = aClass.getMethod("getUsername");
                return  (String) method.invoke(principal);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        return null;
    }

    public static String getMobile() {
        Object principal = LoginUserInfo.getPrincipal();

        if (principal instanceof User) {
            try {
                Class<?> aClass = principal.getClass();
                Method method = aClass.getMethod("getMobile");
                return  (String) method.invoke(principal);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        return null;
    }

    public static String getToken() {
        Object principal = LoginUserInfo.getPrincipal();

        if (principal instanceof User) {
            try {
                Class<?> aClass = principal.getClass();
                Method method = aClass.getMethod("getToken");
                return  (String) method.invoke(principal);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        return null;
    }

    @SuppressWarnings(value = "unchecked")
    public static Role getBankRole() {
        Object principal = LoginUserInfo.getPrincipal();

        if (principal instanceof User) {
            try {
                Class<?> aClass = principal.getClass();
                Method method = aClass.getMethod("getAuthorities");
                Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) method.invoke(principal);
                boolean isInvestor = authorities.stream().anyMatch(authority -> authority.getAuthority().equals(Role.INVESTOR.name()));
                if (isInvestor) {
                    return Role.INVESTOR;
                }
                boolean isLoaner = authorities.stream().anyMatch(authority -> authority.getAuthority().equals(Role.LOANER.name()));
                if (isLoaner) {
                    return Role.LOANER;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }

        return null;
    }

    private static Object getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getPrincipal() : null;
    }
}
