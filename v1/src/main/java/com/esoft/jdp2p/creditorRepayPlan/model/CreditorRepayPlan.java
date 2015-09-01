package com.esoft.jdp2p.creditorRepayPlan.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2015/6/15.
 */

public class CreditorRepayPlan implements Serializable {

    private String repayTime;
    private String totalMoney;

    private String repayDay;
    private String repayMoney;
    private String loanName;
    private String loanId;
    private String userId;
    private String status;

    private String actualDay;

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getRepayDay() {
        return repayDay;
    }

    public void setRepayDay(String repayDay) {
        this.repayDay = repayDay;
    }

    public String getRepayMoney() {
        return repayMoney;
    }

    public void setRepayMoney(String repayMoney) {
        this.repayMoney = repayMoney;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getActualDay() {
        return actualDay;
    }

    public void setActualDay(String actualDay) {
        this.actualDay = actualDay;
    }
}
