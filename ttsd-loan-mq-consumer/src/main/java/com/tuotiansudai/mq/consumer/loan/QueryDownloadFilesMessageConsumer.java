package com.tuotiansudai.mq.consumer.loan;


import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.fudian.download.*;
import com.tuotiansudai.fudian.message.BankQueryDownloadFilesMessage;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.consumer.MessageConsumer;
import com.tuotiansudai.repository.mapper.ReconciliationMapper;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanStatus;
import com.tuotiansudai.repository.model.ReconciliationModel;
import com.tuotiansudai.repository.model.RepayStatus;
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
    private ReconciliationMapper reconciliationMapper;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    private static final String EMAIL_CONTENT_MESSAGE = "EMAIL_CONTENT_MESSAGE:{0}";

    private static final String EMAIL_CONTENT_SIZE = "EMAIL_CONTENT_SIZE:{0}:{1}";

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
                    .put(QueryDownloadLogFilesType.RECHARGE, recharge)
                    .put(QueryDownloadLogFilesType.WITHDRAW, withdraw)
                    .put(QueryDownloadLogFilesType.LOAN_INVEST, loanInvest)
                    .put(QueryDownloadLogFilesType.LOAN_CREDIT_INVEST, loanCreditInvest)
                    .put(QueryDownloadLogFilesType.LOAN_FULL, loanFull)
                    .put(QueryDownloadLogFilesType.LOAN_REPAY, loanRepay)
                    .put(QueryDownloadLogFilesType.LOAN_CALLBACK, loanCallBack)
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
        Map<String, ReconciliationModel> queryMap = dtos.stream().collect(Collectors.toMap(RechargeDownloadDto::getOrderNo, dto -> {
            ReconciliationModel model = new ReconciliationModel(dto.getOrderNo(), AmountConverter.convertStringToCent(dto.getReceivedAmount()));
            if ("0".equals(dto.getStatus())) {
                model.setStatus(BankRechargeStatus.WAIT_PAY.name());
            }
            if ("1".equals(dto.getStatus())) {
                model.setStatus(BankRechargeStatus.SUCCESS.name());
                model.setBillCount(1);
            }
            if (Lists.newArrayList("2", "3").contains(dto.getStatus())) {
                model.setStatus(BankRechargeStatus.FAIL.name());
            }
            return model;
        }));

        Map<String, ReconciliationModel> modelMap = reconciliationMapper.recharge(message.getQueryDate());
        generateContentBody(message.getType(), message.getQueryDate(), queryMap, modelMap);
    };

    private QueryDownloadFilesMessageNotifyAction<BankQueryDownloadFilesMessage> withdraw = (message) -> {
        List<WithdrawDownloadDto> dtos = gson.fromJson(message.getData().toString(), new TypeToken<List<WithdrawDownloadDto>>() {
        }.getType());
        Map<String, ReconciliationModel> queryMap = dtos.stream().collect(Collectors.toMap(WithdrawDownloadDto::getOrderNo, dto -> {
            ReconciliationModel model = new ReconciliationModel(dto.getOrderNo(), AmountConverter.convertStringToCent(dto.getReceivedAmount()));
            if ("0".equals(dto.getStatus())) {
                model.setStatus(WithdrawStatus.WAIT_PAY.name());
            }
            if ("1".equals(dto.getStatus())) {
                model.setStatus(WithdrawStatus.SUCCESS.name());
                model.setBillCount(1);
            }
            if (Lists.newArrayList("2", "3", "4").contains(dto.getStatus())) {
                model.setStatus(WithdrawStatus.FAIL.name());
            }
            return model;
        }));

        Map<String, ReconciliationModel> modelMap = reconciliationMapper.withdraw(message.getQueryDate());
        generateContentBody(message.getType(), message.getQueryDate(), queryMap, modelMap);
    };

    private QueryDownloadFilesMessageNotifyAction<BankQueryDownloadFilesMessage> loanInvest = (message) -> {
        List<LoanInvestDownloadDto> dtos = gson.fromJson(message.getData().toString(), new TypeToken<List<LoanInvestDownloadDto>>() {
        }.getType());
        Map<String, ReconciliationModel> queryMap = dtos.stream().filter(dto -> "1".equals(dto.getInvestType())).collect(Collectors.toMap(LoanInvestDownloadDto::getOrderNo, dto -> {
            ReconciliationModel model = new ReconciliationModel(dto.getOrderNo(), AmountConverter.convertStringToCent(dto.getAmount()));
            if ("0".equals(dto.getStatus())) {
                model.setStatus(InvestStatus.WAIT_PAY.name());
            }
            if (Lists.newArrayList("1", "2", "3", "4").contains(dto.getStatus())) {
                model.setStatus(InvestStatus.SUCCESS.name());
                model.setBillCount(1);
            }
            if ("5".equals(dto.getStatus())) {
                model.setStatus(InvestStatus.CANCEL_INVEST_PAYBACK.name());
                model.setBillCount(2);
            }
            return model;
        }));

        Map<String, ReconciliationModel> modelMap = reconciliationMapper.invest(message.getQueryDate());
        generateContentBody(message.getType(), message.getQueryDate(), queryMap, modelMap);
    };

    private QueryDownloadFilesMessageNotifyAction<BankQueryDownloadFilesMessage> loanCreditInvest = (message) -> {
        List<LoanCreditInvestDownloadDto> dtos = gson.fromJson(message.getData().toString(), new TypeToken<List<LoanCreditInvestDownloadDto>>() {
        }.getType());
        Map<String, ReconciliationModel> queryMap = dtos.stream().collect(Collectors.toMap(LoanCreditInvestDownloadDto::getOrderNo, dto -> {
            ReconciliationModel model = new ReconciliationModel(dto.getOrderNo(), AmountConverter.convertStringToCent(dto.getAmount()));
            if ("0".equals(dto.getStatus())) {
                model.setStatus(InvestStatus.WAIT_PAY.name());
            }
            if (("1").equals(dto.getStatus())) {
                model.setStatus(InvestStatus.SUCCESS.name());
                model.setBillCount(1);
            }
            if ("2".equals(dto.getStatus())) {
                model.setStatus(InvestStatus.FAIL.name());
            }
            return model;
        }));

        Map<String, ReconciliationModel> modelMap = reconciliationMapper.transferInvest(message.getQueryDate());
        generateContentBody(message.getType(), message.getQueryDate(), queryMap, modelMap);
    };

    private QueryDownloadFilesMessageNotifyAction<BankQueryDownloadFilesMessage> loanFull = (message) -> {
        List<LoanFullDownloadDto> dtos = gson.fromJson(message.getData().toString(), new TypeToken<List<LoanFullDownloadDto>>() {
        }.getType());
        Map<String, ReconciliationModel> queryMap = dtos.stream().filter(dto -> Lists.newArrayList("2", "3").contains(dto.getStatus())).collect(Collectors.toMap(LoanFullDownloadDto::getOrderNo, dto -> {
            ReconciliationModel model = new ReconciliationModel(dto.getOrderNo(), AmountConverter.convertStringToCent(dto.getAmount()));
            model.setStatus(LoanStatus.REPAYING.name());
            return model;
        }));

        Map<String, ReconciliationModel> modelMap = reconciliationMapper.loanFull(message.getQueryDate());
        generateContentBody(message.getType(), message.getQueryDate(), queryMap, modelMap);
    };

    private QueryDownloadFilesMessageNotifyAction<BankQueryDownloadFilesMessage> loanRepay = (message) -> {
        List<LoanRepayDownloadDto> dtos = gson.fromJson(message.getData().toString(), new TypeToken<List<LoanRepayDownloadDto>>() {
        }.getType());
        Map<String, ReconciliationModel> queryMap = dtos.stream().collect(Collectors.toMap(LoanRepayDownloadDto::getOrderNo, dto -> {
            ReconciliationModel model = new ReconciliationModel(dto.getOrderNo(), (AmountConverter.convertStringToCent(dto.getCapital()) + AmountConverter.convertStringToCent(dto.getInterest())));
            if ("1".equals(dto.getStatus())){
                model.setStatus(RepayStatus.COMPLETE.name());
                model.setBillCount(1);
            }else{
                model.setStatus(RepayStatus.WAIT_PAY.name());
            }
            return model;
        }));

        Map<String, ReconciliationModel> modelMap = reconciliationMapper.loanRepay(message.getQueryDate());
        generateContentBody(message.getType(), message.getQueryDate(), queryMap, modelMap);
    };

    private QueryDownloadFilesMessageNotifyAction<BankQueryDownloadFilesMessage> loanCallBack = (message) -> {
        List<LoanCallBackDownloadDto> dtos = gson.fromJson(message.getData().toString(), new TypeToken<List<LoanCallBackDownloadDto>>() {
        }.getType());
        Map<String, ReconciliationModel> queryMap = dtos.stream().collect(Collectors.toMap(LoanCallBackDownloadDto::getOrderNo, dto -> {
            ReconciliationModel model = new ReconciliationModel(dto.getOrderNo(), (AmountConverter.convertStringToCent(dto.getCapital()) + AmountConverter.convertStringToCent(dto.getInterest()) + AmountConverter.convertStringToCent(dto.getInterestFee())));
            model.setStatus(RepayStatus.COMPLETE.name());
            model.setBillCount(1);
            return model;
        }));

        Map<String, ReconciliationModel> modelMap = reconciliationMapper.loanCallBack(message.getQueryDate());
        generateContentBody(message.getType(), message.getQueryDate(), queryMap, modelMap);
    };

    private void generateContentBody(QueryDownloadLogFilesType type, String queryDate, Map<String, ReconciliationModel> queryMap, Map<String, ReconciliationModel> modelMap) {
        StringBuilder contentBody = new StringBuilder();
        for (Map.Entry<String, ReconciliationModel> entry : queryMap.entrySet()) {
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("orderNo", entry.getKey());
            ReconciliationModel queryValue = entry.getValue();
            ReconciliationModel modelValue = modelMap.get(entry.getKey());
            if (modelValue == null) {
                resultMap.put("result", "订单不存在");
            } else if (queryValue.getAmount() != modelValue.getAmount() || !queryValue.getStatus().equals(modelValue.getStatus()) || queryValue.getBillCount() != modelValue.getBillCount()) {
                resultMap.put("result", "订单异常");
            } else {
                resultMap.put("result", "交易成功");
            }
            contentBody.append(SendCloudTemplate.FUDIAN_CHECK_RESULT_BODY.generateContent(resultMap));
            redisWrapperClient.incr(MessageFormat.format(EMAIL_CONTENT_SIZE, queryDate, type.name()));
        }

        String content = (redisWrapperClient.hexists(MessageFormat.format(EMAIL_CONTENT_MESSAGE, queryDate), type.name()) ? redisWrapperClient.hget(MessageFormat.format(EMAIL_CONTENT_MESSAGE, queryDate), type.name()) : "") + contentBody.toString();
        redisWrapperClient.hset(MessageFormat.format(EMAIL_CONTENT_MESSAGE, queryDate), type.name(), Strings.isNullOrEmpty(content) ? "<tr><td colspan='2'>无交易记录</td></tr>" : content, 12 * 60 * 60);
    }
}

@FunctionalInterface
interface QueryDownloadFilesMessageNotifyAction<T> {
    void messageNotify(T t);
}
