package com.tuotiansudai.membership.dto;

import com.tuotiansudai.repository.model.Source;

public class MembershipPurchaseDto {

    private String loginName;

    private String mobile;

    private String userName;

    private int level;

    private int duration;

    private long amount;

    private Source source;

    public MembershipPurchaseDto() {
    }

    public MembershipPurchaseDto(String loginName, String mobile, String userName, int level, int duration, long amount, Source source) {
        this.loginName = loginName;
        this.mobile = mobile;
        this.userName = userName;
        this.level = level;
        this.duration = duration;
        this.amount = amount;
        this.source = source;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
