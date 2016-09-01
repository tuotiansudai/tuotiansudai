package com.tuotiansudai.api.dto.v1_0;

import java.util.List;

public class BankLimitResponseDataDto extends BaseResponseDataDto {
    private String rechargeLeftAmount;
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
