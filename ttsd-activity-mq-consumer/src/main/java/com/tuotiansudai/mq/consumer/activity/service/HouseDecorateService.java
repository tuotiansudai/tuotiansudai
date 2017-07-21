package com.tuotiansudai.mq.consumer.activity.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityInvestView;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.message.ExperienceAssigningMessage;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HouseDecorateService {

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    @Value(value = "${activity.house.decorate.startTime}")
    private String startTime;

    @Value(value = "${activity.house.decorate.endTime}")
    private String endTime;

    private final List<ExperienceReward> familyFinanceRewards = Lists.newArrayList(
            new ExperienceReward(1000000l, 10000000l, 0.5),
            new ExperienceReward(10000000l, 30000000l, 0.8),
            new ExperienceReward(30000000l, 60000000l, 1.8),
            new ExperienceReward(60000000l,  Long.MAX_VALUE,2.18));

    public List<ExperienceAssigningMessage> yesterdayObtainExperience(){
        Date grantExperienceStartTime = DateTime.parse(startTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).plusDays(1).toDate();
        Date grantExperienceEndTime = DateTime.parse(endTime, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).plusDays(1).toDate();
        if(new Date().before(grantExperienceStartTime) || new Date().after(grantExperienceEndTime)){
            return null;
        }
        List<ActivityInvestView> list=activityInvestMapper.findSumAmountByNameDateAndActivity(ActivityCategory.HOUSE_DECORATE_ACTIVITY.name(), DateTime.now().minusDays(1).withTimeAtStartOfDay().toDate(), DateTime.now().withTimeAtStartOfDay().minusMillis(1).toDate());
        return experienceReward(list);
    }

    public List<ExperienceAssigningMessage> experienceReward(List<ActivityInvestView> list){
        List<ExperienceAssigningMessage> experienceAssigningMessages = new ArrayList<>();
        for (ActivityInvestView activityView:list) {
            Optional<ExperienceReward> reward = familyFinanceRewards.stream().filter(rewards -> rewards.getStartAmount() <= activityView.getSumAmount() && activityView.getSumAmount() < rewards.getEndAmount()).findAny();
            if (reward.isPresent()){
                experienceAssigningMessages.add(new ExperienceAssigningMessage(activityView.getLoginName(),Math.round(Math.floor(activityView.getSumAmount()*reward.get().getMultiple()/100))*100,
                        ExperienceBillOperationType.IN, ExperienceBillBusinessType.HOUSE_DECORATE));
            }
        }
        return experienceAssigningMessages;
    }


    class ExperienceReward {
        private Long startAmount;
        private Long endAmount;
        private Double multiple;

        public ExperienceReward(Long startAmount, Long endAmount, Double multiple) {
            this.startAmount = startAmount;
            this.endAmount = endAmount;
            this.multiple = multiple;
        }

        public Long getStartAmount() {
            return startAmount;
        }

        public Long getEndAmount() {
            return endAmount;
        }

        public Double getMultiple() {
            return multiple;
        }
    }
}
