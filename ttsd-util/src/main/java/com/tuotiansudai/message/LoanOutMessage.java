package com.tuotiansudai.message;


public class LoanOutMessage {

    private long loanId;

    private String agentLoginName;

    private long investAmountTotal;

    public LoanOutMessage(long loanId, String agentLoginName, long investAmountTotal) {
        this.loanId = loanId;
        this.agentLoginName = agentLoginName;
        this.investAmountTotal = investAmountTotal;
    }

    public long getLoanId() {
        return loanId;
    }

    public void setLoanId(long loanId) {
        this.loanId = loanId;
    }

    public String getAgentLoginName() {
        return agentLoginName;
    }

    public void setAgentLoginName(String agentLoginName) {
        this.agentLoginName = agentLoginName;
    }

    public long getInvestAmountTotal() {
        return investAmountTotal;
    }

    public void setInvestAmountTotal(long investAmountTotal) {
        this.investAmountTotal = investAmountTotal;
    }
}
