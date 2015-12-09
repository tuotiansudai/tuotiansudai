package com.tuotiansudai.coupon.service.impl;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.service.CouponService;
import org.apache.log4j.Logger;

import org.springframework.stereotype.Service;


@Service
public class CouponServiceImpl implements CouponService {

    static Logger logger = Logger.getLogger(CouponServiceImpl.class);


    @Override
    public boolean createCoupon(String loginName,CouponDto couponDto) {

        return false;
    }
}
