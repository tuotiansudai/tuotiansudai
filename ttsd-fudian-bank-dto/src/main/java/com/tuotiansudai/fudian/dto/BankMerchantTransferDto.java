package com.tuotiansudai.fudian.dto;

import com.google.gson.GsonBuilder;

public class BankMerchantTransferDto extends BankBaseDto {

    private long amount;

    public BankMerchantTransferDto() {
    }

    public BankMerchantTransferDto(String loginName, String mobile, String bankUserName, String bankAccountNo, long amount) {
        super(loginName, mobile, bankUserName, bankAccountNo);
        this.amount = amount;
    }

    public long getAmount() {
        return amount;
    }

    @Override
    public boolean isValid() {
        return super.isValid()
                && amount > 0;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
