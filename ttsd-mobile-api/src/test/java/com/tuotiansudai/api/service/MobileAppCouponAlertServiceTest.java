package com.tuotiansudai.api.service;

import com.tuotiansudai.api.dto.BaseParam;
import com.tuotiansudai.api.dto.BaseParamDto;
import com.tuotiansudai.api.dto.BaseResponseDto;
import com.tuotiansudai.api.dto.CouponAlertResponseDataDto;
import com.tuotiansudai.api.service.impl.MobileAppCouponAlertServiceImpl;
import com.tuotiansudai.coupon.dto.CouponAlertDto;
import com.tuotiansudai.coupon.service.impl.CouponAlertServiceImpl;
import com.tuotiansudai.repository.model.CouponType;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class MobileAppCouponAlertServiceTest extends ServiceTestBase {
    @InjectMocks
    private MobileAppCouponAlertServiceImpl mobileAppCouponAlertService;

    @Mock
    private CouponAlertServiceImpl couponAlertService;

    @Test
    public void shouldGetCouponAlertIsSuccess() {

        CouponAlertDto couponAlertDto = new CouponAlertDto();
        couponAlertDto.setAmount(1000);
        couponAlertDto.setCouponType(CouponType.NEWBIE_COUPON);
        couponAlertDto.setExpiredDate(new DateTime(2016, 1, 7, 23, 59, 59).toDate());

        when(couponAlertService.getCouponAlert(anyString())).thenReturn(couponAlertDto);
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("newBieCoupon");
        baseParamDto.setBaseParam(baseParam);
        BaseResponseDto baseDto = mobileAppCouponAlertService.getCouponAlert(baseParamDto);
        CouponAlertResponseDataDto responseDataDto = (CouponAlertResponseDataDto) baseDto.getData();

        assertEquals("新手体验券", responseDataDto.getName());
        assertEquals("10.00", responseDataDto.getAmount());
        assertEquals("2016-01-07", responseDataDto.getEndTime());
    }

}
