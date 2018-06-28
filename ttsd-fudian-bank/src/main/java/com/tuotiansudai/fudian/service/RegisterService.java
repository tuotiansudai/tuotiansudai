package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankRegisterDto;
import com.tuotiansudai.fudian.dto.request.RegisterRequestDto;
import com.tuotiansudai.fudian.dto.request.RegisterRoleType;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.RegisterContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankRegisterMessage;
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
public class RegisterService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(RegisterService.class);

    private final static String BANK_REGISTER_MESSAGE_KEY = "BANK_REGISTER_MESSAGE_{0}";

    private static final ApiType API_TYPE = ApiType.REGISTER;

    private final RedisTemplate<String, String> redisTemplate;

    private final MessageQueueClient messageQueueClient;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectMapper selectMapper;

    @Autowired
    public RegisterService(RedisTemplate<String, String> redisTemplate, MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, SelectMapper selectMapper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.redisTemplate = redisTemplate;
        this.messageQueueClient = messageQueueClient;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectMapper = selectMapper;
    }

    public RegisterRequestDto register(Source source, RegisterRoleType registerRoleType, BankRegisterDto bankRegisterDto) {
        RegisterRequestDto dto = new RegisterRequestDto(source, bankRegisterDto.getLoginName(), bankRegisterDto.getMobile(), bankRegisterDto.getRealName(), registerRoleType.getCode(), bankRegisterDto.getIdentityCode());
        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[register] failed to sign, data{}", bankRegisterDto);
            return null;
        }

        BankRegisterMessage bankRegisterMessage = new BankRegisterMessage(
                bankRegisterDto.getLoginName(),
                bankRegisterDto.getMobile(),
                bankRegisterDto.getIdentityCode(),
                bankRegisterDto.getRealName(),
                bankRegisterDto.getToken(),
                null, null,
                dto.getOrderNo(), dto.getOrderDate()
        );

        String bankRegisterMessageKey = MessageFormat.format(BANK_REGISTER_MESSAGE_KEY, dto.getOrderDate());
        redisTemplate.<String, String>opsForHash().put(bankRegisterMessageKey, dto.getOrderNo(), gson.toJson(bankRegisterMessage));
        redisTemplate.expire(bankRegisterMessageKey, 7, TimeUnit.DAYS);

        insertMapper.insertRegister(dto);
        return dto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void returnCallback(ResponseDto responseData) {
        updateMapper.updateReturnResponse(API_TYPE.name().toLowerCase(), responseData);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[register callback] data is {}", responseData);

        ResponseDto<RegisterContentDto> responseDto = API_TYPE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[register callback] parse callback data error, data is {}", responseData);
            return null;
        }

        if (responseDto.isSuccess()) {
            RegisterContentDto registerContentDto = responseDto.getContent();
            String bankRegisterMessageKey = MessageFormat.format(BANK_REGISTER_MESSAGE_KEY, registerContentDto.getOrderDate());
            String message = redisTemplate.<String, String>opsForHash().get(bankRegisterMessageKey, registerContentDto.getOrderNo());
            BankRegisterMessage bankRegisterMessage = gson.fromJson(message, BankRegisterMessage.class);
            bankRegisterMessage.setBankAccountNo(registerContentDto.getAccountNo());
            bankRegisterMessage.setBankUserName(registerContentDto.getUserName());
            this.messageQueueClient.sendMessage(MessageQueue.RegisterBankAccount_Success, bankRegisterMessage);
        }

        responseDto.setReqData(responseData);
        updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);
        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectMapper.selectNotifyResponseData(API_TYPE.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<RegisterContentDto> responseDto = (ResponseDto<RegisterContentDto>) API_TYPE.getParser().parse(responseData);

        return responseDto.isSuccess();
    }
}
