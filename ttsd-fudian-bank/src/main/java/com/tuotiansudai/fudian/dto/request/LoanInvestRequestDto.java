package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class LoanInvestRequestDto extends PayBaseRequestDto {

    private String amount;

    private String award;

    private String loanTxNo;

    public LoanInvestRequestDto(String loginName, String mobile, String userName, String accountNo, String amount, String award, String loanTxNo, ApiType apiType) {
        super(loginName, mobile, userName, accountNo, apiType);
        this.amount = amount;
        this.award = award;
        this.loanTxNo = loanTxNo;
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