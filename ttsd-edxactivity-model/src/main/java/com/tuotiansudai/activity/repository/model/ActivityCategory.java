package com.tuotiansudai.activity.repository.model;


import com.google.common.collect.Lists;

import java.util.List;

public enum ActivityCategory {
    AUTUMN_PRIZE("旅游奢侈品活动",ConsumeCategory.TASK_COUNT),
    NATIONAL_PRIZE("国庆活动",ConsumeCategory.TASK_COUNT),
    POINT_DRAW_1000("1000积分抽奖",ConsumeCategory.ACCOUNT_POINT_BY_1000),
    POINT_DRAW_10000("10000积分抽奖",ConsumeCategory.ACCOUNT_POINT_BY_10000),
    HERO_RANKING("英雄榜",ConsumeCategory.ACCOUNT_POINT_BY_NO_POINT),
    NEW_HERO_RANKING("英豪榜",ConsumeCategory.ACCOUNT_POINT_BY_NO_POINT),
    CARNIVAL_ACTIVITY("双11狂欢",ConsumeCategory.TASK_COUNT),
    NO_WORK_ACTIVITY("不上班", ConsumeCategory.TASK_COUNT),
    ANNUAL_ACTIVITY("元旦活动",ConsumeCategory.TASK_COUNT),
    CHRISTMAS_ACTIVITY("圣诞节活动",ConsumeCategory.TASK_COUNT),
    HEADLINES_TODAY_ACTIVITY("今日头条拉新抽奖活动",ConsumeCategory.TASK_COUNT),
    POINT_SHOP_DRAW_1000("积分商城抽奖",ConsumeCategory.ACCOUNT_POINT_BY_1000),
    SPRING_FESTIVAL_ACTIVITY("春节活动",ConsumeCategory.TASK_COUNT);

    ActivityCategory(String description, ConsumeCategory consumeCategory) {
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

    public static List<ActivityCategory> getTaskActivityCategory() {
        List<ActivityCategory> activityList = Lists.newArrayList();
        Lists.newArrayList(ActivityCategory.values()).forEach(activityCategory -> {
            if (activityCategory.getConsumeCategory() != null && activityCategory.getConsumeCategory().equals(ConsumeCategory.TASK_COUNT)) {
                activityList.add(activityCategory);
            }
        });
        return activityList;
    }
}
