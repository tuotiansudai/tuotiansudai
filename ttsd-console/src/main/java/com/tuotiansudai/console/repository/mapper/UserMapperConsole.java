package com.tuotiansudai.console.repository.mapper;

import com.tuotiansudai.console.bi.dto.RoleStage;
import com.tuotiansudai.console.repository.model.RemainUserView;
import com.tuotiansudai.console.repository.model.UserOperation;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserView;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.logging.Log;
import org.springframework.security.access.method.P;
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
                         @Param(value = "userOperation") UserOperation userOperation);

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

    long findRemainUsersCount(@Param(value = "loginName") String loginName, @Param(value = "mobile") String mobile,
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
}
