package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Component
public class LoanOutSuccessRewardReferrerMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessRewardReferrerMessageConsumer.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("#{'${pay.user.reward}'.split('\\|')}")
    private List<Double> referrerUserRoleReward;

    @Value("#{'${pay.staff.reward}'.split('\\|')}")
    private List<Double> referrerStaffRoleReward;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_RewardReferrer;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] LoanOutSuccess_RewardReferrer receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] LoanOutSuccess_RewardReferrer receive message is empty");
            return;
        }

        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[标的放款MQ] LoanOutSuccess_RewardReferrer loanId is empty");
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] LoanOutSuccess_RewardReferrer json convert LoanOutSuccessMessage is fail, message:{}", message);
            return;
        }

        long loanId = loanOutInfo.getLoanId();

        logger.info("[标的放款MQ] LoanOutSuccess_RewardReferrer is executing，loanId:" + loanId);
        boolean result;
        try {
            result = payWrapperClient.sendRewardReferrer(loanId).isSuccess();
        } catch (Exception e) {
            logger.error(MessageFormat.format("[标的放款MQ] LoanOutSuccess_RewardReferrer sendRewardReferrer is fail,loanId({0}), error:{0}", String.valueOf(loanId), e));
            mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, "放款发放推荐人奖励失败, 业务处理异常");
            return;
        }

        if (!result) {
            mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, MessageFormat.format("发放推荐人奖励失败,标的ID:{0}", String.valueOf(loanId)));
            logger.error(MessageFormat.format("[标的放款MQ] LoanOutSuccess_RewardReferrer is fail (loanId = {0})", String.valueOf(loanId)));
            throw new RuntimeException("[标的放款MQ] LoanOutSuccess_RewardReferrer is fail. loanOutInfo: " + message);
        }

        logger.info("[标的放款MQ] LoanOutSuccess_RewardReferrer consume success.");
    }
}
