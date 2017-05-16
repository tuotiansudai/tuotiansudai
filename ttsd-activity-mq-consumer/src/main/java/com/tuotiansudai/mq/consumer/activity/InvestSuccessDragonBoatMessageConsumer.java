package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.service.DragonBoatFestivalService;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class InvestSuccessDragonBoatMessageConsumer implements MessageConsumer {

    private final static Logger logger = LoggerFactory.getLogger(InvestSuccessDragonBoatMessageConsumer.class);

    @Autowired
    private DragonBoatFestivalService dragonBoatFestivalService;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_DragonBoat;
    }

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.dragon.boat.startTime}\")}")
    private Date startTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.dragon.boat.endTime}\")}")
    private Date endTime;

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[MQ] InvestSuccess_DragonBoat receive message is empty");
            return;
        }

        InvestSuccessMessage investSuccessMessage;
        try {
            investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
        } catch (IOException e) {
            logger.error("[MQ] InvestSuccess_DragonBoat, json convert InvestSuccessMessage is fail, message:{}", message);
            return;
        }

        if (isInActivityPeriod()) {
            String loginName = investSuccessMessage.getInvestInfo().getLoginName();
            long investAmount = investSuccessMessage.getInvestInfo().getAmount();

            dragonBoatFestivalService.addTotalInvestAmount(loginName, investAmount);

            String group = dragonBoatFestivalService.getGroupByLoginName(loginName);
            if (group != null) {
                logger.info("[MQ] dragon PK, add money for group {}, investor:{}, money:{}", group, loginName, investAmount);
                dragonBoatFestivalService.addPKInvestAmount(loginName, investAmount);
            }
        }
        logger.info("[MQ] receive message: {}: {} done.", this.queue(), message);
    }


    private boolean isInActivityPeriod() {
        Date now = new Date();
        if (now.before(startTime)) {
            logger.info("[MQ] dragon boat activity has not started yet.");
            return false;
        } else if (now.after(endTime)) {
            logger.info("[MQ] dragon boat activity is end.");
            return false;
        } else {
            return true;
        }
    }
}
