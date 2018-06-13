package com.tuotiansudai.dto;

import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.repository.model.RechargeModel;
import com.tuotiansudai.repository.model.RechargePaginationView;
import com.tuotiansudai.util.AmountConverter;

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

    public RechargePaginationItemDataDto() {
    }

    public RechargePaginationItemDataDto(long rechargeId, BankRechargeStatus status, String loginName, String userName, String mobile,) {

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
}
