package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

public class BankLimitUnitDto {

    @ApiModelProperty(value = "单笔限额", example = "100")
    private String singleAmount;    //单笔限额

    @ApiModelProperty(value = "单日限额", example = "10000")
    private String singleDayAmount; //单日限额

    @ApiModelProperty(value = "银行卡缩写", example = "ICBC")
    private String bankCode;

    @ApiModelProperty(value = "银行卡名称", example = "工商银行")
    private String bankName;

    public BankLimitUnitDto() {
    }

    public BankLimitUnitDto(String singleAmount, String singleDayAmount, String bankCode, String bankName) {
        this.singleAmount = singleAmount;
        this.singleDayAmount = singleDayAmount;
        this.bankCode = bankCode;
        this.bankName = bankName;
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
