package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppCouponAlertService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppCouponAlertControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppCouponAlertController controller;
    @Mock
    private MobileAppCouponAlertService service;


    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGetNewBieCouponIsSuccess() throws Exception {
        CouponAlertResponseDataDto newBieCouponResponseDataDto = new CouponAlertResponseDataDto();
        newBieCouponResponseDataDto.setAmount("10.00");
        newBieCouponResponseDataDto.setName("新手体验劵");
        newBieCouponResponseDataDto.setStartTime("2016-01-01");
        newBieCouponResponseDataDto.setEndTime("2016-01-07");


        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage("");
        baseResponseDto.setData(newBieCouponResponseDataDto);
        when(service.getCouponAlert(any(BaseParamDto.class))).thenReturn(baseResponseDto);
        doRequestWithServiceMockedTest("/get/coupon-alert",
                new BaseParamDto()).andExpect(jsonPath("$.data.name").value("新手体验劵"))
                .andExpect(jsonPath("$.data.startTime").value("2016-01-01"))
                .andExpect(jsonPath("$.data.endTime").value("2016-01-07"));
    }
}
