package com.tuotiansudai.api.service.impl;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppBankCardService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.service.AgreementService;
import com.tuotiansudai.service.BindBankCardService;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MobileAppBankCardServiceImpl implements MobileAppBankCardService {

    static Logger log = Logger.getLogger(MobileAppLoanDetailServiceImpl.class);

    @Autowired
    private BindBankCardService bindBankCardService;

    @Autowired
    private AgreementService agreementService;

    @Override
    public BaseResponseDto bindBankCard(BankCardRequestDto requestDto) {
        BaseResponseDto baseDto = new BaseResponseDto();
        try {
            BindBankCardDto bindBankCardDto = requestDto.convertToBindBankCardDto();
            BaseDto<PayFormDataDto> requestFormData = bindBankCardService.bindBankCard(bindBankCardDto);
            if(requestFormData.isSuccess()) {
                PayFormDataDto formData = requestFormData.getData();

                BankCardResponseDto dataDto = new BankCardResponseDto();
                dataDto.setUrl(formData.getUrl());
                dataDto.setRequestData(CommonUtils.mapToFormData(formData.getFields(), true));

                baseDto.setCode(ReturnMessage.SUCCESS.getCode());
                baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
                baseDto.setData(dataDto);
                return baseDto;
            } else {
                log.error("mobile bind card fail, pay wrapper return fail");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("mobile bind card fail", e);
        }
        baseDto.setCode(ReturnMessage.BIND_CARD_FAIL.getCode());
        baseDto.setMessage(ReturnMessage.BIND_CARD_FAIL.getMsg());
        return baseDto;
    }

    @Override
    public BaseResponseDto openFastPay(BankCardRequestDto requestDto) {
        BaseResponseDto baseDto = new BaseResponseDto();
        try {
            AgreementDto agreementDto = requestDto.convertToAgreementDto();
            BaseDto<PayFormDataDto> requestFormData = agreementService.agreement(agreementDto);
            if(requestFormData.isSuccess()) {
                PayFormDataDto formData = requestFormData.getData();

                BankCardResponseDto dataDto = new BankCardResponseDto();
                dataDto.setUrl(formData.getUrl());
                dataDto.setRequestData(CommonUtils.mapToFormData(formData.getFields(), true));

                baseDto.setCode(ReturnMessage.SUCCESS.getCode());
                baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
                baseDto.setData(dataDto);
                return baseDto;
            } else {
                log.error("mobile bind card fail, pay wrapper return fail");
            }
        } catch (UnsupportedEncodingException e) {
            log.error("mobile bind card fail", e);
        }
        baseDto.setCode(ReturnMessage.BIND_CARD_FAIL.getCode());
        baseDto.setMessage(ReturnMessage.BIND_CARD_FAIL.getMsg());
        return baseDto;
    }

    @Override
    public boolean queryBindAndSginStatus(String userId, String operationType) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public BaseResponseDto generateBankCardResponse(BankCardReplaceRequestDto requestDto) {
        throw new NotImplementedException(getClass().getName());
    }

    @Override
    public void save(BankCardModel bankCard) {
        throw new NotImplementedException(getClass().getName());
    }
}
