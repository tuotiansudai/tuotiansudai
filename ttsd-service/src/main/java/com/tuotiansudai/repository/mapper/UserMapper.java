package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserStatus;
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

    void updatePassword(@Param(value = "mobile")String mobile,@Param(value = "password")String password);

    void updateUser(UserModel userModel);

    List<UserModel> findAllUser(@Param(value = "loginName") String loginName,
                                @Param(value = "email") String email,
                                @Param(value = "mobile") String mobile,
                                @Param(value = "beginTime") Date beginTime,
                                @Param(value = "endTime") Date endTime,
                                @Param(value = "role") Role role,
                                @Param(value = "referrer") String referrer,
                                @Param(value = "index") Integer index,
                                @Param(value = "pageSize") Integer pageSize);

    int findAllUserCount(@Param(value = "loginName") String loginName,
                                @Param(value = "email") String email,
                                @Param(value = "mobile") String mobile,
                                @Param(value = "beginTime") Date beginTime,
                                @Param(value = "endTime") Date endTime,
                                @Param(value = "role") Role role,
                                @Param(value = "referrer") String referrer);


    void updatePasswordByLoginName(@Param(value = "loginName") String loginName, @Param(value = "password") String password);

    List<String> findLoginNameLike(String loginName);
}
