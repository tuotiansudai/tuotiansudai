package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankBaseDto;
import com.tuotiansudai.fudian.dto.request.CancelCardBindRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.CancelCardBindContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
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
public class CancelCardBindService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(CancelCardBindService.class);

    private static final ApiType API_TYPE = ApiType.CANCEL_CARD_BIND;

    private final static String BANK_CANCEL_CARD_BIND_MESSAGE_KEY = "BANK_CANCEL_CARD_BIND_MESSAGE_{0}";

    private final RedisTemplate<String, String> redisTemplate;

    private final MessageQueueClient messageQueueClient;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectMapper selectMapper;

    @Autowired
    public CancelCardBindService(RedisTemplate<String, String> redisTemplate, MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, SelectMapper selectMapper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.redisTemplate = redisTemplate;
        this.messageQueueClient = messageQueueClient;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectMapper = selectMapper;
    }

    public CancelCardBindRequestDto cancel(Source source, BankBaseDto bankBaseDto, boolean isInvestor) {
        CancelCardBindRequestDto dto = new CancelCardBindRequestDto(source, bankBaseDto);

        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[Cancel Card Bind] failed to sign, data: {}", bankBaseDto);
            return null;
        }

        insertMapper.insertCancelCardBind(dto);

        BankBindCardMessage bankBindCardMessage = new BankBindCardMessage(bankBaseDto.getLoginName(),
                bankBaseDto.getMobile(),
                bankBaseDto.getBankUserName(),
                bankBaseDto.getBankAccountNo(),
                dto.getOrderNo(),
                dto.getOrderDate(),
                isInvestor);

        String bankCancelCardBindMessageKey = MessageFormat.format(BANK_CANCEL_CARD_BIND_MESSAGE_KEY, dto.getOrderDate());
        redisTemplate.<String, String>opsForHash().put(bankCancelCardBindMessageKey, dto.getOrderNo(), gson.toJson(bankBindCardMessage));
        redisTemplate.expire(bankCancelCardBindMessageKey, 7, TimeUnit.DAYS);

        return dto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        updateMapper.updateReturnResponse(API_TYPE.name().toLowerCase(), responseData);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[Cancel Card Bind] callback data is {}", responseData);

        ResponseDto<CancelCardBindContentDto> responseDto = API_TYPE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[Cancel Card Bind] failed to parse callback data: {}", responseData);
            return null;
        }

        if (!responseDto.isSuccess()) {
            logger.error("[Cancel Card Bind] callback is failure, orderNo: {}, message {}", responseDto.getContent().getOrderNo(), responseDto.getRetMsg());
        }

        responseDto.setReqData(responseData);
        updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);

        CancelCardBindContentDto content = responseDto.getContent();
        if (responseDto.isSuccess() && responseDto.getContent().isSuccess()) {
            String bankCancelCardBindMessageKey = MessageFormat.format(BANK_CANCEL_CARD_BIND_MESSAGE_KEY, content.getOrderDate());
            BankBindCardMessage bankBindCardMessage = gson.fromJson(redisTemplate.<String, String>opsForHash().get(bankCancelCardBindMessageKey, content.getOrderNo()), BankBindCardMessage.class);
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
        String responseData = this.selectMapper.selectNotifyResponseData(API_TYPE.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<CancelCardBindContentDto> responseDto = (ResponseDto<CancelCardBindContentDto>) API_TYPE.getParser().parse(responseData);

        return responseDto.isSuccess() && responseDto.getContent().isSuccess();
    }
}
