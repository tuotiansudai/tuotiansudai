package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.enums.Role;
import com.tuotiansudai.repository.model.UserModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Repository
public interface UserMapperDB {

    UserModel findByLoginNameOrMobile(String loginNameOrMobile);

    UserModel findByIdentityNumber(String identityNumber);

    UserModel findByEmail(String email);

    List<String> findAllLoginNames(); // not implement with mybatis

    int updateEmail(@Param(value = "loginName") String loginName,
                    @Param(value = "email") String email);

    void updateSignInCount(String loginName, int signInCount); // not implement with mybatis

    void updateUserNameAndIdentityNumber(String loginName, String userName, String identityNumber); // not implement with mybatis

    List<UserModel> findUsersByChannel(@Param("channels") List<String> channels);

    List<UserModel> findUsersByRegisterTimeOrReferrer(@Param(value = "startTime") Date startTime,
                                                      @Param(value = "endTime") Date endTime,
                                                      @Param(value = "referrer") String referrer);

    List<String> findAllByRole(Role role);// not implement with mybatis

    long findCountByRole(Role role);// not implement with mybatis

    long findUsersCount();

    List<UserModel> findUserModelByMobileLike(String mobile, int page, int pageSize);// not implement with mybatis

    int findCountByMobileLike(String mobile);// not implement with mybatis

    // call UserMapperDB

    List<String> findAllRecommendation(HashMap<String, Object> districtName);

    UserModel lockByLoginName(String loginName);

    List<UserModel> findUsersByProvince();

    int updateProvinceAndCity(@Param(value = "loginName") String loginName,
                              @Param(value = "province") String province,
                              @Param(value = "city") String city);


    default UserModel findByMobile(String mobile) {
        return findByLoginNameOrMobile(mobile);
    }

    default UserModel findByLoginName(String loginName) {
        return findByLoginNameOrMobile(loginName);
    }
}
