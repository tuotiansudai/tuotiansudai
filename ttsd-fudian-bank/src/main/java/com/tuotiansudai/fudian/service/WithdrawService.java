package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankWithdrawDto;
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

    private final static String BANK_WITHDRAW_KEY = "BANK_WITHDRAW_{0}";

    private final MessageQueueClient messageQueueClient;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    private final RedisTemplate<String, String> redisTemplate;

    private final Gson gson = new GsonBuilder().create();

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
                AmountUtils.toYuan(bankWithdrawDto.getFee()));

        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[withdraw] sign error, data: {}", bankWithdrawDto);
            return null;
        }

        insertMapper.insertWithdraw(dto);

        BankWithdrawMessage message = new BankWithdrawMessage(bankWithdrawDto.getWithdrawId(),
                bankWithdrawDto.getLoginName(),
                bankWithdrawDto.getMobile(),
                bankWithdrawDto.getBankUserName(),
                bankWithdrawDto.getBankAccountNo(),
                bankWithdrawDto.getAmount(),
                bankWithdrawDto.getFee(),
                dto.getOrderNo(),
                dto.getOrderDate(),
                bankWithdrawDto.getOpenId());

        redisTemplate.<String, String>opsForHash().put(MessageFormat.format(BANK_WITHDRAW_KEY, dto.getOrderDate()), dto.getOrderNo(), gson.toJson(message));
        redisTemplate.expire(MessageFormat.format(BANK_WITHDRAW_KEY, dto.getOrderDate()), 7, TimeUnit.DAYS);

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

            if (content.isApplying()) {
                return responseDto;
            }

            BankWithdrawMessage bankWithdrawMessage = gson.fromJson(redisTemplate.<String, String>opsForHash().get(MessageFormat.format(BANK_WITHDRAW_KEY, content.getOrderDate()), content.getOrderNo()), BankWithdrawMessage.class);
            bankWithdrawMessage.setBankCode(content.getBankCode());
            bankWithdrawMessage.setCardNumber(content.getBankCardNo());
            bankWithdrawMessage.setBankName(content.getBankName());
            bankWithdrawMessage.setStatus(responseDto.isSuccess() && responseDto.getContent().isSuccess());
            bankWithdrawMessage.setMessage(responseDto.getRetMsg());


            this.messageQueueClient.publishMessage(MessageTopic.Withdraw, bankWithdrawMessage);
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
