package com.tuotiansudai.spring;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class MyUser extends User {

    private String token;

    private String mobile;

    public MyUser(String token,
                  String username,
                  String password,
                  boolean enabled,
                  boolean accountNonExpired,
                  boolean credentialsNonExpired,
                  boolean accountNonLocked,
                  List<GrantedAuthority> authorities,
                  String mobile) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.token = token;
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String sessionId) {
        this.token = sessionId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
