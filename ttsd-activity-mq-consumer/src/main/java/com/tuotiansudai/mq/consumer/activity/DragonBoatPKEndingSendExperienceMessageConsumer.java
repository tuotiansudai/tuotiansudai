package com.tuotiansudai.mq.consumer.activity;

import com.tuotiansudai.activity.repository.model.DragonBoatFestivalModel;
import com.tuotiansudai.activity.service.DragonBoatFestivalService;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.enums.ExperienceBillBusinessType;
import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.message.ExperienceAssigningMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

// 端午节粽子PK活动结束后，发放体验金
@Component
public class DragonBoatPKEndingSendExperienceMessageConsumer implements MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DragonBoatPKEndingSendExperienceMessageConsumer.class);

    @Autowired
    private DragonBoatFestivalService dragonBoatFestivalService;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.DragonBoatPKEndSendExperience;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        // 获取甜粽和咸粽累计投资金额，判断谁赢了
        long sweetInvestAmount = dragonBoatFestivalService.getGroupInvestAmount("SWEET");
        logger.info("[MQ] dragon boat activity ended. sweet invest amount:{}", sweetInvestAmount);

        long saltyInvestAmount = dragonBoatFestivalService.getGroupInvestAmount("SALTY");
        logger.info("[MQ] dragon boat activity ended. salty invest amount:{}", saltyInvestAmount);

        boolean sweetWin = sweetInvestAmount > saltyInvestAmount;
        logger.info("[MQ] dragon boat activity ended. {} group win.", sweetWin ? "SWEET" : "SALTY");

        // 获取参与PK的用户列表，以及投资金额，给他们发放体验金
        List<DragonBoatFestivalModel> recordList = dragonBoatFestivalService.getDragonBoatFestivalPKUserList();
        for (DragonBoatFestivalModel model : recordList) {
            // 算出用户的体验金奖励金额
            long experienceAmount = model.getPkInvestAmount() / (sweetWin ^ model.getPkGroup().equals("SALTY") ? 1 : 2);

            mqWrapperClient.sendMessage(MessageQueue.ExperienceAssigning,
                    new ExperienceAssigningMessage(model.getLoginName(), experienceAmount, ExperienceBillOperationType.IN, ExperienceBillBusinessType.DRAGON_BOAT_ZONGZI_PK));

            // 更新用户获取PK体验金奖励的记录
            dragonBoatFestivalService.setPKExperienceAmount(model.getLoginName(), experienceAmount);
        }

        logger.info("[MQ] receive message: {}: {} done.", this.queue(), message);
    }

}
