package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class LoanOutDto implements Serializable {

    private String loginName;

    @NotEmpty
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String amount;

    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private String loanId;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLoanId() {
        return loanId;
    }

    public long getLoanIdLong() {
        return Long.parseLong(loanId);
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

}
