package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.GsonBuilder;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankRegisterDto;
import com.tuotiansudai.fudian.dto.ExtMarkDto;
import com.tuotiansudai.fudian.dto.request.RegisterRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.RegisterContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.ReturnUpdateMapper;
import com.tuotiansudai.fudian.mapper.SelectResponseDataMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.fudian.util.MessageQueueClient;
import com.tuotiansudai.mq.client.model.MessageTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegisterService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(RegisterService.class);

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final ReturnUpdateMapper returnUpdateMapper;

    private final MessageQueueClient messageQueueClient;

    private final SelectResponseDataMapper selectResponseDataMapper;

    @Autowired
    public RegisterService(SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, ReturnUpdateMapper returnUpdateMapper, MessageQueueClient messageQueueClient, SelectResponseDataMapper selectResponseDataMapper) {
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.returnUpdateMapper = returnUpdateMapper;
        this.messageQueueClient = messageQueueClient;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    public RegisterRequestDto register(Source source, BankRegisterDto bankRegisterDto) {
        RegisterRequestDto dto = new RegisterRequestDto(source, bankRegisterDto.getLoginName(), bankRegisterDto.getMobile(), bankRegisterDto.getRealName(), bankRegisterDto.getIdentityCode());
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[register] sign error, data{}", bankRegisterDto);
            return null;
        }

        insertMapper.insertRegister(dto);
        return dto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public void returnCallback(ResponseDto responseData) {
        returnUpdateMapper.updateRegister(responseData);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[register callback] data is {}", responseData);

        ResponseDto<RegisterContentDto> responseDto = ApiType.REGISTER.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[register callback] parse callback data error, data is {}", responseData);
            return null;
        }

        if (responseDto.isSuccess()) {
            RegisterContentDto registerContentDto = responseDto.getContent();
            ExtMarkDto extMarkDto = new GsonBuilder().create().fromJson(registerContentDto.getExtMark(), ExtMarkDto.class);
            this.messageQueueClient.publishMessage(MessageTopic.RegisterBankAccount, Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("loginName", extMarkDto.getLoginName())
                    .put("mobile", registerContentDto.getMobilePhone())
                    .put("identityCode", registerContentDto.getIdentityCode())
                    .put("realName", registerContentDto.getRealName())
                    .put("accountNo", registerContentDto.getAccountNo())
                    .put("userName", registerContentDto.getUserName())
                    .put("orderDate", registerContentDto.getOrderDate())
                    .put("orderNo", registerContentDto.getOrderNo())
                    .build()));
        }

        responseDto.setReqData(responseData);
        updateMapper.updateRegister(responseDto);
        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.REGISTER.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<RegisterContentDto> responseDto = (ResponseDto<RegisterContentDto>) ApiType.REGISTER.getParser().parse(responseData);

        return responseDto.isSuccess();
    }
}
