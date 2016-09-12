package com.tuotiansudai.spring.security;

import com.google.common.collect.Lists;
import com.tuotiansudai.spring.MyUser;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class LoginDto {
    private boolean status;

    private String message;

    private String loginName;

    private String mobile;

    private List<String> roles = Lists.newArrayList();

    public LoginDto(MyUser myUser) {
        this.status = true;
        this.loginName = myUser.getUsername();
        this.mobile = myUser.getMobile();
        for (GrantedAuthority grantedAuthority : myUser.getAuthorities()) {
            this.roles.add(grantedAuthority.getAuthority());
        }
    }

    public LoginDto(AuthenticationException exception) {
        this.status = false;
        this.message = exception.getMessage();
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public List<String> getRoles() {
        return roles;
    }
}
