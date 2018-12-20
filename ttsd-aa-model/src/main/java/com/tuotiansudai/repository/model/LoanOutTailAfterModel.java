package com.tuotiansudai.repository.model;


import java.io.Serializable;

public class LoanOutTailAfterModel implements Serializable{

    private long id;
    private long loanId;
    private String financeState;
    private String repayPower;
    private boolean isOverdue;
    private boolean isAdministrativePenalty;
    private String amountUsage;

    public LoanOutTailAfterModel(){}

    public LoanOutTailAfterModel(long loanId, String financeState, String repayPower, boolean isOverdue, boolean isAdministrativePenalty, String amountUsage) {
        this.loanId = loanId;
        this.financeState = financeState;
        this.repayPower = repayPower;
        this.isOverdue = isOverdue;
        this.isAdministrativePenalty = isAdministrativePenalty;
        this.amountUsage = amountUsage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getFinanceState() {
        return financeState;
    }

    public void setFinanceState(String financeState) {
        this.financeState = financeState;
    }

    public String getRepayPower() {
        return repayPower;
    }

    public void setRepayPower(String repayPower) {
        this.repayPower = repayPower;
    }

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }

    public boolean isAdministrativePenalty() {
        return isAdministrativePenalty;
    }

    public void setAdministrativePenalty(boolean administrativePenalty) {
        isAdministrativePenalty = administrativePenalty;
    }

    public String getAmountUsage() {
        return amountUsage;
    }

    public void setAmountUsage(String amountUsage) {
        this.amountUsage = amountUsage;
    }
}