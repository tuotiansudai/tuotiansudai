package com.tuotiansudai.fudian.dto;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

public class BankBaseDto implements Serializable, BankDtoValidator {

    private String loginName;

    private String mobile;

    private String bankUserName;

    private String bankAccountNo;

    public BankBaseDto() {
    }

    public BankBaseDto(String loginName, String mobile, String bankUserName, String bankAccountNo) {
        this.loginName = loginName;
        this.mobile = mobile;
        this.bankUserName = bankUserName;
        this.bankAccountNo = bankAccountNo;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    @Override
    public boolean isValid() {
        return !Strings.isNullOrEmpty(loginName)
                && !Strings.isNullOrEmpty(mobile)
                && !Strings.isNullOrEmpty(bankUserName)
                && !Strings.isNullOrEmpty(bankAccountNo);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
