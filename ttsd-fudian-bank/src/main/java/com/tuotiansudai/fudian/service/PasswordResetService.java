package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.PasswordResetRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.PasswordResetContentDto;
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
public class PasswordResetService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(PasswordResetService.class);

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    @Autowired
    public PasswordResetService(SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    public PasswordResetRequestDto reset(Source source, String loginName, String mobile, String userName, String accountNo) {
        PasswordResetRequestDto dto = new PasswordResetRequestDto(source, loginName, mobile, userName, accountNo);

        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[password reset] sign error, userName: {}, accountNo: {}", userName, accountNo);
            return null;
        }

        insertMapper.insertPasswordReset(dto);
        return dto;
    }

    @Override
    public ResponseDto callback(String responseData) {
        logger.info("[password reset callback] data is {}", responseData);

        ResponseDto responseDto = ApiType.PASSWORD_RESET.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[password reset callback] parse callback data error, data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        updateMapper.updatePasswordReset(responseDto);
        return responseDto;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.PASSWORD_RESET.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<PasswordResetContentDto> responseDto = (ResponseDto<PasswordResetContentDto>) ApiType.PASSWORD_RESET.getParser().parse(responseData);

        return responseDto.isSuccess();
    }

}
