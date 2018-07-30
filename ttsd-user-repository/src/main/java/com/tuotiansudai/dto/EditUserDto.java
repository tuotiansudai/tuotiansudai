package com.tuotiansudai.dto;


import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;

import java.io.Serializable;
import java.util.List;

public class EditUserDto implements Serializable {

    private String loginName;

    private String email;

    private String mobile;

    private String identityNumber;

    private String userName;

    private String referrer;

    private String bankCardNumberUMP;

    private boolean isReferrerStaff;

    private UserStatus status;

    private List<Role> roles;

    private String autoInvestStatus;

    private String bankCardNumberInvestor;

    private String bankCardNumberLoaner;

    private String umpUserName;

    private String umpIdentityNumber;

    public EditUserDto(UserModel userModel, List<Role> roles, boolean autoInvestPlanEnabled) {
        this.loginName = userModel.getLoginName();
        this.email = userModel.getEmail();
        this.mobile = userModel.getMobile();
        this.identityNumber = userModel.getIdentityNumber();
        this.userName = userModel.getUserName();
        if (roles != null) {
            this.roles = roles;
        }
        this.umpIdentityNumber=userModel.getUmpIdentityNumber();
        this.umpUserName=userModel.getUmpUserName();
        this.referrer = userModel.getReferrer();
        this.status = userModel.getStatus();
        this.autoInvestStatus = autoInvestPlanEnabled ? "1" : "0";
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

    public boolean getIsReferrerStaff() {
        return isReferrerStaff;
    }

    public void setReferrerStaff(boolean referrerStaff) {
        isReferrerStaff = referrerStaff;
    }

    public String getBankCardNumberUMP() {
        return bankCardNumberUMP;
    }

    public void setBankCardNumberUMP(String bankCardNumberUMP) {
        this.bankCardNumberUMP = bankCardNumberUMP;
    }

    public String getBankCardNumberInvestor() {
        return bankCardNumberInvestor;
    }

    public void setBankCardNumberInvestor(String bankCardNumberInvestor) {
        this.bankCardNumberInvestor = bankCardNumberInvestor;
    }

    public String getBankCardNumberLoaner() {
        return bankCardNumberLoaner;
    }

    public void setBankCardNumberLoaner(String bankCardNumberLoaner) {
        this.bankCardNumberLoaner = bankCardNumberLoaner;
    }

    public String getUmpUserName() {
        return umpUserName;
    }

    public void setUmpUserName(String umpUserName) {
        this.umpUserName = umpUserName;
    }

    public String getUmpIdentityNumber() {
        return umpIdentityNumber;
    }

    public void setUmpIdentityNumber(String umpIdentityNumber) {
        this.umpIdentityNumber = umpIdentityNumber;
    }
}
