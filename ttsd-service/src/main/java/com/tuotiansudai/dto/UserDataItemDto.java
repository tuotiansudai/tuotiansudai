package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.repository.model.UserStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserDataItemDto implements Serializable{
    private String loginName;
    private String userName;
    private String email;
    private String mobile;
    private String referrer;
    private Date registerTime;
    private List<UserRoleModel> userRoles;
    private UserStatus status;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public List<UserRoleModel> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRoleModel> userRoles) {
        this.userRoles = userRoles;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserDataItemDto(UserModel userModel){
        this.loginName = userModel.getLoginName();
        this.email = userModel.getEmail();
        this.referrer = userModel.getReferrer();
        this.mobile = userModel.getMobile();
        this.registerTime = userModel.getRegisterTime();
        if(userModel.getAccount() != null){
            this.userName = userModel.getAccount().getUserName();
        }
        this.status = userModel.getStatus();

    }
}

