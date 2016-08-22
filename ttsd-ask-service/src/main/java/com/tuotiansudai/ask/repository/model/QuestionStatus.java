package com.tuotiansudai.ask.repository.model;

public enum QuestionStatus {
    UNAPPROVED("待审核"),
    REJECTED("已驳回"),
    UNRESOLVED("待解答"),
    RESOLVED("已解答");

    private final String description;

    QuestionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
