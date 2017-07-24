package com.tuotiansudai.console.activity.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.model.*;
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
public class ActivityConsoleHouseDecorateService {

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    @Autowired
    private ExperienceBillMapper experienceBillMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.house.decorate.startTime}\")}")
    private Date activityHouseDecorateStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.house.decorate.endTime}\")}")
    private Date activityHouseDecorateEndTime;

    private final List<ActivityConsoleHouseDecorateService.ActivityRewards> activityRewards = Lists.newArrayList(
            new ActivityConsoleHouseDecorateService.ActivityRewards("法国进口红酒", 3800000l, 6800000l),
            new ActivityConsoleHouseDecorateService.ActivityRewards("按摩披肩", 6800000l, 8800000l),
            new ActivityConsoleHouseDecorateService.ActivityRewards("足浴盆", 8800000l, 18180000l),
            new ActivityConsoleHouseDecorateService.ActivityRewards("艾美特遥控塔扇", 18180000l, 28180000l),
            new ActivityConsoleHouseDecorateService.ActivityRewards("格兰仕极光微波炉", 28180000l, 38180000l),
            new ActivityConsoleHouseDecorateService.ActivityRewards("松下吸尘器", 38180000l, 58180000l),
            new ActivityConsoleHouseDecorateService.ActivityRewards("小天鹅滚筒洗衣机", 58180000l, Long.MAX_VALUE));


    public BasePaginationDataDto<ActivityInvestRewardView> list(int index, int pageSize) {
        List<ActivityInvestRewardView> activityInvestRewardViews = getInvestRecord();
        int count = activityInvestRewardViews.size();
        int endIndex = pageSize * index;
        int startIndex = (index - 1) * 10;
        if (count <= endIndex) {
            endIndex = count;
        }
        if (count < startIndex) {
            startIndex = count;
        }
        BasePaginationDataDto basePaginationDataDto = new BasePaginationDataDto(index, pageSize, count, activityInvestRewardViews.subList(startIndex, endIndex));
        return basePaginationDataDto;
    }

    private List<ActivityInvestRewardView> getInvestRecord() {

        List<ActivityInvestView> activityInvestViews = activityInvestMapper.sumAmountByNameDateAndActivity(ActivityCategory.HOUSE_DECORATE_ACTIVITY.name(),activityHouseDecorateStartTime,activityHouseDecorateEndTime);
        if(activityInvestViews.size()==0){
            return null;
        }
        List<ActivityInvestRewardView> list = Lists.newArrayList();
        for (ActivityInvestView activityInvestView:activityInvestViews) {
            list.add(new ActivityInvestRewardView(activityInvestView,
                    getRewardDescription(activityInvestView.getSumAmount()),
                    experienceBillMapper.findSumExperienceBillByBusinessType(activityInvestView.getLoginName(), ExperienceBillBusinessType.HOUSE_DECORATE.name())));
        }
        return list;
    }

    private String getRewardDescription(long amount){
        Optional<ActivityRewards> reward = activityRewards.stream().filter(mothersReward -> mothersReward.getStartAmount() <= amount && amount < mothersReward.getEndAmount()).findAny();
        return reward.isPresent() ? reward.get().getDescription() : "";
    }

    class ActivityRewards {
        private String description;
        private Long startAmount;
        private Long endAmount;

        public ActivityRewards(String description, Long startAmount, Long endAmount) {
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
