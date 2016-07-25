package com.tuotiansudai.coupon.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.tuotiansudai.coupon.dto.UserCouponDto;
import com.tuotiansudai.coupon.repository.mapper.CouponMapper;
import com.tuotiansudai.coupon.repository.mapper.UserCouponMapper;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponView;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.coupon.service.UserCouponService;
import com.tuotiansudai.membership.repository.model.MembershipModel;
import com.tuotiansudai.membership.service.UserMembershipEvaluator;
import com.tuotiansudai.repository.mapper.InvestMapper;
import com.tuotiansudai.repository.mapper.LoanMapper;
import com.tuotiansudai.repository.model.CouponType;
import com.tuotiansudai.repository.model.InvestStatus;
import com.tuotiansudai.repository.model.LoanModel;
import com.tuotiansudai.repository.model.ProductType;
import com.tuotiansudai.util.InterestCalculator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
    private UserMembershipEvaluator userMembershipEvaluator;

    @Autowired
    private InvestMapper investMapper;

    @Value(value = "${pay.interest.fee}")
    private double defaultFee;

    @Override
    public List<UserCouponView> getUnusedUserCoupons(String loginName) {
        List<UserCouponView> unusedCoupons = userCouponMapper.findUnusedCoupons(loginName);

        for (int i = unusedCoupons.size() - 1; i >= 0; i--) {
            if (unusedCoupons.get(i).getCouponType() == CouponType.BIRTHDAY_COUPON) {
                unusedCoupons.remove(i);
            }
        }
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
        List<UserCouponModel> userCouponModels = userCouponMapper.findByLoginName(loginName, null);

        List<UserCouponDto> dtoList = Lists.transform(userCouponModels, new Function<UserCouponModel, UserCouponDto>() {
            @Override
            public UserCouponDto apply(UserCouponModel userCouponModel) {
                return new UserCouponDto(couponMapper.findById(userCouponModel.getCouponId()), userCouponModel);
            }
        });

        return Lists.newArrayList(Iterators.filter(dtoList.iterator(), new Predicate<UserCouponDto>() {
            @Override
            public boolean apply(UserCouponDto dto) {
                return dto.isUnused();
            }
        }));
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
            return new UserCouponDto(couponMapper.findById(userCouponModel.getCouponId()), userCouponModel);
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

        List<UserCouponModel> usableUserCoupons = Lists.newArrayList(Iterators.filter(userCouponModels.iterator(), new Predicate<UserCouponModel>() {
            @Override
            public boolean apply(UserCouponModel userCouponModel) {
                CouponModel couponModel = couponMapper.findById(userCouponModel.getCouponId());
                boolean isShared = couponModel.isShared();
                boolean unused = InvestStatus.SUCCESS != userCouponModel.getStatus() && userCouponModel.getEndTime().after(new Date());
                boolean productTypeEnable = couponModel.getProductTypes().contains(loanModel.getProductType());
                boolean isGreatThanInvestLowerLimit = couponModel.getInvestLowerLimit() <= amount;
                return !isShared && unused && productTypeEnable && isGreatThanInvestLowerLimit;
            }
        }));

        MembershipModel membershipModel = userMembershipEvaluator.evaluate(loginName);
        double investFeeRate = membershipModel != null ? membershipModel.getFee() : this.defaultFee;

        List<UserCouponModel> maxBenefitUserCoupons = Lists.newArrayList();
        long maxBenefit = 0;
        for (UserCouponModel usableUserCoupon : usableUserCoupons) {
            CouponModel couponModel = couponMapper.findById(usableUserCoupon.getCouponId());
            long expectedInterest = InterestCalculator.estimateCouponExpectedInterest(amount, loanModel, couponModel);
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
                new UserCouponDto(couponMapper.findById(orderingMaxBenefitUserCoupons.get(0).getCouponId()), orderingMaxBenefitUserCoupons.get(0));
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
    public long findSumBirthdayAndInterestByLoginName(String loginName) {
        return userCouponMapper.findSumBirthdayAndInterestByLoginName(loginName);
    }

    @Override
    public long findSumRedEnvelopeByLoginName(String loginName) {
        return userCouponMapper.findSumRedEnvelopeByLoginName(loginName);
    }

    @Override
    public void createInvestAchievementCoupon(long loanId) {
        LoanModel loanModel = loanMapper.findById(loanId);
        createUserCouponModel(loanModel.getFirstInvestAchievementId(),UserGroup.FIRST_INVEST_ACHIEVEMENT,loanId);
        createUserCouponModel(loanModel.getMaxAmountAchievementId(),UserGroup.MAX_AMOUNT_ACHIEVEMENT,loanId);
        createUserCouponModel(loanModel.getLastInvestAchievementId(),UserGroup.LAST_INVEST_ACHIEVEMENT,loanId);
    }

    public void createUserCouponModel(Long investId,final UserGroup userGroup,long loanId){
        if(investId == null){
            logger.error(MessageFormat.format("loan id : {0} nothing {1}",String.valueOf(loanId),userGroup.getDescription()));
            return;
        }

        List<CouponModel> couponModelList = couponMapper.findAllActiveCoupons();
        for(CouponModel couponModel : couponModelList){
            if(couponModel.getUserGroup().equals(userGroup)){
                userCouponMapper.create(new UserCouponModel(investMapper.findById(investId).getLoginName(),
                        couponModel.getId(), couponModel.getStartTime(), couponModel.getEndTime()));
            }
        }
    }
}