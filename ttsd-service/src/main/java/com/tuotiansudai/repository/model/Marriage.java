package com.tuotiansudai.repository.model;

public enum Marriage {
    UNMARRIED("未婚"),
    MARRIED("已婚"),
    DIVORCE("离异"),
    NONE("不明");

    private final String description;

    Marriage(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
