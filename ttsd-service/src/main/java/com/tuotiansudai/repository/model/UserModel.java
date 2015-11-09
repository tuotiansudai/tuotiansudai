package com.tuotiansudai.repository.model;


import java.util.Date;

public class UserModel implements Cloneable {

    private long id;

    private String loginName;

    private String password;

    private String email;

    private String mobile;

    private Date registerTime = new Date();

    private Date lastLoginTime;

    private Date lastModifiedTime;

    private String lastModifiedUser;

    private String avatar;

    private String referrer;

    private UserStatus status = UserStatus.ACTIVE;

    private String salt;

    private AccountModel account;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

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

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    public AccountModel getAccount() {
        return account;
    }

    public void setAccount(AccountModel account) {
        this.account = account;
    }

    @Override
    public UserModel clone() throws CloneNotSupportedException {
        UserModel clone = (UserModel) super.clone();
        clone.registerTime = this.registerTime != null ? (Date) this.registerTime.clone() : null;
        clone.lastLoginTime = this.lastLoginTime != null ?  (Date) this.lastLoginTime.clone() : null;
        clone.lastModifiedTime = this.lastModifiedTime != null ? (Date) this.lastModifiedTime.clone() : null;
        return clone;
    }
}
