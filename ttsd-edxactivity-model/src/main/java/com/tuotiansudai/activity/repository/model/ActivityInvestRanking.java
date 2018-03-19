package com.tuotiansudai.activity.repository.model;


import com.tuotiansudai.etcd.ETCDConfigReader;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public enum ActivityInvestRanking {

    SPRING_BREEZE_ACTIVITY_RANKING(ETCDConfigReader.getReader().getValue("activity.spring.breeze.startTime"), ETCDConfigReader.getReader().getValue("activity.spring.breeze.endTime"))
    ;

    ActivityInvestRanking(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private String startTime;
    private String endTime;

    public Date getStartTime() {
        return DateTime.parse(startTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
    }

    public Date getEndTime() {
        return DateTime.parse(endTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
    }

    public static ActivityInvestRanking getActivityDateByCurrentDate(){
        List<ActivityInvestRanking> list = Arrays.asList(ActivityInvestRanking.values());
        Optional<ActivityInvestRanking> activityInvestRanking = list.stream().filter(i -> !i.getStartTime().after(new Date()) && !new Date().after(i.getEndTime())).findAny();
        return activityInvestRanking.orElse(null);
    }
}
