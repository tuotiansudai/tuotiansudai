package com.tuotiansudai.repository.mapper;

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

    UserModel findByLoginName(String loginName);

    UserModel lockByLoginName(String loginName);

    UserModel findByMobile(String mobile);

    UserModel findByLoginNameOrMobile(String loginNameOrMobile);

    UserModel findByEmail(String email);

    long findUsersCount();

    List<String> findAllLoginNames();

    List<String> findAllUserChannels();

    List<UserModel> findUsersByProvince();

    List<String> findAllUsersByProvinces(Map<String, Object> params);

    List<String> findNaturalUser(Map<String, Object> params);

    List<String> findAllRecommendation(Map<String, Object> params);

    List<UserModel> findUsersByChannel(Map<String, Object> params);

    List<String> findUsersBirthdayMobile();

    List<String> findAllByRole(Map<String, Object> params);

    List<UserModel> findUserModelByMobile(@Param(value = "mobile") String mobile,
                                          @Param(value = "index") Integer index,
                                          @Param(value = "pageSize") Integer pageSize);

    List<UserModel> findUsersByRegisterTimeOrReferrer(@Param(value = "startTime") Date startTime,
                                                      @Param(value = "endTime") Date endTime,
                                                      @Param(value = "referrer") String referrer);

    UserModel findByIdentityNumber(String identityNumber);

    List<String> findBirthMonthUsers();

    List<String> findBirthDayUsers();

    List<Integer> findScaleByGender(@Param(value = "endDate") Date endDate);

    List<Map<String, String>> findAgeDistributionByAge(@Param(value = "endDate") Date endDate);

    List<Map<String, String>> findCountInvestCityScaleTop3(@Param(value = "endDate") Date endDate);

    long findCountInvestCityScale(@Param(value = "endDate") Date endDate);

}
