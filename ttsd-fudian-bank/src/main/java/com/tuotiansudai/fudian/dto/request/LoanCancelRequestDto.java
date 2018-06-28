package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.dto.BankLoanCancelDto;

public class LoanCancelRequestDto extends BaseRequestDto {

    private String loanTxNo;

    private String loanOrderNo;

    private String loanOrderDate;

    public LoanCancelRequestDto(BankLoanCancelDto bankLoanCancelDto) {
        super(Source.WEB, null, null, null, null);
        this.loanTxNo = bankLoanCancelDto.getLoanTxNo();
        this.loanOrderNo = bankLoanCancelDto.getLoanOrderNo();
        this.loanOrderDate = bankLoanCancelDto.getLoanOrderDate();
    }

    public String getLoanTxNo() {
        return loanTxNo;
    }

    public void setLoanTxNo(String loanTxNo) {
        this.loanTxNo = loanTxNo;
    }

    public String getLoanOrderNo() {
        return loanOrderNo;
    }

    public void setLoanOrderNo(String loanOrderNo) {
        this.loanOrderNo = loanOrderNo;
    }

    public String getLoanOrderDate() {
        return loanOrderDate;
    }

    public void setLoanOrderDate(String loanOrderDate) {
        this.loanOrderDate = loanOrderDate;
    }
}