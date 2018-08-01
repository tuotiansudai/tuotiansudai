package com.tuotiansudai.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserStatus;

import java.io.Serializable;
import java.util.Date;

public class UpdateUserInfoRequestDto implements Serializable {
    @JsonProperty("login_name")
    private final String loginName;
    private String mobile;
    private String email;
    @JsonProperty("user_name")
    private String umpUserName;
    private String password;
    private String referrer;
    private String channel;
    private Source source;
    @JsonProperty("identity_number")
    private String umpIdentityNumber;
    @JsonProperty("last_modified_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModifiedTime;
    @JsonProperty("last_modified_user")
    private String lastModifiedUser;
    private UserStatus status;
    private String province;
    private String city;
    @JsonProperty("sign_in_count")
    private Integer signInCount;

    public UpdateUserInfoRequestDto(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
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

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
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

    public Integer getSignInCount() {
        return signInCount;
    }

    public void setSignInCount(Integer signInCount) {
        this.signInCount = signInCount;
    }
}
