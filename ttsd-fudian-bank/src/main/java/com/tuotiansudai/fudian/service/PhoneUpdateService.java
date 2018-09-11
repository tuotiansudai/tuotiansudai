package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankChangeMobileDto;
import com.tuotiansudai.fudian.dto.request.PhoneUpdateRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.LoanFullContentDto;
import com.tuotiansudai.fudian.dto.response.PhoneUpdateContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.fudian.InsertMapper;
import com.tuotiansudai.fudian.mapper.fudian.SelectMapper;
import com.tuotiansudai.fudian.mapper.fudian.UpdateMapper;
import com.tuotiansudai.fudian.message.BankAuthorizationMessage;
import com.tuotiansudai.fudian.message.BankChangeMobileMessage;
import com.tuotiansudai.fudian.message.BankLoanFullMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

@Service
public class PhoneUpdateService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(PhoneUpdateService.class);

    private static final ApiType API_TYPE = ApiType.PHONE_UPDATE;

    private final SignatureHelper signatureHelper;

    private final SelectMapper selectMapper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private static final String BANK_CHANGE_MOBILE_MESSAGE_KEY = "BANK_CHANGE_MOBILE_MESSAGE_{0}";
    @Autowired
    private  RedisTemplate<String, String> redisTemplate;
    @Autowired
    private  MessageQueueClient messageQueueClient;

    @Autowired
    public PhoneUpdateService(SignatureHelper signatureHelper, SelectMapper selectMapper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectMapper = selectMapper;
    }

    public PhoneUpdateRequestDto update(Source source, BankChangeMobileDto params) {
        PhoneUpdateRequestDto dto = new PhoneUpdateRequestDto(source, params.getLoginName(), params.getMobile(), params.getBankUserName(), params.getBankAccountNo(), params.getNewPhone(), null, params.getType());
        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[Phone Update] sign error, userName: {}, accountNo: {}, newPhone: {}, type: {}", params.getBankUserName()
                    , params.getBankAccountNo(), params.getNewPhone());
            return null;
        }

        insertMapper.insertPhoneUpdate(dto);

        BankChangeMobileMessage bankChangeMobileMessage = new BankChangeMobileMessage(
                params.getLoginName(),params.getBankAccountNo(),params.getBankUserName(), dto.getNewPhone());

        String bankChangeMobileMessageKey = MessageFormat.format(BANK_CHANGE_MOBILE_MESSAGE_KEY, dto.getOrderDate());
        redisTemplate.<String, String>opsForHash().put(bankChangeMobileMessageKey,dto.getOrderNo() , gson.toJson(bankChangeMobileMessage));
        redisTemplate.expire(bankChangeMobileMessageKey, 7, TimeUnit.DAYS);
        return dto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        updateMapper.updateReturnResponse(ApiType.PHONE_UPDATE.name().toLowerCase(), responseData);
    }

    @Override
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[Phone Update] callback data is {}", responseData);

        ResponseDto responseDto = ApiType.PHONE_UPDATE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[Phone Update] parse callback data error, data is {}", responseData);
            return null;
        }

        if (!responseDto.isSuccess()) {
            logger.error("[Phone Update] callback is failure, orderNo: {}, message {}", responseDto.getContent().getOrderNo(), responseDto.getRetMsg());
        }

        responseDto.setReqData(responseData);
        int count=updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);
        PhoneUpdateContentDto content= ((ResponseDto<PhoneUpdateContentDto>) responseDto).getContent();
        if (count > 0 && responseDto.isSuccess() && "1".equals(content.getStatus())) {
            BankChangeMobileMessage bankChangeMobileMessage = gson.fromJson(redisTemplate.<String, String>opsForHash().get(MessageFormat.format(BANK_CHANGE_MOBILE_MESSAGE_KEY, content.getOrderDate()), content.getOrderNo()), BankChangeMobileMessage.class);
            bankChangeMobileMessage.setStatus(true);
            bankChangeMobileMessage.setMessage(responseDto.getRetMsg());
            messageQueueClient.sendMessage(MessageQueue.BankChangeMobile,bankChangeMobileMessage);
            logger.info("[bank change-mobile] change-mobile, send message: {}", bankChangeMobileMessage);
        }
        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectMapper.selectNotifyResponseData(ApiType.PHONE_UPDATE.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<PhoneUpdateContentDto> responseDto = (ResponseDto<PhoneUpdateContentDto>) ApiType.PHONE_UPDATE.getParser().parse(responseData);

        return responseDto.isSuccess();
    }
}
