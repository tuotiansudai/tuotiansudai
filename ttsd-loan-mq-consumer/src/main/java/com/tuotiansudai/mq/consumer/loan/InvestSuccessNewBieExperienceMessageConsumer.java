package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.mapper.UserMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.JsonConverter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Component
public class InvestSuccessNewBieExperienceMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(InvestSuccessNewBieExperienceMessageConsumer.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private MQWrapperClient mqClient;

    private final static long INVEST_LIMIT = 100000L;

    private static final long NEWBIE_COUPON_ID = 382L;

    private final static long INTEREST_COUPON_3_ID = 391L;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_NewBieExperience;
    }

    @Override
    public void consume(String message) {
        logger.info("[新手体验项目MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[新手体验项目MQ] InvestSuccess_NewBieExperience receive message is empty");
            return;
        }

        InvestSuccessMessage investSuccessMessage;
        try {
            investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
        } catch (IOException e) {
            logger.error("[新手体验项目MQ] InvestSuccess_NewBieExperience json convert InvestSuccessMessage is fail, message:{}", message);
            return;
        }

        String loginName = investSuccessMessage.getInvestInfo().getLoginName();
        long investId = investSuccessMessage.getInvestInfo().getInvestId();

        if (isExperienceInterestConditionAvailable(loginName)) {
            logger.info(String.format("[新手体验项目MQ] experience interest condition is available，loginName:%s investId:%s", loginName, investId));
            try {
                BaseDto<PayDataDto> baseDto = payWrapperClient.experienceRepay(investId);
                if (!baseDto.isSuccess()) {
                    logger.error("[新手体验项目MQ] send experience interest consume fail (loginName:{}, message:{}) ", loginName, message);
                    throw new RuntimeException(String.format("InvestSuccess_NewBieExperience consume fail. message: %s", message));
                }
                logger.info(String.format("[新手体验项目MQ] send experience interest end，investId:%s", investId));
            } catch (Exception e) {
                logger.error(MessageFormat.format("[新手体验项目MQ] send experience interest fail (loginName:{0}, message:{1})", loginName, message) , e);
                return;
            }
        } else {
            logger.info(String.format("[新手体验项目MQ] experience interest condition is not available，loginName:%s investId:%s", loginName, investId));
        }


        if (isInterestCouponConditionAvailable(loginName, investId)) {
            logger.info(String.format("[新手体验项目MQ] send interest coupon condition is available，loginName:%s investId:%s", loginName, investId));
            mqClient.sendMessage(MessageQueue.CouponAssigning,
                    MessageFormat.format("{0}:{1}", investSuccessMessage.getInvestInfo().getLoginName(), String.valueOf(investId)));
        } else {
            logger.info(String.format("[新手体验项目MQ] send interest coupon condition is not available，loginName:%s investId:%s", loginName, investId));
        }

        logger.info("[新手体验项目MQ] receive message: {}: {} done.", this.queue(), message);
    }

    private boolean isInterestCouponConditionAvailable(String loginName, long investId) {
        UserModel userModel = userMapper.findByLoginName(loginName);
        return userCouponMapper.findByLoginNameAndCouponId(loginName, INTEREST_COUPON_3_ID) == null
                && investMapper.findById(investId).getTransferInvestId() == null
                && new DateTime(userModel.getRegisterTime()).plusDays(15).isAfterNow();
    }

    private boolean isExperienceInterestConditionAvailable(String loginName) {
        long investAmount = investMapper.sumSuccessInvestCountByLoginName(loginName, false);
        if (investAmount < INVEST_LIMIT) {
            return false;
        }

        List<UserCouponModel> newbieCouponList = userCouponMapper.findByLoginNameAndCouponId(loginName, NEWBIE_COUPON_ID);
        UserCouponModel usedNewbieCoupon = newbieCouponList != null && newbieCouponList.size() == 1 && newbieCouponList.get(0).getStatus() == InvestStatus.SUCCESS ?
                newbieCouponList.get(0) : null;

        if (usedNewbieCoupon == null) {
            return false;
        }

        InvestRepayModel investRepayModel = investRepayMapper.findByInvestIdAndPeriod(usedNewbieCoupon.getInvestId(), 1);

        return investRepayModel != null && investRepayModel.getStatus() == RepayStatus.REPAYING;
    }
}
