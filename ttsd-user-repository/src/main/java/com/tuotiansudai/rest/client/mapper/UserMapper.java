package com.tuotiansudai.rest.client.mapper;

import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserRegisterInfo;

import java.util.Date;
import java.util.List;

public interface UserMapper {
    UserModel findByLoginNameOrMobile(String loginNameOrMobile);

    UserModel findByIdentityNumber(String identityNumber);

    UserModel findByEmail(String email);

    List<String> findAllLoginNames();

    void updateEmail(String loginName, String email);

    void updateUserNameAndIdentityNumber(String loginName, String userName, String identityNumber);

    List<UserModel> findUsersByChannel(List<String> channels);

    List<UserRegisterInfo> findUsersByRegisterTimeOrReferrer(Date startTime, Date endTime, String referrer);

    List<UserRegisterInfo> findUsersHasReferrerByRegisterTime(Date startTime, Date endTime);

    long findUserCountByRegisterTimeOrReferrer(Date startTime, Date endTime, String referrer);

    List<String> findAllByRole(Role role);

    long findCountByRole(Role role);

    long findUsersCount();

    List<UserModel> findUserModelByMobileLike(String mobile, int page, int pageSize);

    int findCountByMobileLike(String mobile);

    List<UserModel> findEmptyProvinceUsers();

    void updateProvinceAndCity(String loginName, String province, String city);

    // call UserMapperDB
    UserModel lockByLoginName(String loginName);

    default UserModel findByMobile(String mobile) {
        return findByLoginNameOrMobile(mobile);
    }

    default UserModel findByLoginName(String loginName) {
        return findByLoginNameOrMobile(loginName);
    }
}

