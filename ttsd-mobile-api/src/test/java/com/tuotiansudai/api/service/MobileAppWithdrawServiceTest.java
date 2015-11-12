package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppCertificationServiceImpl;
import com.tuotiansudai.api.service.impl.MobileAppWithdrawServiceImpl;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.*;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankCardStatus;
import com.tuotiansudai.service.UserService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppWithdrawServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppWithdrawServiceImpl mobileAppWithdrawService;
    @Mock
    private BankCardMapper bankCardMapper;
    @Mock
    private PayWrapperClient payWrapperClient;
    @Test
    public void shouldGenerateWithdrawRequestIsOk(){
        WithdrawOperateRequestDto withdrawOperateRequestDto = new WithdrawOperateRequestDto();
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

        when(bankCardMapper.findByLoginName(anyString())).thenReturn(bankCardModel);

        when(payWrapperClient.withdraw(any(WithdrawDto.class))).thenReturn(formDto);

        BaseResponseDto<WithdrawOperateResponseDataDto> baseResponseDto = mobileAppWithdrawService.generateWithdrawRequest(withdrawOperateRequestDto);

        assertTrue(baseResponseDto.isSuccess());
        assertEquals("url",baseResponseDto.getData().getUrl());

    }

}
