package com.tuotiansudai.rest.client.mapper;

import com.tuotiansudai.enums.ExperienceBillOperationType;
import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.UserModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserMapper {
    UserModel findByLoginNameOrMobile(String loginNameOrMobile);

    UserModel findByMobile(String mobile);

    UserModel findByLoginName(String loginName);

    UserModel findByIdentityNumber(String identityNumber);

    UserModel findByEmail(String email);

    List<String> findAllLoginNames();

    void updateEmail(String loginName, String email);

    void updateSignInCount(String loginName, int signInCount);

    void updateUserNameAndIdentityNumber(String loginName, String userName, String identityNumber);

    List<UserModel> findUsersByChannel(List<String> channels);

    List<UserModel> findUsersByRegisterTimeOrReferrer(Date startTime, Date endTime, String referrer);

    List<String> findAllByRole(Role role);

    long findCountByRole(Role role);

    long findUsersCount();

    List<UserModel> findUserModelByMobileLike(String mobile, int page, int pageSize);

    int findCountByMobileLike(String mobile);

    // call UserMapperDB

    void updateExperienceBalance(String loginName, ExperienceBillOperationType experienceBillOperationType, long experienceAmount);

    Long findExperienceByLoginName(String loginName);

    List<String> findAllRecommendation(HashMap<String, Object> districtName);

    List<Integer> findScaleByGender(Date endDate);

    long findCountInvestCityScale(Date endDate);

    List<Map<String, String>> findCountInvestCityScaleTop3(Date endDate);

    List<Map<String, String>> findAgeDistributionByAge(Date endDate);

    UserModel lockByLoginName(String loginName);

    List<UserModel> findUsersByProvince();

    void updateProvinceAndCity(String loginName, String s, String s1);

    List<String> findAllUserChannels();
}
