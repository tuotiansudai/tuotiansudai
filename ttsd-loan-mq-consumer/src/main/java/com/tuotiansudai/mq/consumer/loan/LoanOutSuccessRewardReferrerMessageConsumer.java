package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Component
public class LoanOutSuccessRewardReferrerMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessRewardReferrerMessageConsumer.class);

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private PayWrapperClient payWrapperClient;

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
        logger.info("[标的放款MQ] LoanOutSuccess_SmsMessage receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            LoanOutSuccessMessage loanOutInfo;
            try {
                loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            } catch (IOException e) {
                logger.error("[标的放款MQ] LoanOutSuccess_RewardReferrer json convert LoanOutSuccessMessage is fail, message:{}", message);
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("发放推荐人奖励失败"));
                throw new RuntimeException(e);
            }

            long loanId = loanOutInfo.getLoanId();
            List<String> fatalSmsList = Lists.newArrayList();

            logger.info("[标的放款MQ] LoanOutSuccess_RewardReferrer is execute，loanId:" + loanId);
            boolean result = true;
            try {
                result = payWrapperClient.sendRewardReferrer(loanId).isSuccess();
            } catch (Exception e) {
                logger.error(MessageFormat.format("[标的放款MQ] LoanOutSuccess_RewardReferrer ConnectTimeoutException, message:{0}", e));
            }
            if (!result) {
                fatalSmsList.add("发放推荐人奖励失败");
                logger.error(MessageFormat.format("[标的放款MQ] LoanOutSuccess_RewardReferrer is fail (loanId = {0})", String.valueOf(loanId)));
            }

            if (CollectionUtils.isNotEmpty(fatalSmsList)) {
                fatalSmsList.add(MessageFormat.format("标的ID:{0}", loanId));
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(Joiner.on(",").join(fatalSmsList)));
                logger.error(MessageFormat.format("[标的放款MQ] LoanOutSuccess_RewardReferrer is fail, sms sending. loanId:{0}, queue:{1}", String.valueOf(loanId), MessageQueue.LoanOutSuccess_RewardReferrer));
                throw new RuntimeException("[标的放款MQ] LoanOutSuccess_RewardReferrer is fail. loanOutInfo: " + message);
            }

            logger.info("[标的放款MQ] LoanOutSuccess_RewardReferrer consume success.");
        }
    }
}
