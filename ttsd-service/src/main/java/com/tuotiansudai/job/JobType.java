package com.tuotiansudai.job;

public enum JobType {
    Default("默认类别"),
    LoanStatue("标的状态从预热转为可投资"),
    LoanOut("放款后续处理");

    private final String description;

    JobType(String description) {
        this.description = description;
    }
}
