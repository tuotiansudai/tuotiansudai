package com.tuotiansudai.point.service.impl;


import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponExchangeMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponExchangeModel;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.point.service.PointService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
public class PointServiceImpl implements PointService {
    static Logger logger = Logger.getLogger(PointServiceImpl.class);

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

    @Override
    public CouponExchangeModel findCouponExchangeByCouponId(long couponId) {
        return couponExchangeMapper.findCouponExchangeByCouponId(couponId);
    }


}
