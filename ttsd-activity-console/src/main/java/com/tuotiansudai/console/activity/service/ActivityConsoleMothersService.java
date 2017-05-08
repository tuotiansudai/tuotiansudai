package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.model.MothersDayView;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.repository.mapper.ExperienceBillMapper;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.util.AmountConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ActivityConsoleMothersService {

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ExperienceBillMapper experienceBillMapper;

    private final List<MothersReward> mothersRewards = Lists.newArrayList(
            new MothersReward("拓天速贷定制礼盒一", 5000000l, 7000000l),
            new MothersReward("拓天速贷定制礼盒二", 7000000l, 11000000l),
            new MothersReward("拓天速贷定制礼盒三", 11000000l, 25000000l),
            new MothersReward("瑞士天梭表", 11000000l, 25000000l),
            new MothersReward("OPPO R9s清新绿", 25000000l, 50000000l),
            new MothersReward("iphone7中国红128G", 50000000l, 130000000l));

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mothers.day.startTime}\")}")
    private Date activityStartTimeStr;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mothers.day.endTime}\")}")
    private Date activityEndTimeStr;


    public BasePaginationDataDto<MothersDayView> list(int index, int pageSize) {
        List<MothersDayView> mothersDayViews = getInvestRecord();
        int count = mothersDayViews.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, mothersDayViews.subList(startIndex, endIndex));
        return basePaginationDataDto;
    }

    private List<MothersDayView> getInvestRecord() {
        List<MothersDayView> list = Lists.newArrayList();
        List<InvestModel> investModels = investMapper.findSuccessInvestByInvestTime(null, activityStartTimeStr, activityEndTimeStr);
        Map<String, Long> investAmountMaps = Maps.newConcurrentMap();
        for (InvestModel investModel : investModels) {
            if (investModel.getLoanId() == 1 || investModel.getTransferInvestId() != null)
                continue;

            if (investAmountMaps.get(investModel.getLoginName()) == null) {
                investAmountMaps.put(investModel.getLoginName(), investModel.getAmount());
                continue;
            }
            investAmountMaps.put(investModel.getLoginName(), investAmountMaps.get(investModel.getLoginName()) + investModel.getAmount());
        }

        investAmountMaps.forEach((k, v) -> {
            UserModel userModel = userMapper.findByLoginName(k);
            list.add(new MothersDayView(k, userModel.getUserName(),
                    userModel.getMobile(),
                    AmountConverter.convertCentToString(v),
                    getRewardDescription(v),
                    experienceBillMapper.findSumExperienceBillByBusinessType(k, ExperienceBillBusinessType.MOTHERS_DAY.name())));
        });

        return list;
    }


    private String getRewardDescription(long amount) {
        Optional<MothersReward> reward = mothersRewards.stream().filter(mothersReward -> mothersReward.getStartAmount() <= amount && amount < mothersReward.getEndAmount()).findAny();
        return reward.isPresent() ? reward.get().getDescription() : "";
    }

    class MothersReward {
        private String description;
        private Long startAmount;
        private Long endAmount;

        public MothersReward(String description, Long startAmount, Long endAmount) {
            this.description = description;
            this.startAmount = startAmount;
            this.endAmount = endAmount;
        }

        public String getDescription() {
            return description;
        }

        public Long getStartAmount() {
            return startAmount;
        }

        public Long getEndAmount() {
            return endAmount;
        }
    }

}
