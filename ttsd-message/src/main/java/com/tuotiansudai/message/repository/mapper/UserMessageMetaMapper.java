package com.tuotiansudai.message.repository.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.ognl.ObjectElementsAccessor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMessageMetaMapper {

    Map<String, Object> findLoanById(@Param(value = "id") long id);

    String findUserNameByLoginName(@Param(value = "loginName") String loginName);

    Map<String, Object> findLoanByLoanRepayId(@Param(value = "loanRepayId") long loanRepayId);

    List<String> findSuccessInvestorByLoanId(@Param(value = "loanId") long loanId);

    List<Map<String, Object>> findInvestReferrerRewardByLoanId(@Param(value = "loanId") long loanId);

    List<Map<String, Object>> findCouponWillExpire(@Param(value = "loginName") String loginName);

    Map<String, Object> findWithdrawById(@Param(value = "id") long id);

    Map<String, Object> findInvestById(@Param(value = "id") long id);

    Map<String, Object> findTransferApplicationByInvestId(@Param(value = "investId") long investId);

    boolean isExpiredLevelFiveMembershipExisted(@Param(value = "loginName") String loginName);

    Map<String, Object> findLoanRepayById(@Param(value = "id") long id);

    List<Map<String, Object>> findInvestsByLoanId(@Param(value = "loanId") long loanId);

    Map<String, Object> findInvestRepayByInvestIdAndPeriod(@Param(value = "investId") long investId, @Param(value = "period") int period);

    List<String> findBirthDayUsers();

    Map<String, Object> findMembershipById(@Param(value = "id") long id);

    Map<String, Object> findTransferApplicationById(@Param(value = "id") long id);

    Map<String,Object> findAssignUserCoupon(@Param(value = "userCouponId") long userCouponId);

    Map<String, Object> findMembershipPurchaseModelById(@Param(value = "id") long id);
}
