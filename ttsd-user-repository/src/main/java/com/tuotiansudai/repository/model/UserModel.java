package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class UserModel implements UserRegisterInfo, Serializable {

    private long id;

    private String loginName;

    private String password;

    private String email;

    private String mobile;

    private String umpUserName;

    private String umpIdentityNumber;

    private Date registerTime = new Date();

    private Date lastModifiedTime;

    private String lastModifiedUser;

    private String referrer;

    private UserStatus status = UserStatus.ACTIVE;

    private String salt;

    private Source source;

    private String channel;

    private String province;

    private String city;

    private Date lastLoginTime;

    private Source lastLoginSource;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getLastModifiedUser() {
        return lastModifiedUser;
    }

    public void setLastModifiedUser(String lastModifiedUser) {
        this.lastModifiedUser = lastModifiedUser;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Source getLastLoginSource() {
        return lastLoginSource;
    }

    public void setLastLoginSource(Source lastLoginSource) {
        this.lastLoginSource = lastLoginSource;
    }

    @Override
    public UserModel clone() throws CloneNotSupportedException {
        UserModel clone = (UserModel) super.clone();
        clone.registerTime = this.registerTime != null ? (Date) this.registerTime.clone() : null;
        clone.lastModifiedTime = this.lastModifiedTime != null ? (Date) this.lastModifiedTime.clone() : null;
        clone.lastLoginTime = this.lastLoginTime != null ? (Date) this.lastLoginTime.clone() : null;
        return clone;
    }
}
