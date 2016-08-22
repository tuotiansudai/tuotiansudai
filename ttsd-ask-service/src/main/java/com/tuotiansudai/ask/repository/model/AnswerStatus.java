package com.tuotiansudai.ask.repository.model;

public enum AnswerStatus {
    UNAPPROVED("待审核"),
    REJECTED("已驳回"),
    UNADOPTED("未采纳"),
    ADOPTED("已采纳");

    private final String description;

    AnswerStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
