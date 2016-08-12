package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.coupon.repository.model.CouponRepayModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepayMapper {

    void create(List<CouponRepayModel> couponRepayModels);

    CouponRepayModel findByUserCouponIdAndPeriod(@Param(value = "userCouponId") long userCouponId,
                                                 @Param(value = "period") long period);

    void update(CouponRepayModel couponRepayModel);

    List<CouponRepayModel> findByUserCouponByInvestId(@Param(value = "investId") long investId);

    List<CouponRepayModel> findCouponRepayByLoanIdAndPeriod(@Param(value = "loanId") long loanId, @Param(value = "period") int period);

    CouponRepayModel findCouponRepayByInvestIdAndPeriod(@Param(value = "investId") long investId, @Param(value = "period") int period);
}
