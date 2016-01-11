package com.tuotiansudai.api.controller;

import com.tuotiansudai.api.dto.*;
import com.tuotiansudai.api.service.MobileAppNewBieCouponService;
import com.tuotiansudai.api.service.MobileAppWithdrawService;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class MobileAppNewBieCouponControllerTest extends ControllerTestBase {
    @InjectMocks
    private MobileAppNewBieCouponController controller;
    @Mock
    private MobileAppNewBieCouponService service;


    @Override
    protected Object getControllerObject() {
        return controller;
    }

    @Test
    public void shouldGetNewBieCouponIsSuccess() throws Exception {
        NewBieCouponResponseDataDto newBieCouponResponseDataDto = new NewBieCouponResponseDataDto();
        newBieCouponResponseDataDto.setAmount("10.00");
        newBieCouponResponseDataDto.setName("新手体验劵");
        newBieCouponResponseDataDto.setStartTime("2016-01-01");
        newBieCouponResponseDataDto.setEndTime("2016-01-07");


        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setCode(ReturnMessage.SUCCESS.getCode());
        baseResponseDto.setMessage("");
        baseResponseDto.setData(newBieCouponResponseDataDto);
        when(service.getNewBieCoupon(any(BaseParamDto.class))).thenReturn(baseResponseDto);
        doRequestWithServiceMockedTest("/get/newbiecoupon",
                new BaseParamDto()).andExpect(jsonPath("$.data.name").value("新手体验劵"))
                .andExpect(jsonPath("$.data.startTime").value("2016-01-01"))
                .andExpect(jsonPath("$.data.endTime").value("2016-01-07"));
    }
}
