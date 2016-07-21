package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepayMapper {

    long create(CouponRepayModel couponRepayModel);

    CouponRepayModel findByUserCouponIdAndPeriod(@Param(value = "userCouponId") long userCouponId,
                                                 @Param(value = "period") long period);

    long update(CouponRepayModel couponRepayModel);

    List<CouponRepayModel> findCouponRepayByInvestIdAndRepayDate(@Param(value = "loginName") String loginName,
                                                                 @Param(value = "year") String year,
                                                                 @Param(value = "month") String month,
                                                                 @Param(value = "day") String day);
}
