package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.AgreementOperateRequestDto;
import com.tuotiansudai.api.dto.v1_0.AgreementOperateResponseDataDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppAgreementService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class MobileAppAgreementServiceImpl implements MobileAppAgreementService{

    static Logger logger = Logger.getLogger(MobileAppAgreementServiceImpl.class);

    @Autowired
    private PayWrapperClient payWrapperClient;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public BaseResponseDto generateAgreementRequest(AgreementOperateRequestDto requestDto) {
        AgreementOperateResponseDataDto responseDataDto = new AgreementOperateResponseDataDto();
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        AgreementDto agreementDto = requestDto.convertToAgreementDto();
        AccountModel accountModel = accountMapper.findByLoginName(agreementDto.getLoginName());

        if (accountModel != null && accountModel.isAutoInvest()) {
            baseResponseDto.setCode(ReturnMessage.AUTO_INVEST.getCode());
            baseResponseDto.setMessage(ReturnMessage.AUTO_INVEST.getMsg());
            baseResponseDto.setData(responseDataDto);
            return baseResponseDto;
        }

        BaseDto<PayFormDataDto> formDto = payWrapperClient.agreement(agreementDto);
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
