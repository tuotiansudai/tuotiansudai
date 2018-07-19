package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankWithdrawDto;
import com.tuotiansudai.fudian.dto.QueryTradeType;
import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.request.WithdrawRequestDto;
import com.tuotiansudai.fudian.dto.response.QueryTradeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.dto.response.WithdrawContentDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankWithdrawMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class WithdrawService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(WithdrawService.class);

    private static final ApiType API_TYPE = ApiType.WITHDRAW;

    private static final Map<String, String> BANK_WITHDRAW_SUCCESS_STATUS = Maps.newHashMap(ImmutableMap.<String, String>builder().put("1", "提现成功").build());

    private static final Map<String, String> BANK_WITHDRAW_FAILURE_STATUS = Maps.newHashMap(ImmutableMap.<String, String>builder()
            .put("2", "提现失败")
            .put("3", "提现失败银行退单")
            .put("4", "提现异常")
            .build());

    private final static String BANK_WITHDRAW_MESSAGE_KEY = "BANK_WITHDRAW_MESSAGE_{0}";

    private final RedisTemplate<String, String> redisTemplate;

    private final RedissonClient redissonClient;

    private final MessageQueueClient messageQueueClient;

    private final SignatureHelper signatureHelper;

    private final QueryTradeService queryTradeService;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectMapper selectMapper;

    @Autowired
    public WithdrawService(RedisTemplate<String, String> redisTemplate, RedissonClient redissonClient, MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, QueryTradeService queryTradeService, SelectMapper selectMapper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.messageQueueClient = messageQueueClient;
        this.signatureHelper = signatureHelper;
        this.queryTradeService = queryTradeService;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectMapper = selectMapper;
    }

    public WithdrawRequestDto withdraw(Source source, BankWithdrawDto bankWithdrawDto) {
        WithdrawRequestDto dto = new WithdrawRequestDto(source, bankWithdrawDto);

        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[Withdraw] failed to sign, data: {}", bankWithdrawDto);
            return null;
        }

        insertMapper.insertWithdraw(dto);

        BankWithdrawMessage bankWithdrawMessage = new BankWithdrawMessage(bankWithdrawDto.getWithdrawId(),
                bankWithdrawDto.getLoginName(),
                bankWithdrawDto.getMobile(),
                bankWithdrawDto.getBankUserName(),
                bankWithdrawDto.getBankAccountNo(),
                bankWithdrawDto.getAmount(),
                bankWithdrawDto.getFee(),
                dto.getOrderNo(),
                dto.getOrderDate());

        String bankWithdrawMessageKey = MessageFormat.format(BANK_WITHDRAW_MESSAGE_KEY, dto.getOrderDate());
        redisTemplate.<String, String>opsForHash().put(bankWithdrawMessageKey, dto.getOrderNo(), gson.toJson(bankWithdrawMessage));
        redisTemplate.expire(bankWithdrawMessageKey, 7, TimeUnit.DAYS);

        return dto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        updateMapper.updateReturnResponse(API_TYPE.name().toLowerCase(), responseData);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[Withdraw] callback data is {}", responseData);

        ResponseDto<WithdrawContentDto> responseDto = API_TYPE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[Withdraw] failed to parse callback data: {}", responseData);
            return null;
        }

        if (!responseDto.isSuccess()) {
            logger.error("[Withdraw] callback is failure, orderNo: {}, message {}", responseDto.getContent().getOrderNo(), responseDto.getRetMsg());
        }

        responseDto.setReqData(responseData);
        updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);

        return responseDto;
    }

    @Scheduled(fixedDelay = FIXED_DELAY, initialDelay = 1000 * 10, zone = "Asia/Shanghai")
    public void schedule() {
        RLock lock = redissonClient.getLock("BANK_WITHDRAW_QUERY_LOCK");

        if (lock.tryLock()) {
            try {
                HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
                List<BaseRequestDto> withdrawRequests = selectMapper.selectResponseInOneHour(API_TYPE.name().toLowerCase());

                for (BaseRequestDto withdrawRequest : withdrawRequests) {
                    try {
                        String message = hashOperations.get(MessageFormat.format(BANK_WITHDRAW_MESSAGE_KEY, withdrawRequest.getOrderDate()), withdrawRequest.getOrderNo());
                        BankWithdrawMessage bankWithdrawMessage = gson.fromJson(message, BankWithdrawMessage.class);
                        if (bankWithdrawMessage == null) {
                            continue;
                        }
                        ResponseDto<QueryTradeContentDto> query = queryTradeService.query(withdrawRequest.getOrderNo(), withdrawRequest.getOrderDate(), QueryTradeType.WITHDRAW);
                        if (query.isSuccess()) {
                            if (BANK_WITHDRAW_SUCCESS_STATUS.containsKey(query.getContent().getQueryState())) {
                                updateMapper.updateQueryResponse(API_TYPE.name().toLowerCase(), query);
                                bankWithdrawMessage.setStatus(true);
                                bankWithdrawMessage.setMessage(BANK_WITHDRAW_SUCCESS_STATUS.get(query.getContent().getQueryState()));
                                this.messageQueueClient.sendMessage(MessageQueue.Withdraw_Success, bankWithdrawMessage);
                                logger.info("[Withdraw Status Schedule] withdraw is success, withdrawId: {}", bankWithdrawMessage.getWithdrawId());
                            }

                            if (BANK_WITHDRAW_FAILURE_STATUS.containsKey(query.getContent().getQueryState())) {
                                updateMapper.updateQueryResponse(API_TYPE.name().toLowerCase(), query);
                                bankWithdrawMessage.setStatus(false);
                                bankWithdrawMessage.setMessage(BANK_WITHDRAW_FAILURE_STATUS.get(query.getContent().getQueryState()));
                                this.messageQueueClient.sendMessage(MessageQueue.Withdraw_Success, bankWithdrawMessage);
                                logger.info("[Withdraw Status Schedule] withdraw is failure, withdrawId: {}", bankWithdrawMessage.getWithdrawId());
                            }
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
            ResponseDto<WithdrawContentDto> responseDto = (ResponseDto<WithdrawContentDto>) API_TYPE.getParser().parse(responseData);
            return responseDto.isSuccess() && responseDto.getContent().isSuccess();
        }

        if (!Strings.isNullOrEmpty(queryResponseData)) {
            ResponseDto<QueryTradeContentDto> queryResponseDto = (ResponseDto<QueryTradeContentDto>) ApiType.QUERY_TRADE.getParser().parse(queryResponseData);
            return queryResponseDto.isSuccess() && BANK_WITHDRAW_SUCCESS_STATUS.containsKey(queryResponseDto.getContent().getQueryState());
        }

        return null;
    }
}
