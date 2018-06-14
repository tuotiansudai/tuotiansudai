package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.Source;

import java.io.Serializable;
import java.util.Date;

public class RechargePaginationItemDataDto implements Serializable {

    private long rechargeId;

    private String status;

    private Date createdTime;

    private String loginName;

    private String userName;

    private String mobile;

    private String isStaff;

    private String amount;

    private boolean isFastPay;

    private Source source;

    private String channel;

    public RechargePaginationItemDataDto() {
    }

    public RechargePaginationItemDataDto(long rechargeId, String status, Date createdTime, String loginName, String userName, String mobile, String isStaff, String amount, String payType, Source source, String channel) {
        this.rechargeId = rechargeId;
        this.status = status;
        this.createdTime = createdTime;
        this.loginName = loginName;
        this.userName = userName;
        this.mobile = mobile;
        this.isStaff = isStaff;
        this.amount = amount;
        this.isFastPay = "FAST_PAY".equals(payType);
        this.source = source;
        this.channel = channel;
    }

    public long getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(long rechargeId) {
        this.rechargeId = rechargeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(String isStaff) {
        this.isStaff = isStaff;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public boolean isFastPay() {
        return isFastPay;
    }

    public void setFastPay(boolean fastPay) {
        isFastPay = fastPay;
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
}
