package com.tuotiansudai.point.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponExchangeMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponActivationService;
import com.tuotiansudai.point.repository.mapper.PointBillMapper;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.PointExchangeService;
import com.tuotiansudai.repository.mapper.AccountMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PointExchangeServiceImpl implements PointExchangeService {
    static Logger logger = Logger.getLogger(PointExchangeServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private CouponExchangeMapper couponExchangeMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponActivationService couponActivationService;
    @Autowired
    private PointBillService pointBillService;

    @Override
    public List<ExchangeCouponDto> findExchangeableCouponList(){
        List<CouponModel> couponModels = couponMapper.findCouponExchangeableList(0,100);
        return Lists.transform(couponModels, new Function<CouponModel, ExchangeCouponDto>() {
            @Override
            public ExchangeCouponDto apply(CouponModel input) {
                ExchangeCouponDto exchangeCouponDto = new ExchangeCouponDto(input);
                exchangeCouponDto.setExchangePoint(couponExchangeMapper.findByCouponId(input.getId()).getExchangePoint());
                return exchangeCouponDto;
            }
        });
    }

    @Override
    @Transactional
    public boolean exchangeableCoupon(long couponId, String loginName){
        long exchange_point = couponExchangeMapper.findByCouponId(couponId).getExchangePoint();
        long availablePoint = accountMapper.findUsersAccountAvailablePoint(loginName);
        CouponModel couponModel = couponMapper.lockById(couponId);
        return availablePoint >= exchange_point && couponModel.getIssuedCount() <= couponModel.getTotalCount();
    }

    @Override
    @Transactional
    public boolean exchangeCoupon(long couponId, String loginName, long exchangePoint){
        try {
            couponActivationService.assignUserCoupon(loginName, Lists.newArrayList(UserGroup.EXCHANGER), couponId);
            pointBillService.createPointBill(loginName,couponId,PointBusinessType.EXCHANGE,(-exchangePoint));
            return true;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return false;
        }
    }
}
