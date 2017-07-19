package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.CouponModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponMapper {

    void create(CouponModel couponModel);

    CouponModel findById(long id);

    CouponModel lockById(@Param(value = "id") long id);

    void updateCoupon(CouponModel couponModel);

    List<CouponModel> findCoupons(@Param(value = "index") int index,
                                  @Param(value = "pageSize") int pageSize);

    List<CouponModel> findCouponsByTypeRedAndMoney(@Param(value = "couponType") String couponType,
                                                   @Param(value = "couponSource") String couponSource,
                                                   @Param(value = "amount") int amount,
                                                   @Param(value = "index") int index,
                                                   @Param(value = "pageSize") int pageSize);

    int findCouponsCountByTypeRedAndMoney(@Param(value = "couponType") String couponType,
                                          @Param(value = "couponSource") String couponSource,
                                          @Param(value = "amount") int amount);

    List<CouponModel> findAllActiveCoupons();

    CouponModel findExchangeableCouponById(@Param(value = "id") long id);

}
