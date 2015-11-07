package com.tuotiansudai.repository.model;


import java.util.Date;

public class DebtRepaymentPlanView {

    private Date repayDate;

    private long totalAmount;

    private Date expertRepayDate;

    private long repayAmount;

    private String loanName;

    private long loanId;

    private String loginName;

    private RepayStatus status;

    private Date actualRepayDate;

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getExpertRepayDate() {
        return expertRepayDate;
    }

    public void setExpertRepayDate(Date expertRepayDate) {
        this.expertRepayDate = expertRepayDate;
    }

    public long getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(long repayAmount) {
        this.repayAmount = repayAmount;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public RepayStatus getStatus() {
        return status;
    }

    public void setStatus(RepayStatus status) {
        this.status = status;
    }

    public Date getActualRepayDate() {
        return actualRepayDate;
    }

    public void setActualRepayDate(Date actualRepayDate) {
        this.actualRepayDate = actualRepayDate;
    }
}
