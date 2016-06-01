package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.dto.v1_0.BankCardRequestDto;
import com.tuotiansudai.api.dto.v1_0.BankCardResponseDto;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppRechargeServiceImpl;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.repository.model.BankCardStatus;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppRechargeServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppRechargeServiceImpl mobileAppRechargeService;

    @Mock
    private PayWrapperClient payWrapperClient;

    @Mock
    private BankCardMapper bankCardMapper;

    @Mock
    private MobileAppChannelService mobileAppChannelService;

    @Test
    public void shouldRechargeSuccess() throws Exception {
        BankCardRequestDto bankCardRequestDto = new BankCardRequestDto();
        BankCardModel bankCardModel = new BankCardModel();
        bankCardModel.setBankCode("ICBC");
        bankCardModel.setStatus(BankCardStatus.PASSED);
        bankCardModel.setCreatedTime(new Date());
        bankCardModel.setLoginName("loginName");
        bankCardModel.setIsFastPayOn(true);
        bankCardModel.setCardNumber("1234567890");
        when(mobileAppChannelService.obtainChannelBySource(any(BaseParam.class))).thenReturn(null);
        when(bankCardMapper.findByLoginNameAndIsFastPayOn(anyString())).thenReturn(bankCardModel);
        bankCardRequestDto.setBaseParam(BaseParamTest.getInstance());

        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setStatus(true);
        payFormDataDto.setUrl("url");
        BaseDto<PayFormDataDto> successResponseDto = new BaseDto<>();
        successResponseDto.setSuccess(true);
        successResponseDto.setData(payFormDataDto);

        when(payWrapperClient.recharge(any(RechargeDto.class))).thenReturn(successResponseDto);

        BaseResponseDto<BankCardResponseDto> responseDto = mobileAppRechargeService.recharge(bankCardRequestDto);
        assertTrue(responseDto.isSuccess());
        assertEquals("url", responseDto.getData().getUrl());
    }

}
