package com.tuotiansudai.api.dto.v1_0;

public class BankLimitUnitDto {
    private double singleAmount;    //单笔限额
    private double singleDayAmount; //单日限额
    private String bankCode;

    public BankLimitUnitDto() {
    }

    public BankLimitUnitDto(double singleAmount, double singleDayAmount, String bankCode) {
        this.singleAmount = singleAmount;
        this.singleDayAmount = singleDayAmount;
        setBankCode(bankCode);
    }

    public double getSingleAmount() {
        return singleAmount;
    }

    public void setSingleAmount(double singleAmount) {
        this.singleAmount = singleAmount;
    }

    public double getSingleDayAmount() {
        return singleDayAmount;
    }

    public void setSingleDayAmount(double singleDayAmount) {
        this.singleDayAmount = singleDayAmount;
    }

    public String getBankCode() {
        return bankCode.toUpperCase();
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode.toUpperCase();
    }
}
