package com.tuotiansudai.mq.consumer.activity;

import com.tuotiansudai.activity.repository.mapper.DragonBoatFestivalMapper;
import com.tuotiansudai.activity.repository.model.DragonBoatFestivalModel;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DragonBoatChampageEndingSendPrizeMessageConsumer implements MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DragonBoatChampageEndingSendPrizeMessageConsumer.class);

    @Autowired
    private DragonBoatFestivalMapper dragonBoatFestivalMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.DragonBoatChampagneEndSendCoupon;
    }

    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);

        List<DragonBoatFestivalModel> recordList = dragonBoatFestivalMapper.getDragonBoatFestivalChampagneList();
        for (DragonBoatFestivalModel model : recordList) {
            if (model.getTotalInvestAmount() >= 500000) {
                sendPrize5Coupon(model.getLoginName());
            }
            if (model.getTotalInvestAmount() >= 6000000) {
                sendPrize4Coupon(model.getLoginName());
            }
            if (model.getTotalInvestAmount() >= 12000000) {
                sendPrize3Coupon(model.getLoginName());
            }
        }

        logger.info("[MQ] receive message: {}: {} done.", this.queue(), message);
    }

    private void sendPrize5Coupon(String loginName) {
        // 422~426
        logger.info("[MQ][dragon boat end] send champagne prize level 5 for {}.", loginName);
        mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":422");
        mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":423");
        mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":424");
        mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":425");
        mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":426");
    }

    private void sendPrize4Coupon(String loginName) {
        // 427~428
        logger.info("[MQ][dragon boat end] send champagne prize level 4 for {}.", loginName);
        mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":427");
        mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":428");
    }

    private void sendPrize3Coupon(String loginName) {
        // 429
        logger.info("[MQ][dragon boat end] send champagne prize level 3 for {}.", loginName);
        mqWrapperClient.sendMessage(MessageQueue.CouponAssigning, loginName + ":429");
    }

}
