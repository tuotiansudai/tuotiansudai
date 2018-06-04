package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankLoanCreditInvestDto;
import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import com.tuotiansudai.fudian.dto.request.LoanCreditInvestRequestDto;
import com.tuotiansudai.fudian.dto.request.QueryTradeType;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.*;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectMapper;
import com.tuotiansudai.fudian.mapper.SelectRequestMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
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
public class LoanCreditInvestService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(LoanCreditInvestService.class);

    private final static String BANK_LOAN_CREDIT_INVEST_HISTORY_KEY_TEMPLATE = "BANK_LOAN_CREDIT_INVEST_HISTORY_{0}";

    private static final ApiType API_TYPE = ApiType.LOAN_CREDIT_INVEST;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final RedisTemplate<String, String> redisTemplate;

    private final MessageQueueClient messageQueueClient;

    private final RedissonClient redissonClient;

    private final SelectRequestMapper selectRequestMapper;

    private final QueryTradeService queryTradeService;

    private final SelectMapper selectMapper;

    private final Gson gson = new GsonBuilder().create();

    @Autowired
    public LoanCreditInvestService(SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, RedisTemplate<String, String> redisTemplate, MessageQueueClient messageQueueClient, RedissonClient redissonClient, SelectRequestMapper selectRequestMapper, QueryTradeService queryTradeService, SelectMapper selectMapper) {
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.redisTemplate = redisTemplate;
        this.messageQueueClient = messageQueueClient;
        this.redissonClient = redissonClient;
        this.selectRequestMapper = selectRequestMapper;
        this.queryTradeService = queryTradeService;
        this.selectMapper = selectMapper;
    }

    public LoanCreditInvestRequestDto invest(Source source, BankLoanCreditInvestDto dto) {
        LoanCreditInvestRequestDto requestDto = new LoanCreditInvestRequestDto(source, dto);

        signatureHelper.sign(API_TYPE, requestDto);

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
        updateMapper.updateReturnResponse(API_TYPE.name().toLowerCase(), responseData);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[loan credit invest] data is {}", responseData);
        ResponseDto responseDto = API_TYPE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[loan credit invest] parse callback data error, data is {}", responseData);
            return null;
        }

        if (!responseDto.isSuccess()) {
            logger.error("[Loan credit Invest] callback is failure, orderNo: {}, message {}", responseDto.getContent().getOrderNo(), responseDto.getRetMsg());
        }

        responseDto.setReqData(responseData);
        updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);
        return responseDto;
    }

    @Scheduled(fixedDelay = 1000 * 10, initialDelay = 1000 * 60, zone = "Asia/Shanghai")
    public void schedule() {
        RLock lock = redissonClient.getLock("BANK_CREDIT_INVEST_HISTORY");
        if (lock.tryLock()) {
            try {
                HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
                List<BaseRequestDto> baseRequestDtos = selectRequestMapper.selectResponseInOneHour(ApiType.LOAN_CREDIT_INVEST.name().toLowerCase());

                for (BaseRequestDto baseRequestDto : baseRequestDtos) {
                    try {
                        String bankLoanCreditInvestValue = hashOperations.get(MessageFormat.format(BANK_LOAN_CREDIT_INVEST_HISTORY_KEY_TEMPLATE, baseRequestDto.getOrderDate()), baseRequestDto.getOrderNo());
                        BankLoanCreditInvestMessage bankLoanCreditInvestMessage = gson.fromJson(bankLoanCreditInvestValue, BankLoanCreditInvestMessage.class);
                        if (Strings.isNullOrEmpty(bankLoanCreditInvestValue)) {
                            logger.error("[LoanCreditInvest Status Schedule] fetch LoanCreditInvest meta data from redis error, bank order no:{}, redis value: {}", baseRequestDto.getOrderNo(), bankLoanCreditInvestValue);
                            continue;
                        }
                        ResponseDto<QueryTradeContentDto> query = queryTradeService.query(bankLoanCreditInvestMessage.getBankOrderNo(), bankLoanCreditInvestMessage.getBankOrderDate(), QueryTradeType.LOAN_CREDIT_INVEST);
                        if (query.isSuccess() && "1".equals(query.getContent().getQueryState())) {
                            updateMapper.updateQueryResponse(API_TYPE.name().toLowerCase(), query);
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
        String responseData = this.selectMapper.selectNotifyResponseData(API_TYPE.name().toLowerCase(), orderNo);
        String queryResponseData = this.selectMapper.selectQueryResponseData(API_TYPE.name().toLowerCase(), orderNo);

        if (!Strings.isNullOrEmpty(responseData)) {
            ResponseDto<LoanCreditInvestContentDto> responseDto = (ResponseDto<LoanCreditInvestContentDto>) API_TYPE.getParser().parse(responseData);
            return responseDto.isSuccess();
        }

        if (!Strings.isNullOrEmpty(queryResponseData)) {
            ResponseDto<QueryTradeContentDto> queryResponseDto = (ResponseDto<QueryTradeContentDto>) ApiType.QUERY_TRADE.getParser().parse(queryResponseData);
            return queryResponseDto.isSuccess() && !"0".equals(queryResponseDto.getContent().getQueryState());
        }

        return null;
    }
}
