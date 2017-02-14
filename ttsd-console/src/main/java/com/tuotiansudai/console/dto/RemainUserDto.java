package com.tuotiansudai.console.dto;

import com.tuotiansudai.console.repository.model.RemainUserView;
import com.tuotiansudai.util.AmountConverter;

import java.util.Date;

public class RemainUserDto {
    private String loginName;
    private String mobile;
    private String userName;
    private Date registerTime;
    private boolean useExperienceCoupon;
    private Date experienceTime;
    private int investCount;
    private String investSum;
    private Date firstInvestTime;
    private String firstInvestAmount;
    private Date secondInvestTime;
    private String secondInvestAmount;

    public RemainUserDto() {
    }

    public RemainUserDto(RemainUserView remainUserView) {
        this.loginName = remainUserView.getLoginName();
        this.mobile = remainUserView.getMobile();
        this.userName = remainUserView.getUserName();
        this.registerTime = remainUserView.getRegisterTime();
        this.useExperienceCoupon = remainUserView.isUseExperienceCoupon();
        this.experienceTime = remainUserView.getExperienceTime();
        this.investCount = remainUserView.getInvestCount();
        this.investSum = AmountConverter.convertCentToString(remainUserView.getInvestSum());
        this.firstInvestTime = remainUserView.getFirstInvestTime();
        this.firstInvestAmount = AmountConverter.convertCentToString(remainUserView.getFirstInvestAmount());
        this.secondInvestTime = remainUserView.getSecondInvestTime();
        this.secondInvestAmount = AmountConverter.convertCentToString(remainUserView.getSecondInvestAmount());
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public boolean isUseExperienceCoupon() {
        return useExperienceCoupon;
    }

    public void setUseExperienceCoupon(boolean useExperienceCoupon) {
        this.useExperienceCoupon = useExperienceCoupon;
    }

    public Date getExperienceTime() {
        return experienceTime;
    }

    public void setExperienceTime(Date experienceTime) {
        this.experienceTime = experienceTime;
    }

    public int getInvestCount() {
        return investCount;
    }

    public void setInvestCount(int investCount) {
        this.investCount = investCount;
    }

    public String getInvestSum() {
        return investSum;
    }

    public void setInvestSum(String investSum) {
        this.investSum = investSum;
    }

    public Date getFirstInvestTime() {
        return firstInvestTime;
    }

    public void setFirstInvestTime(Date firstInvestTime) {
        this.firstInvestTime = firstInvestTime;
    }

    public String getFirstInvestAmount() {
        return firstInvestAmount;
    }

    public void setFirstInvestAmount(String firstInvestAmount) {
        this.firstInvestAmount = firstInvestAmount;
    }

    public Date getSecondInvestTime() {
        return secondInvestTime;
    }

    public void setSecondInvestTime(Date secondInvestTime) {
        this.secondInvestTime = secondInvestTime;
    }

    public String getSecondInvestAmount() {
        return secondInvestAmount;
    }

    public void setSecondInvestAmount(String secondInvestAmount) {
        this.secondInvestAmount = secondInvestAmount;
    }
}
