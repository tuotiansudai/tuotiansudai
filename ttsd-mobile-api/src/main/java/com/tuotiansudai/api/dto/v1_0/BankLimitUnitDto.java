package com.tuotiansudai.api.dto.v1_0;

public class BankLimitUnitDto {
    private String singleAmount;    //单笔限额
    private String singleDayAmount; //单日限额
    private String bankCode;

    public BankLimitUnitDto() {
    }

    public BankLimitUnitDto(String singleAmount, String singleDayAmount, String bankCode) {
        this.singleAmount = singleAmount;
        this.singleDayAmount = singleDayAmount;
        this.bankCode = bankCode;
    }

    public String getSingleAmount() {
        return singleAmount;
    }

    public void setSingleAmount(String singleAmount) {
        this.singleAmount = singleAmount;
    }

    public String getSingleDayAmount() {
        return singleDayAmount;
    }

    public void setSingleDayAmount(String singleDayAmount) {
        this.singleDayAmount = singleDayAmount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
}
