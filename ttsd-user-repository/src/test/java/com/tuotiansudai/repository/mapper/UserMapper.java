package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {

    int create(UserModel userModel);

    int updateUser(UserModel userModel);

    int updateExperienceBalance(@Param(value = "loginName") String loginName,
                                @Param(value = "experienceBillOperationType") ExperienceBillOperationType experienceBillOperationType,
                                @Param(value = "experienceBalance") long experienceBalance);

    UserModel findByLoginName(String loginName);

    UserModel lockByLoginName(String loginName);

    UserModel findByMobile(String mobile);

    UserModel findByLoginNameOrMobile(String loginNameOrMobile);

    UserModel findByEmail(String email);

    long findUsersCount();

    List<String> findAllUsersByProvinces(Map<String, Object> params);

    List<String> findAllByRole(Map<String, Object> params);

    List<UserModel> findUsersByRegisterTimeOrReferrer(@Param(value = "startTime") Date startTime,
                                                      @Param(value = "endTime") Date endTime,
                                                      @Param(value = "referrer") String referrer);

    UserModel findByIdentityNumber(String identityNumber);

    Long findExperienceByLoginName(String loginName);
}
