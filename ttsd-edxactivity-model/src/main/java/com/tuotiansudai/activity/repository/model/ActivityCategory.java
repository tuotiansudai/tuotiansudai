package com.tuotiansudai.activity.repository.model;


import com.google.common.collect.Lists;

import java.util.*;

public enum ActivityCategory {
    AUTUMN_PRIZE("旅游奢侈品活动", ConsumeCategory.TASK_COUNT),
    NATIONAL_PRIZE("国庆活动", ConsumeCategory.TASK_COUNT),
    POINT_DRAW_1000("1000积分抽奖", ConsumeCategory.ACCOUNT_POINT_BY_1000),
    POINT_DRAW_10000("10000积分抽奖", ConsumeCategory.ACCOUNT_POINT_BY_10000),
    HERO_RANKING("英雄榜", ConsumeCategory.ACCOUNT_POINT_BY_NO_POINT),
    NEW_HERO_RANKING("英豪榜", ConsumeCategory.ACCOUNT_POINT_BY_NO_POINT),
    CARNIVAL_ACTIVITY("双11狂欢", ConsumeCategory.TASK_COUNT),
    NO_WORK_ACTIVITY("不上班", ConsumeCategory.TASK_COUNT),
    ANNUAL_ACTIVITY("元旦活动",ConsumeCategory.TASK_COUNT),
    CHRISTMAS_ACTIVITY("圣诞节活动",ConsumeCategory.TASK_COUNT),
    HEADLINES_TODAY_ACTIVITY("今日头条拉新抽奖活动",ConsumeCategory.TASK_COUNT),
    LANTERN_FESTIVAL_ACTIVITY("元宵节活动",ConsumeCategory.TASK_COUNT),
    POINT_SHOP_DRAW_1000("积分商城抽奖",ConsumeCategory.ACCOUNT_POINT_BY_1000),
    SPRING_FESTIVAL_ACTIVITY("春节活动",ConsumeCategory.TASK_COUNT),
    MONEY_TREE("摇钱树活动", ConsumeCategory.TASK_COUNT),
    MONEY_TREE_UNDER_1000_ACTIVITY("摇钱树活动_1000", ConsumeCategory.TASK_COUNT),
    MONEY_TREE_UNDER_10000_ACTIVITY("摇钱树活动_10000", ConsumeCategory.TASK_COUNT),
    MONEY_TREE_UNDER_20000_ACTIVITY("摇钱树活动_20000", ConsumeCategory.TASK_COUNT),
    MONEY_TREE_UNDER_30000_ACTIVITY("摇钱树活动_30000", ConsumeCategory.TASK_COUNT),
    MONEY_TREE_UNDER_40000_ACTIVITY("摇钱树活动_40000", ConsumeCategory.TASK_COUNT),
    MONEY_TREE_UNDER_50000_ACTIVITY("摇钱树活动_50000", ConsumeCategory.TASK_COUNT),
    MONEY_TREE_UNDER_60000_ACTIVITY("摇钱树活动_60000", ConsumeCategory.TASK_COUNT),
    MONEY_TREE_UNDER_70000_ACTIVITY("摇钱树活动_70000", ConsumeCategory.TASK_COUNT),
    MONEY_TREE_UNDER_80000_ACTIVITY("摇钱树活动_80000", ConsumeCategory.TASK_COUNT),
    MONEY_TREE_UNDER_90000_ACTIVITY("摇钱树活动_90000", ConsumeCategory.TASK_COUNT),
    MONEY_TREE_UNDER_100000_ACTIVITY("摇钱树活动_100000", ConsumeCategory.TASK_COUNT),
    MONEY_TREE_ABOVE_100000_ACTIVITY("摇钱树活动_100000以上", ConsumeCategory.TASK_COUNT),
    WOMAN_DAY_ACTIVITY("妇女节活动",ConsumeCategory.TASK_COUNT),
    WECHAT_FIRST_INVEST_PRIZE("微信首投奖励",ConsumeCategory.TASK_COUNT),
    MOTHERS_DAY_ACTIVITY("母亲节活动",ConsumeCategory.TASK_COUNT),
    CELEBRATION_SINGLE_ACTIVITY("周年庆单笔狂欢场活动",ConsumeCategory.TASK_COUNT),
    EXERCISE_WORK_ACTIVITY("运动达人VS职场骄子",ConsumeCategory.TASK_COUNT),
    HOUSE_DECORATE_ACTIVITY("家装节奖励",ConsumeCategory.TASK_COUNT),
    SCHOOL_SEASON_ACTIVITY("开学季活动奖励",ConsumeCategory.TASK_COUNT),
    IPHONEX_ACTIVITY("iphoneX活动奖励",ConsumeCategory.TASK_COUNT),
    DOUBLE_ELEVEN_ACTIVITY("双十一剁手活动",ConsumeCategory.TASK_COUNT),
    YEAR_END_AWARDS_ACTIVITY("年终奖活动",ConsumeCategory.TASK_COUNT),
    START_WORK_ACTIVITY("惊喜不重样加息不打烊活动",ConsumeCategory.TASK_COUNT),
    SPRING_BREEZE_ACTIVITY("4月活动",ConsumeCategory.TASK_COUNT),
    ;

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
                if (activityCategory.name().startsWith("MONEY_TREE") || !activityList.contains(ActivityCategory.MONEY_TREE)) {
                    activityList.add(ActivityCategory.MONEY_TREE);
                } else {
                    activityList.add(activityCategory);
                }

            }
        });
        TreeSet h = new TreeSet(activityList);
        activityList.clear();
        activityList.addAll(h);
        return activityList;
    }
}
