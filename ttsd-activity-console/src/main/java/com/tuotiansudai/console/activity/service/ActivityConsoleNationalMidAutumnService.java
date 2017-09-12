package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.model.ActivityInvestRewardView;
import com.tuotiansudai.activity.repository.model.NationalMidAutumnView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.InvestAchievementView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivityConsoleNationalMidAutumnService {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserBillMapper userBillMapper;

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
        Map<String, NationalMidAutumnView> map = new HashMap<>();
        List<InvestAchievementView> investAchievementViews = investMapper.findAmountByLoanIdAndDesc(activityNationalMidAutumnStartTime, activityNationalMidAutumnEndTime, Arrays.asList("逢万返百", "加息6.8%"));
        for (InvestAchievementView investAchievementView : investAchievementViews) {
            NationalMidAutumnView nationalMidAutumnView = map.get(investAchievementView.getLoginName());
            if (investAchievementView.getLoanDesc().equals("逢万返百")) {
                long moneyAmount = userBillMapper.findUserAmountByBusinessType(investAchievementView.getLoginName(), UserBillBusinessType.NATIONAL_DAY_INVEST);
                map.put(investAchievementView.getLoginName(), nationalMidAutumnView == null ? new NationalMidAutumnView(investAchievementView.getUserName(),
                        investAchievementView.getLoginName(),
                        investAchievementView.getMobile(),
                        investAchievementView.getAmount(),
                        0,
                        moneyAmount) : new NationalMidAutumnView(nationalMidAutumnView.getUserName(), nationalMidAutumnView.getLoginName(), nationalMidAutumnView.getMobile(),
                        investAchievementView.getAmount(), nationalMidAutumnView.getSumCouponInvestAmount(), moneyAmount));

            } else {
                map.put(investAchievementView.getLoginName(), nationalMidAutumnView == null ? new NationalMidAutumnView(investAchievementView.getUserName(),
                        investAchievementView.getLoginName(),
                        investAchievementView.getMobile(),
                        0,
                        investAchievementView.getAmount(),
                        0) : new NationalMidAutumnView(nationalMidAutumnView.getUserName(), nationalMidAutumnView.getLoginName(), nationalMidAutumnView.getMobile(),
                        nationalMidAutumnView.getSumCashInvestAmount(), investAchievementView.getAmount(), nationalMidAutumnView.getSumMoneyAmount()));
            }
        }
        return new ArrayList<>(map.values());
    }
}
