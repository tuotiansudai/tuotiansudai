package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.Source;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserMapper {

    UserModel findByEmail(String email);

    UserModel findByMobile(String mobile);

    UserModel findByLoginName(String loginName);

    UserModel findByLoginNameOrMobile(String loginNameOrMobile);

    void create(UserModel userModel);

    void updateUser(UserModel userModel);

    List<UserModel> searchAllUsers(@Param(value = "loginName") String loginName, @Param(value = "referrer") String referrer, @Param(value = "mobile") String mobile,
                                   @Param(value = "identityNumber") String identityNumber);

    List<UserModel> findAllUser(@Param(value = "loginName") String loginName,
                                @Param(value = "email") String email,
                                @Param(value = "mobile") String mobile,
                                @Param(value = "beginTime") Date beginTime,
                                @Param(value = "endTime") Date endTime,
                                @Param(value = "source") Source source,
                                @Param(value = "role") Role role,
                                @Param(value = "referrer") String referrer,
                                @Param(value = "channel") String channel,
                                @Param(value = "index") Integer index,
                                @Param(value = "pageSize") Integer pageSize);

    int findAllUserCount(@Param(value = "loginName") String loginName,
                         @Param(value = "email") String email,
                         @Param(value = "mobile") String mobile,
                         @Param(value = "beginTime") Date beginTime,
                         @Param(value = "endTime") Date endTime,
                         @Param(value = "source") Source source,
                         @Param(value = "role") Role role,
                         @Param(value = "referrer") String referrer,
                         @Param(value = "channel") String channel);


    void updatePasswordByLoginName(@Param(value = "loginName") String loginName, @Param(value = "password") String password);

    List<String> findStaffByLikeLoginName(String loginName);

    List<String> findLoginNameLike(String loginName);

    List<String> findAllChannels();

    List<String> findAllUserChannels();

    List<UserModel> findUserByProvince();

    int findUserCount();

    List<UserModel> findUsersAccountBalance(@Param(value = "loginName") String loginName,
                                            @Param(value = "balanceMin") int balanceMin,
                                            @Param(value = "balanceMax") int balanceMax,
                                            @Param(value = "startLimit") int startLimit,
                                            @Param(value = "endLimit") int endLimit);

    long findUsersAccountBalanceSum(@Param(value = "loginName") String loginName,
                                    @Param(value = "balanceMin") int balanceMin,
                                    @Param(value = "balanceMax") int balanceMax);

    int findUsersAccountBalanceCount(@Param(value = "loginName") String loginName,
                                     @Param(value = "balanceMin") int balanceMin,
                                     @Param(value = "balanceMax") int balanceMax);
}
