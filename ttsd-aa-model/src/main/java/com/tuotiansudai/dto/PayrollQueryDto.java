package com.tuotiansudai.dto;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.PayrollStatusType;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;

public class PayrollQueryDto implements Serializable {

    private Date createStartTime;
    private Date createEndTime;
    private Date sendStartTime;
    private Date sendEndTime;
    private String amountMin;
    private String amountMax;
    private PayrollStatusType payrollStatusType;
    private String title;
    private Integer index;
    private Integer pageSize;

    public PayrollQueryDto() {
    }

    public Date getCreateStartTime() {
        return createStartTime;
    }

    public void setCreateStartTime(String createStartTime) {
        this.createStartTime = Strings.isNullOrEmpty(createStartTime) ? null : new DateTime(createStartTime).withTimeAtStartOfDay().toDate();
    }

    public Date getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(String createEndTime) {
        this.createEndTime = Strings.isNullOrEmpty(createEndTime) ? null : new DateTime(createEndTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
    }

    public Date getSendStartTime() {
        return sendStartTime;
    }

    public void setSendStartTime(String sendStartTime) {
        this.sendStartTime = Strings.isNullOrEmpty(sendStartTime) ? null : new DateTime(sendStartTime).withTimeAtStartOfDay().toDate();
    }

    public Date getSendEndTime() {
        return sendEndTime;
    }

    public void setSendEndTime(String sendEndTime) {
        this.sendEndTime = Strings.isNullOrEmpty(sendEndTime) ? null : new DateTime(sendEndTime).withTimeAtStartOfDay().plusDays(1).minusMillis(1).toDate();
    }

    public String getAmountMin() {
        return amountMin;
    }

    public void setAmountMin(String amountMin) {
        this.amountMin = amountMin;
    }

    public String getAmountMax() {
        return amountMax;
    }

    public void setAmountMax(String amountMax) {
        this.amountMax = amountMax;
    }

    public PayrollStatusType getPayrollStatusType() {
        return payrollStatusType;
    }

    public void setPayrollStatusType(PayrollStatusType payrollStatusType) {
        this.payrollStatusType = payrollStatusType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIndex() {
        return index == null ? 1 : index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setPageSize(Integer pageSize){
        this.pageSize = pageSize;
    }

    public Integer getPageSize() {
        return pageSize == null ? 10 : pageSize;
    }
}
