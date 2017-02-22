package com.tuotiansudai.console.dto;


import com.tuotiansudai.console.repository.model.InvestRepayExperienceView;
import com.tuotiansudai.repository.model.RepayStatus;

import java.io.Serializable;
import java.util.Date;

public class InvestRepayExperiencePaginationItemDto implements Serializable{

    private String mobile;
    private long experienceBalance;
    private Date repayDate;
    private Date actualRepayDate;
    private long expectedInterest;
    private long actualInterest;
    private RepayStatus status;

    public InvestRepayExperiencePaginationItemDto(){}

    public InvestRepayExperiencePaginationItemDto(InvestRepayExperienceView investRepayExperienceView){
        this.mobile = investRepayExperienceView.getMobile();
        this.experienceBalance = investRepayExperienceView.getExperienceBalance();
        this.repayDate = investRepayExperienceView.getRepayDate();
        this.actualRepayDate = investRepayExperienceView.getActualRepayDate();
        this.expectedInterest = investRepayExperienceView.getExpectedInterest();
        this.actualInterest = investRepayExperienceView.getActualInterest();
        this.status = investRepayExperienceView.getStatus();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getExperienceBalance() {
        return experienceBalance;
    }

    public void setExperienceBalance(long experienceBalance) {
        this.experienceBalance = experienceBalance;
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

    public long getActualInterest() {
        return actualInterest;
    }

    public void setActualInterest(long actualInterest) {
        this.actualInterest = actualInterest;
    }

    public RepayStatus getStatus() {
        return status;
    }

    public void setStatus(RepayStatus status) {
        this.status = status;
    }
}
