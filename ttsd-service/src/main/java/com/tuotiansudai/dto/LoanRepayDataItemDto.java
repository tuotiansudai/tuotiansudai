package com.tuotiansudai.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tuotiansudai.repository.model.LoanRepayModel;
import com.tuotiansudai.repository.model.RepayStatus;
import com.tuotiansudai.utils.AmountUtil;

import java.util.Date;

public class LoanRepayDataItemDto {
    private long loanRepayId;

    private long loanId;

    private int period;

    private String corpus;

    private String expectedInterest;

    private String actualInterest;

    private String defaultInterest;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;

    private Date actualRepayDate;

    private RepayStatus loanRepayStatus;

    private boolean isEnabled;

    private String projectName;

    private String loginName;

    private String totalAmount;


    public LoanRepayDataItemDto(LoanRepayModel loanRepayModel, boolean isEnabled) {
        this.loanRepayId = loanRepayModel.getId();
        this.loanId = loanRepayModel.getLoanId();
        this.period = loanRepayModel.getPeriod();
        this.corpus = AmountUtil.convertCentToString(loanRepayModel.getCorpus());
        this.expectedInterest = AmountUtil.convertCentToString(loanRepayModel.getExpectedInterest());
        this.defaultInterest = AmountUtil.convertCentToString(loanRepayModel.getDefaultInterest());
        this.repayDate = loanRepayModel.getRepayDate();
        this.actualInterest = AmountUtil.convertCentToString(loanRepayModel.getActualInterest());
        this.actualRepayDate = loanRepayModel.getActualRepayDate();
        this.loanRepayStatus = loanRepayModel.getStatus();
        this.totalAmount = AmountUtil.convertCentToString(loanRepayModel.getCorpus() + loanRepayModel.getExpectedInterest() + loanRepayModel.getDefaultInterest());
        this.isEnabled = isEnabled;
        this.loginName = loanRepayModel.getLoan().getLoanerLoginName();
        this.projectName = loanRepayModel.getLoan().getName();
    }

    public long getLoanRepayId() {
        return loanRepayId;
    }

    public long getLoanId() {
        return loanId;
    }

    public int getPeriod() {
        return period;
    }

    public String getCorpus() {
        return corpus;
    }

    public String getExpectedInterest() {
        return expectedInterest;
    }

    public String getActualInterest() {
        return actualInterest;
    }

    public String getDefaultInterest() {
        return defaultInterest;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public Date getActualRepayDate() {
        return actualRepayDate;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setLoanRepayId(long loanRepayId) {
        this.loanRepayId = loanRepayId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public void setCorpus(String corpus) {
        this.corpus = corpus;
    }

    public void setExpectedInterest(String expectedInterest) {
        this.expectedInterest = expectedInterest;
    }

    public void setActualInterest(String actualInterest) {
        this.actualInterest = actualInterest;
    }

    public void setDefaultInterest(String defaultInterest) {
        this.defaultInterest = defaultInterest;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public void setActualRepayDate(Date actualRepayDate) {
        this.actualRepayDate = actualRepayDate;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public RepayStatus getLoanRepayStatus() {
        return loanRepayStatus;
    }

    public void setLoanRepayStatus(RepayStatus loanRepayStatus) {
        this.loanRepayStatus = loanRepayStatus;
    }
}
