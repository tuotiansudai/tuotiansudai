package com.tuotiansudai.activity.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NationalPrizeService {

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.startTime}\")}")
    private Date activityAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.autumn.endTime}\")}")
    private Date activityAutumnEndTime;
}
