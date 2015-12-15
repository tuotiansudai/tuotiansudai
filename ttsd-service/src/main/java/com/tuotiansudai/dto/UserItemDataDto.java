package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRoleModel;
import com.tuotiansudai.repository.model.UserStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserItemDataDto implements Serializable {
    private String loginName;
    private String userName;
    private String email;
    private String mobile;
    private String referrer;
    private String channel;
    private Date registerTime;
    private List<UserRoleModel> userRoles;
    private UserStatus status;
    private Source source;
    private boolean isStaff;
    private boolean isBankCard;

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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
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

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public void setStaff(boolean isStaff) {
        this.isStaff = isStaff;
    }

    public boolean isBankCard() {
        return isBankCard;
    }

    public void setBankCard(boolean isBankCard) {
        this.isBankCard = isBankCard;
    }

    public UserItemDataDto(UserModel userModel) {
        this.loginName = userModel.getLoginName();
        this.email = userModel.getEmail();
        this.referrer = userModel.getReferrer();
        this.channel = userModel.getChannel();
        this.source = userModel.getSource();
        this.mobile = userModel.getMobile();
        this.registerTime = userModel.getRegisterTime();
        if (userModel.getAccount() != null) {
            this.userName = userModel.getAccount().getUserName();
        }
        this.status = userModel.getStatus();

    }
}

