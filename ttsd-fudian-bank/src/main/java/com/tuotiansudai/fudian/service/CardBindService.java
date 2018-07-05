package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankBaseDto;
import com.tuotiansudai.fudian.dto.request.BankUserRole;
import com.tuotiansudai.fudian.dto.request.CardBindRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.CardBindContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankBindCardMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
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
public class CardBindService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(CardBindService.class);

    private static final ApiType API_TYPE = ApiType.CARD_BIND;

    private final static String BANK_CARD_BIND_MESSAGE_KEY = "BANK_CARD_BIND_MESSAGE_{0}";

    private final RedisTemplate<String, String> redisTemplate;

    private final MessageQueueClient messageQueueClient;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectMapper selectMapper;

    @Autowired
    public CardBindService(RedisTemplate<String, String> redisTemplate, MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, SelectMapper selectMapper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.redisTemplate = redisTemplate;
        this.messageQueueClient = messageQueueClient;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectMapper = selectMapper;
    }

    public CardBindRequestDto bind(Source source, BankBaseDto bankBaseDto, BankUserRole bankUserRole) {
        CardBindRequestDto dto = new CardBindRequestDto(source, bankBaseDto);
        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[Card Bind] failed to sign, data: {}", bankBaseDto);
            return null;
        }

        insertMapper.insertCardBind(dto);

        BankBindCardMessage bankBindCardMessage = new BankBindCardMessage(bankBaseDto.getLoginName(),
                bankBaseDto.getMobile(),
                bankBaseDto.getBankUserName(),
                bankBaseDto.getBankAccountNo(),
                dto.getOrderNo(),
                dto.getOrderDate(),
                bankUserRole == BankUserRole.INVESTOR);

        String bankCardBindMessageKey = MessageFormat.format(BANK_CARD_BIND_MESSAGE_KEY, dto.getOrderDate());
        redisTemplate.<String, String>opsForHash().put(bankCardBindMessageKey, dto.getOrderNo(), gson.toJson(bankBindCardMessage));
        redisTemplate.expire(bankCardBindMessageKey, 7, TimeUnit.DAYS);
        return dto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        updateMapper.updateReturnResponse(API_TYPE.name().toLowerCase(), responseData);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[Card Bind] callback data is {}", responseData);

        ResponseDto<CardBindContentDto> responseDto = API_TYPE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[Card Bind] failed to parse callback data: {}", responseData);
            return null;
        }

        if (!responseDto.isSuccess()) {
            logger.error("[Card Bind] callback is failure, orderNo: {}, message {}", responseDto.getContent().getOrderNo(), responseDto.getRetMsg());
        }

        responseDto.setReqData(responseData);
        updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);

        CardBindContentDto content = responseDto.getContent();

        if (responseDto.isSuccess() && content.isSuccess()) {
            String bankCardBindMessageKey = MessageFormat.format(BANK_CARD_BIND_MESSAGE_KEY, content.getOrderDate());
            BankBindCardMessage bankBindCardMessage = gson.fromJson(redisTemplate.<String, String>opsForHash().get(bankCardBindMessageKey, content.getOrderNo()), BankBindCardMessage.class);
            bankBindCardMessage.setBank(content.getBank());
            bankBindCardMessage.setBankCode(content.getBankCode());
            bankBindCardMessage.setCardNumber(content.getBankAccountNo());
            bankBindCardMessage.setStatus(true);
            bankBindCardMessage.setMessage(responseDto.getRetMsg());
            this.messageQueueClient.publishMessage(MessageTopic.BindBankCard, bankBindCardMessage);
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

        ResponseDto<CardBindContentDto> responseDto = (ResponseDto<CardBindContentDto>) API_TYPE.getParser().parse(responseData);

        return responseDto.isSuccess() && responseDto.getContent().isSuccess();
    }
}
