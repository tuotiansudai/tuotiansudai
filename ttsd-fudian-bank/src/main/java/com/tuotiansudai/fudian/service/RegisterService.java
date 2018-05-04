package com.tuotiansudai.fudian.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.tuotiansudai.client.MQWrapperClient;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.RegisterRequestDto;
import com.tuotiansudai.fudian.dto.response.RegisterContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import com.tuotiansudai.mq.client.model.MessageQueue;
import com.tuotiansudai.mq.client.model.MessageTopic;
import com.tuotiansudai.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(RegisterService.class);

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final MQWrapperClient mqWrapperClient;

    @Autowired
    public RegisterService(SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, MQWrapperClient mqWrapperClient) {
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.mqWrapperClient = mqWrapperClient;
    }

    public RegisterRequestDto register(String realName, String identityCode, String mobilePhone) {
        RegisterRequestDto dto = new RegisterRequestDto(realName, identityCode, mobilePhone);
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[register] sign error, realName: {}, identityCode: {}, mobilePhone: {}", realName, identityCode, mobilePhone);
            return null;
        }

        insertMapper.insertRegister(dto);
        return dto;
    }

    @Override
    public ResponseDto callback(String responseData) {
        logger.info("[register callback] data is {}", responseData);

        ResponseDto responseDto = ApiType.REGISTER.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[register callback] parse callback data error, data is {}", responseData);
            return null;
        }

        if (responseDto.isSuccess()){
            RegisterContentDto registerContentDto = (RegisterContentDto) responseDto.getContent();
            HashMap map = Maps.newHashMap(ImmutableMap.<String, String>builder()
                    .put("mobilePhone", registerContentDto.getMobilePhone())
                    .put("identityCode", registerContentDto.getIdentityCode())
                    .put("realName", registerContentDto.getRealName())
                    .put("accountNo", registerContentDto.getAccountNo())
                    .put("userName", registerContentDto.getUserName())
                    .put("orderDate", registerContentDto.getRegDate())
                    .put("orderNo", registerContentDto.getOrderNo())
                    .build());
            try {
                mqWrapperClient.publishMessage(MessageTopic.CertificationSuccess, new Gson().toJson(map));
            } catch (JsonProcessingException e) {
                logger.error("[MQ] CertificationSuccess file JsonData:{}, error:{}", new Gson().toJson(map), e);
            }
        }

        responseDto.setReqData(responseData);
        updateMapper.updateRegister(responseDto);
        return responseDto;
    }
}
