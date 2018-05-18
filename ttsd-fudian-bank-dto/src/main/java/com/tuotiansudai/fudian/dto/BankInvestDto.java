package com.tuotiansudai.fudian.dto;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

public class BankInvestDto extends BankBaseDto {

    private long investId;

    private long amount;

    private String loanTxNo;

    public BankInvestDto() {
    }

    public BankInvestDto(long investId, String loginName, String mobile, String bankUserName, String bankAccountNo, long amount, String loanTxNo) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.investId = investId;
        this.amount = amount;
        this.loanTxNo = loanTxNo;
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

    @Override
    public boolean isValid() {
        return super.isValid()
                && investId > 0
                && amount > 0
                && !Strings.isNullOrEmpty(loanTxNo);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
