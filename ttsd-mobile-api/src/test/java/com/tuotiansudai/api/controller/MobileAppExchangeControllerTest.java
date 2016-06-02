package com.tuotiansudai.api.controller;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.controller.v1_0.MobileAppExchangeController;
import com.tuotiansudai.api.dto.v1_0.*;
import com.tuotiansudai.api.service.v1_0.MobileAppExchangeService;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.ProductType;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppExchangeControllerTest extends ControllerTestBase{

    @InjectMocks
    private MobileAppExchangeController controller;

    @Mock
    private MobileAppExchangeService service;

    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldExchangeRequestIsError() throws Exception{
        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();
        exchangeRequestDto.setExchangeCode("123");
        when(service.exchange(any(ExchangeRequestDto.class))).thenReturn(successResponseDto);
        doRequestWithServiceMockedTest("/get/code-exchange", exchangeRequestDto);
    }

    @Test
    public void shouldExchangeRequestIsOk() throws Exception{
        BaseResponseDto<UserCouponListResponseDataDto> responseDto = new BaseResponseDto<>();
        BaseCouponResponseDataDto item = new BaseCouponResponseDataDto();
        item.setUserCouponId("1");
        item.setType(CouponType.NEWBIE_COUPON);
        item.setName(CouponType.NEWBIE_COUPON.getAbbr());
        item.setAmount("1.00");
        item.setStartDate(new Date());
        item.setEndDate(new Date());
        item.setInvestLowerLimit("1.00");
        item.setProductTypes(Lists.newArrayList(ProductType._30.getProductLine(), ProductType._90.getProductLine(), ProductType._180.getProductLine()));
        UserCouponListResponseDataDto userCouponListResponseDataDto = new UserCouponListResponseDataDto(Lists.newArrayList(item));
        responseDto.setCode(ReturnMessage.SUCCESS.getCode());
        responseDto.setMessage(ReturnMessage.SUCCESS.getMsg());
        responseDto.setData(userCouponListResponseDataDto);

        ExchangeRequestDto exchangeRequestDto = new ExchangeRequestDto();
        exchangeRequestDto.setExchangeCode("123");
        when(service.exchange(any(ExchangeRequestDto.class))).thenReturn(responseDto);
        doRequestWithServiceMockedTest("/get/code-exchange", exchangeRequestDto)
                .andExpect(jsonPath("$.code").value("0000"))
                .andExpect(jsonPath("$.data.coupons[0].userCouponId").value(item.getUserCouponId()))
                .andExpect(jsonPath("$.data.coupons[0].type").value(item.getType().name()))
                .andExpect(jsonPath("$.data.coupons[0].name").value(item.getName()))
                .andExpect(jsonPath("$.data.coupons[0].amount").value(item.getAmount()))
                .andExpect(jsonPath("$.data.coupons[0].startDate").value(new DateTime(item.getStartDate()).toString("yyyy-MM-dd")))
                .andExpect(jsonPath("$.data.coupons[0].endDate").value(new DateTime(item.getEndDate()).toString("yyyy-MM-dd")))
                .andExpect(jsonPath("$.data.coupons[0].investLowerLimit").value(item.getInvestLowerLimit()))
                .andExpect(jsonPath("$.data.coupons[0].productTypes[0]").value("SYL"))
                .andExpect(jsonPath("$.data.coupons[0].productTypes[1]").value("WYX"))
                .andExpect(jsonPath("$.data.coupons[0].productTypes[2]").value("JYF"));

    }

}
