package com.tuotiansudai.dto;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.Source;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class RegisterUserDto implements Serializable {

    @Pattern(regexp = "(?!^\\d+$)^\\w{5,25}$")
    private String loginName;

    @NotEmpty
    @Pattern(regexp = "^1\\d{10}$")
    private String mobile;

    @NotEmpty
    @Pattern(regexp = "^[0-9]{6}$")
    private String captcha;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[^\\d])(.{6,20})$")
    private String password;

    private String openid;

    private String referrer;

    private String channel;

    private Source source = Source.WEB;

    private String redirectToAfterRegisterSuccess = "/";

    private String activityReferrer;

    @AssertTrue
    private boolean agreement;

    public String getLoginName() {
        if (Strings.isNullOrEmpty(loginName)) {
            return loginName;
        }
        return loginName.toLowerCase();
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

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getReferrer() {
        if (Strings.isNullOrEmpty(referrer)) {
            return referrer;
        }
        return referrer.toLowerCase();
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public boolean getAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
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

    public String getRedirectToAfterRegisterSuccess() {
        return redirectToAfterRegisterSuccess;
    }

    public void setRedirectToAfterRegisterSuccess(String redirectToAfterRegisterSuccess) {
        this.redirectToAfterRegisterSuccess = redirectToAfterRegisterSuccess;
    }

    public String getActivityReferrer() {
        return activityReferrer;
    }

    public void setActivityReferrer(String activityReferrer) {
        this.activityReferrer = activityReferrer;
    }
}
