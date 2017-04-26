package com.tuotiansudai.scheduler.loan;


import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.message.ExperienceAssigningMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class MothersDayExperienceScheduler {

    static Logger logger = LoggerFactory.getLogger(MothersDayExperienceScheduler.class);

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mothers.day.startTime}\")}")
    private Date activityStartTimeStr;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.mothers.day.endTime}\")}")
    private Date activityEndTimeStr;

    private final List<ExperienceReward> mothersRewards = Lists.newArrayList(
            new ExperienceReward(688800l, 1000000l, 5000000l),
            new ExperienceReward(3888800l, 5000000l, 10000000l),
            new ExperienceReward(8888800l, 10000000l, 20000000l),
            new ExperienceReward(18888800l, 20000000l, Long.MAX_VALUE));

    @Scheduled(cron = "0 40 15 * * ?", zone = "Asia/Shanghai")
    public void grantMothersDayExperience() {
        logger.info("[mothersDay grant experience start...]");

        List<InvestModel> investModels = investMapper.findSuccessInvestByInvestTime(null, activityStartTimeStr, activityEndTimeStr);
        investModels.stream().
                filter(investModel -> investModel.getAmount() >= 1000000l).
                forEach(investModel -> mqWrapperClient.sendMessage(MessageQueue.ExperienceAssigning,
                        new ExperienceAssigningMessage(investModel.getLoginName(), calculateExperience(investModel.getAmount()), ExperienceBillOperationType.IN, ExperienceBillBusinessType.MOTHERS_TREE)));

        logger.info("[mothersDay grant experience end...]");
    }

    public long calculateExperience(long amount) {
        Optional<ExperienceReward> reward = mothersRewards.stream().filter(mothersReward -> mothersReward.getStartAmount() <= amount && amount < mothersReward.getEndAmount()).findAny();
        return reward.isPresent() ? reward.get().getExperienceAmount() : 0;
    }

    class ExperienceReward {
        private Long experienceAmount;
        private Long startAmount;
        private Long endAmount;

        public ExperienceReward(Long experienceAmount, Long startAmount, Long endAmount) {
            this.experienceAmount = experienceAmount;
            this.startAmount = startAmount;
            this.endAmount = endAmount;
        }

        public Long getExperienceAmount() {
            return experienceAmount;
        }

        public Long getStartAmount() {
            return startAmount;
        }

        public Long getEndAmount() {
            return endAmount;
        }
    }
}
