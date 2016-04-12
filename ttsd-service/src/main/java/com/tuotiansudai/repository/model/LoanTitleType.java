package com.tuotiansudai.repository.model;

public enum LoanTitleType {
    BASE_TITLE_TYPE("基础标题类型"),
    NEW_TITLE_TYPE("新增标题类型");
    LoanTitleType(String description){
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
