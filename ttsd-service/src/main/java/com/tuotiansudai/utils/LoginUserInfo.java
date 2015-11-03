package com.tuotiansudai.utils;

import com.google.common.base.Strings;
import com.tuotiansudai.security.MyUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginUserInfo {

    //用于测试Mock登录人
    private static String loginName;

    //用于测试Mock手机号
    private static String mobile;

    public static String getLoginName() {
        if (!Strings.isNullOrEmpty(loginName)) {
            return loginName;
        }

        Object principal = LoginUserInfo.getPrincipal();

        if (principal instanceof MyUser) {
            return ((MyUser) principal).getUsername();
        }

        return null;
    }

    public static String getMobile() {
        if (!Strings.isNullOrEmpty(mobile)) {
            return mobile;
        }
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

    //测试Mock
    public static void setLoginName(String loginName) {
        LoginUserInfo.loginName = loginName;
    }

    //测试Mock
    public static void setMobile(String mobile) {
        LoginUserInfo.mobile = mobile;
    }
}
