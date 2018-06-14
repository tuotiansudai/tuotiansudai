package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Component
public class LoanOutSuccessGenerateRepayMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessGenerateRepayMessageConsumer.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_GenerateRepay;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] LoanOutSuccess_GenerateRepay receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] LoanOutSuccess_GenerateRepay receive message is empty");
            return;
        }

        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[标的放款MQ] LoanOutSuccess_GenerateRepay loanId is empty");
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] LoanOutSuccess_GenerateRepay json convert LoanOutSuccessMessage is fail, message:{}", message);
            return;
        }

        long loanId = loanOutInfo.getLoanId();
        List<String> fatalSmsList = Lists.newArrayList();

        logger.info("[标的放款MQ] LoanOutSuccess_GenerateRepay ready to consume message: generate repay is executing, loanId:{0}", loanId);
        BaseDto<PayDataDto> baseDto = payWrapperClient.generateRepay(loanId);
        if (!baseDto.getData().getStatus()) {
            fatalSmsList.add(Strings.isNullOrEmpty(baseDto.getData().getMessage()) ? "生成标的回款计划失败" : baseDto.getData().getMessage());
            logger.error(MessageFormat.format("[标的放款MQ] LoanOutSuccess_GenerateRepay is fail. loanId:{0}", String.valueOf(loanId)));
        }

        logger.info(MessageFormat.format("[标的放款MQ] LoanOutSuccess_GenerateRepay generate coupon repay is executing , (loanId : {0}) ", String.valueOf(loanId)));
        if (!payWrapperClient.generateCouponRepay(loanId).isSuccess()) {
            fatalSmsList.add("生成优惠券回款计划失败");
            logger.error(MessageFormat.format("[标的放款MQ] LoanOutSuccess_GenerateRepay generate coupon repay is fail, (loanId : {0})", String.valueOf(loanId)));
        }

        logger.info("[标的放款MQ] LoanOutSuccess_GenerateRepay ready to consume message: rateIncreases is executing, loanId:{0}", loanId);
        if (!payWrapperClient.generateExtraRate(loanId).isSuccess()) {
            fatalSmsList.add("生成阶梯加息错误");
            logger.error(MessageFormat.format("[标的放款MQ] LoanOutSuccess_GenerateRepay rateIncreases is fail. loanId:{0}", String.valueOf(loanId)));
        }

        if (CollectionUtils.isNotEmpty(fatalSmsList)) {
            fatalSmsList.add(MessageFormat.format("标的ID:{0}", loanId));
            mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, Joiner.on(",").join(fatalSmsList));
            logger.error(MessageFormat.format("[标的放款MQ] LoanOutSuccess_GenerateRepay is fail, sms sending. loanId:{0}, queue:{1}", String.valueOf(loanId), MessageQueue.LoanOutSuccess_GenerateRepay));
            throw new RuntimeException("[标的放款MQ] LoanOutSuccess_GenerateRepay is fail. loanOutInfo: " + message);
        }

        logger.info("[标的放款MQ] LoanOutSuccess_GenerateRepay consume success.");
    }
}
