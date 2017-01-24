package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
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

    private final static long INVEST_LIMIT = 100000l;


    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_NewBieExperience;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            InvestSuccessMessage investSuccessMessage;
            try {
                investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (isSendExperienceInterest(investSuccessMessage.getUserInfo())){
                payWrapperClient.sendExperienceInterestInvestSuccess(investSuccessMessage.getInvestInfo().getInvestId());
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
