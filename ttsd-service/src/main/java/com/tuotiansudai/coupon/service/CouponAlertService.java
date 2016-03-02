package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.CouponAlertDto;

public interface CouponAlertService {
    CouponAlertDto getCouponAlert(String loginName);

    void BirthdayNotify();

}
