package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankRegisterDto;
import com.tuotiansudai.fudian.dto.ExtMarkDto;
import com.tuotiansudai.fudian.dto.request.RegisterRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.RegisterContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.message.BankRegisterMessage;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(RegisterService.class);

    private static final ApiType API_TYPE = ApiType.REGISTER;

    private final MessageQueueClient messageQueueClient;

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectMapper selectMapper;

    @Autowired
    public RegisterService(MessageQueueClient messageQueueClient, SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, SelectMapper selectMapper) {
        this.messageQueueClient = messageQueueClient;
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectMapper = selectMapper;
    }

    public RegisterRequestDto register(Source source, BankRegisterDto bankRegisterDto) {
        RegisterRequestDto dto = new RegisterRequestDto(source, bankRegisterDto.getLoginName(), bankRegisterDto.getMobile(), bankRegisterDto.getRealName(), bankRegisterDto.getIdentityCode());
        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[register] failed to sign, data{}", bankRegisterDto);
            return null;
        }

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
            ExtMarkDto extMarkDto = new GsonBuilder().create().fromJson(registerContentDto.getExtMark(), ExtMarkDto.class);
            this.messageQueueClient.publishMessage(MessageTopic.RegisterBankAccount,
                    new BankRegisterMessage(
                            extMarkDto.getLoginName(),
                            extMarkDto.getMobile(),
                            registerContentDto.getIdentityCode(),
                            registerContentDto.getRealName(),
                            registerContentDto.getAccountNo(),
                            registerContentDto.getUserName(),
                            registerContentDto.getOrderDate(),
                            registerContentDto.getOrderNo()));
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
