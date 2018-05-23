package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

import java.util.Map;

public class LoanInvestRequestDto extends PayBaseRequestDto {

    private String amount;

    private String award;

    private String loanTxNo;

    public LoanInvestRequestDto() {
    }

    public LoanInvestRequestDto(Source source, String loginName, String mobile, String userName, String accountNo, String amount, String loanTxNo, ApiType apiType) {
        super(source, loginName, mobile, userName, accountNo, apiType, null);
        this.amount = amount;
        this.award = "0.00";
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