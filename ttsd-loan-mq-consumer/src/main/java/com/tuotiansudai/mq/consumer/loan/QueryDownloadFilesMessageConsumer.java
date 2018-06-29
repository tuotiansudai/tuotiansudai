package com.tuotiansudai.mq.consumer.loan;


import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.fudian.download.*;
import com.tuotiansudai.fudian.message.BankQueryDownloadFilesMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.AmountConverter;
import com.tuotiansudai.util.RedisWrapperClient;
import com.tuotiansudai.util.SendCloudTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class QueryDownloadFilesMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(QueryDownloadFilesMessageConsumer.class);

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    private BankRechargeMapper bankRechargeMapper;

    @Autowired
    private BankWithdrawMapper bankWithdrawMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private LoanRepayMapper loanRepayMapper;

    @Autowired
    private InvestRepayMapper investRepayMapper;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String EMAIL_CONTENT_MESSAGE = "EMAIL_CONTENT_MESSAGE:{0}";

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

            Map<QueryDownloadLogFilesType, QueryDownloadFilesMessageNotifyAction> notify = Maps.newHashMap(ImmutableMap.<QueryDownloadLogFilesType, QueryDownloadFilesMessageNotifyAction>builder()
                    .put(QueryDownloadLogFilesType.recharge, recharge)
                    .put(QueryDownloadLogFilesType.withdraw, withdraw)
                    .put(QueryDownloadLogFilesType.invest, invest)
                    .put(QueryDownloadLogFilesType.creditInvest, creditInvest)
                    .put(QueryDownloadLogFilesType.loanFull, loanFull)
                    .put(QueryDownloadLogFilesType.repayment, repayment)
                    .put(QueryDownloadLogFilesType.loanBack, loanBack)
                    .build());

            notify.get(bankQueryDownloadFilesMessage.getType()).messageNotify(bankQueryDownloadFilesMessage);

        } catch (JsonSyntaxException e) {
            logger.error("[MQ] message is invalid: {}", message);
        }

        logger.info("[MQ] consume message success.");
    }

    private QueryDownloadFilesMessageNotifyAction<BankQueryDownloadFilesMessage> recharge = (message) -> {
        List<RechargeDownloadDto> dtos = gson.fromJson(message.getData().toString(), new TypeToken<List<RechargeDownloadDto>>() {
        }.getType());
        Map<String, String> queryMap = dtos.stream().filter(dto -> dto.getStatus().equals("1"))
                .collect(Collectors.toMap(RechargeDownloadDto::getOrderNo, RechargeDownloadDto::getReceivedAmount));
        Map<String, String> modelMap = bankRechargeMapper.findSuccessByDate(message.getQueryDate()).stream()
                .collect(Collectors.toMap(BankRechargeModel::getBankOrderNo, model -> AmountConverter.convertCentToString(model.getAmount())));
        generateContentBody(message.getType(), message.getQueryDate(), queryMap, modelMap);
    };

    private QueryDownloadFilesMessageNotifyAction<BankQueryDownloadFilesMessage> withdraw = (message) -> {
        List<WithdrawDownloadDto> dtos = gson.fromJson(message.getData().toString(), new TypeToken<List<WithdrawDownloadDto>>() {
        }.getType());
        Map<String, String> queryMap = dtos.stream().filter(dto -> dto.getStatus().equals("1"))
                .collect(Collectors.toMap(WithdrawDownloadDto::getOrderNo, WithdrawDownloadDto::getReceivedAmount));
        Map<String, String> modelMap = bankWithdrawMapper.findSuccessByDate(message.getQueryDate()).stream()
                .collect(Collectors.toMap(BankWithdrawModel::getBankOrderNo, model -> AmountConverter.convertCentToString(model.getAmount())));
        generateContentBody(message.getType(), message.getQueryDate(), queryMap, modelMap);
    };

    private QueryDownloadFilesMessageNotifyAction<BankQueryDownloadFilesMessage> invest = (message) -> {
        List<InvestDownloadDto> dtos = gson.fromJson(message.getData().toString(), new TypeToken<List<InvestDownloadDto>>() {
        }.getType());
        Map<String, String> queryMap = dtos.stream().filter(dto -> "1".equals(dto.getInvestType()) && !("0").equals(dto.getStatus()))
                .collect(Collectors.toMap(InvestDownloadDto::getOrderNo, dto -> "5".equals(dto.getStatus()) ? "WITHDRAWAL" : "SUCCESS"));
        Map<String, String> modelMap = investMapper.findSuccessByDate(message.getQueryDate()).stream().filter(model -> model.getTransferInvestId() == null)
                .collect(Collectors.toMap(InvestModel::getBankOrderNo, model -> model.getStatus().name()));
        generateContentBody(message.getType(), message.getQueryDate(), queryMap, modelMap);
    };

    private QueryDownloadFilesMessageNotifyAction<BankQueryDownloadFilesMessage> creditInvest = (message) -> {
        List<LoanCreditInvestDownloadDto> dtos = gson.fromJson(message.getData().toString(), new TypeToken<List<LoanCreditInvestDownloadDto>>() {
        }.getType());
        Map<String, String> queryMap = dtos.stream().filter(dto -> "1".equals(dto.getStatus()))
                .collect(Collectors.toMap(LoanCreditInvestDownloadDto::getOrderNo, LoanCreditInvestDownloadDto::getCreditAmount));
        Map<String, String> modelMap = investMapper.findSuccessByDate(message.getQueryDate()).stream().filter(model -> model.getTransferInvestId() != null)
                .collect(Collectors.toMap(InvestModel::getBankOrderNo, model -> AmountConverter.convertCentToString(model.getAmount())));
        generateContentBody(message.getType(), message.getQueryDate(), queryMap, modelMap);
    };

    private QueryDownloadFilesMessageNotifyAction<BankQueryDownloadFilesMessage> loanFull = (message) -> {
        List<LoanFullDownloadDto> dtos = gson.fromJson(message.getData().toString(), new TypeToken<List<LoanFullDownloadDto>>() {
        }.getType());
        Map<String, String> queryMap = dtos.stream().filter(dto -> Lists.newArrayList("2", "3").contains(dto.getStatus()))
                .collect(Collectors.toMap(LoanFullDownloadDto::getOrderNo, LoanFullDownloadDto::getAmount));
        Map<String, String> modelMap = loanMapper.findLoanFullSuccessByDate(message.getQueryDate()).stream()
                .collect(Collectors.toMap(LoanModel::getBankOrderNo, model -> AmountConverter.convertCentToString(model.getLoanAmount())));
        generateContentBody(message.getType(), message.getQueryDate(), queryMap, modelMap);
    };

    private QueryDownloadFilesMessageNotifyAction<BankQueryDownloadFilesMessage> repayment = (message) -> {
        List<LoanRepayDownloadDto> dtos = gson.fromJson(message.getData().toString(), new TypeToken<List<LoanRepayDownloadDto>>() {
        }.getType());
        Map<String, String> queryMap = dtos.stream().filter(dto -> "1".equals(dto.getStatus()))
                .collect(Collectors.toMap(LoanRepayDownloadDto::getOrderNo, dto -> AmountConverter.convertCentToString(AmountConverter.convertStringToCent(dto.getCapital()) + AmountConverter.convertStringToCent(dto.getInterest()))));
        Map<String, String> modelMap = loanRepayMapper.findCompleteByDate(message.getQueryDate()).stream()
                .collect(Collectors.toMap(LoanRepayModel::getBankOrderNo, model -> AmountConverter.convertCentToString(model.getRepayAmount())));
        generateContentBody(message.getType(), message.getQueryDate(), queryMap, modelMap);
    };

    private QueryDownloadFilesMessageNotifyAction<BankQueryDownloadFilesMessage> loanBack = (message) -> {
        List<LoanRepayInvestDownloadDto> dtos = gson.fromJson(message.getData().toString(), new TypeToken<List<LoanRepayInvestDownloadDto>>() {
        }.getType());
        Map<String, String> queryMap = dtos.stream()
                .collect(Collectors.toMap(LoanRepayInvestDownloadDto::getOrderNo, dto -> AmountConverter.convertCentToString(AmountConverter.convertStringToCent(dto.getCapital()) + AmountConverter.convertStringToCent(dto.getInterest()) + AmountConverter.convertStringToCent(dto.getInterestFee()))));
        Map<String, String> modelMap = investRepayMapper.findCompleteByDate(message.getQueryDate()).stream()
                .collect(Collectors.toMap(InvestRepayModel::getBankOrderNo, model -> AmountConverter.convertCentToString(model.getRepayAmount())));
        generateContentBody(message.getType(), message.getQueryDate(), queryMap, modelMap);
    };

    private void generateContentBody(QueryDownloadLogFilesType type, String queryDate, Map<String, String> queryMap, Map<String, String> modelMap) {
        StringBuilder contentBody = new StringBuilder();
        for (Map.Entry<String, String> entry : queryMap.entrySet()) {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("orderId", entry.getKey());
            String modelValue = modelMap.get(entry.getKey());
            if (Strings.isNullOrEmpty(modelValue)) {
                resultMap.put("result", "订单不存在");
            } else if (!entry.getValue().equalsIgnoreCase(modelValue)) {
                resultMap.put("result", "订单异常");
            } else {
                resultMap.put("result", "交易成功");
            }
            contentBody.append(SendCloudTemplate.FUDIAN_CHECK_RESULT_BODY.generateContent(resultMap));
        }

        String header = SendCloudTemplate.FUDIAN_CHECK_RESULT_HEADER.generateContent(Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("title", type.getDescribe())
                .put("count", String.valueOf(queryMap.size()))
                .build()));
        String content = header + (Strings.isNullOrEmpty(contentBody.toString()) ? "<tr><td colspan='2'>无交易记录</td></tr>" : contentBody.toString()) + SendCloudTemplate.FUDIAN_CHECK_RESULT_TAIL.getTemplate();
        redisWrapperClient.hset(MessageFormat.format(EMAIL_CONTENT_MESSAGE, queryDate), type.name(), content, 12 * 60 * 60);
    }
}

@FunctionalInterface
interface QueryDownloadFilesMessageNotifyAction<T> {
    void messageNotify(T t);
}
