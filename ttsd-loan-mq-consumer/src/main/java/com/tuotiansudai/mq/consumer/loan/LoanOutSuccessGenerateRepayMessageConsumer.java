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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Component
public class LoanOutSuccessGenerateRepayMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessGenerateRepayMessageConsumer.class);

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_GenerateRepay;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] LoanOutSuccess generate coupon receive message: {0}: {1}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            LoanOutSuccessMessage loanOutInfo;
            try {
                loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            long loanId = loanOutInfo.getLoanId();
            List<String> fatalSmsList = Lists.newArrayList();

            logger.info("[MQ] LoanOutSuccess ready to consume message: generate repay is execute, loanId:{0}", loanId);
            if (!payWrapperClient.generateRepay(loanId).isSuccess()) {
                fatalSmsList.add("生成标的回款计划失败");
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess generate repay is fail. loanId:{0}", String.valueOf(loanId)));
            }

            logger.info(MessageFormat.format("[MQ] LoanOutSuccess generate couponRepay is execute , (loanId : {0}) ", String.valueOf(loanId)));
            if (!payWrapperClient.generateCouponRepay(loanId).isSuccess()) {
                fatalSmsList.add("生成优惠券回款计划失败");
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess generate coupon payment fail, (loanId : {0})", String.valueOf(loanId)));
            }

            logger.info("[MQ] LoanOutSuccess ready to consume message: rateIncreases is execute, loanId:{0}", loanId);
            if (!payWrapperClient.generateExtraRate(loanId).isSuccess()) {
                fatalSmsList.add("生成阶梯加息错误");
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess rateIncreases is fail. loanId:{0}", String.valueOf(loanId)));
            }

            if (CollectionUtils.isNotEmpty(fatalSmsList)) {
                fatalSmsList.add(MessageFormat.format("标的ID:{0}", loanId));
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(Joiner.on(",").join(fatalSmsList)));
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess generate is fail, sms sending. loanId:{0}, queue:{1}", String.valueOf(loanId), MessageQueue.LoanOutSuccess_GenerateRepay));
                throw new RuntimeException("[MQ] LoanOutSuccess_GenerateRepay is fail. loanOutInfo: " + message);
            }

            logger.info("[MQ] LoanOutSuccess consume LoanOutSuccess_GenerateRepay success.");
        }
    }
}
