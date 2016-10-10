package com.tuotiansudai.message.repository.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMessageMetaMapper {

    String findLoanNameById(@Param(value = "id") long id);

    Map<String, Object> findLoanByLoanRepayId(@Param(value = "loanRepayId") long loanRepayId);

    List<String> findSuccessInvestorByLoanId(@Param(value = "loanId") long loanId);

    List<Map<String, Object>> findInvestReferrerRewardByLoanId(@Param(value = "loanId") long loanId);

    List<Map<String, Object>> findCouponWillExpire(@Param(value = "loginName") String loginName);

    Map<String,Object> findAssignUserCoupon(@Param(value = "userCouponId") long userCouponId);

    Map<String,Object> findRechargeById(@Param(value = "id") long id);

    Map<String,Object> findWithdrawById(@Param(value = "id") long id);

    Map<String,Object> findInvestById(@Param(value = "id") long id);

    Map<String,Object> findTransferApplicationByInvestId(@Param(value = "investId") long investId);

    boolean isExpiredLevelFiveMembershipExisted(@Param(value = "loginName") String loginName);
}
