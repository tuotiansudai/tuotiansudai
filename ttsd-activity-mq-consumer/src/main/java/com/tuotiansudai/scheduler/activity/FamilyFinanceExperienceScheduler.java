package com.tuotiansudai.scheduler.activity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FamilyFinanceExperienceScheduler {


    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.family.finance.startTime}\")}")
    private Date startTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.family.finance.endTime}\")}")
    private Date endTime;
}

