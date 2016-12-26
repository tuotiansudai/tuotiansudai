package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.LoanOutInfo;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanOutSuccessSendCouponMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessSendCouponMessageConsumer.class);

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_AssignCoupon;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            LoanOutInfo loanOutInfo;
            try {
                loanOutInfo = JsonConverter.readValue(message, LoanOutInfo.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            long loanId = loanOutInfo.getLoanId();
            List<String> fatalSmsList = Lists.newArrayList();

            logger.info("[MQ] ready to consume message: sendRedEnvelope is execute, loanId:{0}", loanId);
            BaseDto<PayDataDto> baseDto = payWrapperClient.sendRedEnvelopeAfterLoanOut(loanId);
            if (!baseDto.isSuccess()) {
                fatalSmsList.add("发送现金红包失败");
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess sendRedEnvelope is fail. loanId:{0}", String.valueOf(loanId)));
            }

            logger.info(MessageFormat.format("[MQ] assignInvestAchievementUserCoupon is execute , (loanId : {0}) ", String.valueOf(loanId)));
            if (!assignInvestAchievementUserCoupon(loanId)) {
                fatalSmsList.add("发送标王奖励失败");
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess assignInvestAchievementUserCoupon is fail. loanId:{0}", String.valueOf(loanId)));
            }


            if (CollectionUtils.isNotEmpty(fatalSmsList)) {
                fatalSmsList.add(MessageFormat.format("标的ID:{0}", loanId));
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(Joiner.on(",").join(fatalSmsList)));
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess_AssignCoupon fail sms sending. loanId:{0}", String.valueOf(loanId)));
            }

            logger.info("[MQ] consume LoanOutSuccess_AssignCoupon success.");
        }
    }

    private boolean assignInvestAchievementUserCoupon(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        boolean result = true;
        if (!createUserCouponModel(loanModel.getFirstInvestAchievementId(), UserGroup.FIRST_INVEST_ACHIEVEMENT, loanId)) {
            logger.error(MessageFormat.format("[标的放款] assign FIRST_INVEST_ACHIEVEMENT is fail, loanId:{0}", String.valueOf(loanId)));
            result = false;
        }

        if (!createUserCouponModel(loanModel.getMaxAmountAchievementId(), UserGroup.MAX_AMOUNT_ACHIEVEMENT, loanId)) {
            logger.error(MessageFormat.format("[标的放款] assign MAX_AMOUNT_ACHIEVEMENT is fail, loanId:{0}", String.valueOf(loanId)));
            result = false;
        }

        if (!createUserCouponModel(loanModel.getLastInvestAchievementId(), UserGroup.LAST_INVEST_ACHIEVEMENT, loanId)) {
            logger.error(MessageFormat.format("[标的放款] assign LAST_INVEST_ACHIEVEMENT is fail, loanId:{0}", String.valueOf(loanId)));
            result = false;
        }

        return result;
    }

    private boolean createUserCouponModel(Long investId, final UserGroup userGroup, long loanId) {
        if (investId == null || investId == 0) {
            logger.error(MessageFormat.format("loan id : {0} nothing {1}", String.valueOf(loanId), userGroup.name()));
            return false;
        }

        boolean result = true;
        List<CouponModel> couponModelList = couponMapper.findAllActiveCoupons();

        List<CouponModel> collect = couponModelList.stream().filter(couponModel -> couponModel.getUserGroup().equals(userGroup)
                && DateTime.now().toDate().before(couponModel.getEndTime())
                && DateTime.now().toDate().after(couponModel.getStartTime())).collect(Collectors.toList());

        for (CouponModel couponModel : collect) {
            if (!couponAssignmentService.assignInvestAchievementUserCoupon(loanId, investMapper.findById(investId).getLoginName(), couponModel.getId())) {
                result = false;
            }
        }
        return result;
    }
}
