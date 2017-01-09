package com.tuotiansudai.activity.service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.repository.mapper.InvestMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SpringFestivalActivityService {

    @Autowired
    private InvestMapper investMapper;

    final private List<Long> investTaskList = Lists.newArrayList(100000L, 500000L, 1200000L, 3000000L);

    @Value("#{'${activity.spring.festival.period}'.split('\\~')}")
    private List<String> springFestivalTime = Lists.newArrayList();

    public String getTaskProgress(String loginName) {
        Date startTime = DateTime.parse(springFestivalTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(springFestivalTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        long investAmount = investMapper.sumSuccessActivityInvestAmount(loginName, null, startTime, endTime);

        String[] task = {"0", "0", "0", "0"};
        for (int i = 0; i < investTaskList.size(); i++) {
            if (investAmount >= investTaskList.get(i)) {
                task[i] = "1";
            }
        }
        return Joiner.on(",").join(task);
    }

}
