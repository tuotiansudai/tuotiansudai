package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.*;

import java.io.Serializable;
import java.util.List;

public class EditUserDto implements Serializable {

    private String loginName;

    private String email;

    private String mobile;

    private String identityNumber;

    private String userName;

    private String referrer;

    private String bankCardNumber;

    private boolean isReferrerStaff;

    private UserStatus status;

    private List<Role> roles;

    private String autoInvestStatus;

    public EditUserDto(UserModel userModel, AccountModel accountModel, List<Role> roles) {
        this.loginName = userModel.getLoginName();
        this.email = userModel.getEmail();
        this.mobile = userModel.getMobile();
        if (accountModel != null) {
            this.identityNumber = accountModel.getIdentityNumber();
            this.userName = accountModel.getUserName();
        }
        if (roles != null) {
            this.roles = roles;
        }
        this.referrer = userModel.getReferrer();
        this.status = userModel.getStatus();
        this.autoInvestStatus = userModel.getAutoInvestStatus();
    }

    public EditUserDto() {
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getAutoInvestStatus() {
        return autoInvestStatus;
    }

    public void setAutoInvestStatus(String autoInvestStatus) {
        this.autoInvestStatus = autoInvestStatus;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public boolean getIsReferrerStaff() {
        return isReferrerStaff;
    }

    public void setReferrerStaff(boolean referrerStaff) {
        isReferrerStaff = referrerStaff;
    }
}
