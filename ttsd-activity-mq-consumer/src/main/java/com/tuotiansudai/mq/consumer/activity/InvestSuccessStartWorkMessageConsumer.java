package com.tuotiansudai.mq.consumer.activity;


import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.message.*;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class InvestSuccessStartWorkMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessStartWorkMessageConsumer.class);

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.start.work.startTime}\")}")
    private Date activityStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.start.work.endTime}\")}")
    private Date activityEndTime;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_StartWork;
    }

    @Override
    @Transactional
    public void consume(String message) {

        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] message is empty");
            return;
        }

        try {
            InvestSuccessMessage investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);

            UserInfo userInfo = investSuccessMessage.getUserInfo();
            InvestInfo investInfo = investSuccessMessage.getInvestInfo();
            LoanDetailInfo loanDetailInfo = investSuccessMessage.getLoanDetailInfo();

            if (!activityStartTime.after(new Date()) && !new Date().after(activityEndTime) &&
                    !loanDetailInfo.getActivityType().equals("NEWBIE")
                    && !investInfo.getTransferStatus().equals("SUCCESS")
                    && investInfo.getStatus().equals("SUCCESS")) {
                sendExperience(userInfo.getLoginName(), investInfo.getAmount());
            }

        } catch (Exception e) {
            logger.error("[MQ] 程序內部异常: {}: {}.{}", this.queue(), message, e.getMessage());
        }
    }

    private void sendExperience(String loginName, long amount) {
        long experienceAmount = 0;
        if (amount >= 1000000 && amount < 5000000) {
            experienceAmount = 600000;
        }
        if (amount >= 5000000 && amount < 10000000) {
            experienceAmount = 3800000;
        }
        if (amount >= 10000000){
            experienceAmount = 10000000;
        }

        if (experienceAmount == 0) {
            return;
        }
        mqWrapperClient.sendMessage(MessageQueue.ExperienceAssigning,
                new ExperienceAssigningMessage(loginName, experienceAmount, ExperienceBillOperationType.IN, ExperienceBillBusinessType.START_WORK_ACTIVITY));
    }
}
