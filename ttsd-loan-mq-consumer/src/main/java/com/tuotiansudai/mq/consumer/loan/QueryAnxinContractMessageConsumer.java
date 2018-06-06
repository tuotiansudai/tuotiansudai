package com.tuotiansudai.mq.consumer.loan;

import com.google.common.collect.Lists;
import com.tuotiansudai.client.AnxinWrapperClient;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.AnxinDataDto;
import com.tuotiansudai.dto.AnxinQueryContractDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.SmsNotifyDto;
import com.tuotiansudai.enums.JianZhouSmsTemplate;
import com.tuotiansudai.job.DelayMessageDeliveryJobCreator;
import com.tuotiansudai.job.JobManager;
import com.tuotiansudai.message.AnxinContractMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.TransferApplicationMapper;
import com.tuotiansudai.repository.model.AnxinContractType;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.TransferApplicationModel;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.JsonConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import static com.tuotiansudai.constants.AnxinContractCreateRedisKey.LOAN_CONTRACT_IN_CREATING_KEY;
import static com.tuotiansudai.constants.AnxinContractCreateRedisKey.TRANSFER_CONTRACT_IN_CREATING_KEY;


@Component
public class QueryAnxinContractMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(QueryAnxinContractMessageConsumer.class);

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Autowired
    private AnxinWrapperClient anxinWrapperClient;

    @Autowired
    private InvestService investService;

    @Autowired
    private JobManager jobManager;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    @Value("#{'${anxin.contract.notify.mobileList}'.split('\\|')}")
    private List<String> mobileList;

    @Autowired
    private TransferApplicationMapper transferApplicationMapper;

    private final static int SEVEN_DAYS = 60 * 60 * 24 * 7; // 7天

    public final static String ANXIN_CONTRACT_QUERY_TRY_TIMES_KEY = "anxinContractQueryTryTimes:";


    @Override
    public MessageQueue queue() {
        return MessageQueue.QueryAnxinContract;
    }

    @Override
    public void consume(String message) {
        AnxinContractMessage messageBody;
        try {
            messageBody = JsonConverter.readValue(message, AnxinContractMessage.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        long businessId = messageBody.getBusinessId();
        AnxinContractType anxinContractType = AnxinContractType.valueOf(messageBody.getAnxinContractType());

        logger.info(MessageFormat.format("trigger anxin contract handle job, prepare do job. businessId:{0}, Counter:{1}",
                String.valueOf(businessId), redisWrapperClient.get(ANXIN_CONTRACT_QUERY_TRY_TIMES_KEY + businessId)));

        if (redisWrapperClient.incrEx(ANXIN_CONTRACT_QUERY_TRY_TIMES_KEY + businessId, SEVEN_DAYS) > 2) {

            // 尝试超过2次（第3次了），清空计数器，不再尝试了
            redisWrapperClient.del(ANXIN_CONTRACT_QUERY_TRY_TIMES_KEY + businessId);

            // 发短信报警
            sendSms(String.valueOf(businessId));

            if (anxinContractType == AnxinContractType.LOAN_CONTRACT) {
                // 清redis中的inCreating标记
                redisWrapperClient.del(LOAN_CONTRACT_IN_CREATING_KEY + businessId);
            } else if (anxinContractType == AnxinContractType.TRANSFER_CONTRACT) {
                // 清redis中的inCreating标记
                redisWrapperClient.del(TRANSFER_CONTRACT_IN_CREATING_KEY + businessId);
            }
            return;
        }

        BaseDto<AnxinDataDto> result = anxinWrapperClient.queryContract(new AnxinQueryContractDto(businessId, anxinContractType));
        logger.info(MessageFormat.format("trigger anxin contract handle job, loanId:{0}, anxin contract type:{1}", String.valueOf(businessId), anxinContractType.name()));

        if (result == null || !result.isSuccess()) {
            logger.info(MessageFormat.format("query anxin contract failed. businessId:{0}, anxin ContractType:{1}, times:{2}", String.valueOf(businessId), anxinContractType),
                    redisWrapperClient.get(ANXIN_CONTRACT_QUERY_TRY_TIMES_KEY + businessId));
            DelayMessageDeliveryJobCreator.createAnxinContractQueryDelayJob(jobManager, businessId, anxinContractType.name());
        } else {
            // 查询结束，清空计数器
            redisWrapperClient.del(ANXIN_CONTRACT_QUERY_TRY_TIMES_KEY + businessId);

            logger.info(MessageFormat.format("execute query contract over. businessId:{0}", String.valueOf(businessId)));

            // 没有待处理的 batchNo 了，检查该 businessId 下的投资是否已经全部成功
            if (anxinContractType == AnxinContractType.LOAN_CONTRACT) {
                List<InvestModel> contractFailList = investService.findContractFailInvest(businessId);
                if (CollectionUtils.isNotEmpty(contractFailList)) {
                    logger.error(MessageFormat.format("some batch is fail. send sms. businessId:{0}, type:{1}", String.valueOf(businessId), anxinContractType));
                    // 有失败的，发短信
                    sendSms(String.valueOf(businessId));
                }
                // 清redis中的inCreating标记
                redisWrapperClient.del(LOAN_CONTRACT_IN_CREATING_KEY + businessId);
            } else if (anxinContractType == AnxinContractType.TRANSFER_CONTRACT) {
                TransferApplicationModel applicationModel = transferApplicationMapper.findById(businessId);
                InvestModel investModel = investService.findById(applicationModel.getInvestId());
                if (investModel != null && StringUtils.isEmpty(investModel.getContractNo())) {
                    logger.error(MessageFormat.format("some batch is fail. send sms. businessId:{0}, type:{1}", String.valueOf(businessId), anxinContractType));
                    // 失败了，发短信
                    sendSms(String.valueOf(businessId));
                }
                // 清redis中的inCreating标记
                redisWrapperClient.del(TRANSFER_CONTRACT_IN_CREATING_KEY + businessId);
            }
        }
    }

    private void sendSms(String param){
        mqWrapperClient.sendMessage(MessageQueue.SmsNotify, new SmsNotifyDto(JianZhouSmsTemplate.SMS_GENERATE_CONTRACT_ERROR_NOTIFY_TEMPLATE, mobileList, Lists.newArrayList(param)));
    }
}