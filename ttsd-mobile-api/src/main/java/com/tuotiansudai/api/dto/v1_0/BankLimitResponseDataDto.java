package com.tuotiansudai.api.dto.v1_0;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class BankLimitResponseDataDto extends BaseResponseDataDto {

    @ApiModelProperty(value = "当日剩余充值金额", example = "10")
    private String rechargeLeftAmount;

    @ApiModelProperty(value = "银行限额集合", example = "list")
    private List<BankLimitUnitDto> bankLimits;

    public String getRechargeLeftAmount() {
        return rechargeLeftAmount;
    }

    public void setRechargeLeftAmount(String rechargeLeftAmount) {
        this.rechargeLeftAmount = rechargeLeftAmount;
    }

    public List<BankLimitUnitDto> getBankLimits() {
        return bankLimits;
    }

    public void setBankLimits(List<BankLimitUnitDto> bankLimits) {
        this.bankLimits = bankLimits;
    }
}
