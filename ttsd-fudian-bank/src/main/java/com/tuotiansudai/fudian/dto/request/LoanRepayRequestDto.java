package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.dto.BankLoanRepayDto;
import com.tuotiansudai.fudian.util.AmountUtils;

public class LoanRepayRequestDto extends NotifyRequestDto {

    private String loanTxNo;

    private String capital;

    private String interest;

    private String loanFee = "0.00";

    public LoanRepayRequestDto() {
    }

    public LoanRepayRequestDto(Source source, BankLoanRepayDto bankLoanRepayDto) {
        super(source, bankLoanRepayDto.getLoginName(), bankLoanRepayDto.getMobile(), bankLoanRepayDto.getBankUserName(), bankLoanRepayDto.getBankAccountNo());
        this.loanTxNo = bankLoanRepayDto.getLoanTxNo();
        this.capital = AmountUtils.toYuan(bankLoanRepayDto.getCapital());
        this.interest = AmountUtils.toYuan(bankLoanRepayDto.getInterest());
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getLoanFee() {
        return loanFee;
    }

    public void setLoanFee(String loanFee) {
        this.loanFee = loanFee;
    }
}