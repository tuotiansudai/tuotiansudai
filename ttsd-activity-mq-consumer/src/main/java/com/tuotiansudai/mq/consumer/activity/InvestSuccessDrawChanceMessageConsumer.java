package com.tuotiansudai.mq.consumer.activity;

import com.tuotiansudai.activity.repository.mapper.InvestCelebrationDrawChanceMapper;
import com.tuotiansudai.activity.repository.model.ActivityCategory;
import com.tuotiansudai.activity.repository.model.InvestDrawChanceModel;
import com.tuotiansudai.message.InvestInfo;
import com.tuotiansudai.message.InvestSuccessCelebrationOnePenMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
public class InvestSuccessDrawChanceMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(InvestSuccessActivityRewardMessageConsumer.class);

    @Autowired
    private InvestCelebrationDrawChanceMapper investCelebrationDrawChanceMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_InvestDrawChance;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        InvestSuccessCelebrationOnePenMessage investSuccessCelebrationOnePenMessage;
        try {
            investSuccessCelebrationOnePenMessage = JsonConverter.readValue(message, InvestSuccessCelebrationOnePenMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InvestInfo investInfo = investSuccessCelebrationOnePenMessage.getInvestInfo();
        if (!investInfo.getTransferStatus().equals("SUCCESS")
                && investInfo.getStatus().equals("SUCCESS")) {
            System.out.println("=======cfsdvds="+investCelebrationDrawChanceMapper.findByLoginName(investInfo.getLoginName()) != null);
            if (investCelebrationDrawChanceMapper.findByLoginName(investInfo.getLoginName()) != null) {
                investCelebrationDrawChanceMapper.updateChance(investInfo.getLoginName(),
                        investInfo.getAmount() < 1000000 ? 0 : Integer.parseInt(String.valueOf(investInfo.getAmount()).substring(0, 1)));
            } else {
                InvestDrawChanceModel investDrawChanceModel = new InvestDrawChanceModel(investInfo.getLoginName(),
                        investInfo.getAmount() < 1000000 ? 0 : Integer.parseInt(String.valueOf(investInfo.getAmount()).substring(0, 1)),
                        ActivityCategory.CELEBRATION_ONEPEN_ACTIVITY.getDescription());
                System.out.println("-------------"+ActivityCategory.CELEBRATION_ONEPEN_ACTIVITY.getDescription());
                investCelebrationDrawChanceMapper.create(investDrawChanceModel);
            }
        }
    }
}
