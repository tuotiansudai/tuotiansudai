package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserSignInModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSignInMapper {
    int create(@Param(value = "loginName") String loginName,
               @Param(value = "signInCount") int signInCount);

    int getUserSignInCount(@Param(value = "loginName") String loginName);

    int updateSignInCount(@Param(value = "loginName") String loginName,
                          @Param(value = "signInCount") int signInCount);

    boolean exists(@Param(value = "loginName") String loginName);

    UserSignInModel findByLoginName(@Param(value = "loginName") String loginName);
}
