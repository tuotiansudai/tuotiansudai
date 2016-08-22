package com.tuotiansudai.spring;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    private static Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
