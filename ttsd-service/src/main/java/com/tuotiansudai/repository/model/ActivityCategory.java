package com.tuotiansudai.repository.model;


public enum ActivityCategory {
    HERO_RANKING("英雄榜"),
    NEW_HERO_RANKING("英豪榜");

    ActivityCategory(String description) {
        this.description = description;
    }

    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
