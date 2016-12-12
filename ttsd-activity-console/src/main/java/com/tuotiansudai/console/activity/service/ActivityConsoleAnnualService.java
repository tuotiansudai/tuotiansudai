package com.tuotiansudai.console.activity.service;


import com.tuotiansudai.activity.repository.mapper.AnnualPrizeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ActivityConsoleAnnualService {

    @Autowired
    private AnnualPrizeMapper annualPrizeMapper;

    final private Map<Long, String> annualRewardMap = new HashMap<Long, String>() {{
        put(500000L, "20元红包");
        put(1000000L, "爱奇艺会员月卡");
        put(2000000L, "报销50元电影票");
        put(3000000L, "50元话费  ");
        put(5000000L, "100元中国石化加油卡");
        put(10000000L, "报销300元火车票");
        put(20000000L, "700元京东E卡");
        put(30000000L, "800元红包（50元激活");
        put(50000000L, "1600元芒果卡");
        put(70000000L, "小米手机5");
    }};


}
