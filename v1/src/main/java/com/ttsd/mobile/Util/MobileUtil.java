package com.ttsd.mobile.Util;

import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2015/7/15.
 */
@Component
public class MobileUtil {

    private String loginUserId;

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        return attrs.getRequest();
    }

    public static HttpSession getSession() {
        HttpSession session = null;
        try {
            session = getRequest().getSession();
        } catch (Exception e) {}
        return session;
    }

    public static Object getSessionAttribute(String name) {
        return getSession().getAttribute(name);
    }

    public String getLoginUserId() {
        // 获取当前登录用户的id
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) getSessionAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContextImpl != null) {
            loginUserId = securityContextImpl.getAuthentication().getName();
        }
        return loginUserId;
    }

}
