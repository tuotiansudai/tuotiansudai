package com.tuotiansudai.coupon.service;

import com.tuotiansudai.coupon.dto.CouponDto;
import com.tuotiansudai.coupon.dto.ExchangeCouponDto;
import com.tuotiansudai.coupon.repository.model.CouponModel;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.coupon.repository.model.UserGroup;
import com.tuotiansudai.exception.CreateCouponException;
import com.tuotiansudai.repository.model.InvestModel;

import java.util.Date;
import java.util.List;

public interface CouponService {

    ExchangeCouponDto createCoupon(String loginName,ExchangeCouponDto exchangeCouponDto) throws CreateCouponException;

    void editCoupon(String loginName,ExchangeCouponDto exchangeCouponDto) throws CreateCouponException;

    List<CouponDto> findNewbieAndInvestCoupons(int index, int pageSize);

    int findNewbieAndInvestCouponsCount();

    CouponModel findCouponById (long couponId);

    long findEstimatedCount(UserGroup userGroup);

    List<UserCouponModel> findCouponDetail(long couponId, Boolean isUsed, String loginName, String mobile, Date registerStartTime, Date registerEndTime, int index, int pageSize);

    int findCouponDetailCount(long couponId, Boolean isUsed, String loginName, String mobile, Date registerStartTime, Date registerEndTime);

    boolean deleteCoupon(String loginName, long couponId);

    List<CouponDto> findInterestCoupons(int index, int pageSize);

    int findInterestCouponsCount();

    List<CouponDto> findRedEnvelopeCoupons(int index, int pageSize);

    int findRedEnvelopeCouponsCount();

    List<CouponDto> findBirthdayCoupons(int index, int pageSize);

    int findBirthdayCouponsCount();

    long estimateCouponExpectedInterest(String loginName, long loanId, List<Long> couponIds, long amount);

    long findExperienceInvestAmount(List<InvestModel> investModelList);

    CouponModel findExchangeableCouponById(long couponId);

}
