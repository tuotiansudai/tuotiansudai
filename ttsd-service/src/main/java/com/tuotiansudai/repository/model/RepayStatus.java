package com.tuotiansudai.repository.model;

public enum RepayStatus {

    REPAYING("正在还款"),

    COMPLETE("还款完成"),

    OVERDUE("逾期");

    private final String description;

    public String getDescription() {
        return description;
    }

    RepayStatus(String description) {
        this.description = description;
    }

}
