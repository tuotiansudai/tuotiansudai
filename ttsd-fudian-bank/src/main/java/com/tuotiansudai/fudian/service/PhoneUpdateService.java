package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.PhoneUpdateRequestDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.UpdateMapper;
import com.tuotiansudai.fudian.sign.SignatureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneUpdateService implements AsyncCallbackInterface {

    private static Logger logger = LoggerFactory.getLogger(PhoneUpdateService.class);

    private final SignatureHelper signatureHelper;

    private final InsertMapper insertMapper;

    private final UpdateMapper updateMapper;

    @Autowired
    public PhoneUpdateService(SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper) {
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
    }

    public PhoneUpdateRequestDto update(String userName, String accountNo, String newPhone, String type) {
        PhoneUpdateRequestDto dto = new PhoneUpdateRequestDto(userName, accountNo, newPhone, type);
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[phone update] sign error, userName: {}, accountNo: {}, newPhone: {}, type: {}", userName, accountNo, newPhone, type);
            return null;
        }

        insertMapper.insertPhoneUpdate(dto);
        return dto;
    }

    @Override
    public ResponseDto callback(String responseData) {
        logger.info("[phone update callback] data is {}", responseData);

        ResponseDto responseDto = ApiType.PHONE_UPDATE.getParser().parse(responseData);

        if (responseDto == null) {
            logger.error("[phone update callback] parse callback data error, data is {}", responseData);
            return null;
        }

        responseDto.setReqData(responseData);
        updateMapper.updatePhoneUpdate(responseDto);
        return responseDto;
    }
}
