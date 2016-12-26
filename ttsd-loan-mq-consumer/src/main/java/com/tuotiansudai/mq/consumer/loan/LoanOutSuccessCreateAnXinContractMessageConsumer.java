package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
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
public class LoanOutSuccessCreateAnXinContractMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessCreateAnXinContractMessageConsumer.class);

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_GenerateAnXinContract;
    }

    @Transactional
    @Override
    public void consume(String message) {
        logger.info("[MQ] receive message: {}: {}.", this.queue(), message);
        if (!StringUtils.isEmpty(message)) {
            LoanOutSuccessMessage loanOutInfo;
            try {
                loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            long loanId = loanOutInfo.getLoanId();
            List<String> fatalSmsList = Lists.newArrayList();

            logger.info("[MQ] ready to consume message: createLoanContracts is execute, loanId:{0}", loanId);
            BaseDto baseDto = payWrapperClient.createAnXinContract(loanId);
            if (!baseDto.isSuccess()) {
                fatalSmsList.add("生成安心签失败");
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess createLoanContracts is fail. loanId:{0}", String.valueOf(loanId)));
            }

            if (CollectionUtils.isNotEmpty(fatalSmsList)) {
                fatalSmsList.add(MessageFormat.format("标的ID:{0}", loanId));
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(Joiner.on(",").join(fatalSmsList)));
                logger.error(MessageFormat.format("[MQ] LoanOutSuccess_GenerateAnXinContract fail sms sending. loanId:{0}", String.valueOf(loanId)));
            }

            logger.info("[MQ] consume LoanOutSuccess_GenerateAnXinContract success.");
        }
    }
}
