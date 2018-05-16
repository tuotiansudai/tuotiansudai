package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankWithdrawDto;
import com.tuotiansudai.fudian.dto.ExtMarkDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.request.WithdrawRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.dto.response.WithdrawContentDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectResponseDataMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankWithdrawMessage;
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
public class WithdrawService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(WithdrawService.class);

    private static String WITHDRAW_ID_TEMPLATE = "BANK_WITHDRAW_{0}";

    private final MessageQueueClient messageQueueClient;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public WithdrawService(RedisTemplate<String, String> redisTemplate, MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.redisTemplate = redisTemplate;
        this.messageQueueClient = messageQueueClient;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    public WithdrawRequestDto withdraw(Source source, BankWithdrawDto bankWithdrawDto) {
        WithdrawRequestDto dto = new WithdrawRequestDto(source,
                bankWithdrawDto.getLoginName(),
                bankWithdrawDto.getMobile(),
                bankWithdrawDto.getBankUserName(),
                bankWithdrawDto.getBankAccountNo(),
                AmountUtils.toYuan(bankWithdrawDto.getAmount()),
                AmountUtils.toYuan(bankWithdrawDto.getFee()),
                Strings.isNullOrEmpty(bankWithdrawDto.getOpenId()) ? null : Maps.newHashMap(ImmutableMap.<String, String>builder().put("openId", bankWithdrawDto.getOpenId()).build()));

        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[withdraw] sign error, data: {}", bankWithdrawDto);
            return null;
        }

        insertMapper.insertWithdraw(dto);

        redisTemplate.opsForValue().set(MessageFormat.format(WITHDRAW_ID_TEMPLATE, dto.getOrderNo()), String.valueOf(bankWithdrawDto.getWithdrawId()), 7, TimeUnit.DAYS);

        return dto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto callback(String responseData) {
        logger.info("[withdraw callback] data is {}", responseData);

        ResponseDto<WithdrawContentDto> responseDto = ApiType.WITHDRAW.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[withdraw callback] parse callback data error, data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        updateMapper.updateWithdraw(responseDto);

        if (responseDto.isSuccess()) {
            WithdrawContentDto content = responseDto.getContent();
            ExtMarkDto extMarkDto = new GsonBuilder().create().fromJson(responseDto.getContent().getExtMark(), ExtMarkDto.class);

            String withdrawId = redisTemplate.opsForValue().get(MessageFormat.format(WITHDRAW_ID_TEMPLATE, content.getOrderNo()));
            if (Strings.isNullOrEmpty(withdrawId)) {
                return responseDto;
            }

            if (content.isApplying()) {
                return responseDto;
            }

            String openId = extMarkDto.getExtraValues() != null && extMarkDto.getExtraValues().containsKey("openId") ? extMarkDto.getExtraValues().get("openId") : null;
            BankWithdrawMessage message = new BankWithdrawMessage(Long.parseLong(withdrawId),
                    extMarkDto.getLoginName(),
                    extMarkDto.getMobile(),
                    content.getUserName(),
                    content.getAccountNo(),
                    AmountUtils.toCent(content.getAmount()),
                    AmountUtils.toCent(content.getFee()),
                    content.getBankCode(),
                    content.getBankCardNo(),
                    content.getBankName(),
                    content.getOrderNo(),
                    content.getOrderDate(),
                    openId,
                    content.isSuccess());

            this.messageQueueClient.publishMessage(MessageTopic.Withdraw, message);
        }
        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.WITHDRAW.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<WithdrawContentDto> responseDto = (ResponseDto<WithdrawContentDto>) ApiType.WITHDRAW.getParser().parse(responseData);

        return responseDto.isSuccess() && responseDto.getContent().isSuccess();
    }
}
