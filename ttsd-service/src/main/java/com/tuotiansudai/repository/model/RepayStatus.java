package com.tuotiansudai.repository.model;

public enum RepayStatus {

    REPAYING("待还"),

    CONFIRMING("确认中"),

    COMPLETE("完成"),

    OVERDUE("逾期");

    private final String description;

    public String getDescription() {
        return description;
    }

    RepayStatus(String description) {
        this.description = description;
    }

}
