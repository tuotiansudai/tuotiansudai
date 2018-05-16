package com.tuotiansudai.fudian.dto.request;

import com.tuotiansudai.fudian.config.ApiType;

import java.util.Map;

public class LoanCreateRequestDto extends PayBaseRequestDto {

    private String amount;

    private String loanName;

    private String loanType = "1";

    public LoanCreateRequestDto(String loginName, String mobile, String userName, String accountNo, String amount, String loanName, Map<String, String> extraValues) {
        super(Source.WEB, loginName, mobile, userName, accountNo, ApiType.LOAN_CREATE, extraValues);
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