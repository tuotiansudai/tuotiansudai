package com.tuotiansudai.fudian.dto;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

public class BankLoanCreateDto extends BankBaseDto {

    private String loanName;

    private long amount;

    public BankLoanCreateDto() {
    }

    public BankLoanCreateDto(String bankUserName, String bankAccountNo, String loanName, long amount) {
        super(null, null, bankUserName, bankAccountNo);
        this.loanName = loanName;
        this.amount = amount;
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
                && amount > 0;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
