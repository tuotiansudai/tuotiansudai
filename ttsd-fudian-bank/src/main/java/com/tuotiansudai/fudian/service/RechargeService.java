package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.BankRechargeDto;
import com.tuotiansudai.fudian.dto.ExtMarkDto;
import com.tuotiansudai.fudian.dto.RechargePayType;
import com.tuotiansudai.fudian.dto.request.RechargeRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.RechargeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.AmountUtils;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Service
public class RechargeService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(RechargeService.class);

    private static final ApiType API_TYPE = ApiType.RECHARGE;

    private final String RECHARGE_BIND_ORDER_NO = "RECHARGE_BIND_ORDER_NO:{0}";

    private final RedisTemplate<String, String> redisTemplate;

    private final MessageQueueClient messageQueueClient;

    private final BankConfig bankConfig;

    private final SignatureHelper signatureHelper;

    private final SelectMapper selectMapper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    @Autowired
    public RechargeService(RedisTemplate<String, String> redisTemplate, BankConfig bankConfig, MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, SelectMapper selectMapper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.bankConfig = bankConfig;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.messageQueueClient = messageQueueClient;
        this.selectMapper = selectMapper;
        this.redisTemplate = redisTemplate;
    }

    public RechargeRequestDto recharge(Source source, BankRechargeDto bankRechargeDto) {
        RechargeRequestDto dto = new RechargeRequestDto(source, bankRechargeDto.getLoginName(), bankRechargeDto.getMobile(), bankRechargeDto.getBankUserName(), bankRechargeDto.getBankAccountNo(), AmountUtils.toYuan(bankRechargeDto.getAmount()), bankRechargeDto.getPayType());

        signatureHelper.sign(API_TYPE, dto);

        redisTemplate.opsForValue().set(MessageFormat.format(RECHARGE_BIND_ORDER_NO, dto.getOrderNo()), String.valueOf(bankRechargeDto.getRechargeId()), 30 * 24 * 60 * 60, TimeUnit.SECONDS);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[recharge] sign error, data {}", bankRechargeDto);
            return null;
        }
        insertMapper.insertRecharge(dto);
        return dto;

    }

    public RechargeRequestDto merchantRecharge(Source source, String loginName, String mobile, String amount) {
        RechargeRequestDto dto = new RechargeRequestDto(source, loginName, mobile, bankConfig.getMerchantUserName(), bankConfig.getMerchantAccountNo(), amount, RechargePayType.GATE_PAY);

        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[merchant recharge sign] sign error, userName: {}, accountNo: {}, amount: {}, payType: {}",
                    bankConfig.getMerchantUserName(), bankConfig.getMerchantAccountNo(), amount, RechargePayType.GATE_PAY);
            return null;
        }

        insertMapper.insertRecharge(dto);
        return dto;
    }

    public void returnCallback(ResponseDto responseData) {
        updateMapper.updateReturnResponse(ApiType.RECHARGE.name(), responseData);
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
        responseDto.setReqData(responseData);
        updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);

        RechargeContentDto rechargeContentDto = responseDto.getContent();
        String rechargeKey = MessageFormat.format(RECHARGE_BIND_ORDER_NO, rechargeContentDto.getOrderNo());
        String rechargeId = redisTemplate.opsForValue().get(rechargeKey);
        ExtMarkDto extMarkDto = new GsonBuilder().create().fromJson(rechargeContentDto.getExtMark(), ExtMarkDto.class);
        this.messageQueueClient.publishMessage(MessageTopic.Recharge, Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("loginName", extMarkDto.getLoginName())
                .put("mobile", extMarkDto.getMobile())
                .put("rechargeId", rechargeId)
                .put("orderDate", rechargeContentDto.getOrderDate())
                .put("orderNo", rechargeContentDto.getOrderNo())
                .put("isSuccess", String.valueOf(responseDto.isSuccess() && rechargeContentDto.isSuccess()))
                .build()));

        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectMapper.selectNotifyResponseData(ApiType.RECHARGE.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<RechargeContentDto> responseDto = (ResponseDto<RechargeContentDto>) ApiType.RECHARGE.getParser().parse(responseData);

        return responseDto.isSuccess() && responseDto.getContent().isSuccess();
    }
}
