package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.mapper.MidSummerInvestMapper;
import com.tuotiansudai.activity.repository.model.MidSummerInvestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MidSummerService {

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mid.summer.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mid.summer.endTime}\")}")
    private Date activityEndTime;

    private final MidSummerInvestMapper midSummerInvestMapper;

    @Autowired
    public MidSummerService(MidSummerInvestMapper midSummerInvestMapper) {
        this.midSummerInvestMapper = midSummerInvestMapper;
    }

    public long getInvitedCount(String loginName) {
        List<MidSummerInvestModel> models = this.midSummerInvestMapper.findByReferrerLoginName(loginName);

        return models.stream()
                .filter(item -> item.getTradingTime().after(activityStartTime) && item.getTradingTime().before(activityEndTime))
                .collect(Collectors.groupingBy(MidSummerInvestModel::getLoginName, Collectors.summingLong(MidSummerInvestModel::getAmount)))
                .entrySet().stream().filter(item -> item.getValue() > 20000)
                .count();
    }
}










