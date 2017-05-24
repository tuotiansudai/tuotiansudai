package com.tuotiansudai.mq.consumer.activity;

import com.google.common.base.Strings;
import com.tuotiansudai.activity.repository.mapper.DragonBoatFestivalMapper;
import com.tuotiansudai.activity.repository.model.DragonBoatFestivalModel;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.lang3.StringUtils;
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
    private DragonBoatFestivalMapper dragonBoatFestivalMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_DragonBoat;
    }

    @Autowired
    private MQWrapperClient mqWrapperClient;

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

        if (isInActivityPeriod() && investSuccessMessage.getLoanDetailInfo().getLoanId() != 1) {
            String loginName = investSuccessMessage.getUserInfo().getLoginName();
            String userName = investSuccessMessage.getUserInfo().getUserName();
            String mobile = investSuccessMessage.getUserInfo().getMobile();
            long investAmount = investSuccessMessage.getInvestInfo().getAmount();

            DragonBoatFestivalModel dbfModel = dragonBoatFestivalMapper.findByLoginName(loginName);

            // 发放香槟塔优惠券
            sendChampagnePrize(loginName, dbfModel == null ? 0 : dbfModel.getTotalInvestAmount(), investAmount);

            dragonBoatFestivalMapper.addTotalInvestAmount(loginName, userName, mobile, investAmount);

            if (dbfModel != null && StringUtils.isNotEmpty(dbfModel.getPkGroup())) {
                logger.info("[MQ] dragon PK, add money for group {}, investor:{}, money:{}", dbfModel.getPkGroup(), loginName, investAmount);
                dragonBoatFestivalMapper.addPKInvestAmount(loginName, investAmount);
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

    // 根据活动期间内已有投资额和本次投资额，即时发放香槟塔优惠券
    private void sendChampagnePrize(String loginName, long amountBefore, long investAmount) {
        long amountAfter = amountBefore + investAmount;

        // 判断本次投资带来的奖励
        if (amountBefore < 500000 && 500000 <= amountAfter) {
            sendPrize5Coupon(loginName);
        }
        if (amountBefore < 6000000 && 6000000 <= amountAfter) {
            sendPrize4Coupon(loginName);
        }
        if (amountBefore < 12000000 && 12000000 <= amountAfter) {
            sendPrize3Coupon(loginName);
        }
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
