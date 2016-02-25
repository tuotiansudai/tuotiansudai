package com.tuotiansudai.point.service.impl;


import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.point.repository.mapper.CouponExchangeMapper;
import com.tuotiansudai.point.repository.model.CouponExchangeModel;
import com.tuotiansudai.point.repository.model.ExchangeCouponDto;
import com.tuotiansudai.point.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PointServiceImpl implements PointService{

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponExchangeMapper couponExchangeMapper;

    @Override
    @Transactional
    public void createCouponAndExchange(String loginName, ExchangeCouponDto exchangeCouponDto) {
        CouponModel couponModel = new CouponModel(exchangeCouponDto);
        couponModel.setCreatedBy(loginName);
        couponModel.setCreatedTime(new Date());
        couponMapper.create(couponModel);

        CouponExchangeModel couponExchangeModel = new CouponExchangeModel();
        couponExchangeModel.setCouponId(couponModel.getId());
        couponExchangeModel.setExchangePoint(exchangeCouponDto.getExchangePoint());
        couponExchangeMapper.create(couponExchangeModel);

    }

}
