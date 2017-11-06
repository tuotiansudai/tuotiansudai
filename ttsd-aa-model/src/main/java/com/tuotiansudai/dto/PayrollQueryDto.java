package com.tuotiansudai.dto;

import com.google.common.base.Strings;
import com.tuotiansudai.repository.model.PayrollStatusType;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class PayrollQueryDto implements Serializable {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createStartTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createEndTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sendStartTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date sendEndTime;
    private Long amountMin;
    private Long amountMax;
    private PayrollStatusType payrollStatusType;
    private String title;
    private Integer index;
    private Integer pageSize = 10;

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

    public Long getAmountMin() {
        return amountMin;
    }

    public void setAmountMin(Long amountMin) {
        this.amountMin = amountMin;
    }

    public Long getAmountMax() {
        return amountMax;
    }

    public void setAmountMax(Long amountMax) {
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
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public void setPageSize(Integer pageSize){
        this.pageSize = pageSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }
}
