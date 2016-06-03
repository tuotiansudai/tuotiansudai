package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.coupon.repository.model.UserCouponView;
import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.CouponType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserCouponMapper {

    void create(UserCouponModel userCouponModel);

    void update(UserCouponModel userCouponModel);

    List<UserCouponModel> findByLoginName(@Param("loginName") String loginName, @Param("couponTypeList") List<CouponType> couponTypeList);

    UserCouponModel findById(@Param("id") long id);

    List<UserCouponModel> findByLoanId(@Param("loanId") Long loanId, @Param("couponTypeList") List<CouponType> couponTypeList);

    List<UserCouponView> findUnusedCoupons(@Param(value = "loginName") String loginName);

    List<UserCouponView> findUseRecords(@Param(value = "loginName") String loginName);

    List<UserCouponView> findExpiredCoupons(@Param(value = "loginName") String loginName);

    List<UserCouponModel> findByInvestId(@Param("investId") long investId);

    long findSumInvestAmountByCouponId(@Param("couponId") long couponId);

    List<UserCouponModel> findByCouponIdAndStatus(@Param("couponId") long couponId, @Param("isUsed") Boolean isUsed,
                                                  @Param("loginName") String loginName, @Param("mobile") String mobile,
                                                  @Param("registerStartTime") Date registerStartTime, @Param("registerEndTime") Date registerEndTime,
                                                  @Param("index") int index, @Param("pageSize") int pageSize);

    int findCouponDetailCount(@Param("couponId") long couponId, @Param("isUsed") Boolean isUsed,
                              @Param("loginName") String loginName, @Param("mobile") String mobile,
                              @Param("registerStartTime") Date registerStartTime, @Param("registerEndTime") Date registerEndTime);

    List<UserCouponModel> findByCouponId(long couponId);

    List<UserCouponModel> findByLoginNameAndCouponId(@Param("loginName") String loginName, @Param("couponId") long couponId);

    List<UserCouponModel> findBirthdaySuccessByLoginNameAndInvestId(@Param("loginName") String loginName, @Param("investId") long investId);

}
