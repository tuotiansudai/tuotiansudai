package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.WithdrawOperateRequestDto;
import com.tuotiansudai.api.dto.v1_0.WithdrawOperateResponseDataDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppWithdrawServiceImpl;
import com.tuotiansudai.api.util.PageValidUtils;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.WithdrawDto;
import com.tuotiansudai.enums.WithdrawStatus;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankCardStatus;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.WithdrawModel;
import com.tuotiansudai.service.BlacklistService;
import com.tuotiansudai.util.IdGenerator;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppWithdrawServiceTest extends ServiceTestBase {
    @InjectMocks
    private MobileAppWithdrawServiceImpl mobileAppWithdrawService;
    @Mock
    private BankCardMapper bankCardMapper;
    @Mock
    private PayWrapperClient payWrapperClient;
    @Mock
    private BlacklistService blacklistService;
    @Mock
    private PageValidUtils pageValidUtils;

    public MobileAppWithdrawServiceTest() {
    }

    @Test
    public void shouldGenerateWithdrawRequestIsOk() {
        WithdrawOperateRequestDto withdrawOperateRequestDto = new WithdrawOperateRequestDto();
        withdrawOperateRequestDto.setMoney(10);
        withdrawOperateRequestDto.setBaseParam(BaseParamTest.getInstance());

        BankCardModel bankCardModel = new BankCardModel();
        bankCardModel.setBankCode("ICBC");
        bankCardModel.setStatus(BankCardStatus.PASSED);
        bankCardModel.setCreatedTime(new Date());
        bankCardModel.setLoginName("loginName");
        bankCardModel.setIsFastPayOn(true);
        bankCardModel.setCardNumber("1234567890");

        BaseDto<PayFormDataDto> formDto = new BaseDto<>();
        PayFormDataDto dataDto = new PayFormDataDto();
        dataDto.setUrl("url");
        dataDto.setStatus(true);
        formDto.setData(dataDto);

        when(bankCardMapper.findPassedBankCardByLoginName(anyString())).thenReturn(bankCardModel);

        when(payWrapperClient.withdraw(any(WithdrawDto.class))).thenReturn(formDto);

        when(blacklistService.userIsInBlacklist(anyString())).thenReturn(false);

        BaseResponseDto<WithdrawOperateResponseDataDto> baseResponseDto = mobileAppWithdrawService.generateWithdrawRequest(withdrawOperateRequestDto);

        assertTrue(baseResponseDto.isSuccess());
        assertEquals("url", baseResponseDto.getData().getUrl());

    }
}
