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

    List<UserModel> searchAllUsers(@Param(value = "loginName") String loginName,@Param(value = "referrer") String referrer,@Param(value = "mobile") String mobile,
                                   @Param(value = "identityNumber") String identityNumber,@Param(value = "startLimit") Integer startLimit,@Param(value = "endLimit") Integer endLimit);

    int searchAllUsersCount(@Param(value = "loginName") String loginName,@Param(value = "referrer") String referrer,@Param(value = "mobile") String mobile,@Param(value = "identityNumber") String identityNumber);

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

    List<String> findLoginNameLike(String loginName);

    List<String> findAllChannels();

    List<UserModel> findUserByProvince();
    
    int findUserCount();
}
