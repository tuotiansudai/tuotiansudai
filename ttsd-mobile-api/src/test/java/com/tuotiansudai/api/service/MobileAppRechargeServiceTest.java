package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.BaseParamTest;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppChannelService;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppRechargeServiceImpl;
import com.tuotiansudai.client.PayWrapperClient;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.dto.RechargeDto;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.mapper.BankMapper;
import com.tuotiansudai.repository.mapper.RechargeMapper;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.repository.model.RechargeStatus;
import com.tuotiansudai.util.AmountConverter;
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

    @Mock
    private BankMapper bankMapper;

    @Mock
    private RechargeMapper rechargeMapper;

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

    @Test
    public void testGetBankLimit() throws Exception {
        BankModel bankModel = new BankModel();
        bankModel.setId(1L);
        bankModel.setName("ICBC");
        bankModel.setBankCode("ICBC");
        bankModel.setSingleAmount(10000);
        bankModel.setSingleDayAmount(50000);

        when(bankMapper.findBankList(0L, 0L)).thenReturn(Lists.newArrayList(bankModel, bankModel));
        when(bankMapper.findByBankCode("ICBC")).thenReturn(bankModel);
        when(rechargeMapper.findSumRechargeAmount(anyString(), anyString(), any(RechargeSource.class), any(RechargeStatus.class), anyString(), anyString(), any(Date.class),
                any(Date.class))).thenReturn(5000L);

        BankLimitRequestDto bankLimitRequestDto = new BankLimitRequestDto();
        bankLimitRequestDto.setBaseParam(new BaseParam());
        bankLimitRequestDto.setBankCode("ICBC");
        BaseResponseDto baseResponseDto = mobileAppRechargeService.getBankLimit(bankLimitRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(AmountConverter.convertCentToString(50000 - 5000), ((BankLimitResponseDataDto) baseResponseDto.getData()).getRechargeLeftAmount());
        assertEquals(AmountConverter.convertCentToString(10000), ((BankLimitResponseDataDto) baseResponseDto.getData()).getBankLimits().get(0).getSingleAmount());
        assertEquals(AmountConverter.convertCentToString(50000), ((BankLimitResponseDataDto) baseResponseDto.getData()).getBankLimits().get(0).getSingleDayAmount());
        assertEquals(bankModel.getBankCode(), ((BankLimitResponseDataDto) baseResponseDto.getData()).getBankLimits().get(0).getBankCode());
        assertEquals(bankModel.getName(), ((BankLimitResponseDataDto) baseResponseDto.getData()).getBankLimits().get(0).getBankName());
        assertEquals(AmountConverter.convertCentToString(bankModel.getSingleAmount()), ((BankLimitResponseDataDto) baseResponseDto.getData()).getBankLimits().get(0).getSingleAmount());
        assertEquals(AmountConverter.convertCentToString(bankModel.getSingleDayAmount()), ((BankLimitResponseDataDto) baseResponseDto.getData()).getBankLimits().get(0).getSingleDayAmount());

        bankLimitRequestDto.setBankCode("");
        baseResponseDto = mobileAppRechargeService.getBankLimit(bankLimitRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(2, ((BankLimitResponseDataDto) baseResponseDto.getData()).getBankLimits().size());

        bankLimitRequestDto.setBankCode(null);
        baseResponseDto = mobileAppRechargeService.getBankLimit(bankLimitRequestDto);
        assertEquals(ReturnMessage.SUCCESS.getCode(), baseResponseDto.getCode());
        assertEquals(2, ((BankLimitResponseDataDto) baseResponseDto.getData()).getBankLimits().size());
    }
}
