package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.job.DelayMessageDeliveryJobCreator;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.message.LoanOutSuccessMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.InvestRepayMapper;
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
public class LoanOutSuccessCreateAnXinContractMessageConsumer implements MessageConsumer {
    private static Logger logger = LoggerFactory.getLogger(LoanOutSuccessCreateAnXinContractMessageConsumer.class);

    @Autowired
    private AnxinWrapperClient anxinWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    @Autowired
    private JobManager jobManager;

    @Override
    public MessageQueue queue() {
        return MessageQueue.LoanOutSuccess_GenerateAnXinContract;
    }

    @Override
    public void consume(String message) {
        logger.info("[标的放款MQ] LoanOutSuccess_GenerateAnXinContract receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[标的放款MQ] LoanOutSuccess_GenerateAnXinContract receive message is empty");
            return;
        }

        LoanOutSuccessMessage loanOutInfo;
        try {
            loanOutInfo = JsonConverter.readValue(message, LoanOutSuccessMessage.class);
            if (loanOutInfo.getLoanId() == null) {
                logger.error("[标的放款MQ] LoanOutSuccess_GenerateAnXinContract loanId is empty");
                return;
            }
        } catch (IOException e) {
            logger.error("[标的放款MQ] LoanOutSuccess_GenerateAnXinContract json convert LoanOutSuccessMessage is fail, message:{}", message);
            return;
        }

        //检查invest_repay数据生成
        long loanId = loanOutInfo.getLoanId();
        List<InvestModel> successInvests = investMapper.findSuccessInvestsByLoanId(loanId);
        for (InvestModel investModel : successInvests) {
            if (CollectionUtils.isEmpty(investRepayMapper.findByInvestId(investModel.getId()))) {
                try {
                    Thread.sleep(1000 * 60 * 2);
                    logger.info("[标的放款MQ] LoanOutSuccess_GenerateAnXinContract createLoanContracts sleep 2 minute.");
                } catch (InterruptedException e) {
                    logger.info("[标的放款MQ] LoanOutSuccess_GenerateAnXinContract createLoanContracts sleep 2 minute fail.");
                }
                mqWrapperClient.sendMessage(MessageQueue.LoanOutSuccess_GenerateAnXinContract, new LoanOutSuccessMessage(loanId));
                return;
            }
        }

        logger.info("[标的放款MQ] LoanOutSuccess_GenerateAnXinContract createLoanContracts is executing, loanId:{}", String.valueOf(loanId));
        BaseDto baseDto = anxinWrapperClient.createLoanContract(loanId);
        if (!baseDto.isSuccess()) {
            logger.error(MessageFormat.format("[标的放款MQ] LoanOutSuccess_GenerateAnXinContract is fail. loanId:{0}", String.valueOf(loanId)));
            mqWrapperClient.sendMessage(MessageQueue.SmsFatalNotify, "生成安心签失败");
            return;
        }
        DelayMessageDeliveryJobCreator.createAnxinContractQueryDelayJob(jobManager, loanId, AnxinContractType.LOAN_CONTRACT.name());

        logger.info("[标的放款MQ] LoanOutSuccess_GenerateAnXinContract consume LoanOutSuccess_GenerateAnXinContract success.");
        return;
    }
}
