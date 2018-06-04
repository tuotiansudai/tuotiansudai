package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.BankRechargeDto;
import com.tuotiansudai.fudian.dto.RechargePayType;
import com.tuotiansudai.fudian.dto.request.BaseRequestDto;
import com.tuotiansudai.fudian.dto.request.QueryTradeType;
import com.tuotiansudai.fudian.dto.request.RechargeRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.QueryTradeContentDto;
import com.tuotiansudai.fudian.dto.response.RechargeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankRechargeMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageTopic;
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
public class RechargeService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(RechargeService.class);

    private static final ApiType API_TYPE = ApiType.RECHARGE;

    private final static String BANK_RECHARGE_MESSAGE_KEY = "BANK_RECHARGE_MESSAGE_{0}";

    private static final Map<String, String> BANK_RECHARGE_SUCCESS_STATUS = Maps.newHashMap(ImmutableMap.<String, String>builder().put("1", "充值成功").build());

    private static final Map<String, String> BANK_RECHARGE_FAILURE_STATUS = Maps.newHashMap(ImmutableMap.<String, String>builder().put("2", "充值失败").build());

    private final RedisTemplate<String, String> redisTemplate;

    private final RedissonClient redissonClient;

    private final MessageQueueClient messageQueueClient;

    private final BankConfig bankConfig;

    private final SignatureHelper signatureHelper;

    private final SelectMapper selectMapper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final QueryTradeService queryTradeService;

    @Autowired
    public RechargeService(RedisTemplate<String, String> redisTemplate, RedissonClient redissonClient, BankConfig bankConfig, MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, SelectMapper selectMapper, InsertMapper insertMapper, UpdateMapper updateMapper, QueryTradeService queryTradeService) {
        this.bankConfig = bankConfig;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.messageQueueClient = messageQueueClient;
        this.selectMapper = selectMapper;
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.queryTradeService = queryTradeService;
    }

    public RechargeRequestDto recharge(Source source, BankRechargeDto bankRechargeDto) {
        RechargeRequestDto dto = new RechargeRequestDto(source, bankRechargeDto);

        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[recharge] failed to sign, data {}", bankRechargeDto);
            return null;
        }
        insertMapper.insertRecharge(dto);

        BankRechargeMessage bankRechargeMessage = new BankRechargeMessage(bankRechargeDto.getRechargeId(),
                bankRechargeDto.getLoginName(),
                bankRechargeDto.getMobile(),
                bankRechargeDto.getBankUserName(),
                bankRechargeDto.getBankAccountNo(),
                bankRechargeDto.getAmount(),
                dto.getOrderNo(),
                dto.getOrderDate());

        String bankWithdrawMessageKey = MessageFormat.format(BANK_RECHARGE_MESSAGE_KEY, dto.getOrderDate());
        redisTemplate.<String, String>opsForHash().put(bankWithdrawMessageKey, dto.getOrderNo(), gson.toJson(bankRechargeMessage));
        redisTemplate.expire(bankWithdrawMessageKey, 7, TimeUnit.DAYS);

        return dto;
    }

    public RechargeRequestDto merchantRecharge(Source source, String loginName, String mobile, long rechargeId, long amount) {
        BankRechargeDto bankRechargeDto = new BankRechargeDto(loginName, mobile, bankConfig.getMerchantUserName(), bankConfig.getMerchantAccountNo(), rechargeId, amount, RechargePayType.GATE_PAY);
        RechargeRequestDto dto = new RechargeRequestDto(source, bankRechargeDto);

        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[merchant recharge sign] sign error, userName: {}, accountNo: {}, amount: {}, payType: {}",
                    bankConfig.getMerchantUserName(), bankConfig.getMerchantAccountNo(), amount, RechargePayType.GATE_PAY);
            return null;
        }

        insertMapper.insertRecharge(dto);
        return dto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        updateMapper.updateReturnResponse(ApiType.RECHARGE.name().toLowerCase(), responseData);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[recharge callback] data is {}", responseData);

        ResponseDto<RechargeContentDto> responseDto = ApiType.RECHARGE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[recharge callback] parse callback data error, data is {}", responseData);
            return null;
        }

        if (!responseDto.isSuccess()) {
            logger.error("[recharge callback] is failure, orderNo: {}, message {}", responseDto.getContent().getOrderNo(), responseDto.getRetMsg());
        }

        responseDto.setReqData(responseData);
        updateMapper.updateNotifyResponseData(ApiType.RECHARGE.name().toLowerCase(), responseDto);
        return responseDto;
    }

    @Scheduled(fixedDelay = FIXED_DELAY, initialDelay = 1000 * 10, zone = "Asia/Shanghai")
    public void schedule() {
        RLock lock = redissonClient.getLock("BANK_RECHARGE_QUERY_LOCK");

        if (lock.tryLock()) {
            try {
                HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
                List<BaseRequestDto> rechargeRequests = selectMapper.selectResponseInOneHour(API_TYPE.name().toLowerCase());

                for (BaseRequestDto rechargeRequest : rechargeRequests) {
                    try {
                        ResponseDto<QueryTradeContentDto> query = queryTradeService.query(rechargeRequest.getOrderNo(), rechargeRequest.getOrderDate(), QueryTradeType.RECHARGE);
                        String message = hashOperations.get(MessageFormat.format(BANK_RECHARGE_MESSAGE_KEY, rechargeRequest.getOrderDate()), rechargeRequest.getOrderNo());
                        BankRechargeMessage bankRechargeMessage = gson.fromJson(message, BankRechargeMessage.class);
                        if (bankRechargeMessage == null) {
                            continue;
                        }
                        if (query.isSuccess()) {
                            if (BANK_RECHARGE_SUCCESS_STATUS.containsKey(query.getContent().getQueryState())) {
                                updateMapper.updateQueryResponse(API_TYPE.name().toLowerCase(), query);
                                bankRechargeMessage.setStatus(true);
                                this.messageQueueClient.publishMessage(MessageTopic.Recharge, bankRechargeMessage);
                                logger.info("[Recharge Status Schedule] recharge is success, rechargeId: {}", bankRechargeMessage.getRechargeId());
                            }

                            if (BANK_RECHARGE_FAILURE_STATUS.containsKey(query.getContent().getQueryState())) {
                                updateMapper.updateQueryResponse(API_TYPE.name().toLowerCase(), query);
                                bankRechargeMessage.setStatus(false);
                                this.messageQueueClient.publishMessage(MessageTopic.Recharge, bankRechargeMessage);
                                logger.info("[Recharge Status Schedule] recharge is failure, rechargeId: {}", bankRechargeMessage.getRechargeId());
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
            ResponseDto<RechargeContentDto> responseDto = (ResponseDto<RechargeContentDto>) API_TYPE.getParser().parse(responseData);
            return responseDto.isSuccess() && responseDto.getContent().isSuccess();
        }

        if (!Strings.isNullOrEmpty(queryResponseData)) {
            ResponseDto<QueryTradeContentDto> queryResponseDto = (ResponseDto<QueryTradeContentDto>) ApiType.QUERY_TRADE.getParser().parse(queryResponseData);
            return queryResponseDto.isSuccess() && BANK_RECHARGE_SUCCESS_STATUS.containsKey(queryResponseDto.getContent().getQueryState());
        }
        return null;
    }
}
