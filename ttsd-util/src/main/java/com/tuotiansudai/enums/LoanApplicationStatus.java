package com.tuotiansudai.enums;

public enum LoanApplicationStatus {
    WAITING("待审核"),
    APPROVE("已审核"),
    REJECT("已驳回");

    LoanApplicationStatus(String description) {
        this.description = description;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
