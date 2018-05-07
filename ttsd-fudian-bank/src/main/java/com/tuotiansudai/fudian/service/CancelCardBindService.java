package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.CancelCardBindRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CancelCardBindService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(CancelCardBindService.class);

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    @Autowired
    public CancelCardBindService(SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
    }

    public CancelCardBindRequestDto cancel(String userName, String accountNo, String loginName, String mobile) {
        CancelCardBindRequestDto dto = new CancelCardBindRequestDto(userName, accountNo, loginName, mobile);
        signatureHelper.sign(dto, ApiType.CANCEL_CARD_BIND);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[cancel card bind] sign error, userName: {}, accountNo: {}", userName, accountNo);

            return null;
        }

        insertMapper.insertCancelCardBind(dto);
        return dto;
    }

    @Override
    public ResponseDto callback(String responseData) {
        logger.info("[cancel card bind] data is {}", responseData);

        ResponseDto responseDto = ApiType.CANCEL_CARD_BIND.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[cancel card bind] parse callback data error, data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        updateMapper.updateCancelCardBind(responseDto);
        return responseDto;
    }
}
