package com.tuotiansudai.fudian.dto;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

public class BankInvestDto extends BankBaseDto {

    private long investId;

    private long amount;

    private String loanTxNo;

    private long loanId;

    private String loanName;

    public BankInvestDto() {
    }

    public BankInvestDto(String loginName, String mobile, String bankUserName, String bankAccountNo, long investId, long amount, String loanTxNo, long loanId, String loanName) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.investId = investId;
        this.amount = amount;
        this.loanTxNo = loanTxNo;
        this.loanId = loanId;
        this.loanName = loanName;
    }

    public long getInvestId() {
        return investId;
    }

    public long getAmount() {
        return amount;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && investId > 0
                && loanId > 0
                && amount > 0
                && !Strings.isNullOrEmpty(loanTxNo)
                && !Strings.isNullOrEmpty(loanName);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
