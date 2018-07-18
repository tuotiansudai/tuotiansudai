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

    long count();

    List<AccountModel> findAccountWithBalance(@Param(value = "startTime") String startTime,
                                              @Param(value = "offset") int offset);
}
