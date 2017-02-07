package com.tuotiansudai.repository.model;

public enum CategoryType {
    TRANSFER_LOAN("债权转让"),
    LOAN("直投");

    final private String description;

    CategoryType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
