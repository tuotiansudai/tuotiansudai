package com.tuotiansudai.mq.consumer.loan;


import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.dto.Environment;
import com.tuotiansudai.fudian.download.QueryDownloadLogFilesType;
import com.tuotiansudai.fudian.download.RechargeDownloadDto;
import com.tuotiansudai.fudian.download.WithdrawDownloadDto;
import com.tuotiansudai.fudian.message.BankQueryDownloadFilesMessage;
import com.tuotiansudai.message.EMailMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.BankRechargeMapper;
import com.tuotiansudai.repository.mapper.BankWithdrawMapper;
import com.tuotiansudai.repository.model.BankRechargeModel;
import com.tuotiansudai.repository.model.BankWithdrawModel;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.SendCloudTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class QueryDownloadFilesMessageConsumer implements MessageConsumer{

    private static Logger logger = LoggerFactory.getLogger(QueryDownloadFilesMessageConsumer.class);

    private final Gson gson  = new GsonBuilder().create();

    private final BankRechargeMapper bankRechargeMapper;

    private final BankWithdrawMapper bankWithdrawMapper;

    private final MQWrapperClient mqWrapperClient;

    @Value("${common.environment}")
    private Environment environment;

    @Autowired
    public QueryDownloadFilesMessageConsumer(BankRechargeMapper bankRechargeMapper, BankWithdrawMapper bankWithdrawMapper, MQWrapperClient mqWrapperClient){
        this.bankRechargeMapper = bankRechargeMapper;
        this.bankWithdrawMapper = bankWithdrawMapper;
        this.mqWrapperClient = mqWrapperClient;
    }

    @Override
    public MessageQueue queue() {
        return MessageQueue.QueryDownloadFiles;
    }

    @Override
    public void consume(String message) {
        if (Strings.isNullOrEmpty(message)) {
            logger.warn("[MQ] message is empty");
            return;
        }

        try {
            BankQueryDownloadFilesMessage bankQueryDownloadFilesMessage = gson.fromJson(message, BankQueryDownloadFilesMessage.class);
            if (!bankQueryDownloadFilesMessage.isStatus()) {
                return;
            }

            String content = recharge(bankQueryDownloadFilesMessage);

            mqWrapperClient.sendMessage(MessageQueue.EMailMessage, new EMailMessage(Maps.newHashMap(ImmutableMap.<Environment, List<String>>builder()
                    .put(Environment.PRODUCTION, Lists.newArrayList("dev@tuotiansudai.com"))
                    .put(Environment.QA, Lists.newArrayList("zhujiajun@tuotiansudai.com"))
                    .put(Environment.DEV, Lists.newArrayList("zhukun@tuotiansudai.com"))
                    .build()).get(environment), MessageFormat.format("{0}-富滇银行对账", bankQueryDownloadFilesMessage.getQueryDate()), content));


        } catch (JsonSyntaxException e) {
            logger.error("[MQ] message is invalid: {}", message);
        }

        logger.info("[MQ] consume message success.");
    }

    private String recharge(BankQueryDownloadFilesMessage<RechargeDownloadDto> bankQueryDownloadFilesMessage){
        List<RechargeDownloadDto> dtos = gson.fromJson(bankQueryDownloadFilesMessage.getData().toString(), new TypeToken<List<RechargeDownloadDto>>(){}.getType());
        List<RechargeDownloadDto> queryList = dtos.stream().filter(dto->dto.getStatus().equals("1")).collect(Collectors.toList());
        List<BankRechargeModel> modelList = bankRechargeMapper.findSuccessByDate(bankQueryDownloadFilesMessage.getQueryDate());
        Map<String, Long> modelMap = modelList.stream().collect(Collectors.toMap(BankRechargeModel::getBankOrderNo, BankRechargeModel::getAmount));
        StringBuilder contentBody = new StringBuilder();
        queryList.forEach(dto->{
            Long value = modelMap.get(dto.getOrderNo());
            if (value == null){
                contentBody.append(SendCloudTemplate.FUDIAN_CHECK_RESULT_BODY.generateContent(addIssue(dto.getOrderNo(), "订单不存在")));
            }else if (value != AmountConverter.convertStringToCent(dto.getReceivedAmount())){
                contentBody.append(SendCloudTemplate.FUDIAN_CHECK_RESULT_BODY.generateContent(addIssue(dto.getOrderNo(), "金额不一致")));
            }
        });
        if (Strings.isNullOrEmpty(contentBody.toString())){
            return null;
        }
        String contentHeader = generateContentHeader(QueryDownloadLogFilesType.recharge);
        return contentHeader + contentBody.toString() + SendCloudTemplate.FUDIAN_CHECK_RESULT_TAIL.getTemplate();
    }

    private String withdraw(BankQueryDownloadFilesMessage<WithdrawDownloadDto> bankQueryDownloadFilesMessage){
        List<WithdrawDownloadDto> dtos = gson.fromJson(bankQueryDownloadFilesMessage.getData().toString(), new TypeToken<List<WithdrawDownloadDto>>(){}.getType());
        List<WithdrawDownloadDto> queryList = dtos.stream().filter(dto->dto.getStatus().equals("1")).collect(Collectors.toList());
        List<BankWithdrawModel> modelList = bankWithdrawMapper.findSuccessByDate(bankQueryDownloadFilesMessage.getQueryDate());
        Map<String, Long> modelMap = modelList.stream().collect(Collectors.toMap(BankWithdrawModel::getBankOrderNo, BankWithdrawModel::getAmount));
        StringBuilder contentBody = new StringBuilder();
        queryList.forEach(dto->{
            Long value = modelMap.get(dto.getOrderNo());
            if (value == null){
                contentBody.append(SendCloudTemplate.FUDIAN_CHECK_RESULT_BODY.generateContent(addIssue(dto.getOrderNo(), "订单不存在")));
            }else if (value != AmountConverter.convertStringToCent(dto.getReceivedAmount())){
                contentBody.append(SendCloudTemplate.FUDIAN_CHECK_RESULT_BODY.generateContent(addIssue(dto.getOrderNo(), "金额不一致")));
            }
        });
        if (Strings.isNullOrEmpty(contentBody.toString())){
            return null;
        }
        String contentHeader = generateContentHeader(QueryDownloadLogFilesType.withDraw);
        return contentHeader + contentBody.toString() + SendCloudTemplate.FUDIAN_CHECK_RESULT_TAIL.getTemplate();
    }

    private String generateContentHeader(QueryDownloadLogFilesType type){
        return SendCloudTemplate.FUDIAN_CHECK_RESULT_HEADER.generateContent(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("title", type.getDescribe())
                .build()));
    }

    private HashMap<String, String> addIssue(String orderNo, String issue) {
        return Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("orderId", orderNo)
                .put("issue", issue)
                .build());
    }
}
