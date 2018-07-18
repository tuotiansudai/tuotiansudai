package com.tuotiansudai.activity.service;

import com.google.common.base.Strings;
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
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mothers.day.endTime}\")}")
    private Date activityEndTime;

    public String getInvestByLoginName(String loginName) {
        if(Strings.isNullOrEmpty(loginName)){
            return "0";
        }
        return AmountConverter.convertCentToString(investMapper.sumInvestAmountConsole(null,null, loginName, null, null, null, activityStartTime, activityEndTime, InvestStatus.SUCCESS, null, null));
    }
}
