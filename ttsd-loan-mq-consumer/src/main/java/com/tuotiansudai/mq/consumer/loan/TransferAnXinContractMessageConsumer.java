package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Strings;
import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.job.DelayMessageDeliveryJobCreator;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.message.AnxinContractMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.model.AnxinContractType;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

@Component
public class TransferAnXinContractMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(TransferAnXinContractMessageConsumer.class);

    @Autowired
    private SmsWrapperClient smsWrapperClient;

    @Value("#{'${anxin.contract.notify.mobileList}'.split('\\|')}")
    private List<String> mobileList;

    @Autowired
    private AnxinWrapperClient anxinWrapperClient;

    @Autowired
    private JobManager jobManager;


    @Override
    public MessageQueue queue() {
        return MessageQueue.TransferAnxinContract;
    }

    @Override
    public void consume(String message) {
        logger.info("[债权转让] TransferAnxinContract receive message: {}: {}.", this.queue(), message);
        if (Strings.isNullOrEmpty(message)) {
            logger.error("[债权转让] TransferAnxinContract receive message is empty");
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("生成安心签合同失败, MQ消息为空"));
            return;
        }

        AnxinContractMessage messageBody;
        try {
            messageBody = JsonConverter.readValue(message, AnxinContractMessage.class);
            if (messageBody.getBusinessId() == 0) {
                logger.error("[债权转让] TransferAnxinContract transferId is empty");
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("生成安心签合同失败, 消息中transferId为空"));
                return;
            }
        } catch (IOException e) {
            logger.error("[债权转让] TransferAnxinContract json convert transfer is fail, message:{}", message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("生成安心签失败, 解析消息失败"));
            return;
        }

        long transferId = messageBody.getBusinessId();

        logger.info("[债权转让] TransferAnxinContract createLoanContracts is executing, transferId:{}", String.valueOf(transferId));

        BaseDto baseDto = anxinWrapperClient.createTransferContract(transferId);
        if (!baseDto.isSuccess()) {
            logger.error(MessageFormat.format("[债权转让] LoanFull_GenerateAnXinContract is fail. transferId:{0}", String.valueOf(transferId)));
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("生成安心签失败"));
            return;
        }
        DelayMessageDeliveryJobCreator.createAnxinContractQueryDelayJob(jobManager, transferId, AnxinContractType.TRANSFER_CONTRACT.name());

        logger.info("[债权转让] TransferAnxinContract consume TransferAnxinContract success.");
    }
}