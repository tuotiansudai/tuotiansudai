package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.coupon.repository.model.CouponModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponMapper {

    void create(CouponModel couponModel);

    CouponModel findById(long id);

    CouponModel lockById(@Param(value = "id") long id);

    void updateCoupon(CouponModel couponModel);

    List<CouponModel> findNewbieAndInvestCoupons(@Param(value = "index") int index, @Param(value = "pageSize") int pageSize);

    List<CouponModel> findInterestCoupons(@Param(value = "index") int index, @Param(value = "pageSize") int pageSize);

    List<CouponModel> findRedEnvelopeCoupons(@Param(value = "index") int index, @Param(value = "pageSize") int pageSize);

    List<CouponModel> findBirthdayCoupons(@Param(value = "index") int index, @Param(value = "pageSize") int pageSize);

    int findNewbieAndInvestCouponsCount();

    int findInterestCouponsCount();

    int findRedEnvelopeCouponsCount();

    int findBirthdayCouponsCount();

    List<CouponModel> findAllActiveCoupons();

    List<CouponModel> findCouponExchanges(@Param(value = "index") int index, @Param(value = "pageSize") int pageSize);

    int findCouponExchangeCount();

    List<CouponModel> findExchangeableCoupons(@Param(value = "index") Integer index, @Param(value = "pageSize") Integer pageSize);

    int findCouponExchangeableCount();
}
