package com.tuotiansudai.dto;


import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class HuiZuRepayDto implements Serializable {
    private String loginName;
    @NotEmpty
    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String amount;

    private int period;

    @NotEmpty
    @Pattern(regexp = "^\\d+$")
    private String repayPlanId;

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

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getRepayPlanId() {
        return repayPlanId;
    }

    public void setRepayPlanId(String repayPlanId) {
        this.repayPlanId = repayPlanId;
    }
}
