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

    int updateEmail(@Param("loginName") String loginName,
                    @Param("email") String email);

    List<UserModel> findUsersByRegisterTimeOrReferrer(@Param("startTime") Date startTime,
                                                      @Param("endTime") Date endTime,
                                                      @Param("referrer") String referrer,
                                                      @Param("rowLimit") int rowLimit,
                                                      @Param("rowIndex") int rowIndex);

    long findUserCountByRegisterTimeOrReferrer(@Param("startTime") Date startTime,
                                               @Param("endTime") Date endTime,
                                               @Param("referrer") String referrer);

    List<UserModel> findUsersHasReferrerByRegisterTime(@Param("startTime") Date startTime,
                                                       @Param("endTime") Date endTime,
                                                       @Param("rowLimit") int rowLimit,
                                                       @Param("rowIndex") int rowIndex);

    long findUserCountHasReferrerByRegisterTime(@Param("startTime") Date startTime,
                                                @Param("endTime") Date endTime);

    long findUsersCount();


    UserModel lockByLoginName(String loginName);
}
