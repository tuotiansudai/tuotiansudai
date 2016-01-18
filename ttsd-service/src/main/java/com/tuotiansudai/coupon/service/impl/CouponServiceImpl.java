package com.tuotiansudai.coupon.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.CouponService;
import com.tuotiansudai.exception.CreateCouponException;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    static Logger logger = Logger.getLogger(CouponServiceImpl.class);

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private LoanMapper loanMapper;

    @Override
    @Transactional
    public void createCoupon(String loginName, CouponDto couponDto) throws CreateCouponException {
        this.checkCoupon(couponDto);
        CouponModel couponModel = new CouponModel(couponDto);
        couponModel.setCreatedBy(loginName);
        couponMapper.create(couponModel);
    }

    private void checkCoupon(CouponDto couponDto) throws CreateCouponException {
        CouponModel couponModel = new CouponModel(couponDto);
        long amount = couponModel.getAmount();
        long investLowerLimit = couponModel.getInvestLowerLimit();
        if (amount <= 0) {
            throw new CreateCouponException("投资体验券金额应大于0!");
        }
        long totalCount = couponModel.getTotalCount();
        if (totalCount <= 0) {
            throw new CreateCouponException("发放数量应大于0!");
        }
        if (investLowerLimit <= 0) {
            throw new CreateCouponException("使用条件金额应大于0!");
        }
        Date startTime = couponModel.getStartTime();
        Date endTime = couponModel.getEndTime();
        if (CouponType.isNewBieCoupon(couponDto.getCouponType())) {
            if (startTime == null) {
                throw new CreateCouponException("活动起期不能为空!");
            }
            if (endTime == null) {
                throw new CreateCouponException("活动止期不能为空!");
            }
            if (endTime.before(startTime)) {
                throw new CreateCouponException("活动止期早于活动起期!");
            }
        }
    }

    @Override
    @Transactional
    public void editCoupon(String loginName, CouponDto couponDto) throws CreateCouponException {
        this.checkCoupon(couponDto);
        CouponModel couponModel = new CouponModel(couponDto);
        couponModel.setId(couponDto.getId());
        couponModel.setUpdatedBy(loginName);
        couponModel.setUpdatedTime(new Date());
        couponMapper.updateCoupon(couponModel);
    }

    @Override
    @Transactional
    public void assignNewbieCoupon(String loginName) {
        List<CouponModel> newbieCoupons = couponMapper.findNewbieCoupon();
        Optional<CouponModel> found = Iterators.tryFind(newbieCoupons.iterator(), new Predicate<CouponModel>() {
            @Override
            public boolean apply(CouponModel couponModel) {
                return couponModel.isActive()
                        && couponModel.getStartTime().compareTo(new DateTime().withTimeAtStartOfDay().toDate()) <= 0
                        && couponModel.getEndTime().after(new DateTime().withTimeAtStartOfDay().toDate());
            }
        });

        if (found.isPresent()) {
            CouponModel couponModel = couponMapper.lockById(found.get().getId());
            couponModel.setIssuedCount(couponModel.getIssuedCount() + 1);
            couponModel.setTotalCount(couponModel.getTotalCount() + 1);
            couponMapper.updateCoupon(couponModel);

            UserCouponModel userCouponModel = new UserCouponModel(loginName, couponModel.getId());
            userCouponMapper.create(userCouponModel);
        }
    }

    @Override
    public List<CouponDto> findCoupons(int index, int pageSize) {
        List<CouponModel> couponModels = couponMapper.findCoupons((index - 1) * pageSize, pageSize);
        for (CouponModel couponModel : couponModels) {
            couponModel.setTotalInvestAmount(userCouponMapper.findSumInvestAmountByCouponId(couponModel.getId()));
        }
        return Lists.transform(couponModels, new Function<CouponModel, CouponDto>() {
            @Override
            public CouponDto apply(CouponModel input) {
                return new CouponDto(input);
            }
        });
    }

    @Override
    public int findCouponsCount() {
        return couponMapper.findCouponsCount();
    }


    @Override
    public CouponModel findCouponById(long couponId) {
        return couponMapper.findById(couponId);
    }

    @Override
    public long findEstimatedCount(UserGroup userGroup) {
        if (userGroup == UserGroup.INVESTED_USER) {
            return investMapper.findInvestorCount();
        }
        if (userGroup == UserGroup.REGISTERED_NOT_INVESTED_USER) {
            return investMapper.findRegisteredNotInvestCount();
        }
        return 0;
    }

    @Override
    public List<UserCouponModel> findCouponDetail(long couponId, Boolean isUsed) {
        List<UserCouponModel> userCouponModels = userCouponMapper.findByCouponIdAndStatus(couponId, isUsed);
        for (UserCouponModel userCouponModel : userCouponModels) {
            userCouponModel.setLoanName(userCouponModel.getLoanId() != null ? loanMapper.findById(userCouponModel.getLoanId()).getName() : null);
            userCouponModel.setInvestAmount(userCouponModel.getInvestId() != null ? investMapper.findById(userCouponModel.getInvestId()).getAmount() : null);
        }
        return userCouponModels;
    }

    @Override
    public void deleteCoupon(String loginName, long couponId) {
        CouponModel couponModel = couponMapper.findById(couponId);
        couponModel.setUpdatedBy(loginName);
        couponModel.setUpdatedTime(new Date());
        couponModel.setDeleted(true);
        couponModel.setActive(false);
        couponMapper.updateCoupon(couponModel);
    }

    @Override
    public long estimateCouponExpectedInterest(long loanId, long couponId, long amount) {
        LoanModel loanModel = loanMapper.findById(loanId);
        CouponModel couponModel = couponMapper.findById(couponId);
        long expectedInterest = InterestCalculator.estimateCouponExpectedInterest(loanModel, couponModel, amount);

        long expectedFee = new BigDecimal(expectedInterest).multiply(new BigDecimal(loanModel.getInvestFeeRate())).setScale(0, BigDecimal.ROUND_DOWN).longValue();

        return expectedInterest - expectedFee;
    }
}
