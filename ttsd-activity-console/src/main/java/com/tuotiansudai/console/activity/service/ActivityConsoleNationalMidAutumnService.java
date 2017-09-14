package com.tuotiansudai.console.activity.service;

import com.tuotiansudai.activity.repository.model.ActivityInvestRewardView;
import com.tuotiansudai.activity.repository.model.NationalMidAutumnView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.repository.model.InvestAchievementView;
import com.tuotiansudai.repository.model.LoanInvestAmountView;
import com.tuotiansudai.repository.model.UserBillOperationType;
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
        List<LoanInvestAmountView> loanInvestAmountViews = investMapper.findAmountByNationalDayActivity(activityNationalMidAutumnStartTime, activityNationalMidAutumnEndTime, Arrays.asList("逢万返百", "加息6.8%"));
        for (LoanInvestAmountView loanInvestAmountView : loanInvestAmountViews) {
            NationalMidAutumnView nationalMidAutumnView = map.get(loanInvestAmountView.getLoginName());
            if (loanInvestAmountView.getLoanDesc().equals("逢万返百")) {
                long moneyAmount = userBillMapper.findUserFunds(UserBillBusinessType.NATIONAL_DAY_INVEST, UserBillOperationType.TI_BALANCE,loanInvestAmountView.getMobile(),null,null,null,null).stream().mapToLong(i->i.getAmount()).sum();;
                map.put(loanInvestAmountView.getLoginName(), nationalMidAutumnView == null ? new NationalMidAutumnView(loanInvestAmountView.getUserName(),
                        loanInvestAmountView.getLoginName(),
                        loanInvestAmountView.getMobile(),
                        loanInvestAmountView.getAmount(),
                        0,
                        moneyAmount) : new NationalMidAutumnView(nationalMidAutumnView.getUserName(), nationalMidAutumnView.getLoginName(), nationalMidAutumnView.getMobile(),
                        loanInvestAmountView.getAmount(), nationalMidAutumnView.getSumCouponInvestAmount(), moneyAmount));

            } else {
                map.put(loanInvestAmountView.getLoginName(), nationalMidAutumnView == null ? new NationalMidAutumnView(loanInvestAmountView.getUserName(),
                        loanInvestAmountView.getLoginName(),
                        loanInvestAmountView.getMobile(),
                        0,
                        loanInvestAmountView.getAmount(),
                        0) : new NationalMidAutumnView(nationalMidAutumnView.getUserName(), nationalMidAutumnView.getLoginName(), nationalMidAutumnView.getMobile(),
                        nationalMidAutumnView.getSumCashInvestAmount(), loanInvestAmountView.getAmount(), nationalMidAutumnView.getSumMoneyAmount()));
            }
        }
        return new ArrayList<>(map.values());
    }
}
