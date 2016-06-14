package com.tuotiansudai.dto;

import java.io.Serializable;
import java.util.Date;

public class LoanInvestAchievementDto implements Serializable {

    private String firstInvestAchievementLoginName;

    private String lastInvestAchievementLoginName;

    private String maxAmountAchievementLoginName;

    private Date firstInvestAchievementDate;

    private String maxAmountAchievementAmount;

    private Date lastInvestAchievementDate;

    private String loanRemainingAmount;

    public String getFirstInvestAchievementLoginName() {
        return firstInvestAchievementLoginName;
    }

    public void setFirstInvestAchievementLoginName(String firstInvestAchievementLoginName) {
        this.firstInvestAchievementLoginName = firstInvestAchievementLoginName;
    }

    public String getLastInvestAchievementLoginName() {
        return lastInvestAchievementLoginName;
    }

    public void setLastInvestAchievementLoginName(String lastInvestAchievementLoginName) {
        this.lastInvestAchievementLoginName = lastInvestAchievementLoginName;
    }

    public String getMaxAmountAchievementLoginName() {
        return maxAmountAchievementLoginName;
    }

    public void setMaxAmountAchievementLoginName(String maxAmountAchievementLoginName) {
        this.maxAmountAchievementLoginName = maxAmountAchievementLoginName;
    }

    public Date getFirstInvestAchievementDate() {
        return firstInvestAchievementDate;
    }

    public void setFirstInvestAchievementDate(Date firstInvestAchievementDate) {
        this.firstInvestAchievementDate = firstInvestAchievementDate;
    }

    public String getMaxAmountAchievementAmount() {
        return maxAmountAchievementAmount;
    }

    public void setMaxAmountAchievementAmount(String maxAmountAchievementAmount) {
        this.maxAmountAchievementAmount = maxAmountAchievementAmount;
    }

    public Date getLastInvestAchievementDate() {
        return lastInvestAchievementDate;
    }

    public void setLastInvestAchievementDate(Date lastInvestAchievementDate) {
        this.lastInvestAchievementDate = lastInvestAchievementDate;
    }

    public String getLoanRemainingAmount() {
        return loanRemainingAmount;
    }

    public void setLoanRemainingAmount(String loanRemainingAmount) {
        this.loanRemainingAmount = loanRemainingAmount;
    }
}
