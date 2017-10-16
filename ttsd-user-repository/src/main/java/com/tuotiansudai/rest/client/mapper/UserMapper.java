package com.tuotiansudai.rest.client.mapper;

import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.UserModel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

    List<String> findAllRecommendation(HashMap<String, Object> districtName);

    UserModel lockByLoginName(String loginName);

    List<UserModel> findUsersByProvince();

    void updateProvinceAndCity(String loginName, String s, String s1);
}
