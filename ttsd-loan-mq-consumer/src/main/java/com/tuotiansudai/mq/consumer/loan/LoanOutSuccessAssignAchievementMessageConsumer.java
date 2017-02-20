package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;

@Component
public class LoanOutSuccessAssignAchievementMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessAssignAchievementMessageConsumer.class);

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_AssignAchievement;
    }

    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] LoanOutSuccess_AssignAchievement receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] LoanOutSuccess_AssignAchievement receive message is empty");
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发送标王奖励失败, MQ消息为空"));
            return;
        }

        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[标的放款MQ] LoanOutSuccess_AssignAchievement loanId is empty");
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发送标王奖励失败, 消息中loanId为空"));
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] LoanOutSuccess_AssignAchievement json convert LoanOutSuccessMessage is fail, message:{}", message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发送标王奖励失败, 解析消息失败"));
            return;
        }

        long loanId = loanOutInfo.getLoanId();

        logger.info(MessageFormat.format("[标的放款MQ] LoanOutSuccess_AssignAchievement assignInvestAchievementUserCoupon is executing , (loanId : {0}) ", String.valueOf(loanId)));

        if (!payWrapperClient.assignInvestAchievementUserCoupon(loanId).isSuccess()) {
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(MessageFormat.format("标的ID:{0}, 发送标王奖励失败, 优惠券发放失败", String.valueOf(loanId))));
            logger.error(MessageFormat.format("[标的放款MQ] LoanOutSuccess_AssignAchievement assignInvestAchievementUserCoupon is fail. loanId:{0}", String.valueOf(loanId)));
            throw new RuntimeException(MessageFormat.format("[标的放款MQ] LoanOutSuccess_AssignAchievement assignInvestAchievementUserCoupon is fail. loanId:{0}", String.valueOf(loanId)));
        }

        logger.info("[[标的放款MQ] LoanOutSuccess_AssignAchievement consume success.");
    }
}
