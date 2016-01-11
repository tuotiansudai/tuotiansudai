package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseParam;
import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.NewBieCouponResponseDataDto;
import com.tuotiansudai.api.service.impl.MobileAppNewBieCouponServiceImpl;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.service.impl.UserCouponServiceImpl;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppNewBieCouponServiceTest extends ServiceTestBase{
    @InjectMocks
    private MobileAppNewBieCouponServiceImpl mobileAppNewBieCouponService;

    @Mock
    private UserCouponServiceImpl UserCouponService;

    @Test
    public void shouldGetNewBieCouponIsSuccess(){

        UserCouponDto userCouponDto = new UserCouponDto();
        userCouponDto.setAmount(1000);
        userCouponDto.setName("新手体验劵");
        userCouponDto.setStartTime(new DateTime(2016, 1, 1, 0, 0, 0).toDate());
        userCouponDto.setEndTime(new DateTime(2016, 1, 7, 23, 59, 59).toDate());

        when(UserCouponService.getUsableNewbieCoupon(anyString())).thenReturn(userCouponDto);
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("newBieCoupon");
        baseParamDto.setBaseParam(baseParam);
        BaseResponseDto baseDto = mobileAppNewBieCouponService.getNewBieCoupon(baseParamDto);
        NewBieCouponResponseDataDto responseDataDto = (NewBieCouponResponseDataDto)baseDto.getData();

        assertEquals("新手体验劵",responseDataDto.getName());
        assertEquals("10.00",responseDataDto.getAmount());
        assertEquals("2016-01-01",responseDataDto.getStartTime());
        assertEquals("2016-01-07",responseDataDto.getEndTime());
    }

}
