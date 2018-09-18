package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.enums.AsyncUmPayService;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.service.InvestService;
import com.tuotiansudai.util.AmountConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Locale;

@Service
public class MobileAppInvestServiceImpl implements MobileAppInvestService {

    static Logger logger = Logger.getLogger(MobileAppInvestServiceImpl.class);

    @Autowired
    private InvestService investService;

    @Autowired
    private MobileAppChannelService mobileAppChannelService;

    @Value("${pay.callback.app.web.host}")
    private String domainName;

    @Override
    public BaseResponseDto<InvestNoPassResponseDataDto> noPasswordInvest(InvestRequestDto investRequestDto) {
        try {
            InvestDto investDto = convertInvestDto(investRequestDto);
            BaseDto<PayDataDto> baseDto = investService.noPasswordInvest(investDto);

            if (baseDto.getData().getStatus()) {
                BaseResponseDto<InvestNoPassResponseDataDto> responseDto = new BaseResponseDto<>(ReturnMessage.SUCCESS);
                responseDto.setData(new InvestNoPassResponseDataDto(MessageFormat.format("{0}/{1}?order_id={2}", domainName, AsyncUmPayService.INVEST_PROJECT_TRANSFER_NOPWD.getMobileRetCallbackPath(), baseDto.getData().getExtraValues().get("order_id"))));
                return responseDto;
            }
            return new BaseResponseDto<>(ReturnMessage.INVEST_FAILED.getCode(), ReturnMessage.INVEST_FAILED.getMsg() + ":" + baseDto.getData().getMessage());
        } catch (InvestException e) {
            return new BaseResponseDto<>(ReturnMessage.ERROR.getCode(), e.getType().getDescription());
        }
    }

    @Override
    public BaseResponseDto<InvestResponseDataDto> invest(InvestRequestDto investRequestDto) {
        BaseResponseDto<InvestResponseDataDto> responseDto = new BaseResponseDto<>();
        InvestDto investDto = convertInvestDto(investRequestDto);
        try {
            BaseDto<PayFormDataDto> formDto = investService.invest(investDto);

            if (!formDto.isSuccess()) {
                String userCouponIds = "";
                for (Long userCouponId : investDto.getUserCouponIds()) {
                    userCouponIds += String.valueOf(userCouponId);
                }
                logger.error(MessageFormat.format("[MobileAppInvestServiceImpl][invest] invest failed!Maybe service cannot connect to payWrapper. " +
                                "investDto:loanId:{0}, transferApplicationInvestId:{1}, loginName:{2}, amount:{3}, userCouponIds:{4} channel:{5}, source:{6}, noPassword:{7}",
                        investDto.getLoanId(), investDto.getTransferApplicationId(), investDto.getLoginName(), investDto.getAmount(), userCouponIds, investDto.getChannel(),
                        investDto.getSource(), investDto.isNoPassword()));
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
        } catch (InvestException e) {
            responseDto = new BaseResponseDto<>(ReturnMessage.ERROR.getCode(), e.getType().getDescription());
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
}
