package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanCreditInvestDto;
import com.tuotiansudai.fudian.dto.request.*;
import com.tuotiansudai.fudian.dto.response.LoanCreateContentDto;
import com.tuotiansudai.fudian.dto.response.LoanCreditInvestContentDto;
import com.tuotiansudai.fudian.dto.response.QueryTradeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.*;
import com.tuotiansudai.fudian.message.BankLoanCreditInvestMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.AmountUtils;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class LoanCreditInvestService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(LoanCreditInvestService.class);

    private final static String BANK_LOAN_CREDIT_INVEST_HISTORY_KEY_TEMPLATE = "BANK_LOAN_CREDIT_INVEST_HISTORY_{0}";

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final ReturnUpdateMapper returnUpdateMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    private final RedisTemplate<String, String> redisTemplate;

    private final MessageQueueClient messageQueueClient;

    private final RedissonClient redissonClient;

    private final SelectRequestMapper selectRequestMapper;

    private final QueryTradeService queryTradeService;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public LoanCreditInvestService(SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, ReturnUpdateMapper returnUpdateMapper, SelectResponseDataMapper selectResponseDataMapper, RedisTemplate<String, String> redisTemplate, MessageQueueClient messageQueueClient, RedissonClient redissonClient, SelectRequestMapper selectRequestMapper, QueryTradeService queryTradeService) {
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.returnUpdateMapper = returnUpdateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
        this.redisTemplate = redisTemplate;
        this.messageQueueClient = messageQueueClient;
        this.redissonClient = redissonClient;
        this.selectRequestMapper = selectRequestMapper;
        this.queryTradeService = queryTradeService;
    }

    public LoanCreditInvestRequestDto invest(Source source, BankLoanCreditInvestDto dto) {
        LoanCreditInvestRequestDto requestDto = new LoanCreditInvestRequestDto(source, dto.getLoginName(), dto.getMobile(),
                dto.getBankUserName(), dto.getBankAccountNo(),
                dto.getLoanTxNo(), dto.getInvestOrderNo(), dto.getInvestOrderDate(),
                String.valueOf(dto.getTransferApplicationId()), AmountUtils.toYuan(dto.getInvestAmount()),
                AmountUtils.toYuan(dto.getInvestAmount()), AmountUtils.toYuan(dto.getTransferFee()));
        signatureHelper.sign(requestDto);

        if (Strings.isNullOrEmpty(requestDto.getRequestData())) {
            logger.error("[loan credit invest] sign error, data: {}", requestDto.getRequestData());
            return null;
        }

        insertMapper.insertLoanCreditInvest(requestDto);
        String bankLoanCreditInvestHistory = MessageFormat.format(BANK_LOAN_CREDIT_INVEST_HISTORY_KEY_TEMPLATE, requestDto.getOrderDate());
        redisTemplate.opsForHash().put(bankLoanCreditInvestHistory, requestDto.getInvestOrderNo(),
                new Gson().toJson(new BankLoanCreditInvestMessage(dto.getTransferApplicationId(),
                        dto.getInvestId(),
                        dto.getInvestAmount(),
                        dto.getLoginName(),
                        dto.getMobile(),
                        dto.getBankUserName(),
                        dto.getBankAccountNo(),
                        requestDto.getOrderNo(),
                        requestDto.getOrderDate())));
        redisTemplate.expire(bankLoanCreditInvestHistory,30, TimeUnit.DAYS);

        return requestDto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        returnUpdateMapper.updateLoanCreditInvest(responseData);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[loan credit invest] data is {}", responseData);
        ResponseDto<LoanCreditInvestContentDto> responseDto = (ResponseDto<LoanCreditInvestContentDto>) ApiType.LOAN_CREDIT_INVEST.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[loan credit invest] parse callback data error, data is {}", responseData);
            return null;
        }
        responseDto.setReqData(responseData);
        int count = updateMapper.updateLoanCreditInvest(responseDto, LoanInvestStatus.BANK_RESPONSE);
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();

        if (responseDto.isSuccess() && count > 0) {
            String message = hashOperations.get(MessageFormat.format(BANK_LOAN_CREDIT_INVEST_HISTORY_KEY_TEMPLATE, responseDto.getContent().getOriOrderDate()), responseDto.getContent().getCreditNo());
            if (Strings.isNullOrEmpty(message)) {
                logger.error("[loan credit invest callback] callback is success, but queue message is not found, response data is {}", responseData);
                return responseDto;
            }
            messageQueueClient.sendMessage(MessageQueue.LoanCreditInvest_Success, message);
        }
        return responseDto;
    }

    @Scheduled(fixedDelay = 1000 * 10, initialDelay = 1000 * 60, zone = "Asia/Shanghai")
    public void schedule() {
        RLock lock = redissonClient.getLock("BANK_CREDIT_INVEST_HISTORY");
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        if (lock.tryLock()) {
            try {
                List<BaseRequestDto> baseRequestDtos = selectRequestMapper.selectResponseInOneHour(ApiType.LOAN_CREDIT_INVEST.name().toLowerCase());

                for (BaseRequestDto baseRequestDto : baseRequestDtos) {
                    try {
                        String bankLoanCreditInvestValue = hashOperations.get(MessageFormat.format(BANK_LOAN_CREDIT_INVEST_HISTORY_KEY_TEMPLATE, baseRequestDto.getOrderDate()), baseRequestDto.getOrderNo());
                        BankLoanCreditInvestMessage bankLoanCreditInvestMessage = gson.fromJson(bankLoanCreditInvestValue, BankLoanCreditInvestMessage.class);
                        if (Strings.isNullOrEmpty(bankLoanCreditInvestValue)) {
                            logger.error("[LoanCreditInvest Status Schedule] fetch LoanCreditInvest meta data from redis error, bank order no:{}, redis value: {}", baseRequestDto.getOrderNo(), bankLoanCreditInvestValue);
                            continue;
                        }

                        ResponseDto<QueryTradeContentDto> query = queryTradeService.query(bankLoanCreditInvestMessage.getLoginName(), bankLoanCreditInvestMessage.getMobile(), bankLoanCreditInvestMessage.getBankOrderNo(), bankLoanCreditInvestMessage.getBankOrderDate(), QueryTradeType.LOAN_CREDIT_INVEST);
                        if (query.isSuccess() && "1".equals(query.getContent().getQueryState()) && updateMapper.updateLoanCreditInvest(query, LoanInvestStatus.MANUAL_QUERIED) > 0) {
                            messageQueueClient.sendMessage(MessageQueue.LoanCreditInvest_Success, bankLoanCreditInvestValue);
                            logger.error("[LoanCreditInvest Status Schedule] LoanCreditInvest is success, but bank response is not found, should send topic: {}", bankLoanCreditInvestValue);
                        }
                    } catch (JsonSyntaxException ex) {
                        logger.error(ex.getLocalizedMessage(), ex);
                    }
                }
            } finally {
                lock.unlock();
            }
        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.LOAN_CREDIT_INVEST.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<LoanCreateContentDto> responseDto = (ResponseDto<LoanCreateContentDto>) ApiType.LOAN_CREDIT_INVEST.getParser().parse(responseData);

        return responseDto.isSuccess();
    }
}
