package com.tuotiansudai.mq.consumer.loan;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.UserBillMapper;
import com.tuotiansudai.util.JsonConverter;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Component
public class InvestSuccessExperienceInterestMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessExperienceInterestMessageConsumer.class);

    @Autowired
    private MQWrapperClient mqClient;
    @Autowired
    private UserCouponMapper userCouponMapper;
    @Autowired
    private InvestMapper investMapper;
    @Autowired
    private CouponMapper couponMapper;
    @Autowired
    private UserBillMapper userBillMapper;

    private final static long INVEST_LIMIT = 100000l;


    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_ExperienceInterest;
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
            String loginName = investSuccessMessage.getUserInfo().getLoginName();
            List<UserCouponModel> userCouponModels = userCouponMapper.findUsedExperienceByLoginName(loginName);
            long investAmount = investMapper.sumSuccessActivityInvestAmount(loginName, null, null, null);
            boolean investedExperience = userCouponModels.stream().anyMatch(userCouponModel -> couponMapper.findById(userCouponModel.getCouponId()).isDeleted() == false);
            if (investedExperience && investAmount >= INVEST_LIMIT) {
                mqClient.sendMessage(MessageQueue.InvestSuccess_ExperienceInterest, );
            }

        }

    }


}
