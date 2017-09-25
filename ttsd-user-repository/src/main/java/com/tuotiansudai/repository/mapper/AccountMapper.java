package com.tuotiansudai.repository.mapper;

import com.tuotiansudai.repository.model.AccountModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountMapper {

    void create(AccountModel model);

    AccountModel findByLoginName(String loginName);

    AccountModel findByMobile(String mobile);

    AccountModel findByPayUserId(String payUserId);

    AccountModel lockByLoginName(String loginName);

    void update(AccountModel model);

    List<String> findLoginNames();

    List<AccountModel> findUsersAccountPoint(@Param(value = "loginName") String loginName,
                                             @Param(value = "userName") String userName,
                                             @Param(value = "mobile") String mobile,
                                             @Param(value = "startLimit") Integer startLimit,
                                             @Param(value = "endLimit") Integer endLimit);

    int findUsersAccountPointCount(@Param(value = "loginName") String loginName,
                                   @Param(value = "userName") String userName,
                                   @Param(value = "mobile") String mobile);

    long count();

    List<AccountModel> findAccountWithBalance(@Param(value = "startTime") String startTime,
                                              @Param(value = "offset") int offset);
}
