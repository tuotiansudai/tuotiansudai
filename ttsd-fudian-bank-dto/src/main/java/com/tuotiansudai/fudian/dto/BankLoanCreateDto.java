package com.tuotiansudai.fudian.dto;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

public class BankLoanCreateDto extends BankBaseDto {

    private String loanName;

    private long amount;

    private String endTime;

    public BankLoanCreateDto() {
    }

    public BankLoanCreateDto(String bankUserName, String bankAccountNo, String loanName, long amount, String endTime) {
        super(null, null, bankUserName, bankAccountNo);
        this.loanName = loanName;
        this.amount = amount;
        this.endTime = endTime;
    }

    public String getLoanName() {
        return loanName;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(getBankUserName())
                && !Strings.isNullOrEmpty(getBankAccountNo())
                && !Strings.isNullOrEmpty(loanName)
                && amount > 0
                && !Strings.isNullOrEmpty(endTime);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
