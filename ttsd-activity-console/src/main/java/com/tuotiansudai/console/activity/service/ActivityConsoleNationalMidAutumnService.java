package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Iterators;
import com.tuotiansudai.activity.repository.model.ActivityInvestRewardView;
import com.tuotiansudai.activity.repository.model.NationalMidAutumnView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestAchievementView;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivityConsoleNationalMidAutumnService {

    @Autowired
    private InvestMapper investMapper;

    private RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String NATIONAL_MID_AUTUMN_SUM_CASH_KEY = "NATIONAL_MID_AUTUMN_SUM_CASH_KEY";

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.midAutumn.startTime}\")}")
    private Date activityNationalMidAutumnStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.national.midAutumn.endTime}\")}")
    private Date activityNationalMidAutumnEndTime;

    public BasePaginationDataDto<ActivityInvestRewardView> list(int index, int pageSize) {
        List<NationalMidAutumnView> nationalMidAutumnViews = getNationalMidAutumnViews();
        int count = nationalMidAutumnViews.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, nationalMidAutumnViews.subList(startIndex, endIndex));
        return basePaginationDataDto;
    }

    public List<NationalMidAutumnView> getNationalMidAutumnViews() {
        List<NationalMidAutumnView> list = new ArrayList<>();
        List<InvestAchievementView> investAchievementViews = investMapper.findAmountByLoanIdAndDesc(activityNationalMidAutumnStartTime, activityNationalMidAutumnEndTime, Arrays.asList("逢万返百", "加息6.8%"));

        for (InvestAchievementView investAchievementView : investAchievementViews) {
            int loginNameIndex = CollectionUtils.isNotEmpty(list) ?
                    Iterators.indexOf(list.iterator(), input -> input.getLoginName().equalsIgnoreCase(investAchievementView.getLoginName())) + 1 : 0;

            if (investAchievementView.getLoanDesc().equals("逢万返百")) {
                long moneyAmount = 0;
                if (redisWrapperClient.hexists(NATIONAL_MID_AUTUMN_SUM_CASH_KEY, investAchievementView.getLoginName())) {
                    moneyAmount = Long.parseLong(redisWrapperClient.hget(NATIONAL_MID_AUTUMN_SUM_CASH_KEY, investAchievementView.getLoginName()));
                }
                if (loginNameIndex == 0) {
                    list.add(new NationalMidAutumnView(investAchievementView.getUserName(),
                            investAchievementView.getLoginName(),
                            investAchievementView.getMobile(),
                            investAchievementView.getAmount(),
                            0,
                            moneyAmount));
                }else{
                    list.get(loginNameIndex).setSumCashInvestAmount(investAchievementView.getAmount());
                }
            } else {

                if (loginNameIndex == 0) {
                    list.add(new NationalMidAutumnView(investAchievementView.getUserName(),
                            investAchievementView.getLoginName(),
                            investAchievementView.getMobile(),
                            0,
                            investAchievementView.getAmount(),
                            0));
                } else {
                    list.get(loginNameIndex).setSumCouponInvestAmount(investAchievementView.getAmount());
                }
            }
        }
        return list;
    }

}
