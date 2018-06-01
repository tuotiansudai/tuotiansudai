package com.tuotiansudai.fudian.dto;


public class BankLoanCreditInvestDto extends BankBaseDto {

    private long transferApplicationId;

    private long investAmount;

    private long transferFee;

    private String investOrderDate;

    private String investOrderNo;

    private String loanTxNo;

    public BankLoanCreditInvestDto() {
    }

    public BankLoanCreditInvestDto(String loginName, String mobile, String bankUserName, String bankAccountNo, long transferApplicationId, long investAmount, long transferFee, String investOrderDate, String investOrderNo, String loanTxNo) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.transferApplicationId = transferApplicationId;
        this.investAmount = investAmount;
        this.transferFee = transferFee;
        this.investOrderDate = investOrderDate;
        this.investOrderNo = investOrderNo;
        this.loanTxNo = loanTxNo;
    }

    public long getTransferApplicationId() {
        return transferApplicationId;
    }

    public void setTransferApplicationId(long transferApplicationId) {
        this.transferApplicationId = transferApplicationId;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public long getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(long transferFee) {
        this.transferFee = transferFee;
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
