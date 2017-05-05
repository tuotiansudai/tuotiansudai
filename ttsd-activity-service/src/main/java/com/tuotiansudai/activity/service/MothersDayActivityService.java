package com.tuotiansudai.activity.service;

import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class MothersDayActivityService {

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mothers.day.startTime}\")}")
    private Date activityStartTimeStr;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mothers.day.endTime}\")}")
    private Date activityEndTimeStr;

    public String getInvestByLoginName(String loginName) {
        return AmountConverter.convertCentToString(investMapper.sumInvestAmount(null, loginName, null, null, null, activityStartTimeStr, activityEndTimeStr, InvestStatus.SUCCESS, null));
    }
}
