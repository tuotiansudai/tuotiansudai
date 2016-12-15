package com.tuotiansudai.console.activity.service;


import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.dto.AnnualPrizeDto;
import com.tuotiansudai.activity.repository.mapper.AnnualPrizeMapper;
import com.tuotiansudai.activity.repository.model.AnnualPrizeModel;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.util.PaginationUtil;
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
        put(100L, "20元红包");
        put(200L, "爱奇艺会员月卡");
        put(220L, "报销50元电影票");
        put(230L, "50元话费  ");
        put(240L, "100元中国石化加油卡");
        put(250L, "报销300元火车票");
        put(260L, "700元京东E卡");
        put(300L, "800元红包（50元激活");
        put(310L, "1600元芒果卡");
        put(320L, "小米手机5");
    }};


    public BasePaginationDataDto<AnnualPrizeDto> findAnnualList(Integer index, Integer pageSize, String mobile) {
        long count = annualPrizeMapper.findAnnualPrizeCount(mobile);
        List<AnnualPrizeModel> annualPrizeModels = annualPrizeMapper.findAnnualPrizeModels(mobile, PaginationUtil.calculateOffset(index, pageSize, count), pageSize);

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
