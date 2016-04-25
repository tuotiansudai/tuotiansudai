package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppChannelService;
import com.tuotiansudai.api.service.MobileAppInvestService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.InvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Locale;

@Service
public class MobileAppInvestServiceImpl implements MobileAppInvestService {

    @Autowired
    private InvestService investService;

    @Autowired
    private MobileAppChannelService mobileAppChannelService;

    @Value("${pay.callback.app.web.host}")
    private String domainName;

    @Override
    public BaseResponseDto noPasswordInvest(InvestRequestDto investRequestDto) {
        BaseResponseDto<InvestNoPassResponseDataDto> responseDto = new BaseResponseDto<>();
        InvestDto investDto = convertInvestDto(investRequestDto);
        try {
            BaseDto<PayDataDto> baseDto = investService.noPasswordInvest(investDto);
            if (!baseDto.getData().getStatus()) {
                responseDto.setCode(ReturnMessage.INVEST_FAILED.getCode());
                responseDto.setMessage(ReturnMessage.INVEST_FAILED.getMsg() + ":" + baseDto.getData().getMessage());

            }else{
                responseDto.setCode(ReturnMessage.SUCCESS.getCode());
                responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
            }

            responseDto.setData(new InvestNoPassResponseDataDto(MessageFormat.format("{0}/callback/project_transfer_invest?ret_code={1}&message={2}", domainName, responseDto.getCode(),responseDto.getMessage())));

        } catch (InvestException e) {
            return this.convertExceptionToDto(e);
        }
        return responseDto;
    }

    @Override
    public BaseResponseDto invest(InvestRequestDto investRequestDto) {
        BaseResponseDto<InvestResponseDataDto> responseDto = new BaseResponseDto<>();
        InvestDto investDto = convertInvestDto(investRequestDto);
        try {
            BaseDto<PayFormDataDto> formDto = investService.invest(investDto);
            if (formDto.getData().getStatus()) {
                PayFormDataDto formDataDto = formDto.getData();
                String requestData = CommonUtils.mapToFormData(formDataDto.getFields(), true);

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
        } catch (InvestException e) {
            responseDto = convertExceptionToDto(e);
        } catch (UnsupportedEncodingException e) {
            responseDto.setCode(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode());
            responseDto.setMessage(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg());
        }
        return responseDto;
    }

    private InvestDto convertInvestDto(InvestRequestDto investRequestDto) {
        Source source = Source.valueOf(investRequestDto.getBaseParam().getPlatform().toUpperCase(Locale.ENGLISH));
        InvestDto investDto = new InvestDto();
        investDto.setSource(source);
        investDto.setAmount(investRequestDto.getInvestMoney());
        investDto.setLoanId(investRequestDto.getLoanId());
        investDto.setLoginName(investRequestDto.getUserId());
        investDto.setChannel(mobileAppChannelService.obtainChannelBySource(investRequestDto.getBaseParam()));
        investDto.setUserCouponIds(investRequestDto.getUserCouponIds());
        return investDto;
    }

    private BaseResponseDto<InvestResponseDataDto> convertExceptionToDto(InvestException e) {
        BaseResponseDto<InvestResponseDataDto> baseResponseDto = new BaseResponseDto<>();
        switch (e.getType()) {
            case EXCEED_MONEY_NEED_RAISED:
                baseResponseDto.setCode(ReturnMessage.EXCEED_MONEY_NEED_RAISED.getCode());
                baseResponseDto.setMessage(ReturnMessage.EXCEED_MONEY_NEED_RAISED.getMsg());
                break;
            case ILLEGAL_INVEST_AMOUNT:
                baseResponseDto.setCode(ReturnMessage.ILLEGAL_INVEST_AMOUNT.getCode());
                baseResponseDto.setMessage(ReturnMessage.ILLEGAL_INVEST_AMOUNT.getMsg());
                break;
            case LESS_THAN_MIN_INVEST_AMOUNT:
                baseResponseDto.setCode(ReturnMessage.LESS_THAN_MIN_INVEST_AMOUNT.getCode());
                baseResponseDto.setMessage(ReturnMessage.LESS_THAN_MIN_INVEST_AMOUNT.getMsg());
                break;
            case MORE_THAN_MAX_INVEST_AMOUNT:
                baseResponseDto.setCode(ReturnMessage.MORE_THAN_MAX_INVEST_AMOUNT.getCode());
                baseResponseDto.setMessage(ReturnMessage.MORE_THAN_MAX_INVEST_AMOUNT.getMsg());
                break;
            case LOAN_IS_FULL:
                baseResponseDto.setCode(ReturnMessage.LOAN_IS_FULL.getCode());
                baseResponseDto.setMessage(ReturnMessage.LOAN_IS_FULL.getMsg());
                break;
            case OUT_OF_NOVICE_INVEST_LIMIT:
                baseResponseDto.setCode(ReturnMessage.OUT_OF_NOVICE_INVEST_LIMIT.getCode());
                baseResponseDto.setMessage(ReturnMessage.OUT_OF_NOVICE_INVEST_LIMIT.getMsg());
                break;
            case ILLEGAL_LOAN_STATUS:
                baseResponseDto.setCode(ReturnMessage.ILLEGAL_LOAN_STATUS.getCode());
                baseResponseDto.setMessage(ReturnMessage.ILLEGAL_LOAN_STATUS.getMsg());
            case NOT_ENOUGH_BALANCE:
                baseResponseDto.setCode(ReturnMessage.NOT_ENOUGH_BALANCE.getCode());
                baseResponseDto.setMessage(ReturnMessage.NOT_ENOUGH_BALANCE.getMsg());
            case PASSWORD_INVEST_OFF:
                baseResponseDto.setCode(ReturnMessage.PASSWORD_INVEST_OFF.getCode());
                baseResponseDto.setMessage(ReturnMessage.PASSWORD_INVEST_OFF.getMsg());
            case LOAN_NOT_FOUND:
                baseResponseDto.setCode(ReturnMessage.LOAN_NOT_FOUND.getCode());
                baseResponseDto.setMessage(ReturnMessage.LOAN_NOT_FOUND.getMsg());
        }
        return baseResponseDto;
    }
}
