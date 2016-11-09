package com.tuotiansudai.activity.dto;


import com.google.common.collect.Lists;

import java.util.List;

public enum ActivityCategory {
    AUTUMN_PRIZE("旅游奢侈品活动",ConsumeCategory.TASK_COUNT),
    NATIONAL_PRIZE("国庆活动",ConsumeCategory.TASK_COUNT),
    POINT_DRAW_1000("1000积分抽奖",ConsumeCategory.ACCOUNT_POINT_BY_1000),
    POINT_DRAW_10000("10000积分抽奖",ConsumeCategory.ACCOUNT_POINT_BY_10000),
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

    public static List<ActivityCategory> getTaskActivityCategory(){
        List<ActivityCategory> activityList = Lists.newArrayList();
        Lists.newArrayList(ActivityCategory.values()).forEach(activityCategory -> {
            if(activityCategory.getConsumeCategory() != null && activityCategory.getConsumeCategory().equals(ConsumeCategory.TASK_COUNT)){
                activityList.add(activityCategory);
            }
        });
        return activityList;
    }
}
