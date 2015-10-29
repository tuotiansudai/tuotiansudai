package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.BankCardReplaceRequestDto;
import com.tuotiansudai.api.dto.BankCardRequestDto;
import com.tuotiansudai.api.service.MobileAppBankCardService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

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
        doRequestWithServiceMockedTest("/bankcard/bind",
                new BankCardRequestDto());
    }

    @Test
    public void shouldBankCardReplace() throws Exception {
        when(service.generateBankCardResponse(any(BankCardReplaceRequestDto.class)))
                .thenReturn(successResponseDto);

        doRequestWithServiceMockedTest("/bankcard/replace",
                new BankCardReplaceRequestDto());
    }

    @Test
    public void shouldBankCardSign() throws Exception {
        doRequestWithServiceMockedTest("/bankcard/sign",
                new BankCardReplaceRequestDto());
    }

    @Test
    public void queryBindAndSginStatus() throws Exception {
        doRequestWithServiceMockedTest("/bankcard/query",
                new BankCardRequestDto());
    }
}
