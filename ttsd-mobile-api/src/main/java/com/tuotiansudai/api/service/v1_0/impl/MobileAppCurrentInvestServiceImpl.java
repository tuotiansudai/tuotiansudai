package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppCurrentInvestService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.current.client.CurrentRestClient;
import com.tuotiansudai.current.dto.DepositRequestDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

public class MobileAppCurrentInvestServiceImpl implements MobileAppCurrentInvestService {

    static Logger logger = Logger.getLogger(MobileAppInvestServiceImpl.class);

    @Autowired
    private CurrentRestClient currentRestClient;

    @Override
    public BaseResponseDto<InvestResponseDataDto> invest(CurrentInvestRequestDto investRequestDto, String loginName) {
        BaseResponseDto<InvestResponseDataDto> responseDto = new BaseResponseDto<>();
        DepositRequestDto depositRequestDto = convertInvestDto(investRequestDto);
        try {
            BaseDto<PayFormDataDto> formDto = currentRestClient.invest(depositRequestDto, loginName);

            if (!formDto.isSuccess()) {
                logger.error(MessageFormat.format("[MobileAppCurrentInvestServiceImpl][invest] current invest failed!Maybe service cannot connect to payWrapper. " +
                        "investDto:loginName:{0}, amount:{1}, source:{2}", loginName, depositRequestDto.getAmount(), depositRequestDto.getSource()));
                responseDto.setCode(ReturnMessage.NO_MATCHING_OBJECTS_EXCEPTION.getCode());
                responseDto.setMessage(ReturnMessage.NO_MATCHING_OBJECTS_EXCEPTION.getMsg());
                return responseDto;
            }

            if (formDto.getData().getStatus()) {
                PayFormDataDto formDataDto = formDto.getData();
                String requestData = CommonUtils.mapToFormData(formDataDto.getFields());

                InvestResponseDataDto investResponseDataDto = new InvestResponseDataDto();
                investResponseDataDto.setRequestData(requestData);
                investResponseDataDto.setUrl(formDataDto.getUrl());
                responseDto.setCode(ReturnMessage.SUCCESS.getCode());
                responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
                responseDto.setData(investResponseDataDto);
            } else {
                responseDto.setCode(ReturnMessage.INVEST_FAILED.getCode());
                responseDto.setMessage(ReturnMessage.INVEST_FAILED.getMsg() + ":" + formDto.getData().getMessage());
            }
        } catch (RestException | UnsupportedEncodingException e) {
            logger.error("current invest failed", e);
            responseDto.setCode(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode());
            responseDto.setMessage(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg());
        }
        return responseDto;
    }

    private DepositRequestDto convertInvestDto(CurrentInvestRequestDto investRequestDto) {
        Source source = Source.valueOf(investRequestDto.getBaseParam().getPlatform());
        long amount = AmountConverter.convertStringToCent(investRequestDto.getAmount());
        return new DepositRequestDto(amount, source);
    }

    @Override
    public BaseResponseDto<InvestNoPassResponseDataDto> noPasswordInvest(CurrentInvestRequestDto investRequestDto, String loginName) {
        return null;
    }
}
