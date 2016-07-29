package com.tuotiansudai.api.dto.v1_0;

import java.util.List;

public class BankLimitResponseDataDto extends BaseResponseDataDto {
    private double rechargeLeftAmount;
    private List<BankLimitUnitDto> bankLimits;

    public double getRechargeLeftAmount() {
        return rechargeLeftAmount;
    }

    public void setRechargeLeftAmount(double rechargeLeftAmount) {
        this.rechargeLeftAmount = rechargeLeftAmount;
    }

    public List<BankLimitUnitDto> getBankLimits() {
        return bankLimits;
    }

    public void setBankLimits(List<BankLimitUnitDto> bankLimits) {
        this.bankLimits = bankLimits;
    }
}
