package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.rest.client.mapper.UserMapper;
import com.tuotiansudai.util.JsonConverter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Calendar;

@Component
public class InvestSuccessExperienceAssignInterestCouponMessageConsumer implements MessageConsumer {

    private final static Logger logger = LoggerFactory.getLogger(InvestSuccessExperienceAssignInterestCouponMessageConsumer.class);

    private final static long INTEREST_COUPON_3_ID = 391L;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private MQWrapperClient mqClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_ExperienceAssignInterestCoupon;
    }

    @Override
    public void consume(String message) {
        logger.info("[新手体验项目方法加息券MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[新手体验项目方法加息券MQ] InvestSuccess_ExperienceAssignInterestCoupon receive message is empty");
            return;
        }

        InvestSuccessMessage investSuccessMessage;
        try {
            investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
        } catch (IOException e) {
            logger.error("[新手体验项目方法加息券MQ] json convert InvestSuccessMessage is fail, message:{}", message);
            return;
        }

        String loginName = investSuccessMessage.getInvestInfo().getLoginName();
        long investId = investSuccessMessage.getInvestInfo().getInvestId();

        if (isInterestCouponConditionAvailable(loginName, investId)) {
            logger.info(String.format("[新手体验项目方法加息券MQ] send interest coupon condition is available，loginName:%s investId:%s", loginName, investId));
            mqClient.sendMessage(MessageQueue.CouponAssigning,
                    MessageFormat.format("{0}:{1}", investSuccessMessage.getInvestInfo().getLoginName(), String.valueOf(INTEREST_COUPON_3_ID)));
        } else {
            logger.info(String.format("[新手体验项目方法加息券MQ] send interest coupon condition is not available，loginName:%s investId:%s", loginName, investId));
        }

        logger.info("[新手体验项目方法加息券MQ] receive message: {}: {} done.", this.queue(), message);
    }

    private boolean isInterestCouponConditionAvailable(String loginName, long investId) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        return userCouponMapper.findByLoginNameAndCouponId(loginName, INTEREST_COUPON_3_ID).size() == 0
                && investMapper.findById(investId).getTransferInvestId() == null
                && new DateTime(userModel.getRegisterTime()).plusDays(15).isAfterNow();
    }

}
