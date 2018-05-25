package com.tuotiansudai.fudian.service;

import com.google.common.base.Strings;
import com.tuotiansudai.fudian.config.ApiType;
import com.tuotiansudai.fudian.dto.request.PhoneUpdateRequestDto;
import com.tuotiansudai.fudian.dto.request.Source;
import com.tuotiansudai.fudian.dto.response.PhoneUpdateContentDto;
import com.tuotiansudai.fudian.dto.response.ResponseDto;
import com.tuotiansudai.fudian.mapper.InsertMapper;
import com.tuotiansudai.fudian.mapper.ReturnUpdateMapper;
import com.tuotiansudai.fudian.mapper.SelectResponseDataMapper;
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

    private final ReturnUpdateMapper returnUpdateMapper;

    private final SelectResponseDataMapper selectResponseDataMapper;

    @Autowired
    public PhoneUpdateService(SignatureHelper signatureHelper, InsertMapper insertMapper, UpdateMapper updateMapper, ReturnUpdateMapper returnUpdateMapper, SelectResponseDataMapper selectResponseDataMapper) {
        this.signatureHelper = signatureHelper;
        this.insertMapper = insertMapper;
        this.updateMapper = updateMapper;
        this.returnUpdateMapper = returnUpdateMapper;
        this.selectResponseDataMapper = selectResponseDataMapper;
    }

    public PhoneUpdateRequestDto update(Source source, String loginName, String mobile, String userName, String accountNo, String newPhone) {
        PhoneUpdateRequestDto dto = new PhoneUpdateRequestDto(source, loginName, mobile, userName, accountNo, newPhone, null);
        signatureHelper.sign(dto);

        if (Strings.isNullOrEmpty(dto.getRequestData())) {
            logger.error("[phone update] sign error, userName: {}, accountNo: {}, newPhone: {}, type: {}", userName, accountNo, newPhone);
            return null;
        }

        insertMapper.insertPhoneUpdate(dto);
        return dto;
    }

    @Override
    public void returnCallback(ResponseDto responseData) {
        returnUpdateMapper.updatePhoneUpdate(responseData);
    }

    @Override
    public ResponseDto notifyCallback(String responseData) {
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

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean isSuccess(String orderNo) {
        String responseData = this.selectResponseDataMapper.selectResponseData(ApiType.PHONE_UPDATE.name().toLowerCase(), orderNo);
        if (Strings.isNullOrEmpty(responseData)) {
            return null;
        }

        ResponseDto<PhoneUpdateContentDto> responseDto = (ResponseDto<PhoneUpdateContentDto>) ApiType.PHONE_UPDATE.getParser().parse(responseData);

        return responseDto.isSuccess();
    }
}
