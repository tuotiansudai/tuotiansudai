package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankBaseDto;
import com.tuotiansudai.fudian.dto.request.CancelCardBindRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.CancelCardBindContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectResponseDataMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankBindCardMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Service
public class CancelCardBindService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(CancelCardBindService.class);

    private final static String BANK_CANCEL_CARD_BIND_KEY = "BANK_CANCEL_CARD_BIND_{0}";

    private final RedisTemplate<String, String> redisTemplate;

    private final MessageQueueClient messageQueueClient;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    private final Gson gson = new GsonBuilder().create();
    @Autowired
    public CancelCardBindService(RedisTemplate<String, String> redisTemplate, MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.redisTemplate = redisTemplate;
        this.messageQueueClient = messageQueueClient;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    public CancelCardBindRequestDto cancel(Source source, BankBaseDto bankBaseDto) {
        CancelCardBindRequestDto dto = new CancelCardBindRequestDto(source, bankBaseDto.getLoginName(), bankBaseDto.getMobile(), bankBaseDto.getBankUserName(), bankBaseDto.getBankAccountNo());
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[cancel card bind] sign error, data: {}", bankBaseDto);

            return null;
        }

        insertMapper.insertCancelCardBind(dto);

        redisTemplate.<String, String>opsForHash().put(MessageFormat.format(BANK_CANCEL_CARD_BIND_KEY, dto.getOrderDate()), dto.getOrderNo(),
                gson.toJson(new BankBindCardMessage(bankBaseDto.getLoginName(),
                        bankBaseDto.getMobile(),
                        bankBaseDto.getBankUserName(),
                        bankBaseDto.getBankAccountNo(),
                        dto.getOrderNo(),
                        dto.getOrderDate())));
        redisTemplate.expire(MessageFormat.format(BANK_CANCEL_CARD_BIND_KEY, dto.getOrderDate()), 7, TimeUnit.DAYS);

        return dto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto callback(String responseData) {
        logger.info("[cancel card bind] data is {}", responseData);

        ResponseDto<CancelCardBindContentDto> responseDto = ApiType.CANCEL_CARD_BIND.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[cancel card bind] parse callback data error, data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        updateMapper.updateCancelCardBind(responseDto);

        CancelCardBindContentDto content = responseDto.getContent();
        if (responseDto.isSuccess() && responseDto.getContent().isSuccess()) {
            BankBindCardMessage bankBindCardMessage = gson.fromJson(redisTemplate.<String, String>opsForHash().get(MessageFormat.format(BANK_CANCEL_CARD_BIND_KEY, content.getOrderDate()), content.getOrderNo()), BankBindCardMessage.class);
            bankBindCardMessage.setBank(content.getBank());
            bankBindCardMessage.setBankCode(content.getBankCode());
            bankBindCardMessage.setCardNumber(content.getBankAccountNo());
            bankBindCardMessage.setStatus(true);
            bankBindCardMessage.setMessage(responseDto.getRetMsg());
            this.messageQueueClient.sendMessage(MessageQueue.UnbindBankCard_Success, bankBindCardMessage);
        }
        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.CANCEL_CARD_BIND.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<CancelCardBindContentDto> responseDto = (ResponseDto<CancelCardBindContentDto>) ApiType.CANCEL_CARD_BIND.getParser().parse(responseData);

        return responseDto.isSuccess() && responseDto.getContent().isSuccess();
    }
}
