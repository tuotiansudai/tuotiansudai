package com.tuotiansudai.console.repository.mapper;

import com.tuotiansudai.console.bi.dto.RoleStage;
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
                         @Param(value = "channel") String channel);

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
}
