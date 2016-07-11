package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.UserModel;
import com.tuotiansudai.repository.model.UserView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {

    UserModel findByEmail(String email);

    UserModel findByMobile(String mobile);

    UserModel findByLoginName(String loginName);

    UserModel lockByLoginName(String loginName);

    UserModel findByLoginNameOrMobile(String loginNameOrMobile);

    void create(UserModel userModel);

    void updateUser(UserModel userModel);

    List<UserView> searchAllUsers(@Param(value = "loginName") String loginName,
                                  @Param(value = "referrerMobile") String referrerMobile,
                                  @Param(value = "mobile") String mobile,
                                  @Param(value = "identityNumber") String identityNumber);

    void updatePasswordByLoginName(@Param(value = "loginName") String loginName, @Param(value = "password") String password);

    List<String> findStaffByLikeLoginName(String loginName);

    List<String> findLoginNameLike(String loginName);

    List<String> findMobileLike(String mobile);

    List<String> findAccountMobileLike(String mobile);

    List<String> findAllChannels();

    List<String> findAllUserChannels();

    List<UserModel> findUserByProvince();

    List<UserView> findUsersAccountBalance(@Param(value = "mobile") String mobile,
                                            @Param(value = "balanceMin") int balanceMin,
                                            @Param(value = "balanceMax") int balanceMax,
                                            @Param(value = "startLimit") int startLimit,
                                            @Param(value = "endLimit") int endLimit);



    long findUsersAccountBalanceSum(@Param(value = "mobile") String mobile,
                                    @Param(value = "balanceMin") int balanceMin,
                                    @Param(value = "balanceMax") int balanceMax);

    int findUsersAccountBalanceCount(@Param(value = "mobile") String mobile,
                                     @Param(value = "balanceMin") int balanceMin,
                                     @Param(value = "balanceMax") int balanceMax);

    List<String> findAllUsers(Map<String, Object> params);

    List<String> findNaturalUser(Map<String, Object> params);

    List<String> findAllByRole(Map<String, Object> params);

    List<String> findAllRecommendation(Map<String, Object> params);

    List<String> findUsersBirthdayMobile();

    long findUsersCountByChannel(String channel);

    List<UserModel> findUsersByChannel(Map<String, Object> params);

    String findUsersMobileByLoginName(@Param(value = "loginName") String loginName);

    long findUsersCount();

    List<String> findLoginNames();
}
