package com.tuotiansudai.coupon.service.impl;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.exception.CreateCouponException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    static Logger logger = Logger.getLogger(CouponServiceImpl.class);

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Override
    @Transactional
    public void createCoupon(String loginName, CouponDto couponDto) throws CreateCouponException {
        CouponModel couponModel = new CouponModel(couponDto);
        long amount = couponModel.getAmount();
        if (amount <= 0) {
            throw new CreateCouponException("投资体验券金额应大于0!");
        }
        long totalCount = couponModel.getTotalCount();
        if (totalCount <= 0) {
            throw new CreateCouponException("发放数量应大于0!");
        }
        Date startTime = couponModel.getStartTime();
        Date endTime = couponModel.getEndTime();

        if (startTime == null) {
            throw new CreateCouponException("活动起期不能为空!");
        }
        if (endTime == null) {
            throw new CreateCouponException("活动止期不能为空!");
        }
        if (endTime.before(startTime)) {
            throw new CreateCouponException("活动止期早于活动起期!");
        }
        couponModel.setCreateUser(loginName);
        couponMapper.create(couponModel);
    }

    @Override
    @Transactional
    public void afterReturningUserRegistered(String loginName) {
        List<CouponModel> couponModelValid = couponMapper.findValidCoupon();
        for (CouponModel couponModel : couponModelValid) {
            long id = couponModel.getId();
            UserCouponModel userCouponModel = new UserCouponModel(loginName, id);
            userCouponMapper.create(userCouponModel);
            recordIssuedCount(id);
        }
    }

    @Transactional
    public void recordIssuedCount(long id) {
        CouponModel couponModel = couponMapper.lockByCoupon(id);
        long issuedCount = couponModel.getIssuedCount();
        couponModel.setIssuedCount(issuedCount + 1);
        couponMapper.updateCoupon(couponModel);
    }

    @Override
    public List<CouponModel> findCoupons(int index, int pageSize) {
        return couponMapper.findCoupons((index - 1) * pageSize, pageSize);
    }

    @Override
    public int findCouponsCount() {
        return couponMapper.findCouponsCount();
    }

    @Override
    public void updateCoupon(String loginName, long couponId, boolean active) {
        CouponModel couponModel = couponMapper.findById(couponId);
        couponModel.setActive(active);
        couponModel.setActiveTime(new Date());
        couponModel.setActiveUser(loginName);
        couponMapper.updateCoupon(couponModel);
    }

    @Override
    public CouponModel findCouponById(long couponId) {
        return couponMapper.findById(couponId);
    }

}
