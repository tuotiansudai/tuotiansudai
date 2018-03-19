package com.tuotiansudai.activity.repository.model;


import com.google.common.collect.Lists;
import com.tuotiansudai.etcd.ETCDConfigReader;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;
import java.util.List;

public enum ActivityWeChatDrawCoupon {

    SPRING_BREEZE_ACTIVITY_WECHAT("SPRING_BREEZE_ACTIVITY_DRAW_COUPON:{0}", Lists.newArrayList(488L, 489L, 489L, 489L, 490L, 490L, 490L, 490L, 490L, 491L, 492L, 493L), ETCDConfigReader.getReader().getValue("activity.spring.breeze.startTime"), ETCDConfigReader.getReader().getValue("activity.spring.breeze.endTime"))
    ;

    ActivityWeChatDrawCoupon(String key, List<Long> coupons, String startTime, String endTime) {
        this.key = key;
        this.coupons = coupons;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private String key;
    private List<Long> coupons;
    private String startTime;
    private String endTime;

    public String getKey() {
        return key;
    }

    public List<Long> getCoupons() {
        return coupons;
    }

    public Date getStartTime() {
        return DateTime.parse(startTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
    }

    public Date getEndTime() {
        return DateTime.parse(endTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
    }

    public static boolean duringActivities(ActivityWeChatDrawCoupon activityWeChatDrawCoupon){
        return activityWeChatDrawCoupon.getStartTime().before(new Date()) && activityWeChatDrawCoupon.getStartTime().after(new Date());
    }
}
