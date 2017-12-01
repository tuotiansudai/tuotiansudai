package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserMapperDB {

    UserModel findByLoginNameOrMobile(String loginNameOrMobile);

    UserModel findByIdentityNumber(String identityNumber);

    UserModel findByEmail(String email);

    int updateEmail(@Param(value = "loginName") String loginName,
                    @Param(value = "email") String email);

    List<UserModel> findUsersByChannel(@Param("channels") List<String> channels);

    List<UserModel> findUsersByRegisterTimeOrReferrer(@Param(value = "startTime") Date startTime,
                                                      @Param(value = "endTime") Date endTime,
                                                      @Param(value = "referrer") String referrer);

    long findUserCountByRegisterTimeOrReferrer(@Param(value = "startTime") Date startTime,
                                               @Param(value = "endTime") Date endTime,
                                               @Param(value = "referrer") String referrer);

    List<UserModel> findUsersHasReferrerByRegisterTime(@Param(value = "startTime") Date startTime,
                                                      @Param(value = "endTime") Date endTime);

    long findUsersCount();


    UserModel lockByLoginName(String loginName);
}
