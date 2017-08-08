package com.tuotiansudai.api.service.v1_0.impl;

import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppCurrentInvestService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.current.client.CurrentRestClient;
import com.tuotiansudai.current.dto.DepositDto;
import com.tuotiansudai.current.dto.DepositStatus;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.rest.support.client.exceptions.RestException;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

@Service
public class MobileAppCurrentInvestServiceImpl implements MobileAppCurrentInvestService {

    static Logger logger = Logger.getLogger(MobileAppInvestServiceImpl.class);

    @Autowired
    private CurrentRestClient currentRestClient;

    @Value("${pay.callback.app.web.host}")
    private String domainName;

    @Override
    public BaseResponseDto<InvestResponseDataDto> invest(CurrentInvestRequestDto investRequestDto, String loginName) {
        BaseResponseDto<InvestResponseDataDto> responseDto = new BaseResponseDto<>();
        DepositDto depositRequestDto = convertInvestDto(investRequestDto, loginName, false);
        try {
            BaseDto<PayFormDataDto> formDto = currentRestClient.deposit(depositRequestDto);

            if (!formDto.isSuccess()) {
                logger.error(MessageFormat.format("[MobileAppCurrentInvestServiceImpl][deposit] current deposit failed!Maybe service cannot connect to payWrapper. " +
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
            logger.error("current deposit failed", e);
            responseDto.setCode(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getCode());
            responseDto.setMessage(ReturnMessage.UMPAY_INVEST_MESSAGE_INVALID.getMsg());
        }
        return responseDto;
    }

    @Override
    public BaseResponseDto<InvestNoPassResponseDataDto> noPasswordInvest(CurrentInvestRequestDto investRequestDto, String loginName) {
        try {
            DepositDto depositRequestDto = convertInvestDto(investRequestDto, loginName, true);
            BaseDto<PayDataDto> baseDto = currentRestClient.noPasswordDeposit(depositRequestDto);

            if (baseDto.getData().getStatus()) {
                BaseResponseDto<InvestNoPassResponseDataDto> responseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
                responseDto.setData(new InvestNoPassResponseDataDto(MessageFormat.format("{0}/{1}?order_id={2}", domainName, AsyncUmPayService.CURRENT_DEPOSIT_PROJECT_TRANSFER_NOPWD.getMobileRetCallbackPath(), baseDto.getData().getExtraValues().get("order_id"))));
                return responseDto;
            }
            return new BaseResponseDto<>(ReturnMessage.INVEST_FAILED.getCode(), ReturnMessage.INVEST_FAILED.getMsg() + ":" + baseDto.getData().getMessage());
        } catch (RestException e) {
            return new BaseResponseDto<>(ReturnMessage.ERROR.getCode(), e.getReason());
        }
    }

    private DepositDto convertInvestDto(CurrentInvestRequestDto investRequestDto, String loginName, boolean noPassword) {
        Source source = Source.valueOf(investRequestDto.getBaseParam().getPlatform().toUpperCase());
        long amount = AmountConverter.convertStringToCent(investRequestDto.getAmount());
        return new DepositDto(null, loginName, amount, source, DepositStatus.WAITING_PAY, noPassword);
    }
}
