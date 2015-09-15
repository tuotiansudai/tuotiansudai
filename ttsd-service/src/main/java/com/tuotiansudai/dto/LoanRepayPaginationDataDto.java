package com.tuotiansudai.dto;


import com.tuotiansudai.repository.model.RepayStatus;


public class LoanRepayPaginationDataDto extends BasePaginationDataDto {
    private String loanId;
    private String projectName;
    private String loginName;
    private String repayDay;
    private long period;
    private double corpus;
    private double interest;
    private double totalAmount;
    private RepayStatus repayStatus;

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
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

    public String getRepayDay() {
        return repayDay;
    }

    public void setRepayDay(String repayDay) {
        this.repayDay = repayDay;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public double getCorpus() {
        return corpus;
    }

    public void setCorpus(double corpus) {
        this.corpus = corpus;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }



    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public RepayStatus getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(RepayStatus repayStatus) {
        this.repayStatus = repayStatus;
    }
}
