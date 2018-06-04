package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.dto.BankRechargeDto;
import com.tuotiansudai.fudian.dto.RechargePayType;
import com.tuotiansudai.fudian.util.AmountUtils;

public class RechargeRequestDto extends NotifyRequestDto {

    private String amount;

    private String fee = "0.00";

    private String payType;

    public RechargeRequestDto(Source source, BankRechargeDto bankRechargeDto) {
        super(source, bankRechargeDto.getLoginName(), bankRechargeDto.getMobile(), bankRechargeDto.getBankUserName(), bankRechargeDto.getBankAccountNo());
        this.amount = AmountUtils.toYuan(bankRechargeDto.getAmount());
        this.payType = bankRechargeDto.getPayType().getValue();
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}