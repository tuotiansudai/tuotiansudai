package com.tuotiansudai.mq.consumer.loan;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.client.RedisWrapperClient;
import com.tuotiansudai.client.SmsWrapperClient;
import com.tuotiansudai.dto.AnxinQueryContractDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.sms.SmsFatalNotifyDto;
import com.tuotiansudai.message.AnxinContractMessage;
import com.tuotiansudai.message.LoanOutSuccessMessage;
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
    private RedisWrapperClient redisWrapperClient;

    @Autowired
    private AnxinWrapperClient anxinWrapperClient;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    private long DEFAULT_MINUTE = 1000 * 60 * 2;

    private static final int LOAN_ID_LIFT_TIME = 60 * 60 * 24; // bath_NO 在redis里保存1天

    private static final String LOAN_OUT_LOAN_ID_KEY = "transferLoanId:{0}";

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
                logger.error("[债权转让] TransferAnxinContract loanId is empty");
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("生成安心签合同失败, 消息中loanId为空"));
                return;
            }
        } catch (IOException e) {
            logger.error("[债权转让] TransferAnxinContract json convert transfer is fail, message:{}", message);
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("生成安心签失败, 解析消息失败"));
            return;
        }

        long loanId = messageBody.getBusinessId();
        logger.info("[债权转让] TransferAnxinContract ready to consume message , loanId:{}", loanId);

        String redisKey = MessageFormat.format(LOAN_OUT_LOAN_ID_KEY, String.valueOf(loanId));
        int executeCount = 1;
        if (redisWrapperClient.exists(redisKey)) {
            executeCount = Integer.parseInt(redisWrapperClient.get(redisKey));
            executeCount++;
        }

        try {
            Thread.sleep(DEFAULT_MINUTE);
            logger.info("[债权转让] TransferAnxinContract createTransferLoanContracts sleep 2 minute.");
        } catch (InterruptedException e) {
            logger.info("[债权转让] TransferAnxinContract createTransferLoanContracts sleep 2 minute fail.");
        }

        //避免invest_repay等数据未生成
        if (executeCount == 1) {
            logger.info("[债权转让] TransferAnxinContract createLoanContracts is executing, loanId:{}", String.valueOf(loanId));
            BaseDto baseDto = anxinWrapperClient.createTransferContract(loanId);
            if (!baseDto.isSuccess()) {
                logger.error(MessageFormat.format("[债权转让] LoanOutSuccess_GenerateAnXinContract is fail. loanId:{0}", String.valueOf(loanId)));
                smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto("生成安心签失败"));
                return;
            }
            redisWrapperClient.setex(redisKey, LOAN_ID_LIFT_TIME, String.valueOf(executeCount));
            mqWrapperClient.sendMessage(MessageQueue.TransferAnxinContract, new LoanOutSuccessMessage(loanId));
            return;
        }

        //等待安心签生成合同
        if (executeCount < 5) {
            logger.info("[债权转让] TransferAnxinContract executeCount:{}", executeCount);
            redisWrapperClient.setex(redisKey, LOAN_ID_LIFT_TIME, String.valueOf(executeCount));
            mqWrapperClient.sendMessage(MessageQueue.TransferAnxinContract, messageBody);
            return;
        }

        List<String> fatalSmsList = Lists.newArrayList();
        logger.info("[债权转让] TransferAnxinContract queryLoanContracts start .");
        BaseDto baseDto = anxinWrapperClient.queryContract(new AnxinQueryContractDto(loanId, AnxinContractType.TRANSFER_CONTRACT));
        if (baseDto == null || !baseDto.isSuccess()) {
            fatalSmsList.add("查询安心签失败");
            logger.error(MessageFormat.format("[债权转让] TransferAnxinContract queryLoanContracts is fail. loanId:{0}", String.valueOf(loanId)));
        }

        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(fatalSmsList)) {
            fatalSmsList.add(MessageFormat.format("标的ID:{0}", loanId));
            smsWrapperClient.sendFatalNotify(new SmsFatalNotifyDto(Joiner.on(",").join(fatalSmsList)));
            logger.error(MessageFormat.format("[债权转让] TransferAnxinContract is fail, sms sending. loanId:{0}, queue:{1}", String.valueOf(loanId), MessageQueue.TransferAnxinContract));
            throw new RuntimeException("[债权转让] TransferAnxinContract is fail. loanOutInfo: " + message);
        }

        logger.info("[债权转让] TransferAnxinContract consume TransferAnxinContract success.");
    }
}