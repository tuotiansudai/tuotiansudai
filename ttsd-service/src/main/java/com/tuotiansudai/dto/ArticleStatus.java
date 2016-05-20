package com.tuotiansudai.dto;

public enum ArticleStatus {
    RETRACED("已撤回"),

    TO_APPROVE("待审核"),

    APPROVING("审核中");

    private String description;

    ArticleStatus(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
