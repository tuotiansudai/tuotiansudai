package com.tuotiansudai.activity.service;

import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DoubleElevenService {

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.double.eleven.startTime}\")}")
    private Date activityDoubleElevenStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.double.eleven.endTime}\")}")
    private Date activityDoubleElevenEndTime;


    public String sumInvestAmountByloginName(String loginName) {
       long sumAmount = investMapper.sumAmountActivityDoubleElevenByLoginName(loginName,activityDoubleElevenStartTime, activityDoubleElevenEndTime);
       return AmountConverter.convertCentToString(sumAmount);
    }

    public String calculateJDcardByAmount(String amount){
        return String.valueOf((Math.floor(Double.parseDouble(amount)/100000)) * 100);
    }


}
