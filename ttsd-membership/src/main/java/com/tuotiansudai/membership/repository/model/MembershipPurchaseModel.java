package com.tuotiansudai.membership.repository.model;

import com.tuotiansudai.membership.dto.MembershipPurchaseDto;
import com.tuotiansudai.repository.model.Source;

import java.io.Serializable;
import java.util.Date;

public class MembershipPurchaseModel implements Serializable {

    private long id;

    private String loginName;

    private String mobile;

    private String userName;

    private int level;

    private int duration;

    private long amount;

    private Source source;

    private MembershipPurchaseStatus status;

    private Date createdTime;

    public MembershipPurchaseModel() {
    }

    public MembershipPurchaseModel(long id, String loginName, String mobile, String userName, int level, int duration, long amount, Source source) {
        this.id = id;
        this.loginName = loginName;
        this.mobile = mobile;
        this.userName = userName;
        this.level = level;
        this.duration = duration;
        this.amount = amount;
        this.source = source;
        this.status = MembershipPurchaseStatus.WAIT_PAY;
        this.createdTime = new Date();
    }

    public MembershipPurchaseModel(long id, MembershipPurchaseDto dto) {
        this.id = id;
        this.loginName = dto.getLoginName();
        this.mobile = dto.getMobile();
        this.userName = dto.getUserName();
        this.level = dto.getLevel();
        this.duration = dto.getDuration();
        this.amount = dto.getAmount();
        this.source = dto.getSource();
        this.status = MembershipPurchaseStatus.WAIT_PAY;
        this.createdTime = new Date();
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

    public MembershipPurchaseStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipPurchaseStatus status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
