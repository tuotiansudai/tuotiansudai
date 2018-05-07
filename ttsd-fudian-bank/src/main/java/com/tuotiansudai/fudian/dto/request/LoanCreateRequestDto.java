package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

public class LoanCreateRequestDto extends PayBaseRequestDto {

    private String amount;

    private String loanName;

    private String loanType = "1";

    public LoanCreateRequestDto(String userName, String accountNo, String amount, String loanName, String loginName, String mobile) {
        super(userName, accountNo, ApiType.LOAN_CREATE, loginName, mobile);
        this.amount = amount;
        this.loanName = loanName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLoanName() {
        return loanName;
    }

    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
}