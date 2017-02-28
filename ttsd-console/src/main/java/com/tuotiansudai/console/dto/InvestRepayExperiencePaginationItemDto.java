package com.tuotiansudai.console.dto;


import com.tuotiansudai.console.repository.model.InvestRepayExperienceView;
import com.tuotiansudai.repository.model.RepayStatus;

import java.io.Serializable;
import java.util.Date;

public class InvestRepayExperiencePaginationItemDto implements Serializable{

    private String mobile;
    private long amount;
    private Date repayDate;
    private Date actualRepayDate;
    private long expectedInterest;
    private long repayAmount;
    private RepayStatus status;

    public InvestRepayExperiencePaginationItemDto(){}

    public InvestRepayExperiencePaginationItemDto(InvestRepayExperienceView investRepayExperienceView){
        this.mobile = investRepayExperienceView.getMobile();
        this.amount = investRepayExperienceView.getAmount();
        this.repayDate = investRepayExperienceView.getRepayDate();
        this.actualRepayDate = investRepayExperienceView.getActualRepayDate();
        this.expectedInterest = investRepayExperienceView.getExpectedInterest();
        this.repayAmount = investRepayExperienceView.getRepayAmount();
        this.status = investRepayExperienceView.getStatus();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public Date getActualRepayDate() {
        return actualRepayDate;
    }

    public void setActualRepayDate(Date actualRepayDate) {
        this.actualRepayDate = actualRepayDate;
    }

    public long getExpectedInterest() {
        return expectedInterest;
    }

    public void setExpectedInterest(long expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public RepayStatus getStatus() {
        return status;
    }

    public void setStatus(RepayStatus status) {
        this.status = status;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(long repayAmount) {
        this.repayAmount = repayAmount;
    }
}
