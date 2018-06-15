package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.BankBaseDto;
import com.tuotiansudai.fudian.dto.request.PasswordResetRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.PasswordResetContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.SelectMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(PasswordResetService.class);

    private static final ApiType API_TYPE = ApiType.PASSWORD_RESET;

    private final SignatureHelper signatureHelper;

    private final SelectMapper selectMapper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;


    @Autowired
    public PasswordResetService(SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, SelectMapper selectMapper) {
        this.signatureHelper = signatureHelper;
        this.selectMapper = selectMapper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
    }

    public PasswordResetRequestDto reset(Source source, BankBaseDto params) {
        PasswordResetRequestDto dto = new PasswordResetRequestDto(source, params.getLoginName(), params.getMobile(), params.getBankUserName(), params.getBankAccountNo());

        signatureHelper.sign(API_TYPE, dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[Password Reset] sign error, data: {}", params);
            return null;
        }

        insertMapper.insertPasswordReset(dto);
        return dto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        updateMapper.updateReturnResponse(API_TYPE.name().toLowerCase(), responseData);
    }

    @Override
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[Password Reset] callback data is {}", responseData);

        ResponseDto responseDto = API_TYPE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[Password Reset] parse callback data error, data is {}", responseData);
            return null;
        }

        if (!responseDto.isSuccess()) {
            logger.error("[Password Reset] callback is failure, orderNo: {}, message {}", responseDto.getContent().getOrderNo(), responseDto.getRetMsg());
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

        ResponseDto<PasswordResetContentDto> responseDto = (ResponseDto<PasswordResetContentDto>) API_TYPE.getParser().parse(responseData);

        return responseDto.isSuccess();
    }

}
