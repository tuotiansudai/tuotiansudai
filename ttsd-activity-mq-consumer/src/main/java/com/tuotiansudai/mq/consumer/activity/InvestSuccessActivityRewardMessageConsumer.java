package com.tuotiansudai.mq.consumer.activity;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanDetailsMapper;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.LoanDetailsModel;
import com.tuotiansudai.repository.model.TransferStatus;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

@Component
public class InvestSuccessActivityRewardMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessActivityRewardMessageConsumer.class);

    @Autowired
    private MQWrapperClient mqClient;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    final private static String LOAN_ACTIVITY_DESCRIPTION = "圣诞专享";

    final static private long INTEREST_COUPON_OF_ZERO_5_PERCENT_COUPON_ID = 324L;

    final static private long INVEST_LIMIT = 3000000L;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.startTime}\")}")
    private Date activityChristmasStartTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.christmas.endTime}\")}")
    private Date activityChristmasEndTime;

    @Override
    public MessageQueue queue() {
        return MessageQueue.InvestSuccess_ActivityReward;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            this.assignActivityChristmasInterestCoupon(Long.parseLong(message));
        }
    }

    private void assignActivityChristmasInterestCoupon(long investId){
        Date nowDate = DateTime.now().toDate();
        InvestModel investModel = investMapper.findById(investId);
        if(investModel == null){
            logger.info("[MQ] query invest by investId is not exists {}.", investId);
            return;
        }

        LoanDetailsModel loanDetailsModel = loanDetailsMapper.getByLoanId(investModel.getLoanId());
        if (loanDetailsModel == null) {
            logger.error("[MQ] query loanDetails by loanId is not exists {}.", investModel.getLoanId());
            return;
        }

        logger.info("[MQ] ready to consume activity message: assigning coupon.");
        if (investModel.getTransferStatus() != TransferStatus.SUCCESS
                && (activityChristmasStartTime.before(nowDate) && activityChristmasEndTime.after(nowDate))
                && loanDetailsModel.isActivity() && loanDetailsModel.getActivityDesc().equals(LOAN_ACTIVITY_DESCRIPTION)
                && investModel.getAmount() >= INVEST_LIMIT) {
            UserCouponModel userCoupon = couponAssignmentService.assign(investModel.getLoginName(), INTEREST_COUPON_OF_ZERO_5_PERCENT_COUPON_ID, null);
            if (userCoupon != null) {
                logger.info("[MQ] assigning activity coupon success, begin publish message.");
                mqClient.sendMessage(MessageQueue.InvestSuccess_ActivityReward, "UserCoupon:" + userCoupon.getId());
            } else {
                logger.info("[MQ] no user activity coupon assign.");
            }
            logger.info("[MQ] consume activity message success.");
        }
    }
}
