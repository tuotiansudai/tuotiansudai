package com.tuotiansudai.spring.security;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class UserInfo implements Serializable {
    private String loginName;

    private String mobile;

    private List<String> roles;

    public String getLoginName() {
        return loginName;
    }

    @JsonProperty(value = "login_name")
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
