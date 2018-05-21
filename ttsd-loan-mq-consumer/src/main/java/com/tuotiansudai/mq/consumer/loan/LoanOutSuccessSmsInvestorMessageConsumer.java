package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.sms.LoanOutCompleteNotifyDto;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LoanOutSuccessSmsInvestorMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessSmsInvestorMessageConsumer.class);

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private InvestMapper investMapper;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_SmsInvestor;
    }

    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] LoanOutSuccess_SmsInvestor receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] LoanOutSuccess_SmsInvestor receive message is empty");
            return;
        }

        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[标的放款MQ] LoanOutSuccess_SmsInvestor loanId is empty");
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] LoanOutSuccess_SmsInvestor json convert LoanOutSuccessMessage is fail, message:{}", message);
            return;
        }

        LoanModel loanModel = loanMapper.findById(loanOutInfo.getLoanId());
        List<String> mobiles = investMapper.findMobileByLoanId(loanModel.getId());
        if (CollectionUtils.isNotEmpty(mobiles)){
            String rate = String.valueOf((loanModel.getBaseRate() + loanModel.getActivityRate()) * 100) + "%";
            smsWrapperClient.sendLoanOutCompleteNotify(new LoanOutCompleteNotifyDto(mobiles, loanModel.getName(), rate));
        }
    }
}
