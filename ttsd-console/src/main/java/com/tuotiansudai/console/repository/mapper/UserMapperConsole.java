package com.tuotiansudai.console.repository.mapper;

import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.repository.model.RemainUserView;
import com.tuotiansudai.console.repository.model.UserMicroModelView;
import com.tuotiansudai.console.repository.model.UserOperation;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository(value = "consoleUserMapper")
public interface UserMapperConsole {

    List<UserView> findAllUser(@Param(value = "loginName") String loginName,
                               @Param(value = "email") String email,
                               @Param(value = "mobile") String mobile,
                               @Param(value = "beginTime") Date beginTime,
                               @Param(value = "endTime") Date endTime,
                               @Param(value = "source") Source source,
                               @Param(value = "roleStage") RoleStage roleStage,
                               @Param(value = "referrerMobile") String referrerMobile,
                               @Param(value = "channel") String channel,
                               @Param(value = "userOperation") UserOperation userOperation,
                               @Param(value = "hasStaff") Boolean hasStaff,
                               @Param(value = "staffMobile") String staffMobile,
                               @Param(value = "index") Integer index,
                               @Param(value = "pageSize") Integer pageSize);

    int findAllUserCount(@Param(value = "loginName") String loginName,
                         @Param(value = "email") String email,
                         @Param(value = "mobile") String mobile,
                         @Param(value = "beginTime") Date beginTime,
                         @Param(value = "endTime") Date endTime,
                         @Param(value = "source") Source source,
                         @Param(value = "roleStage") RoleStage roleStage,
                         @Param(value = "referrerMobile") String referrerMobile,
                         @Param(value = "channel") String channel,
                         @Param(value = "userOperation") UserOperation userOperation,
                         @Param(value = "hasStaff") Boolean hasStaff,
                         @Param(value = "staffMobile") String staffMobile);

    int findUserCountByRegisterTime(@Param(value = "startTime") Date startTime);

    List<UserView> searchAllUsers(@Param(value = "loginName") String loginName,
                                  @Param(value = "referrerMobile") String referrerMobile,
                                  @Param(value = "mobile") String mobile,
                                  @Param(value = "identityNumber") String identityNumber);

    long findUsersAccountBalanceSum(@Param(value = "mobile") String mobile,
                                    @Param(value = "balanceMin") long balanceMin,
                                    @Param(value = "balanceMax") long balanceMax);


    long findUsersAccountBalanceCount(@Param(value = "mobile") String mobile,
                                      @Param(value = "balanceMin") long balanceMin,
                                      @Param(value = "balanceMax") long balanceMax);

    List<UserView> findUsersAccountBalance(@Param(value = "mobile") String mobile,
                                           @Param(value = "balanceMin") long balanceMin,
                                           @Param(value = "balanceMax") long balanceMax,
                                           @Param(value = "startLimit") Integer startLimit,
                                           @Param(value = "endLimit") Integer endLimit);

    long findUsersCountByChannel(@Param(value = "channel") String channel);

    List<String> findStaffByLikeLoginName(String loginName);

    List<String> findMobileLike(String mobile);

    List<String> findLoginNameLike(String loginName);

    List<String> findAllLoanerLikeLoginName(String loginName);

    List<String> findAccountLikeLoginName(String loginName);

    int findUserMicroModelCount(@Param(value = "mobile") String mobile,
                                @Param(value = "role") Role role,
                                @Param(value = "registerTimeStart") Date registerTimeStart,
                                @Param(value = "registerTimeEnd") Date registerTimeEnd,
                                @Param(value = "hasCertify") String hasCertify,
                                @Param(value = "invested") String invested,
                                @Param(value = "totalInvestAmountStart") Long totalInvestAmountStart,
                                @Param(value = "totalInvestAmountEnd") Long totalInvestAmountEnd,
                                @Param(value = "totalWithdrawAmountStart") Long totalWithdrawAmountStart,
                                @Param(value = "totalWithdrawAmountEnd") Long totalWithdrawAmountEnd,
                                @Param(value = "userBalanceStart") Long userBalanceStart,
                                @Param(value = "userBalanceEnd") Long userBalanceEnd,
                                @Param(value = "investCountStart") Integer investCountStart,
                                @Param(value = "investCountEnd") Integer investCountEnd,
                                @Param(value = "loanCountStart") Integer loanCountStart,
                                @Param(value = "loanCountEnd") Integer loanCountEnd,
                                @Param(value = "transformPeriodStart") Integer transformPeriodStart,
                                @Param(value = "transformPeriodEnd") Integer transformPeriodEnd,
                                @Param(value = "invest1st2ndTimingStart") Integer invest1st2ndTimingStart,
                                @Param(value = "invest1st2ndTimingEnd") Integer invest1st2ndTimingEnd,
                                @Param(value = "invest1st3ndTimingStart") Integer invest1st3ndTimingStart,
                                @Param(value = "invest1st3ndTimingEnd") Integer invest1st3ndTimingEnd,
                                @Param(value = "lastInvestTimeStart") Date lastInvestTimeStart,
                                @Param(value = "lastInvestTimeEnd") Date lastInvestTimeEnd,
                                @Param(value = "repayingAmountStart") Long repayingAmountStart,
                                @Param(value = "repayingAmountEnd") Long repayingAmountEnd,
                                @Param(value = "lastLoginTimeStart") Date lastLoginTimeStart,
                                @Param(value = "lastLoginTimeEnd") Date lastLoginTimeEnd,
                                @Param(value = "lastLoginSource") Source lastLoginSource,
                                @Param(value = "lastRepayTimeStart") Date lastRepayTimeStart,
                                @Param(value = "lastRepayTimeEnd") Date lastRepayTimeEnd,
                                @Param(value = "lastWithdrawTimeStart") Date lastWithdrawTimeStart,
                                @Param(value = "lastWithdrawTimeEnd") Date lastWithdrawTimeEnd);

    List<UserMicroModelView> queryUserMicroModel(@Param(value = "mobile") String mobile,
                                                 @Param(value = "role") Role role,
                                                 @Param(value = "registerTimeStart") Date registerTimeStart,
                                                 @Param(value = "registerTimeEnd") Date registerTimeEnd,
                                                 @Param(value = "hasCertify") String hasCertify,
                                                 @Param(value = "invested") String invested,
                                                 @Param(value = "totalInvestAmountStart") Long totalInvestAmountStart,
                                                 @Param(value = "totalInvestAmountEnd") Long totalInvestAmountEnd,
                                                 @Param(value = "totalWithdrawAmountStart") Long totalWithdrawAmountStart,
                                                 @Param(value = "totalWithdrawAmountEnd") Long totalWithdrawAmountEnd,
                                                 @Param(value = "userBalanceStart") Long userBalanceStart,
                                                 @Param(value = "userBalanceEnd") Long userBalanceEnd,
                                                 @Param(value = "investCountStart") Integer investCountStart,
                                                 @Param(value = "investCountEnd") Integer investCountEnd,
                                                 @Param(value = "loanCountStart") Integer loanCountStart,
                                                 @Param(value = "loanCountEnd") Integer loanCountEnd,
                                                 @Param(value = "transformPeriodStart") Integer transformPeriodStart,
                                                 @Param(value = "transformPeriodEnd") Integer transformPeriodEnd,
                                                 @Param(value = "invest1st2ndTimingStart") Integer invest1st2ndTimingStart,
                                                 @Param(value = "invest1st2ndTimingEnd") Integer invest1st2ndTimingEnd,
                                                 @Param(value = "invest1st3ndTimingStart") Integer invest1st3ndTimingStart,
                                                 @Param(value = "invest1st3ndTimingEnd") Integer invest1st3ndTimingEnd,
                                                 @Param(value = "lastInvestTimeStart") Date lastInvestTimeStart,
                                                 @Param(value = "lastInvestTimeEnd") Date lastInvestTimeEnd,
                                                 @Param(value = "repayingAmountStart") Long repayingAmountStart,
                                                 @Param(value = "repayingAmountEnd") Long repayingAmountEnd,
                                                 @Param(value = "lastLoginTimeStart") Date lastLoginTimeStart,
                                                 @Param(value = "lastLoginTimeEnd") Date lastLoginTimeEnd,
                                                 @Param(value = "lastLoginSource") Source lastLoginSource,
                                                 @Param(value = "lastRepayTimeStart") Date lastRepayTimeStart,
                                                 @Param(value = "lastRepayTimeEnd") Date lastRepayTimeEnd,
                                                 @Param(value = "lastWithdrawTimeStart") Date lastWithdrawTimeStart,
                                                 @Param(value = "lastWithdrawTimeEnd") Date lastWithdrawTimeEnd,
                                                 @Param(value = "index") int index,
                                                 @Param(value = "pageSize") int pageSize);

    List<RemainUserView> findRemainUsers(@Param(value = "loginName") String loginName,
                                         @Param(value = "mobile") String mobile,
                                         @Param(value = "registerStartTime") Date registerStartTime,
                                         @Param(value = "registerEndTime") Date registerEndTime,
                                         @Param(value = "useExperienceCoupon") Boolean useExperienceCoupon,
                                         @Param(value = "experienceStartTime") Date experienceStartTime,
                                         @Param(value = "experienceEndTime") Date experienceEndTime,
                                         @Param(value = "investCountLowLimit") Integer investCountLowLimit,
                                         @Param(value = "investCountHighLimit") Integer investCountHighLimit,
                                         @Param(value = "investSumLowLimit") Long investSumLowLimit,
                                         @Param(value = "investSumHighLimit") Long investSumHighLimit,
                                         @Param(value = "firstInvestStartTime") Date firstInvestStartTime,
                                         @Param(value = "firstInvestEndTime") Date firstInvestEndTime,
                                         @Param(value = "secondInvestStartTime") Date secondInvestStartTime,
                                         @Param(value = "secondInvestEndTime") Date secondInvestEndTime,
                                         @Param(value = "index") int index, @Param(value = "pageSize") int pageSize);

    long findRemainUsersCount(@Param(value = "loginName") String loginName,
                              @Param(value = "mobile") String mobile,
                              @Param(value = "registerStartTime") Date registerStartTime,
                              @Param(value = "registerEndTime") Date registerEndTime,
                              @Param(value = "useExperienceCoupon") Boolean useExperienceCoupon,
                              @Param(value = "experienceStartTime") Date experienceStartTime,
                              @Param(value = "experienceEndTime") Date experienceEndTime,
                              @Param(value = "investCountLowLimit") Integer investCountLowLimit,
                              @Param(value = "investCountHighLimit") Integer investCountHighLimit,
                              @Param(value = "investSumLowLimit") Long investSumLowLimit,
                              @Param(value = "investSumHighLimit") Long investSumHighLimit,
                              @Param(value = "firstInvestStartTime") Date firstInvestStartTime,
                              @Param(value = "firstInvestEndTime") Date firstInvestEndTime,
                              @Param(value = "secondInvestStartTime") Date secondInvestStartTime,
                              @Param(value = "secondInvestEndTime") Date secondInvestEndTime);

    List<UserView> findExperienceBalance(@Param(value = "mobile") String mobile,
                                          @Param(value = "balanceMin") String balanceMin,
                                          @Param(value = "balanceMax") String balanceMax,
                                          @Param(value = "index") Integer index,
                                          @Param(value = "pageSize") Integer pageSize);

    int findCountExperienceBalance(@Param(value = "mobile") String mobile,
                                   @Param(value = "balanceMin") String balanceMin,
                                   @Param(value = "balanceMax") String balanceMax);

    long sumExperienceBalance(@Param(value = "mobile") String mobile,
                              @Param(value = "balanceMin") String balanceMin,
                              @Param(value = "balanceMax") String balanceMax);

    List<String> findAllUserChannels();
}
