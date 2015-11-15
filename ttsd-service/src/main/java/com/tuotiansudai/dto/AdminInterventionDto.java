package com.tuotiansudai.dto;

import com.tuotiansudai.repository.model.UserBillOperationType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class AdminInterventionDto implements Serializable {

    @NotNull
    private String loginName;

    @NotNull
    private UserBillOperationType operationType;

    @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$")
    private String amount;

    @NotNull
    private String description;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public UserBillOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(UserBillOperationType operationType) {
        this.operationType = operationType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
