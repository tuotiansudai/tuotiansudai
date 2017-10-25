package com.tuotiansudai.repository.model;

import java.io.Serializable;
import java.util.Date;

public class PayrollModel implements Serializable {

    private long id;

    private String title;

    private long totalAmount;

    private long headCount;

    private PayrollStatusType status;

    private String remark;

    private Date grantTime;

    private Date createdTime = new Date();

    private Date updatedTime;

    public PayrollModel() {
    }

    public PayrollModel(String title, long totalAmount, long headCount, PayrollStatusType status, String remark, Date grantTime, Date createdTime, Date updatedTime) {
        this.title = title;
        this.totalAmount = totalAmount;
        this.headCount = headCount;
        this.status = status;
        this.remark = remark;
        this.grantTime = grantTime;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
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

    public PayrollStatusType getStatus() {
        return status;
    }

    public void setStatus(PayrollStatusType status) {
        this.status = status;
    }

    public Date getGrantTime() {
        return grantTime;
    }

    public void setGrantTime(Date grantTime) {
        this.grantTime = grantTime;
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

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updateTime) {
        this.updatedTime = updatedTime;
    }
}
