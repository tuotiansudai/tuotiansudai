package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.UserCouponMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.JsonConverter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InvestSuccessExperienceRepayMessageConsumer implements MessageConsumer {

    private final static Logger logger = LoggerFactory.getLogger(InvestSuccessExperienceRepayMessageConsumer.class);

    private final static long INVEST_LIMIT = 100L;

    private static final long NEWBIE_COUPON_ID = 382L;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_ExperienceRepay;
    }

    @Override
    public void consume(String message) {
        logger.info("[新手体验项目收益发放MQ] receive message: {}: {}.", this.queue(), message);

        if (Strings.isNullOrEmpty(message)) {
            logger.error("[新手体验项目收益发放MQ] InvestSuccess_ExperienceRepay receive message is empty");
            return;
        }

        try {
            InvestSuccessMessage investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
            String loginName = investSuccessMessage.getInvestInfo().getLoginName();
            this.isExperienceInterestConditionAvailable(loginName);

            if (!isExperienceInterestConditionAvailable(loginName)) {
                logger.info("[新手体验项目收益发放MQ] 条件不符合，{}", loginName);
                return;
            }

            logger.info("[新手体验项目收益发放MQ] 条件符合，{}", loginName);

            BaseDto<PayDataDto> baseDto = payWrapperClient.experienceRepay(loginName);

            if (!baseDto.isSuccess()) {
                logger.error("[新手体验项目收益发放MQ] 发放体验金收益失败 {}", loginName);
                return;
            }
            logger.info("[新手体验项目收益发放MQ] 发放体验金收益成功，{}", loginName);
        } catch (Exception e) {
            logger.error("[新手体验项目收益发放MQ] experience repay is fail, message:{}", message);
        }

        logger.info("[新手体验项目收益发放MQ] receive message: {}: {} done.", this.queue(), message);
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

        return investRepayModel != null
                && investRepayModel.getStatus() == RepayStatus.REPAYING
                && new DateTime(investRepayModel.getRepayDate()).withTimeAtStartOfDay().isEqual(new DateTime().withTimeAtStartOfDay());
    }
}
