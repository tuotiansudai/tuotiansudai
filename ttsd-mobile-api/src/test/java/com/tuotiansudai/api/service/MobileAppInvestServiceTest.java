package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppInvestServiceImpl;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.InvestDto;
import com.tuotiansudai.dto.PayDataDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.exception.InvestException;
import com.tuotiansudai.exception.InvestExceptionType;
import com.tuotiansudai.repository.model.AccountModel;
import com.tuotiansudai.service.AccountService;
import com.tuotiansudai.service.InvestService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppInvestServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppInvestServiceImpl mobileAppInvestService;

    @Mock
    private InvestService investService;

    @Mock
    private MobileAppChannelService channelService;

    @Mock
    private AccountService accountService;

    @Test
    public void shouldNoPasswordInvestSuccess() throws Exception {
        InvestRequestDto investRequestDto = new InvestRequestDto();
        investRequestDto.setBaseParam(BaseParamTest.getInstance());

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        baseDto.setSuccess(true);
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setStatus(true);
        baseDto.setData(payDataDto);

        when(investService.noPasswordInvest(any(InvestDto.class))).thenReturn(baseDto);
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);
        AccountModel accountModel = new AccountModel();
        accountModel.setAutoInvest(true);
        accountModel.setNoPasswordInvest(true);
        when(accountService.findByLoginName(anyString())).thenReturn(accountModel);
        BaseResponseDto baseResponseDto = mobileAppInvestService.noPasswordInvest(investRequestDto);
        assertThat(baseResponseDto.isSuccess(), is(true));
        assertThat(baseResponseDto.getCode(), is("0000"));
    }

    @Test
    public void shouldNoPasswordInvestFailedInvestorNotOpen() throws Exception {
        InvestRequestDto investRequestDto = new InvestRequestDto();
        investRequestDto.setBaseParam(BaseParamTest.getInstance());

        BaseDto<PayDataDto> baseDto = new BaseDto<>();
        baseDto.setSuccess(true);
        PayDataDto payDataDto = new PayDataDto();
        payDataDto.setStatus(true);
        baseDto.setData(payDataDto);

        when(investService.noPasswordInvest(any(InvestDto.class))).thenReturn(baseDto);
        AccountModel accountModel = new AccountModel();
        accountModel.setAutoInvest(true);
        accountModel.setNoPasswordInvest(false);
        BaseResponseDto baseResponseDto = mobileAppInvestService.noPasswordInvest(investRequestDto);
        assertThat(baseResponseDto.isSuccess(), is(true));
    }

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
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);

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
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);

        BaseResponseDto<InvestResponseDataDto> responseDto = mobileAppInvestService.invest(investRequestDto);
        assert !responseDto.isSuccess();
    }

    @Test
    public void shouldInvestFail2() throws Exception {
        InvestRequestDto investRequestDto = new InvestRequestDto();
        investRequestDto.setBaseParam(BaseParamTest.getInstance());

        InvestException investException = new InvestException(InvestExceptionType.LOAN_IS_FULL);

        when(investService.invest(any(InvestDto.class))).thenThrow(investException);
        when(channelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);

        BaseResponseDto<InvestResponseDataDto> responseDto = mobileAppInvestService.invest(investRequestDto);
        assert !responseDto.isSuccess();
        assert ReturnMessage.LOAN_IS_FULL.getCode().equals(responseDto.getCode());
    }
}
