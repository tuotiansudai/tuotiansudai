package com.tuotiansudai.enums;

public enum HelpCategory {
    HOT("热门问题");

    private final String description;

    HelpCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
