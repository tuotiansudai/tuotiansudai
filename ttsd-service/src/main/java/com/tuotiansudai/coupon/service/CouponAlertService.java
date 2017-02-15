package com.tuotiansudai.coupon.service;

import com.tuotiansudai.dto.CouponAlertDto;
import com.tuotiansudai.enums.CouponType;

import java.util.List;

public interface CouponAlertService {

    CouponAlertDto getCouponAlert(String loginName, List<CouponType> couponTypes);

}
