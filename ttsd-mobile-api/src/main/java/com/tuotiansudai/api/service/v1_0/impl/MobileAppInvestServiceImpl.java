package com.tuotiansudai.api.service.v1_0.impl;


import com.tuotiansudai.api.dto.v1_0.BankAsynResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.InvestRequestDto;
import com.tuotiansudai.api.dto.v1_0.ReturnMessage;
import com.tuotiansudai.api.service.v1_0.MobileAppInvestService;
import com.tuotiansudai.api.util.CommonUtils;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.fudian.message.BankAsyncMessage;
import com.tuotiansudai.fudian.message.BankReturnCallbackMessage;
import com.tuotiansudai.service.InvestService;
import org.springframework.beans.factory.annotation.Autowired;

public class MobileAppInvestServiceImpl implements MobileAppInvestService {

    private final InvestService investService;

    @Autowired
    public MobileAppInvestServiceImpl(InvestService investService) {
        this.investService = investService;
    }

    @Override
    public BaseResponseDto<BankAsynResponseDto> invest(InvestRequestDto investRequestDto) {
        try {
            BankAsyncMessage bankAsyncMessage = investService.invest(investRequestDto.convertToInvestDto());
            return CommonUtils.mapToFormData(bankAsyncMessage);
        } catch (InvestException e) {
            return new BaseResponseDto<>(ReturnMessage.INVEST_FAILED.getCode(), e.getType().getDescription());
        }
    }

    @Override
    public BaseResponseDto<BankAsynResponseDto> noPasswordInvest(InvestRequestDto investRequestDto) {
        try {
            BankReturnCallbackMessage bankAsyncMessage = investService.noPasswordInvest(investRequestDto.convertToInvestDto());
            if (bankAsyncMessage.isStatus()) {
                return new BaseResponseDto<>(ReturnMessage.SUCCESS);
            }
        } catch (InvestException e) {
            return new BaseResponseDto<>(ReturnMessage.INVEST_FAILED.getCode(), e.getType().getDescription());
        }
        return new BaseResponseDto<>(ReturnMessage.INVEST_FAILED);
    }
}
