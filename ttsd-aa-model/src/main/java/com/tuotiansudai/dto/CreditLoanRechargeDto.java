package com.tuotiansudai.dto;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class CreditLoanRechargeDto implements Serializable {

    private String operatorLoginName;

    @NotEmpty
    private String mobile;

    @NotEmpty
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String amount;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOperatorLoginName() {
        return operatorLoginName;
    }

    public void setOperatorLoginName(String operatorLoginName) {
        this.operatorLoginName = operatorLoginName;
    }
}
