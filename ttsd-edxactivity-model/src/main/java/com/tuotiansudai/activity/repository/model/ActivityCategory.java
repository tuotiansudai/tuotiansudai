package com.tuotiansudai.activity.repository.model;


public enum ActivityCategory {
    AUTUMN_PRIZE("旅游奢侈品活动",ConsumeCategory.TASK_COUNT),
    NATIONAL_PRIZE("国庆活动",ConsumeCategory.TASK_COUNT),
    POINT_DRAW_1000("1000积分抽奖",ConsumeCategory.ACCOUNT_POINT_BY_1000),
    POINT_DRAW_10000("10000积分抽奖",ConsumeCategory.ACCOUNT_POINT_BY_10000),
    HERO_RANKING("英雄榜",null),
    NEW_HERO_RANKING("英豪榜",null),
    CARNIVAL_ACTIVITY("双11狂欢",ConsumeCategory.TASK_COUNT);

    ActivityCategory(String description,ConsumeCategory consumeCategory) {
        this.description = description;
        this.consumeCategory = consumeCategory;
    }

    String description;

    ConsumeCategory consumeCategory;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ConsumeCategory getConsumeCategory() {
        return consumeCategory;
    }

    public void setConsumeCategory(ConsumeCategory consumeCategory) {
        this.consumeCategory = consumeCategory;
    }
}
