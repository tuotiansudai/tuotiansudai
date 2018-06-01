package com.tuotiansudai.fudian.dto;


import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

public class BankLoanCreditInvestDto extends BankBaseDto {

    private long transferApplicationId;

    private long investId;

    private long investAmount;

    private long transferFee;

    private String investOrderDate;

    private String investOrderNo;

    private String loanTxNo;

    public BankLoanCreditInvestDto() {
    }

    public BankLoanCreditInvestDto(String loginName, String mobile, String bankUserName, String bankAccountNo, long transferApplicationId, long investId, long investAmount, long transferFee, String investOrderDate, String investOrderNo, String loanTxNo) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.transferApplicationId = transferApplicationId;
        this.investId = investId;
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

    public long getInvestId() {
        return investId;
    }

    public void setInvestId(long investId) {
        this.investId = investId;
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

    @Override
    public boolean isValid() {
        return super.isValid()
                && transferApplicationId > 0
                && investId > 0
                && investAmount > 0
                && transferFee >= 0
                && !Strings.isNullOrEmpty(loanTxNo)
                && !Strings.isNullOrEmpty(investOrderNo)
                && !Strings.isNullOrEmpty(investOrderDate);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

}
