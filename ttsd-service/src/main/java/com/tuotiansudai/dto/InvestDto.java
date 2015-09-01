package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.InvestSource;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class InvestDto implements Serializable {

    private String loginName;

    @NotEmpty
    @Pattern(regexp = "^\\d+\\.\\d{2}$")
    private String amount;

    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private String loanId;

    private InvestSource investSource;

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

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public InvestSource getInvestSource() {
        return investSource;
    }

    public void setInvestSource(InvestSource investSource) {
        this.investSource = investSource;
    }
}
