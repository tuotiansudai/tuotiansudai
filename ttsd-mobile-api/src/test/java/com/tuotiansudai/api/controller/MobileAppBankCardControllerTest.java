package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BankCardRequestDto;
import com.tuotiansudai.api.dto.BankCardResponseDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.ReturnMessage;
import com.tuotiansudai.api.service.MobileAppBankCardService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppBankCardControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppBankCardController controller;

    @Mock
    private MobileAppBankCardService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldBankCardBind() throws Exception {
        BankCardResponseDto dataDto = new BankCardResponseDto();
        dataDto.setUrl("url");
        dataDto.setRequestData("requestData");

        BaseResponseDto baseDto = new BaseResponseDto();
        baseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        baseDto.setData(dataDto);

        when(service.bindBankCard(any(BankCardRequestDto.class))).thenReturn(baseDto);
        doRequestWithServiceMockedTest("/bankcard/bind",
                new BankCardRequestDto())
                .andExpect(jsonPath("$.data.url").value("url"))
                .andExpect(jsonPath("$.data.requestData").value("requestData"));
    }

    @Test
    public void shouldBankCardReplace() throws Exception {
        /*
        when(service.generateBankCardResponse(any(BankCardReplaceRequestDto.class)))
                .thenReturn(successResponseDto);

        doRequestWithServiceMockedTest("/bankcard/replace",
                new BankCardReplaceRequestDto());
        */
    }

    @Test
    public void shouldBankCardSign() throws Exception {
        when(service.openFastPay(any(BankCardRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/bankcard/sign",
                new BankCardRequestDto());
    }

    @Test
    public void queryBindAndSginStatus() throws Exception {
        when(service.queryStatus(any(BankCardRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/bankcard/query",
                new BankCardRequestDto());
    }
}
