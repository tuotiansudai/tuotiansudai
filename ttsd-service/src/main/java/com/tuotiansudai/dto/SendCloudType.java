package com.tuotiansudai.dto;

public enum SendCloudType {
    TEXT,
    CONTENT;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
