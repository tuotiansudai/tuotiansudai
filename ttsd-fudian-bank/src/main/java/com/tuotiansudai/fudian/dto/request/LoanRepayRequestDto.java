package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class LoanRepayRequestDto extends PayBaseRequestDto {

    private String loanTxNo;

    private String capital;

    private String interest;

    private String loanFee = "0.00";

    public LoanRepayRequestDto(String loginName, String mobile, String userName, String accountNo, String loanTxNo, String capital, String interest, ApiType apiType) {
        super(loginName, mobile, userName, accountNo, apiType);
        this.loanTxNo = loanTxNo;
        this.capital = capital;
        this.interest = interest;
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