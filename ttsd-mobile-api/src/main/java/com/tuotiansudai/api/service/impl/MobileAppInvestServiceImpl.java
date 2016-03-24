package com.tuotiansudai.api.service.impl;


import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.InvestRequestDto;
import com.tuotiansudai.api.dto.InvestResponseDataDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppChannelService;
import com.tuotiansudai.api.service.MobileAppInvestService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.InvestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

@Service
public class MobileAppInvestServiceImpl implements MobileAppInvestService {

    @Autowired
    private InvestService investService;

    @Autowired
    private MobileAppChannelService mobileAppChannelService;

    @Autowired
    private AccountService accountService;

    @Override
    public BaseResponseDto noPasswordInvest(InvestRequestDto investRequestDto) {
        BaseResponseDto responseDto = new BaseResponseDto();
        InvestDto investDto = convertInvestDto(investRequestDto);
        AccountModel accountModel = accountService.findByLoginName(investRequestDto.getUserId());
        if (accountModel.isAutoInvest() && accountModel.isNoPasswordInvest()) {
            try {
                BaseDto<PayDataDto> baseDto = investService.noPasswordInvest(investDto);
                if (baseDto.getData().getStatus()) {
                    responseDto.setCode(ReturnMessage.SUCCESS.getCode());
                    responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
                } else {
                    responseDto.setCode(ReturnMessage.INVEST_FAILED.getCode());
                    responseDto.setMessage(ReturnMessage.INVEST_FAILED.getMsg() + ":" + baseDto.getData().getMessage());
                }
            } catch (InvestException e) {
                responseDto = convertExceptionToDto(e);
            }
            return responseDto;
        } else {
            return new BaseResponseDto(ReturnMessage.INVEST_FAILED_NOT_OPEN_NO_PASSWORD_INVEST);
        }
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

    private BaseResponseDto convertExceptionToDto(InvestException e) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
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
            case UMPAY_INVEST_MESSAGE_INVALID:
                baseResponseDto.setCode(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode());
                baseResponseDto.setMessage(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg());
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
        }
        return baseResponseDto;
    }
}
