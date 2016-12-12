package com.tuotiansudai.console.activity.service;


import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.AnnualPrizeDto;
import com.tuotiansudai.activity.repository.mapper.AnnualPrizeMapper;
import com.tuotiansudai.activity.repository.model.AnnualPrizeModel;
import com.tuotiansudai.dto.BasePaginationDataDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    public BasePaginationDataDto<AnnualPrizeDto> findAnnualList(Integer index, Integer pageSize, String mobile) {
        List<AnnualPrizeModel> annualPrizeModels = annualPrizeMapper.findAnnualPrizeModels(mobile, index, pageSize);
        int count = annualPrizeMapper.findAnnualPrizeCount(mobile);

        List<AnnualPrizeDto> transform = Lists.transform(annualPrizeModels, annual -> {
            List<String> rewardList = new ArrayList<>();
            annualRewardMap.forEach((k, v) -> {
                if (k <= annual.getInvestAmount()) {
                    rewardList.add(v);
                }
            });
            return new AnnualPrizeDto(annual, Joiner.on("、").join(rewardList));
        });

        return new BasePaginationDataDto<>(index, pageSize, count, transform);
    }

}
