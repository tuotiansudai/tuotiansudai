package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.dto.BankInvestDto;
import com.tuotiansudai.fudian.util.AmountUtils;

public class LoanInvestRequestDto extends NotifyRequestDto {

    private String amount;

    private String award;

    private String loanTxNo;

    public LoanInvestRequestDto() {
    }

    public LoanInvestRequestDto(Source source, BankInvestDto bankInvestDto) {
        super(source, bankInvestDto.getLoginName(), bankInvestDto.getMobile(), bankInvestDto.getBankUserName(), bankInvestDto.getBankAccountNo());
        this.amount = AmountUtils.toYuan(bankInvestDto.getAmount());
        this.award = "0.00";
        this.loanTxNo = AmountUtils.toYuan(bankInvestDto.getLoanTxNo());
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAward() {
        return award;
    }

    public void setAward(String award) {
        this.award = award;
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }
}