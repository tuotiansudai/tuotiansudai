package com.tuotiansudai.coupon.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.tuotiansudai.coupon.service.CouponAssignmentService;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.dto.UserCouponDto;
import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.membership.service.MembershipPrivilegePurchaseService;
import com.tuotiansudai.repository.mapper.*;
import com.tuotiansudai.repository.model.*;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCouponServiceImpl implements UserCouponService {

    static Logger logger = Logger.getLogger(UserCouponServiceImpl.class);

    @Autowired
    private LoanMapper loanMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private InvestMapper investMapper;

    @Autowired
    private CouponAssignmentService couponAssignmentService;

    @Autowired
    private MembershipPrivilegePurchaseService membershipPrivilegePurchaseService;

    @Autowired
    private LoanDetailsMapper loanDetailsMapper;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Value(value = "${web.coupon.lock.seconds}")
    private int couponLockSeconds;

    @Override
    public List<UserCouponView> getUnusedUserCoupons(String loginName) {
        List<UserCouponView> unusedCoupons = userCouponMapper.findUnusedCoupons(loginName);

        for (int i = unusedCoupons.size() - 1; i >= 0; i--) {
            if (unusedCoupons.get(i).getCouponType() == CouponType.BIRTHDAY_COUPON) {
                unusedCoupons.remove(i);
            }
        }

        unusedCoupons = unusedCoupons
                .stream()
                .filter(unusedCoupon -> unusedCoupon.getUsedTime() == null || new DateTime(unusedCoupon.getUsedTime()).plusSeconds(couponLockSeconds).isBefore(new DateTime()))
                .collect(Collectors.toList());


        Collections.sort(unusedCoupons);
        return unusedCoupons;
    }

    @Override
    public List<UserCouponView> findUseRecords(String loginName) {
        return userCouponMapper.findUseRecords(loginName);
    }

    @Override
    public List<UserCouponView> getExpiredUserCoupons(String loginName) {
        List<UserCouponView> expiredCoupons = userCouponMapper.findExpiredCoupons(loginName);

        for (int i = expiredCoupons.size() - 1; i >= 0; i--) {
            if (expiredCoupons.get(i).getCouponType() == CouponType.BIRTHDAY_COUPON) {
                expiredCoupons.remove(i);
            }
        }
        Collections.sort(expiredCoupons);
        return expiredCoupons;
    }

    @Override
    public List<UserCouponDto> getInvestUserCoupons(String loginName, long loanId) {
        if (loanDetailsMapper.getByLoanId(loanId).getDisableCoupon()) {
            return Lists.newArrayList();
        }

        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, null);

        List<UserCouponDto> dtoList = userCouponModels
                .stream()
                .map(userCouponModel -> new UserCouponDto(couponMapper.findById(userCouponModel.getCouponId()), userCouponModel, couponLockSeconds))
                .collect(Collectors.toList());

        return dtoList.stream().filter(UserCouponDto::isUnused).collect(Collectors.toList());
    }

    @Override
    public UserCouponDto getExperienceInvestUserCoupon(String loginName) {
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, Lists.newArrayList(CouponType.NEWBIE_COUPON));

        Optional<UserCouponModel> userCouponModelOptional = Iterators.tryFind(userCouponModels.iterator(), new Predicate<UserCouponModel>() {
            @Override
            public boolean apply(UserCouponModel userCouponModel) {
                CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                return couponModel.getProductTypes().contains(ProductType.EXPERIENCE)
                        && userCouponModel.getStatus() != InvestStatus.SUCCESS
                        && userCouponModel.getEndTime().after(new Date());
            }
        });

        if (userCouponModelOptional.isPresent()) {
            UserCouponModel userCouponModel = userCouponModelOptional.get();
            return new UserCouponDto(couponMapper.findById(userCouponModel.getCouponId()), userCouponModel, 0);
        }

        return null;
    }

    /**
     * 收益相同时，选择最早即将过期的优惠券。过期时间仍然相同时，按照生日福利、红包、新手体验券、投资体验券、加息券的顺序选择，若券的类别也相同时，任选其一。
     */
    @Override
    public UserCouponDto getMaxBenefitUserCoupon(String loginName, long loanId, final long amount) {
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, null);

        final LoanModel loanModel = loanMapper.findById(loanId);
        if (loanModel == null || amount < loanModel.getMinInvestAmount() || amount > loanModel.getMaxInvestAmount()) {
            return null;
        }
        if (loanDetailsMapper.getByLoanId(loanId).getDisableCoupon()) {
            return null;
        }

        List<UserCouponModel> usableUserCoupons = Lists.newArrayList(Iterators.filter(userCouponModels.iterator(), new Predicate<UserCouponModel>() {
            @Override
            public boolean apply(UserCouponModel userCouponModel) {
                CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                boolean isShared = couponModel.isShared();
                boolean unused = InvestStatus.SUCCESS != userCouponModel.getStatus()
                        && userCouponModel.getEndTime().after(new Date())
                        && (userCouponModel.getUsedTime() == null || new DateTime(userCouponModel.getUsedTime()).plusSeconds(couponLockSeconds).isBefore(new DateTime()));
                boolean productTypeEnable = couponModel.getProductTypes().contains(loanModel.getProductType());
                boolean isGreatThanInvestLowerLimit = couponModel.getInvestLowerLimit() <= amount;
                return !isShared && unused && productTypeEnable && isGreatThanInvestLowerLimit;
            }
        }));

        double investFeeRate = membershipPrivilegePurchaseService.obtainServiceFee(loginName);

        List<UserCouponModel> maxBenefitUserCoupons = Lists.newArrayList();
        long maxBenefit = 0;
        for (UserCouponModel usableUserCoupon : usableUserCoupons) {
            CouponModel couponModel = couponMapper.findById(usableUserCoupon.getCouponId());
            long expectedInterest = InterestCalculator.estimateCouponExpectedInterest(amount, loanModel, couponModel, new Date());
            long expectedFee = InterestCalculator.estimateCouponExpectedFee(loanModel, couponModel, amount, investFeeRate);
            long actualInterest = expectedInterest - expectedFee;
            if (maxBenefit == actualInterest) {
                maxBenefitUserCoupons.add(usableUserCoupon);
            }
            if (maxBenefit < actualInterest) {
                maxBenefit = actualInterest;
                maxBenefitUserCoupons = Lists.newArrayList(usableUserCoupon);
            }
        }

        final List<CouponType> couponTypePriority = Lists.newArrayList(CouponType.BIRTHDAY_COUPON, CouponType.RED_ENVELOPE, CouponType.NEWBIE_COUPON, CouponType.INVEST_COUPON, CouponType.INTEREST_COUPON);

        List<UserCouponModel> orderingMaxBenefitUserCoupons = new Ordering<UserCouponModel>() {
            @Override
            public int compare(UserCouponModel left, UserCouponModel right) {
                int endTimeCompare = Longs.compare(left.getEndTime().getTime(), right.getEndTime().getTime());
                if (endTimeCompare != 0) {
                    return endTimeCompare;
                }

                CouponType leftCouponType = couponMapper.findById(left.getCouponId()).getCouponType();
                CouponType rightCouponType = couponMapper.findById(right.getCouponId()).getCouponType();
                return Ints.compare(couponTypePriority.indexOf(leftCouponType), couponTypePriority.indexOf(rightCouponType));
            }
        }.sortedCopy(maxBenefitUserCoupons);

        return orderingMaxBenefitUserCoupons.isEmpty() ? null :
                new UserCouponDto(couponMapper.findById(orderingMaxBenefitUserCoupons.get(0).getCouponId()), orderingMaxBenefitUserCoupons.get(0), 0);
    }

    @Override
    public boolean isUsableUserCouponExist(String loginName) {
        final List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, null);
        return Iterators.tryFind(userCouponModels.iterator(), new Predicate<UserCouponModel>() {
            @Override
            public boolean apply(UserCouponModel input) {
                return InvestStatus.SUCCESS != input.getStatus() && input.getEndTime().after(new Date());
            }
        }).isPresent();
    }

    @Override
    @Transactional
    public void updateCouponAndAssign(long investId) {
        InvestModel investModel = investMapper.findById(investId);
        LoanModel loanModel = loanMapper.findById(investModel.getLoanId());
        List<UserCouponModel> userCouponModels = userCouponMapper.findByInvestId(investId);

        for (UserCouponModel userCoupon : userCouponModels) {
            if (userCoupon.getStatus() == InvestStatus.SUCCESS) {
                // 如果用户优惠券状态已经被更新为Success，则不再重复处理。
                logger.info(MessageFormat.format("coupon status is already SUCCESS, won't update again. investId:{0}, userCouponId:{1}",
                        String.valueOf(investId), String.valueOf(userCoupon.getId())));
                continue;
            }
            CouponModel couponModel = couponMapper.lockById(userCoupon.getCouponId());
            couponModel.setUsedCount(couponModel.getUsedCount() + 1);
            couponMapper.updateCoupon(couponModel);

            userCoupon.setLoanId(loanModel.getId());
            userCoupon.setInvestId(investId);
            userCoupon.setUsedTime(new Date());
            userCoupon.setStatus(InvestStatus.SUCCESS);
            long expectedInterest = InterestCalculator.estimateCouponExpectedInterest(investModel.getAmount(), loanModel, couponModel, new Date());
            long expectedFee = InterestCalculator.estimateCouponExpectedFee(loanModel, couponModel, investModel.getAmount(), investModel.getInvestFeeRate());
            userCoupon.setExpectedInterest(expectedInterest);
            userCoupon.setExpectedFee(expectedFee);
            userCouponMapper.update(userCoupon);
        }

        couponAssignmentService.asyncAssignUserCoupon(investModel.getLoginName(), Lists.newArrayList(UserGroup.ALL_USER,
                UserGroup.INVESTED_USER,
                UserGroup.IMPORT_USER,
                UserGroup.AGENT,
                UserGroup.CHANNEL,
                UserGroup.STAFF,
                UserGroup.STAFF_RECOMMEND_LEVEL_ONE));
    }
}