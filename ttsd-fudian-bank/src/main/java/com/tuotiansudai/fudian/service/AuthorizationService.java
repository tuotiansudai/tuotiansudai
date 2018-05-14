package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.AuthorizationRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectResponseDataMapper;
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

    private final SelectResponseDataMapper selectResponseDataMapper;

    @Autowired
    public AuthorizationService(SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    public AuthorizationRequestDto auth(Source source, String loginName, String mobile, String userName, String accountNo) {
        AuthorizationRequestDto dto = new AuthorizationRequestDto(source, loginName, mobile, userName, accountNo);

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

    @Override
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.AUTHORIZATION.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto responseDto = ApiType.AUTHORIZATION.getParser().parse(responseData);

        return responseDto.isSuccess();
    }
}
