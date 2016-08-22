package com.tuotiansudai.spring;


import org.springframework.security.access.AccessDeniedException;

public class UserRoleAccessDeniedException extends AccessDeniedException {

    public UserRoleAccessDeniedException(String msg) {
        super(msg);
    }

}
