package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.config.BankConfig;
import com.tuotiansudai.fudian.dto.ExtMarkDto;
import com.tuotiansudai.fudian.dto.request.RechargePayType;
import com.tuotiansudai.fudian.dto.request.RechargeRequestDto;
import com.tuotiansudai.fudian.dto.response.RechargeContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.mq.tools.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
public class RechargeService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(RechargeService.class);

    private final BankConfig bankConfig;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final MessageQueueClient messageQueueClient;

    private final RedisClient redisClient;

    private final String RECHARGE_BIND_ORDER_NO = "RECHARGE_BIND_ORDER_NO:{}";

    @Autowired
    public RechargeService(BankConfig bankConfig, SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, MessageQueueClient messageQueueClient, RedisClient redisClient) {
        this.bankConfig = bankConfig;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.messageQueueClient = messageQueueClient;
        this.redisClient = redisClient;
    }

    public RechargeRequestDto recharge(String rechargeId, String loginName, String mobile, String userName, String accountNo, String amount, RechargePayType payType) {
        RechargeRequestDto dto = new RechargeRequestDto(loginName, mobile, userName, accountNo, amount, payType);
        signatureHelper.sign(dto);

        redisClient.newJedis().set(MessageFormat.format(RECHARGE_BIND_ORDER_NO, dto.getOrderNo()), rechargeId);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[recharge] sign error, userName: {}, accountNo: {}, amount: {}, payType: {}", userName, accountNo, amount, payType);
            return null;
        }
        insertMapper.insertRecharge(dto);
        return dto;

    }

    public RechargeRequestDto merchantRecharge(String loginName, String mobile, String amount) {
        RechargeRequestDto dto = new RechargeRequestDto(loginName, mobile, bankConfig.getMerchantUserName(), bankConfig.getMerchantAccountNo(), amount, RechargePayType.GATE_PAY);

        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[merchant recharge sign] sign error, userName: {}, accountNo: {}, amount: {}, payType: {}",
                    bankConfig.getMerchantUserName(), bankConfig.getMerchantAccountNo(), amount, RechargePayType.GATE_PAY);
            return null;
        }

        insertMapper.insertRecharge(dto);
        return dto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto callback(String responseData) {
        logger.info("[recharge callback] data is {}", responseData);

        ResponseDto<RechargeContentDto> responseDto = ApiType.RECHARGE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[recharge callback] parse callback data error, data is {}", responseData);
            return null;
        }

        RechargeContentDto rechargeContentDto = responseDto.getContent();
        String rechargeKey = MessageFormat.format(RECHARGE_BIND_ORDER_NO, rechargeContentDto.getOrderNo());
        String rechargeId = redisClient.newJedis().get(rechargeKey);
        ExtMarkDto extMarkDto = new GsonBuilder().create().fromJson(rechargeContentDto.getExtMark(), ExtMarkDto.class);
        this.messageQueueClient.publishMessage(MessageTopic.Recharge, Maps.newHashMap(ImmutableMap.<String, String>builder()
                .put("loginName", extMarkDto.getLoginName())
                .put("mobile", extMarkDto.getMobile())
                .put("rechargeId", rechargeId)
                .put("orderDate", rechargeContentDto.getOrderDate())
                .put("orderNo", rechargeContentDto.getOrderNo())
                .put("isSuccess", String.valueOf(responseDto.isSuccess() && rechargeContentDto.isSuccess()))
                .build()));

        redisClient.newJedis().del(rechargeKey);
        responseDto.setReqData(responseData);
        updateMapper.updateRecharge(responseDto);
        return responseDto;
    }
}
