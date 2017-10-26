package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class PayrollDetailModel implements Serializable {

    private long id;

    private long payrollId;

    private String loginName;

    private String userName;

    private String mobile;

    private long amount;

    private PayrollPayStatus status;

    private Date createdTime = new Date();

    public PayrollDetailModel() {
    }

    public PayrollDetailModel(String userName, String mobile, long amount) {
        this.userName = userName;
        this.mobile = mobile;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(long payrollId) {
        this.payrollId = payrollId;
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

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public PayrollPayStatus getStatus() {
        return status;
    }

    public void setStatus(PayrollPayStatus status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
