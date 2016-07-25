package com.tuotiansudai.dto;

import java.io.Serializable;
import java.util.Date;

public class LoanInvestAchievementDto implements Serializable {

    private String firstInvestAchievementMobile;

    private String lastInvestAchievementMobile;

    private String maxAmountAchievementMobile;

    private Date firstInvestAchievementDate;

    private String maxAmountAchievementAmount;

    private Date lastInvestAchievementDate;

    public String getFirstInvestAchievementMobile() {
        return firstInvestAchievementMobile;
    }

    public void setFirstInvestAchievementMobile(String firstInvestAchievementMobile) {
        this.firstInvestAchievementMobile = firstInvestAchievementMobile;
    }

    public String getLastInvestAchievementMobile() {
        return lastInvestAchievementMobile;
    }

    public void setLastInvestAchievementMobile(String lastInvestAchievementMobile) {
        this.lastInvestAchievementMobile = lastInvestAchievementMobile;
    }

    public String getMaxAmountAchievementMobile() {
        return maxAmountAchievementMobile;
    }

    public void setMaxAmountAchievementMobile(String maxAmountAchievementMobile) {
        this.maxAmountAchievementMobile = maxAmountAchievementMobile;
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
}
