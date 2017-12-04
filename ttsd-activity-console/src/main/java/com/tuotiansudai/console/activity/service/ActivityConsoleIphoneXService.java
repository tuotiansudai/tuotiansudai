package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.dto.IphoneXActivityDto;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestProductTypeView;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivityConsoleIphoneXService {

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.iphoneX.startTime}\")}")
    private Date activityIphoneXStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.iphoneX.endTime}\")}")
    private Date activityIphoneXEndTime;

    private final List<ActivityConsoleIphoneXService.CashReward> cashRewards = Lists.newArrayList(
            new ActivityConsoleIphoneXService.CashReward("88", 10000000l, 20000000l),
            new ActivityConsoleIphoneXService.CashReward("388", 20000000l, 40000000l),
            new ActivityConsoleIphoneXService.CashReward("788", 40000000l, 60000000l),
            new ActivityConsoleIphoneXService.CashReward("1288", 60000000l, 80000000l),
            new ActivityConsoleIphoneXService.CashReward("1888", 80000000l, 100000000l),
            new ActivityConsoleIphoneXService.CashReward("iPhoneX", 100000000l, Long.MAX_VALUE));

    public BasePaginationDataDto<IphoneXActivityDto> list(int index, int pageSize) {
        List<IphoneXActivityDto> list = getIphoneXList();
        int count = list.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, list.subList(startIndex, endIndex));
        return basePaginationDataDto;
    }

    public List<IphoneXActivityDto> getIphoneXList() {
        Map<String, IphoneXActivityDto> map = new HashMap<>();
        List<InvestProductTypeView> list = investMapper.findAmountOrderByNameAndProductType(activityIphoneXStartTime, activityIphoneXEndTime, null);
        for (InvestProductTypeView investProductTypeView : list) {
            IphoneXActivityDto iphoneXActivityDto = map.get(investProductTypeView.getLoginName());
            map.put(investProductTypeView.getLoginName(),
                    iphoneXActivityDto == null ?
                            new IphoneXActivityDto(
                                    investProductTypeView.getLoginName(),
                                    investProductTypeView.getUserName(),
                                    investProductTypeView.getMobile(),
                                    investProductTypeView.getSumAmount(),
                                    getAnnualizedAmount(investProductTypeView),
                                    getSumCashReward(getAnnualizedAmount(investProductTypeView))) :
                            new IphoneXActivityDto(
                                    iphoneXActivityDto.getLoginName(),
                                    iphoneXActivityDto.getUserName(),
                                    iphoneXActivityDto.getMobile(),
                                    AmountConverter.convertStringToCent(iphoneXActivityDto.getSumInvestAmount()) + investProductTypeView.getSumAmount(),
                                    AmountConverter.convertStringToCent(iphoneXActivityDto.getSumAnnualizedAmount()) + getAnnualizedAmount(investProductTypeView),
                                    getSumCashReward(AmountConverter.convertStringToCent(iphoneXActivityDto.getSumAnnualizedAmount()) + getAnnualizedAmount(investProductTypeView))));
        }
        return new ArrayList<>(map.values());
    }

    public Long getAnnualizedAmount(InvestProductTypeView investProductTypeView) {
        long amount = 0l;
        switch (investProductTypeView.getProductType()) {
            case _90:
                amount = (investProductTypeView.getSumAmount()) / 4;
                break;
            case _180:
                amount = (investProductTypeView.getSumAmount()) / 2;
                break;
            case _360:
                amount = investProductTypeView.getSumAmount();
                break;
        }
        return amount;
    }

    public String getSumCashReward(long annualizedAmount) {
        Optional<CashReward> reward = cashRewards.stream().filter(mothersReward -> mothersReward.getStartAmount() <= annualizedAmount && annualizedAmount < mothersReward.getEndAmount()).findAny();
        return reward.isPresent() ? reward.get().getReward() : "0";
    }

    class CashReward {
        private String reward;
        private Long startAmount;
        private Long endAmount;

        public CashReward(String reward, Long startAmount, Long endAmount) {
            this.reward = reward;
            this.startAmount = startAmount;
            this.endAmount = endAmount;
        }

        public String getReward() {
            return reward;
        }

        public Long getStartAmount() {
            return startAmount;
        }

        public Long getEndAmount() {
            return endAmount;
        }
    }

}
