package com.tuotiansudai.transfer.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.model.InvestPaginationItemView;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.util.AmountConverter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

public class TransferableInvestPaginationItemDataDto implements Serializable {
    private long investId;

    private long loanId;

    private String loanName;

    private String amount;

    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private Date nextRepayDate;

    private String nextRepayAmount;

    private String transferStatus;

    private String baseRate;

    private String activityRate;

    private String sumRate;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "Asia/Shanghai")
    private Date lastRepayDate;

    private int leftPeriod;

    public TransferableInvestPaginationItemDataDto(){

    }
     public TransferableInvestPaginationItemDataDto(TransferableInvestView transferableInvestView){
         this.investId = transferableInvestView.getInvestId();
         this.loanId = transferableInvestView.getLoanId();
         this.loanName = transferableInvestView.getLoanName();
         this.nextRepayDate = transferableInvestView.getNextRepayDate();
         this.nextRepayAmount = AmountConverter.convertCentToString(transferableInvestView.getNextRepayAmount());
         this.baseRate = String.valueOf(transferableInvestView.getBaseRate());
         this.activityRate = String.valueOf(transferableInvestView.getActivityRate());
         this.sumRate = transferableInvestView.getSumRatePercent();
         this.amount = AmountConverter.convertCentToString(transferableInvestView.getAmount());
         this.createdTime = transferableInvestView.getCreatedTime();
     }

    public long getInvestId() {
        return investId;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public String getAmount() {
        return amount;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public Date getNextRepayDate() {
        return nextRepayDate;
    }

    public String getNextRepayAmount() {
        return nextRepayAmount;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(String baseRate) {
        this.baseRate = baseRate;
    }

    public String getActivityRate() {
        return activityRate;
    }

    public void setActivityRate(String activityRate) {
        this.activityRate = activityRate;
    }

    public Date getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(Date lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public int getLeftPeriod() {
        return leftPeriod;
    }

    public void setLeftPeriod(int leftPeriod) {
        this.leftPeriod = leftPeriod;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public void setNextRepayDate(Date nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }

    public void setNextRepayAmount(String nextRepayAmount) {
        this.nextRepayAmount = nextRepayAmount;
    }

    public String getSumRate() {
        return sumRate;
    }

    public void setSumRate(String sumRate) {
        this.sumRate = sumRate;
    }

}
