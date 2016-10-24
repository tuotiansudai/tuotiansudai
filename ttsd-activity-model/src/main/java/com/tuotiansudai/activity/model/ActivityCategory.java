package com.tuotiansudai.activity.model;


public enum ActivityCategory {
    AUTUMN_PRIZE("旅游奢侈品活动",1),
    NATIONAL_PRIZE("国庆活动",1),
    POINT_DRAW_1000("1000积分抽奖",1000),
    POINT_DRAW_10000("10000积分抽奖",10000),
    HERO_RANKING("英雄榜",0),
    NEW_HERO_RANKING("英豪榜",0);

    ActivityCategory(String description, int point) {
        this.description = description;
        this.point = point;
    }

    String description;

    int point;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
