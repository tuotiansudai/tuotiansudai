package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.AuthorizationRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
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
public class AuthorizationService implements ReturnCallbackInterface, NotifyCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

    private static final ApiType API_TYPE = ApiType.AUTHORIZATION;

    private final SignatureHelper signatureHelper;

    private final SelectMapper selectMapper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    @Autowired
    public AuthorizationService(SignatureHelper signatureHelper, SelectMapper selectMapper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.signatureHelper = signatureHelper;
        this.selectMapper = selectMapper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
    }

    public AuthorizationRequestDto auth(Source source, String loginName, String mobile, String userName, String accountNo) {
        AuthorizationRequestDto dto = new AuthorizationRequestDto(source, loginName, mobile, userName, accountNo, null);

        signatureHelper.sign(API_TYPE, dto);
        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[authorization] sign error, userName: {}, accountNo: {}", userName, accountNo);

            return null;
        }

        insertMapper.insertAuthorization(dto);
        return dto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        updateMapper.updateReturnResponse(API_TYPE.name().toLowerCase(), responseData);
    }

    @Override
    public ResponseDto notifyCallback(String responseData) {
        logger.info("[authorization] callback data is {}", responseData);

        ResponseDto responseDto = API_TYPE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[authorization] parse callback data error, data is {}", responseData);
            return null;
        }

        if (!responseDto.isSuccess()) {
            logger.error("[authorization] callback is failure, orderNo: {}, message {}", responseDto.getContent().getOrderNo(), responseDto.getRetMsg());
        }

        updateMapper.updateNotifyResponseData(API_TYPE.name().toLowerCase(), responseDto);
        responseDto.setReqData(responseData);
        return responseDto;
    }

    @Override
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectMapper.selectNotifyResponseData(API_TYPE.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto responseDto = API_TYPE.getParser().parse(responseData);

        return responseDto.isSuccess();
    }
}
