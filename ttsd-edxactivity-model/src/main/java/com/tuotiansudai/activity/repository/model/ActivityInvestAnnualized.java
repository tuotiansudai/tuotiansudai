package com.tuotiansudai.activity.repository.model;


import com.tuotiansudai.etcd.ETCDConfigReader;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public enum ActivityInvestAnnualized {

    NEW_YEAR_ACTIVITY("新年更有钱活动", null, ETCDConfigReader.getReader().getValue("activity.new.year.startTime"), ETCDConfigReader.getReader().getValue("activity.new.year.endTime")),
    START_WORK_ACTIVITY("惊喜不重样加息不打烊活动", null, ETCDConfigReader.getReader().getValue("activity.start.work.startTime"), ETCDConfigReader.getReader().getValue("activity.start.work.endTime")),
    THIRD_ANNIVERSARY_ACTIVITY("拓天速贷3周年活动", null, ETCDConfigReader.getReader().getValue("activity.third.anniversary.startTime"), ETCDConfigReader.getReader().getValue("activity.third.anniversary.endTime"))
    ;

    ActivityInvestAnnualized() {

    }

    ActivityInvestAnnualized(String activityName, String activityDesc, String startTime, String endTime) {
        this.activityName = activityName;
        this.activityDesc = activityDesc;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    private String activityName;
    private String activityDesc;
    private String startTime;
    private String endTime;

    public String getActivityName() {
        return activityName;
    }

    public String getActivityDesc() {
        return activityDesc;
    }

    public Date getStartTime() {
        return DateTime.parse(startTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
    }

    public Date getEndTime() {
        return DateTime.parse(endTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
    }

    public static ActivityInvestAnnualized getActivityByDesc(String activityDesc){
        List<ActivityInvestAnnualized> list = Arrays.asList(ActivityInvestAnnualized.values());
        Optional<ActivityInvestAnnualized> activityInvestAnnualized = list.stream().filter(i -> !i.getStartTime().after(new Date()) && !new Date().after(i.getEndTime())).findAny();
        if (!activityInvestAnnualized.isPresent()){
            return null;
        }
        String activityLoanDesc = activityInvestAnnualized.get().getActivityDesc();

        if (activityLoanDesc == null) {
            return activityInvestAnnualized.get();
        }

        if (activityDesc != null && activityLoanDesc.equals(activityDesc)) {
            return activityInvestAnnualized.get();
        }
        return null;
    }
}
