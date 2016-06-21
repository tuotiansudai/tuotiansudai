package com.tuotiansudai.point.service.impl;


import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponExchangeMapper;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponExchangeModel;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.service.PointBillService;
import com.tuotiansudai.point.service.PointService;
import com.tuotiansudai.repository.model.InvestModel;
import com.tuotiansudai.repository.mapper.AccountMapper;
import com.tuotiansudai.repository.model.AccountModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;


@Service
public class PointServiceImpl implements PointService {
    static Logger logger = Logger.getLogger(PointServiceImpl.class);

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CouponExchangeMapper couponExchangeMapper;

    @Autowired
    private PointBillService pointBillService;

    @Autowired
    private AccountMapper accountMapper;

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
    @Transactional
    public void obtainPointInvest(InvestModel investModel) {
        long point = new BigDecimal(investModel.getAmount()).divide(new BigDecimal(100), 0, BigDecimal.ROUND_DOWN).longValue();
        pointBillService.createPointBill(investModel.getLoginName(), investModel.getId(), PointBusinessType.INVEST, point);
        logger.debug(MessageFormat.format("{0} has obtained point {1}", investModel.getId(), point));
    }

    @Override
    public long getAvailablePoint(String loginName) {
        AccountModel accountModel = accountMapper.findByLoginName(loginName);
        if (accountModel == null) {
            return 0;
        }
        return accountModel.getPoint();
    }
}
