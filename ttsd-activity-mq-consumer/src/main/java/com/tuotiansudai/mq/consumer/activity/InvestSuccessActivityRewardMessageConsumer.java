package com.tuotiansudai.mq.consumer.activity;

import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.message.InvestSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;

@Component
public class InvestSuccessActivityRewardMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(InvestSuccessActivityRewardMessageConsumer.class);

    @Autowired
    private MQWrapperClient mqClient;

    final private static String LOAN_ACTIVITY_DESCRIPTION = "圣诞专享";

    final static private long INTEREST_COUPON_OF_ZERO_5_PERCENT_COUPON_ID = 324L;

    final static private long INVEST_LIMIT = 300L;

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
            this.assignActivityChristmasInterestCoupon(message);
        }
    }

    private void assignActivityChristmasInterestCoupon(String message) {
        InvestSuccessMessage investSuccessMessage = null;
        try {
            investSuccessMessage = JsonConverter.readValue(message, InvestSuccessMessage.class);
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
        Date nowDate = DateTime.now().toDate();

        logger.info("[MQ] ready to consume activity message: assigning coupon.");
        if ((activityChristmasStartTime.before(nowDate) && activityChristmasEndTime.after(nowDate))
                && investSuccessMessage.getLoanDetailInfo().isActivity() && investSuccessMessage.getLoanDetailInfo().getActivityDesc().equals(LOAN_ACTIVITY_DESCRIPTION)
                && (!investSuccessMessage.getInvestInfo().getTransferStatus().equals("SUCCESS") && investSuccessMessage.getInvestInfo().getStatus().equals("SUCCESS"))
                && investSuccessMessage.getInvestInfo().getAmount() >= INVEST_LIMIT) {

            mqClient.sendMessage(MessageQueue.CouponAssigning, investSuccessMessage.getInvestInfo().getLoginName() + ":" + INTEREST_COUPON_OF_ZERO_5_PERCENT_COUPON_ID);
        }
    }
}
