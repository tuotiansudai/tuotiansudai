package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserRoleModel implements Serializable {

    private String loginName;

    private Role role;

    private Date createdTime = new Date();

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public UserRoleModel(String loginName,Role role){
        this.loginName = loginName;
        this.role = role;
    }

    public UserRoleModel(){

    }
}
