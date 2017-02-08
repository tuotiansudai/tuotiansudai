package com.tuotiansudai.api.service;

import com.google.common.collect.Lists;
import com.tuotiansudai.api.dto.v1_0.BaseParam;
import com.tuotiansudai.api.dto.v1_0.BaseParamDto;
import com.tuotiansudai.api.dto.v1_0.BaseResponseDto;
import com.tuotiansudai.api.dto.v1_0.CouponAlertResponseDataDto;
import com.tuotiansudai.api.service.v1_0.impl.MobileAppCouponAlertServiceImpl;
import com.tuotiansudai.dto.CouponAlertDto;
import com.tuotiansudai.coupon.service.impl.CouponAlertServiceImpl;
import com.tuotiansudai.enums.CouponType;
import org.joda.time.DateTime;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.TestCase.assertEquals;
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

        when(couponAlertService.getCouponAlert("newBieCoupon", Lists.newArrayList(CouponType.NEWBIE_COUPON, CouponType.RED_ENVELOPE))).thenReturn(couponAlertDto);
        BaseParamDto baseParamDto = new BaseParamDto();
        BaseParam baseParam = new BaseParam();
        baseParam.setUserId("newBieCoupon");
        baseParamDto.setBaseParam(baseParam);
        BaseResponseDto baseDto = mobileAppCouponAlertService.getCouponAlert(baseParamDto);
        CouponAlertResponseDataDto responseDataDto = (CouponAlertResponseDataDto) baseDto.getData();

        assertEquals(CouponType.NEWBIE_COUPON.getName(), responseDataDto.getName());
        assertEquals("10.00", responseDataDto.getAmount());
        assertEquals("2016-01-07", responseDataDto.getEndTime());
    }

}
