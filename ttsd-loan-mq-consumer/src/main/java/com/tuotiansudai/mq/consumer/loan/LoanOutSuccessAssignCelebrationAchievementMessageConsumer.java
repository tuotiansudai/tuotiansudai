package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.model.InvestAchievementView;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LoanOutSuccessAssignCelebrationAchievementMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessAssignCelebrationAchievementMessageConsumer.class);

    @Autowired
    private SmsWrapperClient smsWrapperClient;
    @Autowired
    private InvestMapper investMapper;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.celebration.achievement.startTime}\")}")
    private Date startTime;

    @Value(value = "#{new java.text.SimpleDateFormat(\"yyyy-MM-dd HH:mm:ss\").parse(\"${activity.celebration.achievement.endTime}\")}")
    private Date endTime;

    @Autowired
    private MQWrapperClient mqClient;

    private static final Long RED_ENVELOPE_50_COUPON_ID = 439l;

    private static final Long RED_ENVELOPE_20_COUPON_ID = 440l;

    private static final String THE_SECOND_PRIZE = "CELEBRATION_SECOND_ACHIEVEMENT";

    private static final String THE_THIRD_PRIZE = "CELEBRATION_THIRD_ACHIEVEMENT";

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_AssignAchievementCelebration;
    }

    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] AssignAchievementCelebration receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] LoanOutSuccess_AssignAchievementCelebration receive message is empty");
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发送周年庆-标王奖励失败, MQ消息为空"));
            return;
        }

        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[标的放款MQ] LoanOutSuccess_AssignAchievementCelebration loanId is empty");
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发送周年庆-标王奖励失败, 消息中loanId为空"));
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] LoanOutSuccess_AssignAchievementCelebration json convert LoanOutSuccessMessage is fail, message:{}", message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发送周年庆-标王奖励失败, 解析消息失败"));
            return;
        }

        long loanId = loanOutInfo.getLoanId();

        try {
            logger.info(MessageFormat.format("[标的放款MQ] LoanOutSuccess_AssignAchievementCelebration assignInvestAchievementUserCoupon is executing , (loanId : {0}) ", String.valueOf(loanId)));

            List<InvestAchievementView> invests = investMapper.findAmountOrderByLoanId(loanId, startTime, endTime);

            if (invests.size() > 1) {
                if (!redisWrapperClient.hexists(THE_SECOND_PRIZE, String.valueOf(loanId))) {
                    logger.info(MessageFormat.format("send {0} the second red envelope ...", invests.get(1).getLoginName()));
                    mqClient.sendMessage(MessageQueue.CouponAssigning, invests.get(1).getLoginName() + ":" + RED_ENVELOPE_50_COUPON_ID);
                    redisWrapperClient.hset(THE_SECOND_PRIZE, String.valueOf(loanId), invests.get(1).getLoginName());
                } else {
                    logger.info(MessageFormat.format("{0} had send the second red envelope", invests.get(1).getLoginName()));
                }
            }
            if (invests.size() > 2) {
                if (!redisWrapperClient.hexists(THE_THIRD_PRIZE, String.valueOf(loanId))) {
                    logger.info(MessageFormat.format("send {0} the THIRD red envelope ...", invests.get(2).getLoginName()));
                    mqClient.sendMessage(MessageQueue.CouponAssigning, invests.get(2).getLoginName() + ":" + RED_ENVELOPE_20_COUPON_ID);
                    redisWrapperClient.hset(THE_THIRD_PRIZE, String.valueOf(loanId), invests.get(2).getLoginName());
                } else {
                    logger.info(MessageFormat.format("{0} had send the THIRD red envelope", invests.get(1).getLoginName()));
                }

            }
            logger.info("[[标的放款MQ] LoanOutSuccess_AssignAchievementCelebration consume success.");

        } catch (Exception e) {
            logger.error("[[标的放款MQ] LoanOutSuccess_AssignAchievementCelebration consume fail.", e.getLocalizedMessage());
            throw new RuntimeException(MessageFormat.format("[标的放款MQ] LoanOutSuccess_AssignAchievementCelebration  is fail. loanId:{0}", String.valueOf(loanId)));
        }
    }
}
