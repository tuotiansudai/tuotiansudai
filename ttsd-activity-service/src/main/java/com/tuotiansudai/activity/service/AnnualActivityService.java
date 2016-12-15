package com.tuotiansudai.activity.service;


import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.util.AmountConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AnnualActivityService {

    @Autowired
    private InvestMapper investMapper;

    @Value("#{'${activity.annual.period}'.split('\\~')}")
    private List<String> annualTime = Lists.newArrayList();

    final private List<Long> investTaskList = Lists.newArrayList(500000L, 1000000L, 2000000L, 3000000L, 5000000L, 10000000L, 20000000L, 30000000L, 50000000L, 70000000L);

    public Map<String, String> getInvestAmountTask(String loginName) {
        Date startTime = DateTime.parse(annualTime.get(0), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        Date endTime = DateTime.parse(annualTime.get(1), DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).toDate();
        long investAmount = investMapper.sumSuccessActivityInvestAmount(loginName, LotteryDrawActivityService.ACTIVITY_DESCRIPTION, startTime, endTime);

        Map<String, String> param = Maps.newHashMap();
        param.put("investAmount", AmountConverter.convertCentToString(investAmount));
        param.put("nextAmount", AmountConverter.convertCentToString(investTaskList.stream().filter(c -> c >= investAmount).findFirst().get() - investAmount));

        return param;
    }

    public String getTaskProgress(String investAmount){
        long amount = investAmount.equals("0.00") ? 0 : Long.parseLong(investAmount);
        String[] task = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};

        for(int i = 0; i < investTaskList.size(); i ++){
            if(amount >= investTaskList.get(i)){
                task[i] = "1";
            }
        }
        return Joiner.on(",").join(task);
    }

}
