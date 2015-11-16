package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.impl.MobileAppBankCardServiceImpl;
import com.tuotiansudai.dto.AgreementDto;
import com.tuotiansudai.dto.BaseDto;
import com.tuotiansudai.dto.BindBankCardDto;
import com.tuotiansudai.dto.PayFormDataDto;
import com.tuotiansudai.repository.mapper.BankCardMapper;
import com.tuotiansudai.repository.model.BankCardModel;
import com.tuotiansudai.service.AgreementService;
import com.tuotiansudai.service.BindBankCardService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppBankCardServiceTest extends ServiceTestBase {

    @InjectMocks
    private MobileAppBankCardServiceImpl mobileAppBankCardService;

    @Mock
    private BindBankCardService bindBankCardService;

    @Mock
    private AgreementService agreementService;

    @Mock
    private BankCardMapper bankCardMapper;

    @Test
    public void bindBankCardTest() {
        when(bindBankCardService.bindBankCard(any(BindBankCardDto.class))).thenReturn(generateMockPayFormData());
        BankCardRequestDto requestDto = new BankCardRequestDto();
        requestDto.setBaseParam(BaseParamTest.getInstance());
        BaseResponseDto<BankCardResponseDto> responseDto = mobileAppBankCardService.bindBankCard(requestDto);
        assert responseDto.isSuccess();
        assertEquals("url", responseDto.getData().getUrl());
        assertEquals("orderId=123&merDate=date", responseDto.getData().getRequestData());
    }

    @Test
    public void openFastPayTest() {
        when(agreementService.agreement(any(AgreementDto.class))).thenReturn(generateMockPayFormData());
        BankCardRequestDto requestDto = new BankCardRequestDto();
        requestDto.setBaseParam(BaseParamTest.getInstance());
        BaseResponseDto<BankCardResponseDto> responseDto = mobileAppBankCardService.openFastPay(requestDto);
        assert responseDto.isSuccess();
        assertEquals("url", responseDto.getData().getUrl());
        assertEquals("orderId=123&merDate=date", responseDto.getData().getRequestData());
    }

    @Test
    public void queryStatusTest() {
        BankCardModel bankCardModel = new BankCardModel();
        bankCardModel.setIsFastPayOn(false);
        bankCardModel.setCardNumber("1111");
        when(bankCardMapper.findByLoginName(anyString())).thenReturn(bankCardModel);

        BankCardRequestDto requestDto = new BankCardRequestDto();
        requestDto.setBaseParam(BaseParamTest.getInstance());
        requestDto.setUserId("user");
        requestDto.setOperationType(MobileAppCommonConstants.QUERY_BIND_STATUS);
        BaseResponseDto responseDto = mobileAppBankCardService.queryStatus(requestDto);
        assert responseDto.isSuccess();

        requestDto.setOperationType(MobileAppCommonConstants.QUERY_SIGN_STATUS);
        responseDto = mobileAppBankCardService.queryStatus(requestDto);
        assert !responseDto.isSuccess();
    }

    private BaseDto<PayFormDataDto> generateMockPayFormData() {
        Map<String, String> fields = new HashMap<>();
        fields.put("orderId", "123");
        fields.put("merDate", "date");

        PayFormDataDto payFormDataDto = new PayFormDataDto();
        payFormDataDto.setUrl("url");
        payFormDataDto.setFields(fields);

        BaseDto<PayFormDataDto> requestFormData = new BaseDto<>();
        requestFormData.setData(payFormDataDto);
        requestFormData.setSuccess(true);

        return requestFormData;
    }
}
