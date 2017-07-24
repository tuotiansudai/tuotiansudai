package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.ActivityInvestMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.ActivityInvestModel;
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

import java.io.IOException;
import java.util.Date;

@Component
public class InvestSuccessHouseDecorateMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessHouseDecorateMessageConsumer.class);

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.house.decorate.startTime}\")}")
    private Date activityHouseDecorateStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.house.decorate.endTime}\")}")
    private Date activityHouseDecorateEndTime;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_HouseDecorate;
    }

    @Override
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
            activityInvestMapper.create(new ActivityInvestModel(investInfo.getInvestId(),
                    userInfo.getLoginName(),
                    userInfo.getUserName(),
                    userInfo.getMobile(),
                    investInfo.getAmount(),
                    ActivityCategory.HOUSE_DECORATE_ACTIVITY.name()));

        } catch (IOException e) {
            logger.error("[MQ] parse message failed: {}: {}.", this.queue(), message);
        }

    }
}
