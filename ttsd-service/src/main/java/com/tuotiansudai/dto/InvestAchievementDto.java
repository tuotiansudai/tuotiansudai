package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.LoanStatus;

import java.io.Serializable;
import java.util.Date;

public class InvestAchievementDto implements Serializable{

    private String name;

    private LoanStatus loanStatus;

    private int periods;

    private long loanAmount;

    private Date raisingCompleteTime;

    private String whenFirstInvest;

    private String whenCompleteInvest;

    private String firstInvestLoginName;

    private long firstInvestAmount;

    private String lastInvestLoginName;

    private long lastInvestAmount;

    private String maxAmountLoginName;

    private long maxAmount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }

    public int getPeriods() {
        return periods;
    }

    public void setPeriods(int periods) {
        this.periods = periods;
    }

    public long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Date getRaisingCompleteTime() {
        return raisingCompleteTime;
    }

    public void setRaisingCompleteTime(Date raisingCompleteTime) {
        this.raisingCompleteTime = raisingCompleteTime;
    }

    public String getWhenFirstInvest() {
        return whenFirstInvest;
    }

    public void setWhenFirstInvest(String whenFirstInvest) {
        this.whenFirstInvest = whenFirstInvest;
    }

    public String getWhenCompleteInvest() {
        return whenCompleteInvest;
    }

    public void setWhenCompleteInvest(String whenCompleteInvest) {
        this.whenCompleteInvest = whenCompleteInvest;
    }

    public String getFirstInvestLoginName() {
        return firstInvestLoginName;
    }

    public void setFirstInvestLoginName(String firstInvestLoginName) {
        this.firstInvestLoginName = firstInvestLoginName;
    }

    public long getFirstInvestAmount() {
        return firstInvestAmount;
    }

    public void setFirstInvestAmount(long firstInvestAmount) {
        this.firstInvestAmount = firstInvestAmount;
    }

    public String getLastInvestLoginName() {
        return lastInvestLoginName;
    }

    public void setLastInvestLoginName(String lastInvestLoginName) {
        this.lastInvestLoginName = lastInvestLoginName;
    }

    public long getLastInvestAmount() {
        return lastInvestAmount;
    }

    public void setLastInvestAmount(long lastInvestAmount) {
        this.lastInvestAmount = lastInvestAmount;
    }

    public String getMaxAmountLoginName() {
        return maxAmountLoginName;
    }

    public void setMaxAmountLoginName(String maxAmountLoginName) {
        this.maxAmountLoginName = maxAmountLoginName;
    }

    public long getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(long maxAmount) {
        this.maxAmount = maxAmount;
    }

}
