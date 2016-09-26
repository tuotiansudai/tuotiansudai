package com.tuotiansudai.point.service.impl;


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.dto.BasePaginationDataDto;
import com.tuotiansudai.point.repository.mapper.ProductMapper;
import com.tuotiansudai.point.repository.mapper.ProductOrderMapper;
import com.tuotiansudai.point.repository.model.PointBusinessType;
import com.tuotiansudai.point.repository.model.ProductOrderViewDto;
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
    private ProductMapper productMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private ProductOrderMapper productOrderMapper;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private PointBillService pointBillService;

    @Override
    public List<ExchangeCouponDto> findExchangeableCouponList() {
        List<CouponModel> couponModels = couponMapper.findExchangeableCoupons(null, null);
        return Lists.transform(couponModels, new Function<CouponModel, ExchangeCouponDto>() {
            @Override
            public ExchangeCouponDto apply(CouponModel input) {
                ExchangeCouponDto exchangeCouponDto = new ExchangeCouponDto(input);
                exchangeCouponDto.setExchangePoint(productMapper.findByCouponId(input.getId()).getPoints());
                return exchangeCouponDto;
            }
        });
    }

    @Override
    @Transactional
    public boolean exchangeableCoupon(long couponId, String loginName) {
        long exchangePoint = productMapper.findByCouponId(couponId).getPoints();
        long availablePoint = accountMapper.findByLoginName(loginName).getPoint();
        CouponModel couponModel = couponMapper.lockById(couponId);
        return availablePoint >= exchangePoint && couponModel.getIssuedCount() < couponModel.getTotalCount();
    }

    @Override
    public BasePaginationDataDto<ProductOrderViewDto> findProductOrderListByLoginNamePagination(String loginName, int index, int pageSize) {
        long count = productOrderMapper.findProductOrderListByLoginNameCount(loginName);
        List<ProductOrderViewDto> records = productOrderMapper.findProductOrderListByLoginName(loginName, (index-1)*pageSize, pageSize);
        BasePaginationDataDto<ProductOrderViewDto> dto = new BasePaginationDataDto<ProductOrderViewDto>(index, pageSize, count, records);
        dto.setStatus(true);
        return dto;
    }

    @Override
    public long findProductOrderListByLoginNameCount(String loginName) {
        return productOrderMapper.findProductOrderListByLoginNameCount(loginName);
    }

    @Override
    @Transactional
    public boolean exchangeCoupon(long couponId, String loginName, long exchangePoint) {
        try {
            couponAssignmentService.assignUserCoupon(loginName, couponId);
            pointBillService.createPointBill(loginName, couponId, PointBusinessType.EXCHANGE, (-exchangePoint));
            return true;
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return false;
        }
    }


}
