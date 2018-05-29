package com.tuotiansudai.fudian.dto;


public class BankLoanCreditInvestDto extends BankBaseDto{

    private long creditNo;

    private long amount;

    private long creditAmount;

    private long creditFee;

    private String investOrderDate;

    private String investOrderNo;

    private String loanTxNo;

    public long getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(long creditNo) {
        this.creditNo = creditNo;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(long creditAmount) {
        this.creditAmount = creditAmount;
    }

    public long getCreditFee() {
        return creditFee;
    }

    public void setCreditFee(long creditFee) {
        this.creditFee = creditFee;
    }

    public String getInvestOrderDate() {
        return investOrderDate;
    }

    public void setInvestOrderDate(String investOrderDate) {
        this.investOrderDate = investOrderDate;
    }

    public String getInvestOrderNo() {
        return investOrderNo;
    }

    public void setInvestOrderNo(String investOrderNo) {
        this.investOrderNo = investOrderNo;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }

}
