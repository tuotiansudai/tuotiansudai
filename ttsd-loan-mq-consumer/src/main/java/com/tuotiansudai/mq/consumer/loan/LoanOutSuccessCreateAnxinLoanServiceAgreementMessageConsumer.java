package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.AnxinDataDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.job.DelayMessageDeliveryJobCreator;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.AnxinContractType;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.util.JsonConverter;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;


@Component
public class LoanOutSuccessCreateAnxinLoanServiceAgreementMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessCreateAnxinLoanServiceAgreementMessageConsumer.class);

    @Autowired
    private AnxinWrapperClient anxinWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_GenerateLoanServiceAgreement;
    }

    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] LoanOutSuccess_GenerateLoanServiceAgreement receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] LoanOutSuccess_GenerateLoanServiceAgreement receive message is empty");
            return;
        }

        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[标的放款MQ] LoanOutSuccess_GenerateLoanServiceAgreement loanId is empty");
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] LoanOutSuccess_GenerateLoanServiceAgreement json convert LoanOutSuccessMessage is fail, message:{}", message);
            return;
        }

        long loanId = loanOutInfo.getLoanId();
        logger.info("[标的放款MQ] LoanOutSuccess_GenerateLoanServiceAgreement createLoanContracts is executing, loanId:{}", String.valueOf(loanId));
        BaseDto<AnxinDataDto> baseDto = anxinWrapperClient.createLoanServiceAgreement(loanId);
        if (!baseDto.isSuccess() || !baseDto.getData().getStatus()) {
            logger.error(MessageFormat.format("[标的放款MQ] LoanOutSuccess_GenerateLoanServiceAgreement is fail. loanId:{0}", String.valueOf(loanId)));
            mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, MessageFormat.format("生成借款:{0}信息咨询与服务协议合同失败", String.valueOf(loanId)));
            return;
        }
        logger.info("[标的放款MQ] LoanOutSuccess_GenerateLoanServiceAgreement consume LoanOutSuccess_GenerateLoanServiceAgreement success.");
    }
}
