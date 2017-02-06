package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.controller.v1_0.MobileAppPointShopController;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppPointShopService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class MobileAppPointShopControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppPointShopController controller;
    @Mock
    private MobileAppPointShopService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldFindUserAddressResponseDtoIsOk() throws Exception {
        when(service.findUserAddressResponseDto(any(BaseParamDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/user-address",
                new BankCardRequestDto());
    }

    @Test
    public void shouldUpdateUserAddressIsOk() throws Exception {
        when(service.updateUserAddress(any(UserAddressRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/update/user-address",
                new BankCardRequestDto());
    }

    @Test
    public void shouldFindPointHomeIsOk() throws Exception {
        when(service.findPointHome(any(ProductListRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/point-home",
                new BankCardRequestDto());
    }

    @Test
    public void shouldFindProductDetailIsOk() throws Exception {
        when(service.findProductDetail(any(ProductDetailRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/product-detail",
                new BankCardRequestDto());
    }

    @Test
    public void shouldProductExchangeIsOk() throws Exception {
        when(service.productExchange(any(ProductDetailRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/product-exchange",
                new BankCardRequestDto());
    }

}
