package com.tuotiansudai.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class UserInfo implements Serializable {
    @JsonProperty("login_name")
    private String loginName;
    private String mobile;
    @JsonProperty("last_login_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;
    @JsonProperty("last_login_source")
    private String lastLoginSource;

    private String email;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("identity_number")
    private String identityNumber;
    @JsonProperty("ump_user_name")
    private String umpUserName;
    @JsonProperty("ump_identity_number")
    private String umpIdentityNumber;
    @JsonProperty("register_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registerTime;
    @JsonProperty("last_modified_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModifiedTime;
    @JsonProperty("last_modified_user")
    private String lastModifiedUser;
    private String referrer;
    private String status;
    private String channel;
    private String province;
    private String city;
    private String source;

    public UserModel toUserModel() {
        UserModel userModel = new UserModel();
        userModel.setLoginName(loginName);
        userModel.setMobile(mobile);
        userModel.setLastLoginTime(lastLoginTime);
        userModel.setLastLoginSource(lastLoginSource == null ? null : Source.valueOf(lastLoginSource));
        userModel.setEmail(email);
        userModel.setUserName(userName);
        userModel.setIdentityNumber(identityNumber);
        userModel.setUmpUserName(umpUserName);
        userModel.setUmpIdentityNumber(umpIdentityNumber);
        userModel.setRegisterTime(registerTime);
        userModel.setLastModifiedTime(lastModifiedTime);
        userModel.setLastModifiedUser(lastModifiedUser);
        userModel.setReferrer(referrer);
        userModel.setStatus(status == null ? null : UserStatus.valueOf(status));
        userModel.setChannel(channel);
        userModel.setProvince(province);
        userModel.setCity(city);
        userModel.setSource(source == null ? null : Source.valueOf(source));
        return userModel;
    }

    public static UserInfo fromUserModel(UserModel userModel) {
        UserInfo userInfo = new UserInfo();
        userInfo.setLoginName(userModel.getLoginName());
        userInfo.setMobile(userModel.getMobile());
        userInfo.setLastLoginTime(userModel.getLastLoginTime());
        userInfo.setLastLoginSource(userModel.getLastLoginSource() == null ? null : String.valueOf(userModel.getLastLoginSource()));
        userInfo.setEmail(userModel.getEmail());
        userInfo.setUserName(userModel.getUserName());
        userInfo.setIdentityNumber(userModel.getIdentityNumber());
        userInfo.setUmpUserName(userModel.getUmpUserName());
        userInfo.setUmpIdentityNumber(userModel.getUmpIdentityNumber());
        userInfo.setRegisterTime(userModel.getRegisterTime());
        userInfo.setLastModifiedTime(userModel.getLastModifiedTime());
        userInfo.setLastModifiedUser(userModel.getLastModifiedUser());
        userInfo.setReferrer(userModel.getReferrer());
        userInfo.setStatus(String.valueOf(userModel.getStatus()));
        userInfo.setChannel(userModel.getChannel());
        userInfo.setProvince(userModel.getProvince());
        userInfo.setCity(userModel.getCity());
        userInfo.setSource(userModel.getSource() == null ? null : String.valueOf(userModel.getSource()));
        return userInfo;

    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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

    public String getLastLoginSource() {
        return lastLoginSource;
    }

    public void setLastLoginSource(String lastLoginSource) {
        this.lastLoginSource = lastLoginSource;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
