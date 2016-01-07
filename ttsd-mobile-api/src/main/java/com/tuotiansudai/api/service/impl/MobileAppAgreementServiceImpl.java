package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.AgreementOperateRequestDto;
import com.tuotiansudai.api.dto.AgreementOperateResponseDataDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppAgreementService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MobileAppAgreementServiceImpl implements MobileAppAgreementService{

    static Logger logger = Logger.getLogger(MobileAppAgreementServiceImpl.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Override
    public BaseResponseDto generateAgreementRequest(AgreementOperateRequestDto requestDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        requestDto.setAutoInvest(true);
        requestDto.setFastPay(false);
        AgreementDto agreementDto = requestDto.convertToAgreementDto();

        BaseDto<PayFormDataDto> formDto = payWrapperClient.agreement(agreementDto);
        AgreementOperateResponseDataDto responseDataDto = new AgreementOperateResponseDataDto();
        try {
            if (formDto.isSuccess()) {
                responseDataDto.setUrl(formDto.getData().getUrl());
                responseDataDto.setRequestData(CommonUtils.mapToFormData(formDto.getData().getFields(), true));
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getLocalizedMessage(), e);
            return new BaseResponseDto(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode(), ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg());
        }
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseResponseDto.setData(responseDataDto);
        return baseResponseDto;
    }

}
