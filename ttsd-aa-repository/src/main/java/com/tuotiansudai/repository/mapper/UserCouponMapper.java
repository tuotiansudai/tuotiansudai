package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.CouponType;
import com.tuotiansudai.repository.model.UserCouponModel;
import com.tuotiansudai.repository.model.UserCouponView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserCouponMapper {

    void create(UserCouponModel userCouponModel);

    void update(UserCouponModel userCouponModel);

    List<UserCouponModel> findByLoginName(@Param("loginName") String loginName, @Param("couponTypes") List<CouponType> couponTypes);

    List<UserCouponModel> findUserCouponWithCouponByLoginName(@Param("loginName") String loginName, @Param("couponTypes") List<CouponType> couponTypes);

    UserCouponModel findById(@Param("id") long id);

    UserCouponModel lockById(@Param("id") long id);

    List<UserCouponModel> findByLoanId(@Param("loanId") Long loanId, @Param("couponTypeList") List<CouponType> couponTypeList);

    List<UserCouponView> findUnusedCoupons(@Param(value = "loginName") String loginName);

    List<UserCouponView> findUseRecords(@Param(value = "loginName") String loginName);

    List<UserCouponView> findExpiredCoupons(@Param(value = "loginName") String loginName);

    List<UserCouponModel> findByInvestId(@Param("investId") long investId);

    List<UserCouponModel> findUserCouponSuccessByInvestId(@Param("investId") long investId);

    List<UserCouponModel> findUserCouponSuccessAndCouponTypeByInvestId(
            @Param("investId") long investId,
            @Param("couponTypeList") List<CouponType> couponTypeList
    );

    long findSumInvestAmountByCouponId(@Param("couponId") long couponId);

    List<UserCouponModel> findByCouponIdAndStatus(@Param("couponId") long couponId, @Param("isUsed") Boolean isUsed,
                                                  @Param("loginName") String loginName, @Param("mobile") String mobile,
                                                  @Param("createdTime") Date createdTime,
                                                  @Param("registerStartTime") Date registerStartTime, @Param("registerEndTime") Date registerEndTime,
                                                  @Param("investStartTime") Date investStartTime, @Param("investEndTime") Date investEndTime,
                                                  @Param("index") int index, @Param("pageSize") int pageSize);

    int findCouponDetailCount(@Param("couponId") long couponId, @Param("isUsed") Boolean isUsed,
                              @Param("loginName") String loginName, @Param("mobile") String mobile,
                              @Param("registerStartTime") Date registerStartTime, @Param("registerEndTime") Date registerEndTime,
                              @Param("investStartTime") Date investStartTime, @Param("investEndTime") Date investEndTime);

    List<UserCouponModel> findByCouponId(long couponId);

    List<UserCouponModel> findByLoginNameAndCouponId(@Param("loginName") String loginName, @Param("couponId") Long couponId);

    List<UserCouponModel> findBirthdaySuccessByLoginNameAndInvestId(@Param("loginName") String loginName, @Param("investId") long investId);

    int findByExchangeCode(@Param("exchangeCode") String exchangeCode);

    List<UserCouponModel> findUsedExperienceByLoginName(@Param("loginName") String loginName);

    List<UserCouponModel> findExpireAfterFiveDays();

    long findSumAmountByCouponId(@Param("loginName") String loginName, @Param("couponIdList") List<Long> couponIdList);
}
