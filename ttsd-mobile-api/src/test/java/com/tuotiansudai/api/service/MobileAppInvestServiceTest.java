package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppInvestServiceImpl;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.exception.InvestExceptionType;
import com.tuotiansudai.service.InvestService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppInvestServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppInvestServiceImpl mobileAppInvestService;

    @Mock
    private InvestService investService;

    @Test
    public void shouldInvestSuccess() throws Exception {
        InvestRequestDto investRequestDto = new InvestRequestDto();
        investRequestDto.setBaseParam(BaseParamTest.getInstance());

        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setStatus(true);
        payFormDataDto.setUrl("url");
        BaseDto<PayFormDataDto> successResponseDto = new BaseDto<>();
        successResponseDto.setSuccess(true);
        successResponseDto.setData(payFormDataDto);

        when(investService.invest(any(InvestDto.class))).thenReturn(successResponseDto);

        BaseResponseDto<InvestResponseDataDto> responseDto = mobileAppInvestService.invest(investRequestDto);
        assert responseDto.isSuccess();
        assert "url".equals(responseDto.getData().getUrl());
    }

    @Test
    public void shouldInvestFail1() throws Exception {
        InvestRequestDto investRequestDto = new InvestRequestDto();
        investRequestDto.setBaseParam(BaseParamTest.getInstance());

        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setStatus(false);
        payFormDataDto.setUrl("url");
        BaseDto<PayFormDataDto> successResponseDto = new BaseDto<>();
        successResponseDto.setSuccess(true);
        successResponseDto.setData(payFormDataDto);

        when(investService.invest(any(InvestDto.class))).thenReturn(successResponseDto);

        BaseResponseDto<InvestResponseDataDto> responseDto = mobileAppInvestService.invest(investRequestDto);
        assert !responseDto.isSuccess();
    }

    @Test
    public void shouldInvestFail2() throws Exception {
        InvestRequestDto investRequestDto = new InvestRequestDto();
        investRequestDto.setBaseParam(BaseParamTest.getInstance());

        InvestException investException = new InvestException(InvestExceptionType.LOAN_IS_FULL);

        when(investService.invest(any(InvestDto.class))).thenThrow(investException);

        BaseResponseDto<InvestResponseDataDto> responseDto = mobileAppInvestService.invest(investRequestDto);
        assert !responseDto.isSuccess();
        assert ReturnMessage.LOAN_IS_FULL.getCode().equals(responseDto.getCode());
    }
}
