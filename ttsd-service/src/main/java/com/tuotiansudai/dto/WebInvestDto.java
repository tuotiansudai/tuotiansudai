package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.InvestSource;
import com.tuotiansudai.utils.AmountUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class WebInvestDto implements Serializable {
    @NotEmpty
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String amount;

    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private String loanId;

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

    public InvestDto toInvestDto(String loginName) {
        if (StringUtils.isBlank(loginName)) {
            throw new NullPointerException("loginName");
        }
        InvestDto investDto = new InvestDto();
        investDto.setInvestSource(InvestSource.WEB);
        investDto.setLoanId(loanId);
        investDto.setAmount(String.valueOf(AmountUtil.convertStringToCent(amount)));
        investDto.setLoginName(loginName);
        return investDto;
    }
}
