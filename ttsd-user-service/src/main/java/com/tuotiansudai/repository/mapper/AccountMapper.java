package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AccountModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapper {

    void create(AccountModel model);

    AccountModel findByLoginName(String loginName);

    AccountModel findByPayUserId(String payUserId);

    AccountModel lockByLoginName(String loginName);

    void update(AccountModel model);

    List<AccountModel> findByIdentityNumber(String identityNumber);

    List<String> findLoginNames();

    List<String> findBirthMonthUsers();

    List<String> findBirthDayUsers();

    List<AccountModel> findUsersAccountPoint(@Param(value = "loginName") String loginName,
                                             @Param(value = "userName") String userName,
                                             @Param(value = "mobile") String mobile,
                                             @Param(value = "startLimit") Integer startLimit,
                                             @Param(value = "endLimit") Integer endLimit);

    int findUsersAccountPointCount(@Param(value = "loginName") String loginName,
                                   @Param(value = "userName") String userName,
                                   @Param(value = "mobile") String mobile);

    long count();

    List<AccountModel> findAccountWithBalance(@Param(value = "startLimit") int startLimit,
                                              @Param(value = "endLimit") int endLimit);
}
