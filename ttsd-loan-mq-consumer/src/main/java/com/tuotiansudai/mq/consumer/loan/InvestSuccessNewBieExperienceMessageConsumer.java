package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.enums.UserBillBusinessType;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.message.UserInfo;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Component
public class InvestSuccessNewBieExperienceMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessNewBieExperienceMessageConsumer.class);
    @Autowired
    private UserCouponMapper userCouponMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private UserBillMapper userBillMapper;
    @Autowired
    private PayWrapperClient payWrapperClient;
    @Autowired
    private SmsWrapperClient smsWrapperClient;

    private final static long INVEST_LIMIT = 100000l;


    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_NewBieExperience;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[新手体验项目MQ] InvestSuccess_NewBieExperience receive message is empty");
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("新手体验项目发奖励失败, MQ消息为空"));
            return;
        }
        InvestSuccessMessage investSuccessMessage;
        try {
            investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
        } catch (IOException e) {
            logger.error("[新手体验项目MQ] InvestSuccess_NewBieExperience json convert InvestSuccessMessage is fail, message:{}", message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("新手体验项目发奖励失败, 解析消息失败"));
            throw new RuntimeException(e);
        }
        long investId = investSuccessMessage.getInvestInfo().getInvestId();
        logger.info(String.format("[新手体验项目MQ] InvestSuccess_NewBieExperience ready，investId:%s", investId));
        if (isSendExperienceInterest(investSuccessMessage.getUserInfo())) {
            try {
                logger.info(String.format("[新手体验项目MQ] InvestSuccess_NewBieExperience begin，investId:%s", investId));
                BaseDto<PayDataDto> baseDto = payWrapperClient.sendExperienceInterestInvestSuccess(investId);
                if (!baseDto.isSuccess()) {
                    logger.error("[新手体验项目MQ] InvestSuccess_NewBieExperience  consume fail (message = {0}) ", message);
                    throw new RuntimeException(String.format("InvestSuccess_NewBieExperience consume fail. message: %s",message));
                }
                logger.info(String.format("[新手体验项目MQ] InvestSuccess_NewBieExperience end，investId:%s", investId));
            } catch (Exception e) {
                logger.error("[新手体验项目MQ] InvestSuccess_NewBieExperience send experience interest is fail,investId({0}), error:{0}", String.valueOf(investId), e);
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(String.format("新手体验项目发奖励失败, 业务处理异常,投资ID:{0}", String.valueOf(investId))));
                return;
            }
        }

    }

    private boolean isSendExperienceInterest(UserInfo userInfo) {
        String loginName = userInfo.getLoginName();
        String mobile = userInfo.getMobile();
        List<UserCouponModel> userCouponModels = userCouponMapper.findUsedExperienceByLoginName(loginName);
        long investAmount = investMapper.sumSuccessActivityInvestAmount(loginName, null, null, null);
        boolean investedExperience = userCouponModels.stream().anyMatch(userCouponModel -> couponMapper.findById(userCouponModel.getCouponId()).isDeleted() == false);
        int experienceInterest = userBillMapper.findUserFundsCount(UserBillBusinessType.EXPERIENCE_INTEREST, null, mobile, null, null);
        return investedExperience && investAmount >= INVEST_LIMIT && experienceInterest <= 0;
    }


}
