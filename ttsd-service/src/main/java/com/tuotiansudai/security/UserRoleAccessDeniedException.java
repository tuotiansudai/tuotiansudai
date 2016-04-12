package com.tuotiansudai.security;


import org.springframework.security.access.AccessDeniedException;

public class UserRoleAccessDeniedException extends AccessDeniedException {

    public UserRoleAccessDeniedException(String msg) {
        super(msg);
    }

    public UserRoleAccessDeniedException(String msg, Throwable t) {
        super(msg, t);
    }
}
