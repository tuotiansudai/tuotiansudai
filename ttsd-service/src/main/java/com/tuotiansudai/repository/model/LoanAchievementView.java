package com.tuotiansudai.repository.model;


import java.io.Serializable;
import java.util.Date;

public class LoanAchievementView implements Serializable{

    private long loanId;

    private String name;

    private LoanStatus loanStatus;

    private int periods;

    private long loanAmount;

    private Date fundraisingStartTime;

    private Date raisingCompleteTime;

    private String whenFirstInvest;

    private String whenCompleteInvest;

    private Long firstInvestAchievementId;

    private String firstInvestLoginName;

    private long firstInvestAmount;

    private Long lastInvestAchievementId;

    private String lastInvestLoginName;

    private long lastInvestAmount;

    private Long maxAmountAchievementId;

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

    public Date getFundraisingStartTime() {
        return fundraisingStartTime;
    }

    public void setFundraisingStartTime(Date fundraisingStartTime) {
        this.fundraisingStartTime = fundraisingStartTime;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public Long getFirstInvestAchievementId() {
        return firstInvestAchievementId;
    }

    public void setFirstInvestAchievementId(Long firstInvestAchievementId) {
        this.firstInvestAchievementId = firstInvestAchievementId;
    }

    public Long getLastInvestAchievementId() {
        return lastInvestAchievementId;
    }

    public void setLastInvestAchievementId(Long lastInvestAchievementId) {
        this.lastInvestAchievementId = lastInvestAchievementId;
    }

    public Long getMaxAmountAchievementId() {
        return maxAmountAchievementId;
    }

    public void setMaxAmountAchievementId(Long maxAmountAchievementId) {
        this.maxAmountAchievementId = maxAmountAchievementId;
    }

}
