package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class PayrollModel implements Serializable {

    private long id;

    private String title;

    private long totalAmount;

    private long headCount;

    private String status;

    private PayrollStatusType payrollStatusType;

    private String remark;

    private Date grantTime;

    private Date createdTime = new Date();

    private Date updateTime;

    public PayrollModel() {
    }

    public PayrollModel(String title, long totalAmount, long headCount, String status, PayrollStatusType payrollStatusType, String remark, Date grantTime, Date createdTime, Date updateTime) {
        this.title = title;
        this.totalAmount = totalAmount;
        this.headCount = headCount;
        this.status = status;
        this.payrollStatusType = payrollStatusType;
        this.remark = remark;
        this.grantTime = grantTime;
        this.createdTime = createdTime;
        this.updateTime = updateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getHeadCount() {
        return headCount;
    }

    public void setHeadCount(long headCount) {
        this.headCount = headCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PayrollStatusType getPayrollStatusType() {
        return payrollStatusType;
    }

    public void setPayrollStatusType(PayrollStatusType payrollStatusType) {
        this.payrollStatusType = payrollStatusType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
