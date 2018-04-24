package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.AuthorizationRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    @Autowired
    public AuthorizationService(SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
    }

    public AuthorizationRequestDto auth(String userName, String accountNo) {
        AuthorizationRequestDto dto = new AuthorizationRequestDto(userName, accountNo);

        signatureHelper.sign(dto);
        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[authorization] sign error, userName: {}, accountNo: {}", userName, accountNo);

            return null;
        }

        insertMapper.insertAuthorization(dto);
        return dto;
    }

    @Override
    public ResponseDto callback(String responseData) {
        logger.info("[authorization] data is {}", responseData);

        ResponseDto responseDto = ApiType.AUTHORIZATION.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[authorization] parse callback data error, data is {}", responseData);
            return null;
        }

        updateMapper.updateAuthorization(responseDto);
        responseDto.setReqData(responseData);
        return responseDto;

    }
}
