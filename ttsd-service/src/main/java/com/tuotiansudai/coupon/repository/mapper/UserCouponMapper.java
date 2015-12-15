package com.tuotiansudai.coupon.repository.mapper;

import com.tuotiansudai.coupon.repository.model.UserCouponModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCouponMapper {

    void create(UserCouponModel userCouponModel);

    void update(UserCouponModel userCouponModel);

    List<UserCouponModel> findByLoginName(@Param("loginName") String loginName);

    UserCouponModel findById(@Param("id") long id);

    List<UserCouponModel> findByLoanId(@Param("loanId") long loanId);
}
