package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.RegisterRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
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

    @Autowired
    public RegisterService(SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
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

        responseDto.setReqData(responseData);
        updateMapper.updateRegister(responseDto);
        return responseDto;
    }
}
