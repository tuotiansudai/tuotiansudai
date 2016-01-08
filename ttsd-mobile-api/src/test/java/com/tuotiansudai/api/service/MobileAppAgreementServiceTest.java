package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.AgreementOperateRequestDto;
import com.tuotiansudai.api.dto.AgreementOperateResponseDataDto;
import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.service.impl.MobileAppAgreementServiceImpl;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppAgreementServiceTest extends ServiceTestBase{

    @InjectMocks
    private MobileAppAgreementServiceImpl mobileAppAgreementService;

    @Mock
    private PayWrapperClient payWrapperClient;

    @Test
    public void shouldGenerateAgreementRequestIsOk() {
        AgreementOperateRequestDto agreementOperateRequestDto = new AgreementOperateRequestDto();
        agreementOperateRequestDto.setFastPay(false);
        agreementOperateRequestDto.setAutoInvest(true);
        agreementOperateRequestDto.setBaseParam(BaseParamTest.getInstance());

        BaseDto<PayFormDataDto> formDto = new BaseDto<>();
        PayFormDataDto dataDto = new PayFormDataDto();
        dataDto.setUrl("url");
        dataDto.setStatus(true);
        formDto.setData(dataDto);

        when(payWrapperClient.agreement(any(AgreementDto.class))).thenReturn(formDto);
        BaseResponseDto<AgreementOperateResponseDataDto> baseResponseDto = mobileAppAgreementService.generateAgreementRequest(agreementOperateRequestDto);

        assertTrue(baseResponseDto.isSuccess());
        assertEquals("url", baseResponseDto.getData().getUrl());
    }

}
