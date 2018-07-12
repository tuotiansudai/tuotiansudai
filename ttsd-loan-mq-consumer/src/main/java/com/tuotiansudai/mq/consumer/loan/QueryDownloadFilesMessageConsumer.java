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
import com.tuotiansudai.enums.BankRechargeStatus;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.fudian.download.*;
import com.tuotiansudai.fudian.message.BankQueryDownloadFilesMessage;
import com.tuotiansudai.message.EMailMessage;
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
import org.joda.time.DateTime;
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
public class QueryDownloadFilesMessageConsumer implements MessageConsumer {

    private static Logger logger = LoggerFactory.getLogger(QueryDownloadFilesMessageConsumer.class);

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    private ReconciliationMapper reconciliationMapper;

    @Autowired
    private MQWrapperClient mqWrapperClient;

    private final RedisWrapperClient redisWrapperClient = RedisWrapperClient.getInstance();

    @Value("${common.environment}")
    private Environment environment;

    @Value("#{'${check.fudian.bill.notify.email}'.split('\\|')}")
    private List<String> emailAddress;

    private static final String EMAIL_CONTENT_MESSAGE = "EMAIL_CONTENT_MESSAGE:{0}";

    private static final String EMAIL_CONTENT_COUNT = "EMAIL_CONTENT_COUNT:{0}:{1}";

    private static final String EMAIL_CONTENT_TOTAL = "EMAIL_CONTENT_TOTAL:{0}:{1}";

    private static final String EMAIL_QUERY_IS_SEND = "EMAIL_QUERY_IS_SEND:{0}";

    private static final int lifeSecond = 24 * 60 * 60;

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
            BankQueryDownloadFilesMessage filesMessage = gson.fromJson(message, BankQueryDownloadFilesMessage.class);
            if (!filesMessage.isStatus()) {
                return;
            }

            String countKey = MessageFormat.format(EMAIL_CONTENT_COUNT, filesMessage.getQueryDate(), filesMessage.getType().name());
            int count = redisWrapperClient.exists(countKey) ? Integer.parseInt(redisWrapperClient.get(countKey)) : 0;
            redisWrapperClient.setex(countKey, lifeSecond, String.valueOf(count + filesMessage.getData().size()));

            redisWrapperClient.setex(MessageFormat.format(EMAIL_CONTENT_TOTAL, filesMessage.getQueryDate(), filesMessage.getType().name()), lifeSecond,
                    String.valueOf(filesMessage.getTotal()));

            Map<QueryDownloadLogFilesType, QueryDownloadFilesMessageNotifyAction> notify = Maps.newHashMap(ImmutableMap.<QueryDownloadLogFilesType, QueryDownloadFilesMessageNotifyAction>builder()
                    .put(QueryDownloadLogFilesType.RECHARGE, recharge)
                    .put(QueryDownloadLogFilesType.WITHDRAW, withdraw)
                    .put(QueryDownloadLogFilesType.LOAN_INVEST, loanInvest)
                    .put(QueryDownloadLogFilesType.LOAN_CREDIT_INVEST, loanCreditInvest)
                    .put(QueryDownloadLogFilesType.LOAN_FULL, loanFull)
                    .put(QueryDownloadLogFilesType.LOAN_REPAY, loanRepay)
                    .put(QueryDownloadLogFilesType.LOAN_CALLBACK, loanCallBack)
                    .build());

            notify.get(filesMessage.getType()).messageNotify(filesMessage);

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

    private void generateContentBody(QueryDownloadLogFilesType queryType, String queryDate, Map<String, ReconciliationModel> queryMap, Map<String, ReconciliationModel> modelMap) {
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
        }
        String content = (redisWrapperClient.hexists(MessageFormat.format(EMAIL_CONTENT_MESSAGE, queryDate), queryType.name()) ? redisWrapperClient.hget(MessageFormat.format(EMAIL_CONTENT_MESSAGE, queryDate), queryType.name()) : "") + contentBody.toString();
        redisWrapperClient.hset(MessageFormat.format(EMAIL_CONTENT_MESSAGE, queryDate), queryType.name(), Strings.isNullOrEmpty(content) ? "<tr><td colspan='2'>无交易记录</td></tr>" : content, lifeSecond);

        if (isQueryComplete(queryDate)){
            sendEmailMessage();
            redisWrapperClient.setex(MessageFormat.format(EMAIL_QUERY_IS_SEND, queryDate), lifeSecond, "SUCCESS");
        }
    }

    private boolean isQueryComplete(String queryDate){
        boolean isQueryComplete = true;
        for (QueryDownloadLogFilesType type : QueryDownloadLogFilesType.values()) {
            String totalKey = MessageFormat.format(EMAIL_CONTENT_TOTAL, queryDate, type.name());
            String countKey = MessageFormat.format(EMAIL_CONTENT_COUNT, queryDate, type.name());
            if (!redisWrapperClient.exists(totalKey) || !redisWrapperClient.exists(countKey) || Long.parseLong(redisWrapperClient.get(countKey)) < Long.parseLong(redisWrapperClient.get(totalKey))){
                isQueryComplete = false;
                break;
            }
        }
        return isQueryComplete;
    }

    public void sendEmailMessage(){
        String queryDate = DateTime.now().minusDays(1).toString("yyyyMMdd");
        Map<String, String> map = redisWrapperClient.hgetAll(MessageFormat.format(EMAIL_CONTENT_MESSAGE, queryDate));
        StringBuilder contentBody = new StringBuilder();

        List<QueryDownloadLogFilesType> types = Lists.newArrayList(QueryDownloadLogFilesType.values());
        types.forEach(type -> {
            String totalKey = MessageFormat.format(EMAIL_CONTENT_TOTAL, queryDate, type.name());
            String countKey = MessageFormat.format(EMAIL_CONTENT_COUNT, queryDate, type.name());
            String header = SendCloudTemplate.FUDIAN_CHECK_RESULT_HEADER.generateContent(Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("title", type.getDescribe())
                    .put("total", redisWrapperClient.exists(totalKey) ? redisWrapperClient.get(totalKey) : "0")
                    .put("count", redisWrapperClient.exists(countKey) ? redisWrapperClient.get(countKey) : "0")
                    .build()));
            String content = map.get(type.name());
            contentBody.append(Strings.isNullOrEmpty(content) ? MessageFormat.format("{0}查询失败</br>", type.getDescribe()) : header + content + SendCloudTemplate.FUDIAN_CHECK_RESULT_TAIL.getTemplate());
        });

        mqWrapperClient.sendMessage(MessageQueue.EMailMessage, new EMailMessage(Maps.newHashMap(ImmutableMap.<Environment, List<String>>builder()
                .put(Environment.PRODUCTION, Lists.newArrayList("dev@tuotiansudai.com"))
                .put(Environment.QA1, emailAddress)
                .put(Environment.QA2, emailAddress)
                .put(Environment.QA3, emailAddress)
                .put(Environment.QA4, emailAddress)
                .put(Environment.QA5, emailAddress)
                .put(Environment.DEV, Lists.newArrayList("zhukun@tuotiansudai.com"))
                .build()).get(environment), MessageFormat.format("{0}富滇银行{1}对账信息", environment.name(), queryDate), contentBody.toString()));
    }
}

@FunctionalInterface
interface QueryDownloadFilesMessageNotifyAction<T> {
    void messageNotify(T t);
}
