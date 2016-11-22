package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepayMapper {

    void create(CouponRepayModel couponRepayModel);

    CouponRepayModel findByUserCouponIdAndPeriod(@Param(value = "userCouponId") long userCouponId,
                                                 @Param(value = "period") long period);

    long update(CouponRepayModel couponRepayModel);

    List<CouponRepayModel> findCouponRepayByInvestIdAndRepayDate(@Param(value = "loginName") String loginName,
                                                                 @Param(value = "investId") long investId,
                                                                 @Param(value = "year") String year,
                                                                 @Param(value = "month") String month,
                                                                 @Param(value = "day") String day);


    List<CouponRepayModel> findByUserCouponByInvestId(@Param(value = "investId") long investId);

    CouponRepayModel findByUserCouponByInvestIdAndPeriod(@Param(value = "investId") long investId,
                                                         @Param(value = "period") int period);

    List<CouponRepayModel> findCouponRepayByLoanIdAndPeriod(@Param(value = "loanId") long loanId, @Param(value = "period") int period);

    CouponRepayModel findCouponRepayByInvestIdAndPeriod(@Param(value = "investId") long investId, @Param(value = "period") int period);

    CouponRepayModel findById(@Param(value = "id") long id);
}
