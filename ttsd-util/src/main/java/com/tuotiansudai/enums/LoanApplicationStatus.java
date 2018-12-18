package com.tuotiansudai.enums;

public enum LoanApplicationStatus {
    WAITING("等待审核"),
    APPROVE("通过"),
    REJECT("驳回");

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
