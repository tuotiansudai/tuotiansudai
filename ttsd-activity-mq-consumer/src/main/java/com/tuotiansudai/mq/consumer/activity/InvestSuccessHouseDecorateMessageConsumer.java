package com.tuotiansudai.mq.consumer.activity;

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

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.house.decorate.startTime}\")}")
    private Date startTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.house.decorate.endTime}\")}")
    private Date endTime;

    @Autowired
    private ActivityInvestMapper activityInvestMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_HouseDecorate;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        InvestSuccessMessage investSuccessMessage;
        try {
            investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UserInfo userInfo = investSuccessMessage.getUserInfo();
        InvestInfo investInfo = investSuccessMessage.getInvestInfo();
        if (startTime.compareTo(new Date()) <= 0 && endTime.compareTo(new Date()) >=0
                && !investInfo.getTransferStatus().equals("SUCCESS")
                && investInfo.getStatus().equals("SUCCESS")) {
            ActivityInvestModel activityInvestModel = new ActivityInvestModel(investInfo.getInvestId(),
                    userInfo.getLoginName(),
                    userInfo.getUserName(),
                    userInfo.getMobile(),
                    investInfo.getAmount(),
                    ActivityCategory.HOUSE_DECORATE_ACTIVITY.name()
            );
            activityInvestMapper.create(activityInvestModel);
        }
    }
}
