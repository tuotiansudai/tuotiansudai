package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.dto.BankWithdrawDto;
import com.tuotiansudai.fudian.util.AmountUtils;

public class WithdrawRequestDto extends NotifyRequestDto {

    private String amount;

    private String fee;

    public WithdrawRequestDto(Source source, BankWithdrawDto bankWithdrawDto) {
        super(source, bankWithdrawDto.getLoginName(), bankWithdrawDto.getMobile(), bankWithdrawDto.getBankUserName(), bankWithdrawDto.getBankAccountNo());
        this.amount = AmountUtils.toYuan(bankWithdrawDto.getAmount());
        this.fee = AmountUtils.toYuan(bankWithdrawDto.getFee());
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
}