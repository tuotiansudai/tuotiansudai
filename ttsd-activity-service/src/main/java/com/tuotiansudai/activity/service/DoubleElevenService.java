package com.tuotiansudai.activity.service;

import com.tuotiansudai.activity.repository.mapper.UserLotteryPrizeMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.UserLotteryPrizeView;
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

    @Autowired
    private LotteryDrawActivityService lotteryDrawActivityService;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.double.eleven.startTime}\")}")
    private Date activityDoubleElevenStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.double.eleven.endTime}\")}")
    private Date activityDoubleElevenEndTime;


    public String sumInvestAmountByLoginName(String loginName) {
        long sumAmount = investMapper.sumAmountActivityDoubleElevenByLoginName(loginName, activityDoubleElevenStartTime, activityDoubleElevenEndTime);
        return AmountConverter.convertCentToString(sumAmount);
    }

    public int calculateJDCardAmountByInvestAmount(String amount) {
        return (int) (Math.floor(Double.parseDouble(amount) / 100000) * 100);
    }
}
